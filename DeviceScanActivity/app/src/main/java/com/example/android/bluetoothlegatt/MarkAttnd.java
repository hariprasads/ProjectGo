package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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

public class MarkAttnd extends Activity {

    String macid;
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    TextView tv;
    String class_name, uname;
    String date[] = new String[20];
    String dates = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
    Button logout_btn,back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attnd);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Log.d("Username", extras.getString("username"));
            macid = extras.getString(EXTRAS_DEVICE_ADDRESS);
            uname = extras.getString("username");

            RequestParams params = new RequestParams();
            params.put("id",uname);
            getData(uname,params);

//            if(macid.equals("B8:27:EB:5D:2B:E5")){
//                class_name = "class273";
//                try {
//                    putlDetail("users/");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                Toast.makeText(MarkAttnd.this, class_name + " Attendance Marked for " + uname, Toast.LENGTH_SHORT).show();
//            }else if(macid.equals("78:A5:04:29:02:BD")){
//                class_name = "class275";
//                try {
//                    putlDetail("users/");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                Toast.makeText(MarkAttnd.this,class_name+" Attendance Marked for "+uname,Toast.LENGTH_SHORT).show();
//            }

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
                    Intent i = new Intent(MarkAttnd.this, Login.class);
                    startActivity(i);
                }
            });


        }

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


    public void getData(final String uname, final RequestParams requestParams) {

        String url = "http://52.53.196.31:5000/users/";
        url = url.concat(uname);
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

                    JSONArray JT = json.getJSONArray("class273");
                    JSONArray JL = json.getJSONArray("class275");

                    if (JT != null) {
                        for (int i = 0; i < JT.length(); i++) {

                            JSONObject jObj = JT.getJSONObject(i);

                            date[i] = jObj.getString("date");
                            Log.d("Dates", String.valueOf(date[i]));

                            //  sem3_id[i] = jObj.getString("sem3id");
                            // Log.d("Hmap-----", String.valueOf(hmap));
                            //sem3id = String.valueOf(jObj.getString("sem3id"));
                        }

                        Log.d("Check------", String.valueOf(JT));

                        if(JL != null){
                            for (int j = 0; j< JL.length(); j++){
                                JSONObject jObj = JT.getJSONObject(j);

                                date[j] = jObj.getString("date");
                                Log.d("Dates", String.valueOf(date[j]));
                            }
                        }

                    }else{
                        if (macid.equals("B8:27:EB:5D:2B:E5")) {
                            class_name = "class273";
                            try {
                                putlDetail("users/");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(MarkAttnd.this, class_name + " Attendance Marked for " + uname, Toast.LENGTH_SHORT).show();
                        } else if (macid.equals("78:A5:04:29:02:BD")) {
                            class_name = "class275";
                            try {
                                putlDetail("users/");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(MarkAttnd.this, class_name + " Attendance Marked for " + uname, Toast.LENGTH_SHORT).show();
                        }
                    }





                    for (int j = 0; j < 10; j++) {
                        if (date[j].equals(dates)) {
                            Toast.makeText(MarkAttnd.this, "Already Marked", Toast.LENGTH_SHORT).show();
                        } else {
                            if (macid.equals("B8:27:EB:5D:2B:E5")) {
                                class_name = "class273";
                                try {
                                    putlDetail("users/");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(MarkAttnd.this, class_name + " Attendance Marked for " + uname, Toast.LENGTH_SHORT).show();
                            } else if (macid.equals("78:A5:04:29:02:BD")) {
                                class_name = "class275";
                                try {
                                    putlDetail("users/");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(MarkAttnd.this, class_name + " Attendance Marked for " + uname, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    // Toast.makeText(getApplicationContext(), "Error Occurred [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();

                    //    Toast.makeText(MarkAttnd.this, "Exception Caught", Toast.LENGTH_SHORT).show();
                    if (macid.equals("B8:27:EB:5D:2B:E5")) {
                        class_name = "class273";

                        try {
                            putlDetail("users/");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }

                        Toast.makeText(MarkAttnd.this, class_name + " Attendance Marked for " + uname, Toast.LENGTH_SHORT).show();
                    } else if (macid.equals("78:A5:04:29:02:BD")) {
                        class_name = "class275";
                        try {
                            putlDetail("users/");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }

                        Toast.makeText(MarkAttnd.this, class_name + " Attendance Marked for " + uname, Toast.LENGTH_SHORT).show();
                    }

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

}
