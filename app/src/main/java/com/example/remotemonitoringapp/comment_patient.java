package com.example.remotemonitoringapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class comment_patient extends AppCompatActivity {

    private ImageButton create_comment_btn;
    private ProgressDialog pBar;
    private static final String STRING_EMPTY = "";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_TASK_ID = "task_id";
    private String comment;
    private EditText commentTxt;
    private String PatientEmail,PatientName;
    private TextView task_desc,task_employer_show;
    private String UserID;
    private int success;
    private static final String BASE_URL = "http://p.sparksschools.com/Mobile/Patient/";
    private SessionHandler session;

    private RequestQueue mRequestQueue;
    private SwipeRefreshLayout swipeContainer;

    private String KEY_LOGGED = "EventsLoggedStat";
    private String PREF_NAME = "Pop-InEventsSession";
    private static final String KEY_USER_ID = "EventUserId";
    String active_user_id;
    String task_id;
    private static final String KEY_SESSION_ID = "session_id";
    Handler mHandler;
    commentAdapter adapter;
    RecyclerView recyclerView;
    List<commentData> list;
    private static final String TAG = "MainActivity";
    Firebase reference1, reference2;
    LinearLayout layout;
    RelativeLayout layout_2;
    ScrollView scrollView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(getApplicationContext(), taskpage_patient.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_patient);

        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (sp.getBoolean(KEY_LOGGED, false)) {
            active_user_id = sp.getString(KEY_USER_ID,"");
        } else {
            Intent i = new Intent(getApplicationContext(), patientLogin.class);
            startActivity(i);
        }

        Bundle extras = getIntent().getExtras();

        if(extras !=null){
            task_id = extras.getString("Task Id");
        }

        list = new ArrayList<>();
        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);
        scrollView = findViewById(R.id.scrollView);

        task_desc = (TextView)findViewById(R.id.task_desc_employee);
        task_employer_show = (TextView)findViewById(R.id.task_employer);

        //automatic process
        try {
            volleyJsonObjectRequest(BASE_URL + "get_employee_task_desc.php");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        commentTxt = (EditText)findViewById(R.id.commentText);
        create_comment_btn = (ImageButton)findViewById(R.id.btnComment);


        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://pregnancy-planner-default-rtdb.firebaseio.com/messages/Remote_Pregnancy_Appointment_App");

        create_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = commentTxt.getText().toString();

                if(!messageText.equals("") && !PatientEmail.equals("") && !PatientName.equals("") && !task_id.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", active_user_id);
                    map.put("user_email", PatientEmail);
                    map.put("user_name", PatientName);
                    map.put("appointment_id", task_id);
                    reference1.push().setValue(map);
                    //reference2.push().setValue(map);
                    commentTxt.setText("");
                } else {

                    Toast.makeText(comment_patient.this, "No field should be empty", Toast.LENGTH_LONG).show();

                }
            }
        });

        reference1.orderByChild("appointment_id").equalTo(task_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userId = map.get("user").toString();
                String userName = map.get("user_name").toString();

                if(userId.equals(active_user_id)){
                    addMessageBox(message,"", 1);
                }
                else{
                    addMessageBox(message,"Sent by: "+userName, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void addMessageBox(String message,String sender_text, int type){
        TextView textView = new TextView(comment_patient.this);
        textView.setText(message);

        TextView textView2 = new TextView(comment_patient.this);
        textView2.setText(sender_text);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 7.0f;

        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            lp2.topMargin = 10;
            textView.setPadding(20,20,20,20);
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            lp2.topMargin = 10;
            textView.setPadding(20,20,20,20);
            textView.setBackgroundResource(R.drawable.bubble_out);
            textView.setTextColor(Color.WHITE);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        layout.addView(textView2);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }


    public void volleyJsonObjectRequest(String url) throws JSONException {

        String REQUEST_TAG = "getTaskData";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching task description...");
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
                            //UserID = jsonObject.getString("user_id");

                            if (success == 1) {
                                PatientEmail = jsonObject.getString("employee_email");
                                PatientName = jsonObject.getString("employee_name");
                                task_desc.setText(jsonObject.getString("task"));
                                task_employer_show.setText(jsonObject.getString("employer"));
                            } else {
                                Toast.makeText(comment_patient.this, "Failed to get task description.", Toast.LENGTH_LONG).show();
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
                httpParams.put(KEY_TASK_ID, task_id);
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
