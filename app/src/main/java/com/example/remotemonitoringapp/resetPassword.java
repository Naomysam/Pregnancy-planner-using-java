package com.example.remotemonitoringapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class resetPassword extends AppCompatActivity {

    private EditText email_address;
    private Button reset_button;
    private ProgressDialog pBar;
    private static final String STRING_EMPTY = "";
    private static final String KEY_EMAIL = "loginEmail";
    private String UserEmail;
    private int success;
    private String UserID;
    private static final String BASE_URL = "http://p.sparksschools.com/Mobile/Patient/";
    private SessionHandler session;

    private RequestQueue mRequestQueue;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(getApplicationContext(), patientLogin.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        session = new SessionHandler(getApplicationContext());

        reset_button = (Button) findViewById(R.id.resetPassBtn);


        //User user = session.getUserDetails();


        /*if(!user.getUserID().isEmpty()) {
            load_dashboard();
        }*/
        //String barcode = getIntent().getStringExtra("code");

        email_address = (EditText) findViewById(R.id.Email);

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    reset_pass();
                } else {
                    Toast.makeText(resetPassword.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public void reset_pass() {

        if (!STRING_EMPTY.equals(email_address.getText().toString())) {
            UserEmail = email_address.getText().toString();
            volleyJsonObjectRequest(BASE_URL + "reset_password.php");
        } else {
            Toast.makeText(resetPassword.this, "No field should be empty", Toast.LENGTH_LONG).show();
        }

    }

    public void load_dashboard() {
        Intent i = new Intent(getApplicationContext(), patientLogin.class);
        startActivity(i);
        finish();
    }

    public void volleyJsonObjectRequest(String url) {

        String REQUEST_TAG = "passwordReset";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait as we reset your password...");
        progressDialog.show();

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(),1024*1024*5); // 5MB cache size

        // Setup the network to use HttpURLConnection as the HTTP client
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with cache and network
        mRequestQueue = new RequestQueue(cache,network);

        // Start the RequestQueue
        mRequestQueue.start();

        StringRequest jsonObjectReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                            success = Integer.parseInt(jsonObject.getString("success"));

                            if (success == 1) {

                                    load_dashboard();

                                    Toast.makeText(resetPassword.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(resetPassword.this, "Failed to reset your password.", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.getMessage();
                        }


                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                progressDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> httpParams = new HashMap<>();
                httpParams.put(KEY_EMAIL, UserEmail);
                return httpParams;
            }

        };

        // Adding JsonObject request to request queue
        mRequestQueue.add(jsonObjectReq);
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

}