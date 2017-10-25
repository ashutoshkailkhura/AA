package com.example.android.aa.tabshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.aa.HttpManager;
import com.example.android.aa.R;
import com.example.android.aa.RequestPackage;
import com.example.android.aa.repo.CheckInternetConnection;
import com.example.android.aa.repo.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ShopDetail extends AppCompatActivity implements View.OnClickListener {


    TextView name, prise, stock, number;
    ImageView mainimage, plusimg, minusimg;
    double rate;
    Button bag;

    int clickcount = 0, num = 1;


    private ArrayList<String> values = new ArrayList<>();
    CheckInternetConnection isOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get intent data
        Intent i = getIntent();
        values = i.getStringArrayListExtra("detail");
        setContentView(R.layout.activity_shop_detail);
        //get xml view reference
        plusimg = (ImageView) findViewById(R.id.plus);
        minusimg = (ImageView) findViewById(R.id.minus);
        name = (TextView) findViewById(R.id.detail_shop_name);
        mainimage = (ImageView) findViewById(R.id.detail_shop_image);
        stock = (TextView) findViewById(R.id.in_stock_or_not);
        number = (TextView) findViewById(R.id.number_of_quantity);
        prise = (TextView) findViewById(R.id.detail_shop_prise);
        bag = (Button) findViewById(R.id.shodetailbutton);
        //set view
        String productId = values.get(0);
        name.setText(values.get(1));
        prise.setText(values.get(2));
        String image = values.get(3);

        if (CheckInternetConnection.getInstance(this).isOnline()) {
            ButtonState(productId);
            detailImage detailImage = new detailImage();
            detailImage.execute(image);
        } else {
            mainimage.setImageResource(R.drawable.wifi);
        }
        plusimg.setOnClickListener(this);
        minusimg.setOnClickListener(this);
        bag.setOnClickListener(this);
    }

    private void ButtonState(String productId) {
        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(Const.CHECK_BUTTON);
        p.setParam("userId", "5001");
        p.setParam("productId", productId);
        ButtonStateAs atb = new ButtonStateAs();
        atb.execute(p);
    }

    private class ButtonStateAs extends AsyncTask<RequestPackage, Void, String> {

        @Override
        protected String doInBackground(RequestPackage... params) {
            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
           // Toast.makeText(ShopDetail.this, s, Toast.LENGTH_LONG).show();
            if (s == "already") {
                setButtonToAddBag();
            }
            if (s == "not yet") {
                setButtonToGoBag();
            } else {
                setButtonToAddBag();
            }
        }
    }

    public class detailImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = null;
            try {
                String Image_url = Const.IMAGE_BASE_URL_SHOP_IMAGE + params[0];
                InputStream in = (InputStream) new URL(Image_url).getContent();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mainimage.setImageBitmap(bitmap);
        }
    }

    private void setButtonToAddBag() {
        bag.setText("Add To Bag");
        bag.setBackgroundResource(R.drawable.button_press);
        bag.setTextColor(Color.WHITE);
    }

    private void setButtonToGoBag() {
        bag.setText("Go To Bag");
        bag.setBackgroundResource(R.drawable.button_unpress);
        bag.setTextColor(Color.WHITE);
        clickcount = clickcount + 1;
    }

    private void addToBag(String productId) {
        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(Const.ADD_TO_BAG);
        p.setParam("productId", productId);
        p.setParam("userId", "5001");
        addToBag atb = new addToBag();
        atb.execute(p);
    }

    @Override
    public void onClick(View v) {
        if (v == plusimg) {
            if (num < 6 || num == 0) {
                num++;
                number.setText(String.valueOf(num));
            } else {
                Toast.makeText(ShopDetail.this, "You Can Select Max 6 Quantity", Toast.LENGTH_SHORT).show();
            }

        }
        if (v == minusimg) {
            if (num > 1) {
                num--;
                number.setText(String.valueOf(num));
            }

        }
        if (v == bag) {
            if (clickcount == 0) {
                clickcount = clickcount + 1;
                addToBag(values.get(0));
                //setButtonToGoBag();
            } else {
                startActivity(new Intent(this, MyBag.class));
            }
        }
    }

    private class addToBag extends AsyncTask<RequestPackage, Void, String> {

        @Override
        protected String doInBackground(RequestPackage... params) {
            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == "not added") {
                Toast.makeText(ShopDetail.this, "Item Not Added To Bag", Toast.LENGTH_LONG).show();
            } else if (s == null) {
                Toast.makeText(ShopDetail.this, "Web Servicese Are Not Available", Toast.LENGTH_LONG).show();
            } else {
                setButtonToGoBag();
            }
        }
    }

}

