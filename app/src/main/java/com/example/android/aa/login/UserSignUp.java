package com.example.android.aa.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.aa.HttpManager;
import com.example.android.aa.MainActivity;
import com.example.android.aa.R;
import com.example.android.aa.RequestPackage;
import com.example.android.aa.repo.Const;
import com.example.android.aa.repo.MyPref;


public class UserSignUp extends AppCompatActivity implements View.OnClickListener {

    EditText name, email, pass, repass;
    String na, mai, pas, rpas;
    Button signup;
    MyPref myPref;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);
        //get view referernce
        name = (EditText) findViewById(R.id.signin_username);
        email = (EditText) findViewById(R.id.signin_email);
        pass = (EditText) findViewById(R.id.signin_password);
        repass = (EditText) findViewById(R.id.signin_conpassword);
        signup = (Button) findViewById(R.id.signup_button);

        //action
        signup.setOnClickListener(this);
    }

    private void requestData(String uri) {

        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        p.setParam("name", na);
        p.setParam("mail", mai);
        p.setParam("pass", pas);
        signupuser task = new signupuser();
        task.execute(p);
    }

    public boolean validation() {
        na = name.getText().toString();
        mai = email.getText().toString();
        pas = pass.getText().toString();
        rpas = repass.getText().toString();
        if (mai.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mai).matches()
                && pas.isEmpty() || pas.length() < 6 || rpas.isEmpty() || rpas.length() < 6 || pas != rpas
                && na.isEmpty() || na.length() < 6) {
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == signup) {
            if (validation()) {

                requestData(Const.SIGN_UP_URL);
            } else {
                Toast.makeText(UserSignUp.this, "Please Make Valid Entry", Toast.LENGTH_LONG).show();
            }

        }
    }


    class signupuser extends AsyncTask<RequestPackage, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(UserSignUp.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Toast.makeText(UserSignUp.this, s, Toast.LENGTH_LONG).show();

            if (s == "Welcome") {
                startActivity(new Intent(UserSignUp.this, MainActivity.class));
                myPref.setFirstTimeLaunch(false);
                finish();
            } else if (s == "error" || s==null) {
                Toast.makeText(UserSignUp.this, "Web Services Are Not Available", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(UserSignUp.this, s, Toast.LENGTH_LONG).show();
            }
        }
    }
}
