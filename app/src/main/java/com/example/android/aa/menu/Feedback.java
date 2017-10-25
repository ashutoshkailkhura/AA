package com.example.android.aa.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.aa.HttpManager;
import com.example.android.aa.R;
import com.example.android.aa.RequestPackage;
import com.example.android.aa.repo.Const;

public class Feedback extends AppCompatActivity {
    EditText FeedbackText;
    Button submit;
    Spinner feedback;
    String textboxvalue, spinnervalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = (Spinner) findViewById(R.id.feedback);
        FeedbackText = (EditText) findViewById(R.id.feedback_text);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textboxvalue = FeedbackText.getText().toString();
                spinnervalue = feedback.getSelectedItem().toString();
                requestData(Const.FEEDBACK_URL);
            }
        });
    }

    private void requestData(String uri) {

        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        p.setParam("feedback", textboxvalue);
        p.setParam("type", spinnervalue);
        submitFeedback task = new submitFeedback();
        task.execute(p);
    }

    class submitFeedback extends AsyncTask<RequestPackage, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("recived")) {
                Toast.makeText(Feedback.this, "Your Feedback Is Submitted", Toast.LENGTH_LONG).show();
            } else if (s.equals("not resived") || s==null) {
                Toast.makeText(Feedback.this, "Web Services Are Not Available", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Feedback.this, s, Toast.LENGTH_LONG).show();
            }
        }
    }
}
