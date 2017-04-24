package com.fsu.mobile.dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.fsu.mobile.model.Borrow;
import com.fsu.mobile.model.Return;
import com.fsu.mobile.util.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class BorrowDao {

    public static boolean sendRequest(int studentId,int copyId,Date startDate,Date returnDate, final Context context){
        class Rs{
            boolean status=false;
        }
        final Rs rs= new Rs();
        Ion.with(context, Constants.SERVER_URL + "borrow/send")
                .setBodyParameter("studentId", studentId + "")
                .setBodyParameter("copyId", copyId + "")
                .setBodyParameter("dateOfRent", startDate.toGMTString())
                .setBodyParameter("dateReturn", returnDate.toGMTString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        rs.status = true;
                    }
                });

        try { Thread.sleep(200); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return  true;
    }

    public static void makeReturn(int studentId,int copyId, final Context context){

        Ion.with(context, Constants.SERVER_URL + "book/return")
                .setBodyParameter("studentId", studentId + "")
                .setBodyParameter("copyId", copyId + "")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Toast.makeText(context,"Return done successfully !",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public List<Borrow> getAllBorrows(ProgressDialog progressDialog,int studentId,Context context){
        List<Borrow> res = new ArrayList<Borrow>();
        JsonObject jsonObject ;
        String url;
        try {
            jsonObject = Ion.with(context, Constants.SERVER_URL + "borrow/list/"+studentId+"")
                    .progressDialog(progressDialog)
                    .asJsonObject()
                    .get();
            if(!jsonObject.toString().equals("{}")) {
                JsonArray jsonArray = jsonObject.get("values").getAsJsonArray();
                int i = 0;
                while (i < jsonArray.size()) {
                    Borrow r = new Borrow();
                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                    r.setBorrowId(jsonObject1.get("borrowId").getAsInt());
                    r.setBookTitle(jsonObject1.get("bookTitle").getAsString());
                    r.setCopyAN(jsonObject1.get("copyAN").getAsString());
                    r.setDate(jsonObject1.get("date").getAsString());
                    r.setStatus(jsonObject1.get("status").getAsString());
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
