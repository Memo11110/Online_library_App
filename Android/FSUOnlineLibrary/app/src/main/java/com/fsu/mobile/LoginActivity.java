package com.fsu.mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fsu.mobile.activity.MainActivity;
import com.fsu.mobile.activity.RegistrationActivity;
import com.fsu.mobile.util.AppController;
import com.fsu.mobile.util.Constants;
import com.fsu.mobile.util.Utils;
import com.fus.mobile.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class LoginActivity extends Activity {

    EditText mLogin,mPassword;
    Button mLoginBtn,btnSignUp;
    private ProgressDialog pDialog;
    private ProgressBar progressBar;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = AppController.sharedPreferences;

        boolean loggedIn = prefs.getBoolean("loggedIn",false);

        if(loggedIn){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        mLogin = (EditText)findViewById(R.id.login);
        mPassword = (EditText)findViewById(R.id.password);
        mLoginBtn = (Button)findViewById(R.id.btnLogin);
        btnSignUp = (Button)findViewById(R.id.btnRegistration);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        progressBar = new ProgressBar(this);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext().getApplicationContext(), RegistrationActivity.class));
                finish();
            }
        });

    }

    private void makeRequest(){
       pDialog.show();
       Ion.with(this, Constants.SERVER_URL + "student/login")
                .uploadProgressBar(progressBar)
                .setBodyParameter("login", mLogin.getText().toString())
                .setBodyParameter("password", mPassword.getText().toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                                 @Override
                                 public void onCompleted(Exception e, JsonObject result) {
                                     pDialog.dismiss();
                                     try {
                                         int studentId = result.get("studentId").getAsInt();
                                         if (studentId == -1) {
                                             Toast.makeText(getApplicationContext(), "Invalid login or password, plz try again!", Toast.LENGTH_LONG).show();
                                         } else {
                                             prefs = AppController.sharedPreferences;
                                             SharedPreferences.Editor editor = prefs.edit();
                                             editor.putInt("studentId", studentId);
                                             editor.putBoolean("loggedIn", true);
                                             editor.commit();
                                             Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                             startActivity(intent);
                                             finish();
                                         }
                                     }
                                     catch(Exception ex){
                                         Toast.makeText(getApplicationContext(), "An error occured, plz try again!!", Toast.LENGTH_LONG).show();
                                     }
                                 }
                             }
                );
    }
}
