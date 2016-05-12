package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Attendance extends Activity {

    Button attendance_btn;
    Button logout_btn,back_btn;
    String macid;
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    TextView tv;
    String class_name, uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
           // Log.d("Username", extras.getString("username"));
             macid = extras.getString(EXTRAS_DEVICE_ADDRESS);;
             uname = extras.getString("username");


        }

        back_btn = (Button)findViewById(R.id.button2);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logout_btn = (Button)findViewById(R.id.button3);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Attendance.this, Login.class);
                startActivity(i);
            }
        });
        tv = (TextView)findViewById(R.id.textView);
        //tv.setText(macid);
        attendance_btn = (Button)findViewById(R.id.button);
        attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(macid.equals("B8:27:EB:5D:2B:E5")){
                    class_name = "class273";
                    try {
                        putlDetail("users/");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Attendance.this,class_name+" Attendance Marked for "+uname,Toast.LENGTH_SHORT).show();
                }else if(macid.equals("78:A5:04:29:02:BD")){
                    class_name = "class275";
                    try {
                        putlDetail("users/");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Attendance.this,class_name+" Attendance Marked for "+uname,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void putlDetail(String tablename) throws JSONException, UnsupportedEncodingException {

        tablename = tablename.concat(uname);
        RequestParams params = new RequestParams();
        JSONObject reqObj = new JSONObject();
        JSONArray req = new JSONArray();
        JSONObject json = new JSONObject();

        String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());

        reqObj.put("present", "yes");
        reqObj.put("date", date);
        req.put(reqObj);

        //json.put("email", email.getText().toString());
        //json.put("password", password.getText().toString());
        json.put("id", uname);
        json.put(class_name, req);

        Log.d("Details", json.toString());

        StringEntity entity = new StringEntity(json.toString());

        RestClient.put(this.getApplicationContext(), tablename, entity, "application/json", new JsonHttpResponseHandler() {

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
                Log.d("response--------", "fail");

            }

        });

    }


}
