package com.fsu.mobile.dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.fsu.mobile.LoginActivity;
import com.fsu.mobile.activity.RegistrationActivity;
import com.fsu.mobile.util.Constants;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 *
 */
public class RegistrationDao {

    public static void sendMembershipRequest(String firstName,String lastName,String email,String password, final RegistrationActivity registrationActivity, final ProgressDialog progressDialog){
        Ion.with(registrationActivity)
                .load(Constants.SERVER_URL+"student/registrationRequest/")
                .progressDialog(progressDialog)
                .setBodyParameter("firstName",firstName)
                .setBodyParameter("lastName",lastName)
                .setBodyParameter("email",email)
                .setBodyParameter("password",password)
                .asJsonObject()
                .setCallback(registrationActivity);
    }
}
