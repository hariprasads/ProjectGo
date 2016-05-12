package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Login extends Activity {

    AutoCompleteTextView email;
    EditText pass;
    Button signin;
    String email_value, pass_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        email = (AutoCompleteTextView)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);
        signin = (Button)findViewById(R.id.email_sign_in_button);

    }

    public void signupview(View view){
         Intent i = new Intent(Login.this, signup.class);
         startActivity(i);
    }

    public void signinview(View view){
        email_value = email.getText().toString();
        pass_value = pass.getText().toString();
        //email_value = "admin";
        //pass_value = "password";
        RequestParams params = new RequestParams();
        Boolean checkemail = isValidEmail(email_value);

        if ((!email_value.isEmpty()) && (!pass_value.isEmpty())) {
            if(!checkemail){
                // Put Http parameter username with value of Email Edit View control
                params.put("id",email_value);
                // Put Http parameter password with value of Password Edit Value control
                params.put("password", pass_value);
                // Invoke RESTful Web Service with Http parameters

                Intent i = new Intent(Login.this, DeviceScanActivity.class);
                i.putExtra("username", email_value);
                //startActivity(i);
                invokeBackEnd(email_value, pass_value, params);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }




    }

    public void invokeBackEnd(final String userName, final String pwd, final RequestParams requestParams) {
        String url = "http://52.53.196.31:5000/users/";
        url = url.concat(userName);
        Log.d("string", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String s = new String(responseBody);
                    Log.d("string", s);
                    JSONObject json = new JSONObject(s);

                    Log.d("JSONObject", json.toString());

                    //JSONArray items = json.getJSONArray("_items");
                    //Log.d("JSONArray", items.toString());

                    if (json.length() != 0 && pwd.equals(json.getString("password"))) {
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Login.this, DeviceScanActivity.class);
                        i.putExtra("username",email_value);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter correct email/password", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occurred [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    boolean isValidEmail(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}

