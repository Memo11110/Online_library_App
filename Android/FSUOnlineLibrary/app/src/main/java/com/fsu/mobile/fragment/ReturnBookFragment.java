package com.fsu.mobile.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
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
import java.util.Date;

/**
 *
 */
public class ReturnBookFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final Book book = (Book) getActivity().getIntent().getSerializableExtra("selectedBook");
        final int studentId = AppController.sharedPreferences.getInt("studentId", 0);

        final ArrayList<Copy> availableCopies = new BookDao(getActivity()).getBorrowedCopies(book.getBookId(),studentId);

        String[] accessionNumbers = new String[availableCopies.size()];
        int i = 0;
        for(Copy cp :availableCopies){
            accessionNumbers[i] = cp.getAccessionNumber();
            i++;
        }
        final View view = inflater.inflate(R.layout.return_book_fragment, null);
        final Spinner spinner = (Spinner) view.findViewById(R.id.borrowedCopies);
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
                        BorrowDao.makeReturn(studentId, copyId, context);
                        boolean generateRecipient = ((CheckBox)view.findViewById(R.id.generateRecipient)).isChecked();
                        if (generateRecipient) {
                            Student student = StudentDao.getStudentById(studentId, context);
                            Copy copy = BookDao.getCopyByID(copyId, context);
                            String url = Utils.generateRecepient(false, "Return a book", student, book, copy, null, new Date(),context);
                            Toast.makeText(context, "The generated recipient is located in: " + url, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().dismiss();
                    }
                });

        if(availableCopies.size() == 0){
            dismiss();
            Toast.makeText(getActivity().getApplicationContext(),"No copies of this book to return !!",Toast.LENGTH_LONG).show();
        }
        return builder.create();
    }
}
