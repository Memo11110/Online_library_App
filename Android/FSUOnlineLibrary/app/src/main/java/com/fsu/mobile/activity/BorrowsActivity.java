package com.fsu.mobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fsu.mobile.dao.BorrowDao;
import com.fsu.mobile.model.Borrow;
import com.fsu.mobile.model.Return;
import com.fsu.mobile.util.AppController;
import com.fus.mobile.R;

import java.util.List;

public class BorrowsActivity extends Activity {
    List<Borrow> borrows;
    ListView mBorrowsList;
    ProgressDialog progressDialog;
    BorrowDao borrowDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrows);

        mBorrowsList = (ListView)findViewById(R.id.borrows);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        int studentId = AppController.sharedPreferences.getInt("studentId",0);
        borrowDao = new BorrowDao();

        if(studentId == 0){
            Toast.makeText(this, "An error occurs , plz try again", Toast.LENGTH_LONG).show();
        }
        else{
            mBorrowsList = (ListView)findViewById(R.id.borrows);
            borrows = borrowDao.getAllBorrows(progressDialog,studentId,this);
            mBorrowsList.setAdapter(new BorrowAdapter(this,borrows));
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

class BorrowAdapter extends ArrayAdapter<Borrow> {
    List<Borrow> returns;
    Context context;
    public BorrowAdapter(Context context,List<Borrow> returns){
        super(context, R.layout.return_row,returns);
        this.context = context;
        this.returns = returns;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.borrow_row, parent, false);

        TextView num = (TextView)rowView.findViewById(R.id.returnNum);
        TextView bookTitle = (TextView)rowView.findViewById(R.id.bookTitle);
        TextView copyAN = (TextView)rowView.findViewById(R.id.copyAN);
        TextView date = (TextView)rowView.findViewById(R.id.date);
        TextView status = (TextView)rowView.findViewById(R.id.status);

        Borrow b = returns.get(position);
        num.setText("Borrow n°: "+(position+1));
        bookTitle.setText("Book Title: "+b.getBookTitle());
        copyAN.setText("Copy Code: "+b.getCopyAN());
        date.setText("Date: "+b.getDate());
        status.setText("Status: "+b.getStatus());

        return rowView;
    }
}