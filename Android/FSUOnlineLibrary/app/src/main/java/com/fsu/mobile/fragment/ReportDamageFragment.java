package com.fsu.mobile.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fsu.mobile.dao.BookDao;
import com.fsu.mobile.dao.BorrowDao;
import com.fsu.mobile.model.Book;
import com.fsu.mobile.model.Copy;
import com.fsu.mobile.util.AppController;
import com.fsu.mobile.util.Utils;
import com.fus.mobile.R;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 */
public class ReportDamageFragment extends DialogFragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Book book = (Book) getActivity().getIntent().getSerializableExtra("selectedBook");

        final ArrayList<Copy> availableCopies = new BookDao(getActivity()).getAvailableCopies(book.getBookId());
        String[] accessionNumbers = new String[availableCopies.size()];
        int i = 0;
        for(Copy cp :availableCopies){
            accessionNumbers[i] = cp.getAccessionNumber();
            i++;
        }
        final View view = inflater.inflate(R.layout.report_damage_fragment, null);

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
                        int copyId = availableCopies.get(spinner.getSelectedItemPosition()).getCopyId();
                        int studentId = AppController.sharedPreferences.getInt("studentId", 0);
                        TextView value = (TextView) view.findViewById(R.id.value);
                        if (value.getText().toString().trim().equals("")) {
                            value.setError("This input is required !");
                            getDialog().onContentChanged();
                        } else {
                            BookDao.reportDamage(studentId, copyId, value.getText().toString(), getActivity().getApplicationContext());
                            Toast.makeText(getActivity().getApplicationContext(), "Report sent successfully !", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().dismiss();
                    }
                });

        return builder.create();
    }

}
