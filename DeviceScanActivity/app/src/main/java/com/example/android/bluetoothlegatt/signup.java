package com.example.android.bluetoothlegatt;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class signup extends Activity {

    AutoCompleteTextView username, email, password, repass;
    String user_name, email_value, password_value, repass_value;
    Button btn;
    Boolean verifyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        btn = (Button)findViewById(R.id.email_sign_in_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                password = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
                repass = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView3);
                username = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView4);

                user_name = username.getText().toString();
                email_value = email.getText().toString();
                password_value = password.getText().toString();
                repass_value = repass.getText().toString();

                //verifyEmail = isValidEmail(email_value);

                Log.d("Email", email_value);
                Toast.makeText(signup.this, email_value, Toast.LENGTH_SHORT).show();


                    if (password_value.equals(repass_value)) {


                        try {
                            postlDetail("users/");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(signup.this,Login.class);
                        startActivity(i);
                        Toast.makeText(signup.this, "Sign up Successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(signup.this, "Check your password again", Toast.LENGTH_SHORT).show();
                    }

            }
        });

    }



    public void postlDetail(String tablename) throws JSONException, UnsupportedEncodingException {

        RequestParams params = new RequestParams();

        JSONObject json = new JSONObject();

        json.put("name", email.getText().toString());
        json.put("password", password.getText().toString());
        json.put("id", username.getText().toString());

        Log.d("Details", json.toString());

        StringEntity entity = new StringEntity(json.toString());


        RestClient.post(this.getApplicationContext(),tablename,entity,"application/json", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("response----------", response.toString());
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.d("response", response.toString());

            }

        });

    }

    boolean isValidEmail(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }


}
