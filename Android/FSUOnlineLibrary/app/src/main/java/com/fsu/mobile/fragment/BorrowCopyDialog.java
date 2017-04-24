package com.fsu.mobile.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fsu.mobile.dao.BookDao;
import com.fsu.mobile.dao.BorrowDao;
import com.fsu.mobile.dao.StudentDao;
import com.fsu.mobile.model.Book;
import com.fsu.mobile.model.Copy;
import com.fsu.mobile.model.Student;
import com.fsu.mobile.util.AppController;
import com.fsu.mobile.util.Utils;
import com.fus.mobile.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class BorrowCopyDialog extends DialogFragment {
    Context context;
    Book book;
    ArrayList<Copy> availableCopies;

    View view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Book book = (Book) getActivity().getIntent().getSerializableExtra("selectedBook");

        availableCopies = new BookDao(getActivity()).getAvailableCopies(book.getBookId());
        String[] accessionNumbers = new String[availableCopies.size()];
        int i = 0;
        for(Copy cp :availableCopies){
            accessionNumbers[i] = cp.getAccessionNumber();
            i++;
        }
        view = inflater.inflate(R.layout.borrow_copy_fragment, null);
        final DatePicker datePickerStart = (DatePicker) view.findViewById(R.id.dateStartPicker);
        final DatePicker datePickerReturn = (DatePicker)view.findViewById(R.id.dateReturnPicker);

        final Spinner spinner = (Spinner) view.findViewById(R.id.availableCopies);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,accessionNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                       }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().dismiss();
                    }
                });

        return builder.create();


    }
    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog)getDialog();
        context = getActivity().getApplicationContext();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final Spinner spinner = (Spinner) view.findViewById(R.id.availableCopies);
                    int copyId = availableCopies.get(spinner.getSelectedItemPosition()).getCopyId();
                    int studentId = AppController.sharedPreferences.getInt("studentId", 0);

                    Date dateStart = Utils.getDateFromDatePicket((DatePicker) view.findViewById(R.id.dateStartPicker));
                    Date dateReturn = Utils.getDateFromDatePicket((DatePicker) view.findViewById(R.id.dateReturnPicker));
                    boolean generateRecipient = ((CheckBox) view.findViewById(R.id.generateRecipient)).isChecked();
                    if(dateReturn.getTime()<dateStart.getTime()){
                        Toast.makeText(context, "Invalid date of return !!, please try again", Toast.LENGTH_LONG).show();
                    }
                    else{
                        boolean rs = BorrowDao.sendRequest(studentId, copyId, dateStart, dateReturn, context);
                        if (rs) {
                            if (generateRecipient) {
                                Student student = StudentDao.getStudentById(studentId, context);
                                Copy copy = BookDao.getCopyByID(copyId, context);
                                String url = Utils.generateRecepient(true, "Borrow a book", student, book, copy, dateStart, dateReturn, context);
                                Toast.makeText(context, "Request sent successfully !,the generated recipient is located in: " + url, Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(context, "Request sent successfully !", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(context, "An error occurs !", Toast.LENGTH_LONG).show();

                        getDialog().dismiss();
                    }

                }
            });
        }
    }
}
