package com.example.android.aa.findhelp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.aa.R;
import com.example.android.aa.repo.CheckInternetConnection;
import com.example.android.aa.repo.Const;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class DetailFindHelpActivity extends AppCompatActivity {

    private static final int ERROR_DIALOG_REQUEST = 9001;
    ArrayList<String> rvalues = new ArrayList<>();
    ArrayList<String> svalues;
    Button showinMap;
    TextView placename, mobilenum, DescriptionText, Address;
    ImageView img;
    private AlertDialog gpsdialog;
    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_find_help);
        //get intent data
        Intent i = getIntent();
        rvalues = i.getStringArrayListExtra("detail");
        //get xml view refernece
        img = (ImageView) findViewById(R.id.detailfindhelp_image);
        showinMap = (Button) findViewById(R.id.showInMap_button);
        placename = (TextView) findViewById(R.id.placename_findus);
        mobilenum = (TextView) findViewById(R.id.mobile_findus);
        DescriptionText = (TextView) findViewById(R.id.discription_findus);
        Address = (TextView) findViewById(R.id.address_findus);
        //set values
        String imgname = rvalues.get(2);
        placename.setText(rvalues.get(1));
        mobilenum.setText(rvalues.get(3));
        DescriptionText.setText(rvalues.get(6));
        Address.setText(rvalues.get(0));
        //action
        mobilenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String con1 = mobilenum.getText().toString();
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:" + con1));
                startActivity(call);
            }
        });

        showinMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!servicesOK() || !gpsEnable()) {
                    if (!servicesOK()) {
                        servicesOK();
                    }
                    if (!gpsEnable()) {
                        gpsEnable();
                    }
                } else {
                    launchShowInMapActivity();
                }
            }
        });

        if (CheckInternetConnection.getInstance(this).isOnline()) {
            DFHAsyn dfhAsyn = new DFHAsyn();
            dfhAsyn.execute(imgname);
        } else {
            img.setImageResource(R.drawable.logo);
        }
    }

    private void launchShowInMapActivity() {
        Intent intent = new Intent(DetailFindHelpActivity.this, ShowInMap.class);
        svalues = new ArrayList<>();
        svalues.add(rvalues.get(4));
        svalues.add(rvalues.get(5));
        svalues.add(rvalues.get(1));
        intent.putStringArrayListExtra("detail", svalues);
        startActivity(intent);
    }

    private boolean gpsEnable() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            showGPSDisabledAlertToUser();
        }
        return false;
    }

    private void showGPSDisabledAlertToUser() {


        final AlertDialog.Builder alldone = new AlertDialog.Builder(this);
        alldone.setCancelable(false);
        alldone.setTitle("Enable GPS To Access Your Location");
        alldone.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
                dialog.dismiss();
                launchShowInMapActivity();
            }
        });
        alldone.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                launchShowInMapActivity();
            }
        });
        AlertDialog dialog = alldone.create();
        dialog.show();

    }

    public boolean servicesOK() {

        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog =
                    GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to mapping service", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public class DFHAsyn extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            img.setImageResource(R.drawable.logo);
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = null;
            try {
                String Image_url = Const.IMAGE_BASE_URL_FIND_HELP + params[0];
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
            img.setImageBitmap(bitmap);
        }
    }

}
