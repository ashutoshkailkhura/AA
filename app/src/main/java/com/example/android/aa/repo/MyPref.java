package com.example.android.aa.repo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.aa.RequestPackage;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MyPref {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    // shared pref mode
    private int PRIVATE_MODE = 0;

    public MyPref(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(Const.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(Const.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(Const.IS_FIRST_TIME_LAUNCH, true);
    }

    public void setUserLogIn(boolean isFirstTime) {
        editor.putBoolean(Const.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isUserLogIn() {
        return pref.getBoolean(Const.IS_FIRST_TIME_LAUNCH, true);
    }

    public void saveUserId(int id) {
        editor.putInt("userid", id);
        editor.commit();
    }
    public int getUserId(){
        return pref.getInt("userid",0);
    }

    public void saveUserDetail(GoogleSignInAccount gacc) {
        String mail = gacc.getEmail();
        String name = gacc.getDisplayName();
        String userid = gacc.getId();
        editor.putString("userId", userid);
        editor.putString("usermail", mail);
        editor.putString("username", name);
        editor.commit();
    }

    public String getUserDetail(String Key) {

        return "5001";

    }
}