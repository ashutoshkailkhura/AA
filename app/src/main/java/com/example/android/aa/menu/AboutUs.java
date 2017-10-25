package com.example.android.aa.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.aa.R;

public class AboutUs extends AppCompatActivity implements View.OnClickListener {
    TextView contact1, contact2, contact3, contact4, mail;
    ImageView facebook, youtube, instagram, twitter;

    public static String FACEBOOK_URL = "https://www.facebook.com/AnimalAidUnlimited";
    public static String FACEBOOK_PAGE_ID = "https://www.facebook.com/AnimalAidUnlimited";
    public static String Email_Address = "erikaabrams@yahoo.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_contact_us);

        contact1 = (TextView) findViewById(R.id.contact1);
        contact1.setPaintFlags(contact1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        contact2 = (TextView) findViewById(R.id.contact2);
        contact2.setPaintFlags(contact2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        contact3 = (TextView) findViewById(R.id.contact3);
        contact3.setPaintFlags(contact3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        contact4 = (TextView) findViewById(R.id.contact4);
        contact4.setPaintFlags(contact4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mail = (TextView) findViewById(R.id.contact_email);
        mail.setPaintFlags(mail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

//        facebook = (ImageView) findViewById(R.id.facebook_imageview);
//        youtube = (ImageView) findViewById(R.id.youtube_imageview);
//        instagram = (ImageView) findViewById(R.id.instagram_imageview);
//        twitter = (ImageView) findViewById(R.id.twitter_imageview);
        contact1.setOnClickListener(this);
        contact2.setOnClickListener(this);
        contact3.setOnClickListener(this);
        contact4.setOnClickListener(this);

//        facebook.setOnClickListener(this);
//        mail.setOnClickListener(this);
//        youtube.setOnClickListener(this);
//        twitter.setOnClickListener(this);
//        instagram.setOnClickListener(this);


    }

    //method to get right url to use in the intent
    public String getFacebookUrl(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        try {
            int versioncode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versioncode >= 3002850) {
                return "fb://page/" + FACEBOOK_PAGE_ID;
            } else {
                //for older version
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == facebook) {
            Intent facebookintent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookUrl(this);
            facebookintent.setData(Uri.parse(facebookUrl));
            startActivity(facebookintent);
        }
        if (v == mail) {
            Intent sendMail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "erikaabrams@yahoo.com", null));
            sendMail.putExtra(Intent.EXTRA_SUBJECT, "subject");
            sendMail.putExtra(Intent.EXTRA_EMAIL, Email_Address);
            startActivity(Intent.createChooser(sendMail, "send mail"));

        }
        if (v == youtube) {
            Intent Appyt = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC4kyYTypYb3mQ6ZL25kly6g"));
            Intent Webyt = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC4kyYTypYb3mQ6ZL25kly6g"));
            try {
                startActivity(Appyt);
            } catch (ActivityNotFoundException ex) {
                startActivity(Webyt);
            }

        }
        if (v == twitter) {


        }
        if (v == instagram) {

        }
        if (v == contact1) {
            String con1 = "9829843726";
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + con1));
            startActivity(call);
        }
        if (v == contact2) {
            String con1 = "9784005989";
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + con1));
            startActivity(call);
        }
        if (v == contact3) {
            String con1 = "9602055895";
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + con1));
            startActivity(call);
        }
        if (v == contact4) {
            String con1 = "9602054037";
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + con1));
            startActivity(call);
        }

    }
}
