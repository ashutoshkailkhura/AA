package com.example.android.aa.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.aa.HttpManager;
import com.example.android.aa.MainActivity;
import com.example.android.aa.R;
import com.example.android.aa.RequestPackage;
import com.example.android.aa.repo.Const;
import com.example.android.aa.repo.MyPref;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserLogIn extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    EditText ed_mail, ed_password;
    String mail, password;
    TextView gotosignup;
    Button login;
    SignInButton googlebutton;
    private static final int RES_CODE_SIGN_CODE = 1001;
    GoogleApiClient mGoogleApiClient;
    MyPref myPref;
    public static int APP_REQUEST_CODE = 1;
    LoginButton loginButton;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Checking for first time launch - before calling setContentView()
        myPref = new MyPref(this);
        if (!myPref.isUserLogIn()) {
            launchHomeScreen();
            finish();
        }
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_user_log_in);
        //get view reference
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("email");
        ed_mail = (EditText) findViewById(R.id.login_email);
        ed_password = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login_button);
        gotosignup = (TextView) findViewById(R.id.gotosignUp);
        gotosignup.setPaintFlags(gotosignup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        googlebutton = (SignInButton) findViewById(R.id.gsidnIn_button);
        //google singin option
        googleSignInAnimalAid();
        //action

        // Login Button callback registration
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                launchHomeScreen();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                // display error
                String toastMessage = exception.getMessage();
                Toast.makeText(UserLogIn.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });
        googlebutton.setOnClickListener(this);
        login.setOnClickListener(this);
        gotosignup.setOnClickListener(this);
    }


    private void googleSignInAnimalAid() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void launchHomeScreen() {
        myPref.setUserLogIn(false);
        startActivity(new Intent(UserLogIn.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Forward result to the callback manager for Login Button
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RES_CODE_SIGN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            signInResultHandler(result);
        }
    }

    private void signInResultHandler(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount gacc = result.getSignInAccount();
            try {
                myPref.saveUserDetail(gacc);
                myPref.setFirstTimeLaunch(false);
            } catch (NullPointerException ignored) {

            }
            launchHomeScreen();
        } else {
            Status status = result.getStatus();
            int statusCode = status.getStatusCode();
            if (statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                Toast.makeText(this, "Cant Connect", Toast.LENGTH_LONG).show();
            } else if (statusCode == GoogleSignInStatusCodes.SIGN_IN_FAILED) {
                Toast.makeText(this, "Log In Fail", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Cant Connect", Toast.LENGTH_LONG).show();
            }
        }

    }


    private void requestData(String uri) {

        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        p.setParam("type", "accountkit");
        p.setParam("mail", mail);
        p.setParam("token", password);
        checkUser task = new checkUser();
        task.execute(p);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {

        if (v == googlebutton) {
            Intent signinintent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signinintent, RES_CODE_SIGN_CODE);
        }
        if (v == login) {
            if (validation()) {
                login.setEnabled(true);
                requestData(Const.LOG_IN_URL);
            } else {
                Toast.makeText(UserLogIn.this, "Please Enter Valid Input", Toast.LENGTH_LONG).show();

            }

        }
        if (v == gotosignup) {
            startActivity(new Intent(UserLogIn.this, UserSignUp.class));

        }
    }

    private boolean validation() {
        mail = ed_mail.getText().toString();
        password = ed_password.getText().toString();
        if (mail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches() && password.isEmpty() || password.length() < 6) {
            return false;
        }
        return true;
    }

    private class checkUser extends AsyncTask<RequestPackage, Void, String> {

        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            try {
                JSONObject obj = new JSONObject(content);
                JSONArray data = obj.getJSONArray("userid");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonlocation = data.getJSONObject(i);
                    myPref.saveUserId(jsonlocation.getInt("userid"));
                }

            } catch (JSONException e) {
                return null;

            }
            return content;
        }

        @Override
        protected void onPostExecute(String s) {

            if (myPref.getUserId() == 0) {
                Toast.makeText(UserLogIn.this, "Error", Toast.LENGTH_SHORT).show();
            } else {
                launchHomeScreen();
            }
        }
    }
}
