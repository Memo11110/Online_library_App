package com.fsu.mobile.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fsu.mobile.dao.QuestionDao;
import com.fsu.mobile.util.AppController;
import com.fus.mobile.R;

/**
 *
 */
public class SendNewQuestionFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.send_question_fragment,null);
        final int bookId  = getActivity().getIntent().getIntExtra("bookId",0);
        final int studentId = AppController.sharedPreferences.getInt("studentId",0);
        if(bookId == 0 || studentId == 0){
            Toast.makeText(getActivity().getApplicationContext(),"An error occured, plz try again!!",Toast.LENGTH_SHORT).show();
            dismiss();
        }
        builder.setView(view)
                .setPositiveButton("Send!",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView value = (TextView) view.findViewById(R.id.value);
                        QuestionDao.sendQuestion(studentId,bookId,value.getText().toString(),getActivity().getApplicationContext());
                        Toast.makeText(getActivity().getApplicationContext(),"Question sent successfully!!",Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });


        return builder.create();
    }
}
