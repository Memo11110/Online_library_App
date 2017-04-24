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

import com.fsu.mobile.dao.ReturnDao;
import com.fsu.mobile.model.Return;
import com.fsu.mobile.util.AppController;
import com.fus.mobile.R;

import org.w3c.dom.Text;

import java.util.List;

public class ReturnsActivity extends Activity {

    List<Return> returnList;
    ReturnDao returnDao;
    ListView mReturns;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returns);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        returnDao = new ReturnDao(this);
        int studentId = AppController.sharedPreferences.getInt("studentId",0);
        if(studentId == 0){
            Toast.makeText(this,"An error occurs , plz try again",Toast.LENGTH_LONG).show();
        }
        else{
            mReturns = (ListView)findViewById(R.id.returns);
            mReturns.setAdapter(new ReturnAdapter(this,returnDao.getAllBooks(progressDialog,studentId)));
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

class ReturnAdapter extends ArrayAdapter<Return>{
    List<Return> returns;
    Context context;
    public ReturnAdapter(Context context,List<Return> returns){
        super(context, R.layout.return_row,returns);
        this.context = context;
        this.returns = returns;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.return_row, parent, false);
        TextView num = (TextView)rowView.findViewById(R.id.returnNum);
        TextView bookTitle = (TextView)rowView.findViewById(R.id.bookTitle);
        TextView copyAN = (TextView)rowView.findViewById(R.id.copyAN);
        TextView date = (TextView)rowView.findViewById(R.id.date);

        Return r = returns.get(position);
        num.setText("Return n°: "+(position+1));
        bookTitle.setText("Book Title: "+r.getBookTitle());
        copyAN.setText("Copy Code: "+r.getCopyAN());
        date.setText("Date: "+r.getDate());

        return rowView;
    }
}