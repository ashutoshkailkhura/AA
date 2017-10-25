package com.example.android.aa.tabshop;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.aa.HttpManager;
import com.example.android.aa.R;
import com.example.android.aa.RequestPackage;
import com.example.android.aa.repo.Const;
import com.example.android.aa.repo.MyPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyBag extends AppCompatActivity {

    List<ShopModel> mShopModel;
    private ShopAdapter shopAdapter;
    RecyclerView recyclerview;
    Button buy;
    MyPref myPref;
    ArrayList<Double> totalprice = new ArrayList<>();
    double s1 = 0;
    TextView total;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bag);
        //get xml view
        pb = (ProgressBar) findViewById(R.id.progressBar_mybag);
        recyclerview = (RecyclerView) findViewById(R.id.mybag_recycler);
        buy = (Button) findViewById(R.id.buybutton_mybag);
        total = (TextView) findViewById(R.id.totalinbag_textview);
        mShopModel = new ArrayList<>();
        shopAdapter = new ShopAdapter(this, mShopModel);
        recyclerview.setAdapter(shopAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        requestData(Const.BAG_URL);
        //buy.setOnClickListener(this);
        setUpTotalAmount();
    }

    private void setUpTotalAmount() {
        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(Const.BAG_URL);
        mytotal mt = new mytotal();
        mt.execute(p);
    }


    protected void updateDisplay() {
        ShopAdapter adapter = new ShopAdapter(this, mShopModel);
        recyclerview.setAdapter(adapter);
    }

    private void requestData(String uri) {

        //String userId = myPref.getUserDetail("userId");
        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        p.setParam("userId", "5001");
        mybagitem task = new mybagitem();
        task.execute(p);
    }

    class mytotal extends AsyncTask<RequestPackage, Void, String> {

        @Override
        protected String doInBackground(RequestPackage... params) {

            String content = HttpManager.getData(params[0]);
            try {
                JSONObject obj = new JSONObject(content);
                JSONArray data = obj.getJSONArray("mybag");

                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonlocation = data.getJSONObject(i);
                    double price = jsonlocation.getDouble("price");
                    totalprice.add(price);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return "true";
        }

        @Override
        protected void onPostExecute(String s) {

            if (s == "true") {
                for (int i = 0; i < totalprice.size(); i++) {
                    s1 = s1 + totalprice.get(i);
                }
                String sum = String.valueOf(s1);
                total.setText(sum);
            }

        }
    }

    class mybagitem extends AsyncTask<RequestPackage, String, List<ShopModel>> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<ShopModel> doInBackground(RequestPackage... params) {


            String content = HttpManager.getData(params[0]);
            mShopModel = ShopParser.parseFeed(content);

            for (ShopModel shopModel : mShopModel) {
                try {
                    String Image_url = Const.IMAGE_BASE_URL_SHOP_IMAGE + shopModel.getPhoto();
                    InputStream in = (InputStream) new URL(Image_url).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    shopModel.setBitmap(bitmap);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return mShopModel;
        }

        @Override
        protected void onPostExecute(List<ShopModel> result) {
            mShopModel = result;
            pb.setVisibility(View.GONE);
            updateDisplay();
        }

    }
}
