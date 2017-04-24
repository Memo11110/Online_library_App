package com.fsu.mobile.dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fsu.mobile.model.Book;
import com.fsu.mobile.model.Copy;
import com.fsu.mobile.util.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ResponseFuture;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class BookDao {
    Context context;
    public BookDao(Context context){
        this.context = context;
    }

    public List<Book> getAllBooks(ProgressDialog progressDialog){
        List<Book> res = new ArrayList<Book>();
        JsonObject jsonObject ;
        String url;
        try {
            jsonObject = Ion.with(context, Constants.SERVER_URL + "book/list")
                    .progressDialog(progressDialog)
                    .asJsonObject()
                    .get();
            if(!jsonObject.toString().equals("{}")) {
                JsonArray jsonArray = jsonObject.get("values").getAsJsonArray();
                int i = 0;
                while (i < jsonArray.size()) {
                    Book b = new Book();
                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                    b.setTitle(jsonObject1.get("bookTitle").getAsString());
                    url = Constants.RESOURCE_URL +jsonObject1.get("coverImage").getAsString();
                    url  = url.replaceAll(" ","%20");
                    b.setImageCoverURL(url);
                    b.setAutor(jsonObject1.get("bookAutor").getAsString());
                    b.setQuantity(jsonObject1.get("avaibleQuantity").getAsInt());
                    b.setBookId(jsonObject1.get("bookId").getAsInt());
                    b.setCategoryName(jsonObject1.get("category").getAsJsonObject().get("categoryName").getAsString());
                    b.setIsbn(jsonObject1.get("isbn").getAsString());
                    res.add(b);
                    i++;
                }

                progressDialog.hide();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return res;
    }

    public Book findBookByISBN(String isbn){
        Book res = null;
        JsonObject jsonObject;
        String url;
        try {
            jsonObject = Ion.with(context, Constants.SERVER_URL + "book/find/isbn/"+isbn)
                        .asJsonObject()
                        .get();
            int bookId = jsonObject.get("bookId").getAsInt();
            if(bookId == -1){
                return null;
            }
            else{
                res = new Book();
                res.setBookId(bookId);
                res.setTitle(jsonObject.get("bookTitle").getAsString());
                res.setAutor(jsonObject.get("bookAutor").getAsString());
                res.setQuantity(jsonObject.get("avaibleQuantity").getAsInt());
                url = Constants.RESOURCE_URL +jsonObject.get("coverImage").getAsString();
                url  = url.replaceAll(" ","%20");
                res.setImageCoverURL(url);
                res.setCategoryName(jsonObject.get("category").getAsJsonObject().get("categoryName").getAsString());
                res.setIsbn(jsonObject.get("isbn").getAsString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return res;
    }
    public Book findBookByTitle(String title){
        Book res = null;
        JsonObject jsonObject;
        String url;
        try {
            String tmp = title.replaceAll(" ", "%20");
            jsonObject = Ion.with(context,Constants.SERVER_URL + "book/find/title/"+tmp)
                    .asJsonObject()
                    .get();
            int bookId = jsonObject.get("bookId").getAsInt();
            if(bookId == -1){
                return null;
            }
            else{
                res = new Book();
                res.setBookId(bookId);
                res.setTitle(jsonObject.get("bookTitle").getAsString());
                res.setAutor(jsonObject.get("bookAutor").getAsString());
                res.setQuantity(jsonObject.get("avaibleQuantity").getAsInt());
                url = Constants.RESOURCE_URL +jsonObject.get("coverImage").getAsString();
                url  = url.replaceAll(" ","%20");
                res.setImageCoverURL(url);
                res.setCategoryName(jsonObject.get("category").getAsJsonObject().get("categoryName").getAsString());
                res.setIsbn(jsonObject.get("isbn").getAsString());

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return res;
    }

    public ArrayList<Copy> getAvailableCopies(int bookId){
        ArrayList<Copy> res = new ArrayList<Copy>();

        JsonObject jsonObject ;
        try {
            jsonObject = Ion.with(context, Constants.SERVER_URL + "book/list/copies/"+bookId)
                    .asJsonObject()
                    .get();
            if(!jsonObject.toString().equals("{}")) {
            JsonArray jsonArray = jsonObject.get("values").getAsJsonArray();
            int i =0;
            while(i<jsonArray.size()){
                JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                Copy tmp = new Copy();
                tmp.setAccessionNumber(jsonObject1.get("accessionNumber").getAsString());
                tmp.setCopyId(jsonObject1.get("copyId").getAsInt());
                res.add(tmp);
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
    public ArrayList<Copy> getBorrowedCopies(int bookId,int studentId){
        ArrayList<Copy> res = new ArrayList<Copy>();
        JsonArray jsonArray = null;
        JsonObject jsonObject ;
        try {
            jsonObject = Ion.with(context, Constants.SERVER_URL + "book/list/borrowedCopies?studentId="+studentId+"&bookId="+bookId)
                    .asJsonObject()
                    .get();
            if(!jsonObject.toString().equals("{}")) {
                jsonArray = jsonObject.get("values").getAsJsonArray();
                int i = 0;
                while (i < jsonArray.size()) {
                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                    Copy tmp = new Copy();
                    tmp.setAccessionNumber(jsonObject1.get("accessionNumber").getAsString());
                    tmp.setCopyId(jsonObject1.get("copyId").getAsInt());
                    res.add(tmp);
                    i++;
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

        return res;
    }

    public static Copy getCopyByID(int copyId,Context context){
        JsonObject jsonObject ;
        Copy res = new Copy();
        try {
            jsonObject = Ion.with(context, Constants.SERVER_URL + "book/copy/"+copyId)
                    .asJsonObject()
                    .get();
            if(!jsonObject.toString().equals("{}")) {
                res.setCopyId(copyId);
                res.setAccessionNumber(jsonObject.get("accessionNumber").getAsString());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    return res;
    }
    public static void reportDamage(int studentId,int copyId,String value,Context context){
        Ion.with(context, Constants.SERVER_URL + "book/report")
                .setBodyParameter("studentId", studentId + "")
                .setBodyParameter("copyId", copyId + "")
                .setBodyParameter("value", value)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                    }
                });

    }

}
