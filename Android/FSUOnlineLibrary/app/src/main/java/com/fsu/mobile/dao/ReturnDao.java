package com.fsu.mobile.dao;

import android.app.ProgressDialog;
import android.content.Context;

import com.fsu.mobile.model.Book;
import com.fsu.mobile.model.Return;
import com.fsu.mobile.util.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class ReturnDao {
    private Context context;
    public ReturnDao(Context context){
        this.context = context;
    }

    public List<Return> getAllBooks(ProgressDialog progressDialog,int studentId){
        List<Return> res = new ArrayList<Return>();
        JsonObject jsonObject ;
        String url;
        try {
            jsonObject = Ion.with(context, Constants.SERVER_URL + "return/list/"+studentId+"")
                    .progressDialog(progressDialog)
                    .asJsonObject()
                    .get();
            if(!jsonObject.toString().equals("{}")) {
                JsonArray jsonArray = jsonObject.get("values").getAsJsonArray();
                int i = 0;
                while (i < jsonArray.size()) {
                    Return r = new Return();
                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                    r.setBookTitle(jsonObject1.get("bookTitle").getAsString());
                    r.setCopyAN(jsonObject1.get("copyAccessionNumber").getAsString());
                    r.setDate(jsonObject1.get("date").getAsString());
                    res.add(r);
                    i++;
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return res;
    }
}
