package com.example.remotemonitoringapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class reports_employee extends AppCompatActivity {

    String active_user_id;
    int success;
    private static final String KEY_SESSION_ID = "session_id";
    private static final String BASE_URL = "http://p.sparksschools.com/Mobile/Patient/";
    private String KEY_LOGGED = "EventsLoggedStat";
    private String PREF_NAME = "Pop-InEventsSession";
    private static final String KEY_USER_ID = "EventUserId";
    private RequestQueue mRequestQueue;
    public Button create_report_btn;
    CustomCalendar appointmentCalendarView;
    public HashMap<Integer,Object> dateHashmap;

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
        setContentView(R.layout.activity_reports_doctor);

        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (sp.getBoolean(KEY_LOGGED, false)) {
            active_user_id = sp.getString(KEY_USER_ID, "");
        } else {
            Intent i = new Intent(getApplicationContext(), patientLogin.class);
            startActivity(i);
        }

        appointmentCalendarView=findViewById(R.id.appointmentCalendar);


        dateHashmap = new HashMap<>();

        // Initialize description hashmap
        HashMap<Object, Property> descHashMap=new HashMap<>();

        // Initialize default property
        Property defaultProperty=new Property();

        // Initialize default resource
        defaultProperty.layoutResource=R.layout.default_view;

        // Initialize and assign variable
        defaultProperty.dateTextViewResource=R.id.text_view;

        // Put object and property
        descHashMap.put("default",defaultProperty);

        // for current date
        Property currentProperty=new Property();
        currentProperty.layoutResource=R.layout.current_view;
        currentProperty.dateTextViewResource=R.id.text_view;
        descHashMap.put("current",currentProperty);

        // for present date
        Property presentProperty=new Property();
        presentProperty.layoutResource=R.layout.present_view;
        presentProperty.dateTextViewResource=R.id.text_view;
        descHashMap.put("present",presentProperty);

        // For absent
        Property absentProperty =new Property();
        absentProperty.layoutResource=R.layout.absent_view;
        absentProperty.dateTextViewResource=R.id.text_view;
        descHashMap.put("absent",absentProperty);

        // set desc hashmap on custom calendar
        appointmentCalendarView.setMapDescToProp(descHashMap);

        // Initialize date hashmap
        //dateHashMap=new HashMap<>();

        // initialize calendar
        Calendar calendar=  Calendar.getInstance();


        try {
            volleyJsonCalendarRequest(BASE_URL + "show_employee_tasks.php",calendar);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Toast.makeText(reports_employee.this,""+dhm,Toast.LENGTH_LONG).show();



        System.out.println(" ===== "+dateHashmap+" ===== ");

        create_report_btn = (Button) findViewById(R.id.create_reports);


        create_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    volleyJsonObjectRequest(BASE_URL + "generate_employee_report.php");
                } else {
                    Toast.makeText(reports_employee.this,
                            "Ensure you are connected to the internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void process_booked_days(String response,final Calendar calendar) throws JSONException,ParseException {

        Calendar calendar1=  Calendar.getInstance();
        //Toast.makeText(reports_employee.this,response,Toast.LENGTH_LONG).show();
        JSONArray jsonArray = new JSONArray(response);

        List<taskData> list = new ArrayList<>();
        //list = getData();

        HashMap<Integer,Object> dhm = new HashMap<>();

        for(int i=0; i<jsonArray.length();i++) {

            final JSONObject jObject = (JSONObject)jsonArray.get(i);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
            String dateInString = jObject.getString("deadline");
            Date date = sdf.parse(dateInString);

            calendar.setTime(date);


            if(date.after(new Date())) {
                dhm.put(calendar.get(Calendar.DAY_OF_MONTH), "present");
            } else {
                dhm.put(calendar.get(Calendar.DAY_OF_MONTH), "absent");
            }

            dhm.put(calendar1.get(Calendar.DAY_OF_MONTH),"current");

        }

        appointmentCalendarView.setDate(calendar,dhm);



    }


    public void volleyJsonCalendarRequest(String url, final Calendar calendar) throws JSONException {

        String  REQUEST_TAG = "managerTasks";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Accessing available appointments...");
        progressDialog.show();

        StringRequest jsonObjectReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        progressDialog.hide();


                        try {



                            process_booked_days(response,calendar);



                        } catch(JSONException e) {
                            e.printStackTrace();
                        }  catch (ParseException e) {
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
                //request_map.put(KEY_SESSION_ID,active_user_id);
                request_map.put(KEY_SESSION_ID,active_user_id);
                return request_map;
            }


        };


        // Adding JsonObject request to request queue
        //mRequestQueue.add(jsonObjectReq);
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }

    public void volleyJsonObjectRequest(String url) {

        String REQUEST_TAG = "createReport";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating report and sending to email. Please wait...");
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
                                Toast.makeText(reports_employee.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(reports_employee.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                httpParams.put(KEY_SESSION_ID, active_user_id);
                return httpParams;
            }

        };

        // Adding JsonObject request to request queue
        mRequestQueue.add(jsonObjectReq);
        // Adding JsonObject request to request queue
        // AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }
}