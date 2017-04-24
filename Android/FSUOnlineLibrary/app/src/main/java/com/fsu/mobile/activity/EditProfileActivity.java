package com.fsu.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fsu.mobile.util.AppController;
import com.fsu.mobile.util.Constants;
import com.fus.mobile.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class EditProfileActivity extends Activity {

    private Button editBtn ;
    private EditText login,password,passwordConfirm;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        progressBar = new ProgressBar(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().show();
        editBtn = (Button)findViewById(R.id.btnEdit);
        login = (EditText)findViewById(R.id.newLogin);
        password =(EditText)findViewById(R.id.newPassword);
        passwordConfirm = (EditText)findViewById(R.id.passwordConfirm);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login.getText().toString().isEmpty() || password.getText().toString().isEmpty() || passwordConfirm.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill all fields!!",Toast.LENGTH_LONG).show();
                }
                else if(!password.getText().toString().equals(passwordConfirm.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please try again, the password doesn't match ",Toast.LENGTH_LONG).show();
                }
                else{
                    makeUpdateRequest();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeUpdateRequest(){
        Ion.with(this, Constants.SERVER_URL+"student/update")
                .uploadProgressBar(progressBar)
                .setBodyParameter("studentId", AppController.sharedPreferences.getInt("studentId",0)+"")
                .setBodyParameter("login",login.getText().toString())
                .setBodyParameter("newPassword",password.getText().toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                          //  Toast.makeText(getApplicationContext(),"An error occurs when updating your profile",Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),"Your profile is updated correctly",Toast.LENGTH_LONG).show();
                            onBackPressed();
                            finish();
                    }
                });
    }
}
