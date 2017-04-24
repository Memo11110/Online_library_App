package com.fsu.mobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fsu.mobile.LoginActivity;
import com.fsu.mobile.dao.RegistrationDao;
import com.fus.mobile.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

public class RegistrationActivity extends Activity implements FutureCallback<JsonObject> {

    Button btnSubmit;
    EditText firstName,lastName,password,passwordConfirm,email;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        firstName = (EditText)findViewById(R.id.regfirstName);
        lastName = (EditText)findViewById(R.id.reglastName);
        password = (EditText)findViewById(R.id.regNewPassword);
        passwordConfirm = (EditText)findViewById(R.id.regPasswordConfirm);
        email = (EditText)findViewById(R.id.newLogin);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstName.getText().toString().equals("")||
                        lastName.getText().toString().equals("")||
                        email.getText().toString().equals("")||
                        password.getText().toString().equals("")||
                        passwordConfirm.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please fill all fields!!", Toast.LENGTH_LONG).show();
                } else if(!password.getText().toString().equals(passwordConfirm.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please try again, the password doesn't match ", Toast.LENGTH_LONG).show();
                } else {
                    makeRequest();
                }
            }
        });

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeRequest(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait!");
        RegistrationDao.sendMembershipRequest(firstName.getText().toString(),lastName.getText().toString(),
        email.getText().toString(),password.getText().toString(),this,progressDialog);
    }

    @Override
    public void onCompleted(Exception e, JsonObject result) {
        progressDialog.hide();
        Toast.makeText(this, "Membership request sent successfully!!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
