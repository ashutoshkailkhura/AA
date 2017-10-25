package com.example.android.aa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.aa.findhelp.FindHelp;
import com.example.android.aa.menu.AboutUs;
import com.example.android.aa.menu.Feedback;
import com.example.android.aa.tabshop.ShopTab;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    AlertDialog.Builder exitDialog;
    CardView shop, help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);

        shop = (CardView) findViewById(R.id.shop);
        help = (CardView) findViewById(R.id.help);

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShopTab.class));
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FindHelp.class));
            }
        });

    }


    @Override
    public void onBackPressed() {
        exitDialog = new AlertDialog.Builder(MainActivity.this);
        exitDialog.setTitle("Exit Animal Aid");

        exitDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        exitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = exitDialog.create();
        dialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_feedback:
                startActivity(new Intent(this, Feedback.class));
                break;

            case R.id.menu_aboutus:
                startActivity(new Intent(this, AboutUs.class));
                break;

            case R.id.menu_logout:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
