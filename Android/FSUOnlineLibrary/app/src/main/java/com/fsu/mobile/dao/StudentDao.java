package com.fsu.mobile.dao;

import android.content.Context;

import com.fsu.mobile.model.Student;
import com.fsu.mobile.util.Constants;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;

/**
 *
 */
public class StudentDao {

    public static Student getStudentById(int studentId,Context context){
        JsonObject jsonObject;
        Student res = new Student();
        try {
            jsonObject = Ion.with(context, Constants.SERVER_URL + "student/"+studentId+"")
                    .asJsonObject()
                    .get();

            res.setStudentId(jsonObject.get("studentId").getAsInt());
            res.setFirstName(jsonObject.get("firstName").getAsString());
            res.setLastName(jsonObject.get("lastName").getAsString());
            res.setEmail(jsonObject.get("email").getAsString());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return  res;
    }
}
