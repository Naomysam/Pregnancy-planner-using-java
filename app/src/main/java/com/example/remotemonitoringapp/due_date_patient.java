package com.example.remotemonitoringapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class due_date_patient extends AppCompatActivity {

    private EditText task_description;
    private Button create_task_btn;
    private ProgressDialog pBar;
    private static final String STRING_EMPTY = "";
    private static final String KEY_END_PERIOD_DATE = "deadline_date";
    private static final String KEY_DUE_DATE = "due_date";
    private String TaskDesc;
    private String PatientEmail;
    private String DeadlineDate, DueDate;
    private Calendar calendar;
    private int year, month, day,hr,mn;
    private EditText TxtStopDate, TxtStopTime;
    private String UserID;
    private int success;
    private static final String BASE_URL = "http://p.sparksschools.com/Mobile/Patient/";
    private SessionHandler session;

    private RequestQueue mRequestQueue;

    private Spinner deptSpinner, employeeSpiner;
    private ArrayAdapter<CharSequence> adapter;

    public ArrayAdapter<String> spinnerArrayAdapter;
    public List<String> employeeList;
    private SwipeRefreshLayout swipeContainer;

    private String KEY_LOGGED = "EventsLoggedStat";
    private String PREF_NAME = "Pop-InEventsSession";
    private static final String KEY_USER_ID = "EventUserId";
    String active_user_id;
    private static final String KEY_SESSION_ID = "session_id";

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(getApplicationContext(), patient_dashboard.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_patient);

        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (sp.getBoolean(KEY_LOGGED, false)) {
            active_user_id = sp.getString(KEY_USER_ID,"");
        } else {
            Intent i = new Intent(getApplicationContext(), patientLogin.class);
            startActivity(i);
        }

        task_description = (EditText) findViewById(R.id.task_desc);
        TxtStopDate = (EditText)findViewById(R.id.date);
        TxtStopTime = (EditText)findViewById(R.id.time);
        create_task_btn = (Button) findViewById(R.id.create_task);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        hr = calendar.get(Calendar.HOUR);
        mn = calendar.get(Calendar.MINUTE);


        findViewById(R.id.btn_calender_stop_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method uploadBitmap to upload image
                //volleyJsonObjectRequest(bitmap);
                //showDate(year, month+1, day);
                setDate(view,300);
            }
        });




        //automatic process
        try {

            volleyFetchPatientListRequest(BASE_URL + "show_all_due_date.php");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_recycler);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                try {
                    volleyFetchPatientListRequest(BASE_URL + "show_all_due_date.php");
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        create_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    create_task();
                } else {
                    Toast.makeText(due_date_patient.this,
                            "Ensure you are connected to the internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // start of choose date methods

    @SuppressWarnings("deprecation")
    public void setDate(View view, int status_id) {
        showDialog(status_id);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 300) {
            return new DatePickerDialog(this,
                    stopDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener stopDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    stopDate(arg1, arg2+1, arg3);
                }
            };

    private void stopDate(int year, int month, int day) {
        TxtStopDate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = LocalDate.of(year,month,day);
            String dateString = date.plusDays(280).toString();
            TxtStopTime.setText(dateString);
        }

    }


    // end of choose date methods

    public void create_task() {

        if (!STRING_EMPTY.equals(TxtStopDate.getText().toString())) {


            DeadlineDate = TxtStopDate.getText().toString();
            DueDate = TxtStopTime.getText().toString();

            volleyJsonObjectRequest(BASE_URL + "create_due_date.php");


        } else {
            Toast.makeText(due_date_patient.this, "No field should be empty", Toast.LENGTH_LONG).show();
        }

    }

    public void load_dashboard() {
        Intent i = new Intent(getApplicationContext(), patient_dashboard.class);
        startActivity(i);
        finish();
    }

    public void volleyJsonObjectRequest(String url) {

        String REQUEST_TAG = "createTask";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Calculating due date. Please wait...");
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

                            //Toast.makeText(upload_employer.this, response, Toast.LENGTH_LONG).show();

                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                            success = Integer.parseInt(jsonObject.getString("success"));

                            if (success == 1) {
                                Toast.makeText(due_date_patient.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                load_dashboard();
                            } else {
                                Toast.makeText(due_date_patient.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                httpParams.put(KEY_END_PERIOD_DATE, DeadlineDate);
                httpParams.put(KEY_DUE_DATE, DueDate);
                httpParams.put(KEY_SESSION_ID, active_user_id);
                return httpParams;
            }

        };

        // Adding JsonObject request to request queue
        mRequestQueue.add(jsonObjectReq);
        // Adding JsonObject request to request queue
        // AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

    public void volleyFetchPatientListRequest(String url) throws JSONException {

        String  REQUEST_TAG = "companies";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading page...");
        progressDialog.show();

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(),1024*1024*5); // 5MB cache size

        // Setup the network to use HttpURLConnection as the HTTP client
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with cache and network
        mRequestQueue = new RequestQueue(cache,network);

        // Start the RequestQueue
        mRequestQueue.start();

        StringRequest jsonObjectReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        progressDialog.hide();

                        try {

                            //Toast.makeText(due_date_employee.this,response,Toast.LENGTH_LONG).show();

                            JSONArray jsonArray = new JSONArray(response);


                                    JSONObject jObject2 = (JSONObject) jsonArray.get(0);

                                    TxtStopDate.setText(jObject2.getString("end_period"));
                                    TxtStopTime.setText(jObject2.getString("due_date"));



                        } catch(JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                progressDialog.hide();
            }
        }){

            @Override
            protected Map<String,String> getParams() {
                Map<String,String> request_map = new HashMap<>();
                request_map.put(KEY_SESSION_ID,active_user_id);
                return request_map;
            }


        };

        // Adding JsonObject request to request queue
        mRequestQueue.add(jsonObjectReq);
        // Adding JsonObject request to request queue
        //mRequestQueue.add(jsonObjectReq);
        //AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }
}