 package com.example.remotemonitoringapp;

 import android.app.ProgressDialog;
 import android.content.Context;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.os.Bundle;
 import android.os.Handler;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.view.View;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.widget.Toolbar;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

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
 import com.google.android.material.bottomnavigation.BottomNavigationView;
 import com.google.android.material.floatingactionbutton.FloatingActionButton;

 import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

 public class aob_patient extends AppCompatActivity {

     aobPatientAdapter adapter;
     RecyclerView recyclerView;
     BottomNavigationView bottom_view;
     String active_user_id,company_id;
     int success;
     Toolbar toolbar;
     private SessionHandler session;
     private static final String KEY_SESSION_ID = "session_id";
     private static final String BASE_URL = "http://p.sparksschools.com/Mobile/Patient/";
     private String KEY_LOGGED = "EventsLoggedStat";
     private String PREF_NAME = "Pop-InEventsSession";
     private static final String KEY_USER_ID = "EventUserId";
    private FloatingActionButton floatingActionButton;

    private String onlineUserID;

    private ProgressDialog loader;

    private String key = "";
    private String task;
    private String description;
     Handler mHandler;
     private RequestQueue mRequestQueue;

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
        setContentView(R.layout.activity_aob);

        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (sp.getBoolean(KEY_LOGGED, false)) {
            active_user_id = sp.getString(KEY_USER_ID, "");
        } else {
            Intent i = new Intent(getApplicationContext(), patientLogin.class);
            startActivity(i);
        }

        //automatic process
        try {
            volleyJsonObjectRequest(BASE_URL + "show_employee_aob.php");
            volleyJsonObjectRequestAsync(BASE_URL + "get_employee_info.php");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        toolbar = findViewById(R.id.aobToolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("AOB");


        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(aob_patient.this, upload_aob_patient.class);
                startActivity(intent);
                finish();

            }

        });


    }


     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aob_menu,menu);
         return super.onCreateOptionsMenu(menu);
     }

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.communication_menu:
                start_comm_activity();
                break;
            case R.id.task_menu_btn:
                start_task_activity();
                break;
            /*case R.id.logout:
                Intent intent = new Intent(taskpage_employee.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();*/
        }
         return super.onOptionsItemSelected(item);
     }

     public void start_comm_activity() {
         Intent i = new Intent(getApplicationContext(), communication_patient.class);
         i.putExtra("Company Id", company_id);
         startActivity(i);
         finish();
     }

     public void start_task_activity() {
         Intent i = new Intent(getApplicationContext(), taskpage_patient.class);
         startActivity(i);
         finish();
     }

     public void load_recycler(String json) throws JSONException
     {
         JSONArray jsonArray = new JSONArray(json);

         List<aobData> list = new ArrayList<>();
         //list = getData();

         for(int i=0; i<jsonArray.length();i++) {
             final JSONObject jObject = (JSONObject)jsonArray.get(i);
             list.add(new aobData("Title :"+jObject.getString("aob"),"Created by: "+jObject.getString("created_by"),
                     "Addressed to: "+jObject.getString("addressed_to"),jObject.getString("aob_id")));

         }

         recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
         adapter = new aobPatientAdapter(list, getApplication());
         recyclerView.setAdapter(adapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(aob_patient.this));

     }

     public void volleyJsonObjectRequest(String url) throws JSONException {

         String  REQUEST_TAG = "managerTasks";

         final ProgressDialog progressDialog = new ProgressDialog(this);
         progressDialog.setMessage("Accessing available aob...");
         progressDialog.show();

         StringRequest jsonObjectReq = new StringRequest(Request.Method.POST,url,
                 new Response.Listener<String>() {

                     @Override
                     public void onResponse(String response) {


                         progressDialog.hide();

                         try {

                             //Toast.makeText(Task.this,response,Toast.LENGTH_LONG).show();

                             load_recycler(response);

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
                 //request_map.put(KEY_SESSION_ID,active_user_id);
                 request_map.put(KEY_SESSION_ID,active_user_id);
                 return request_map;
             }


         };

         // Adding JsonObject request to request queue
         //mRequestQueue.add(jsonObjectReq);
         AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
     }

     public void volleyJsonObjectRequestAsync(String url) throws JSONException {

         String REQUEST_TAG = "getUserData";

        /*final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching user information...");
        progressDialog.show();*/

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
                             //UserID = jsonObject.getString("user_id");

                             if (success == 1) {
                                 company_id = jsonObject.getString("company_id");
                             } else {
                                 Toast.makeText(aob_patient.this, "Failed to get user information.", Toast.LENGTH_LONG).show();
                             }

                         } catch (JSONException e) {
                             e.getMessage();
                         }


                         //progressDialog.hide();

                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 VolleyLog.d("Error: " + error.getMessage());
                 //progressDialog.hide();
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
