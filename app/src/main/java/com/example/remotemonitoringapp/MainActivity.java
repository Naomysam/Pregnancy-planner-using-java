package com.example.remotemonitoringapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity{

   public Button founderLogin, employeeLogin;
    private AdView mAdView;

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        founderLogin = findViewById(R.id.founderLogin);
        employeeLogin = findViewById(R.id.employeeLogin);

        founderLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View view) {
                Intent i = new Intent(getApplicationContext(), founderRegister.class);
                startActivity(i);
                finish();
            }

        });

        employeeLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), patientLogin.class);
                startActivity(i);
                finish();

            }

        });


    }

}