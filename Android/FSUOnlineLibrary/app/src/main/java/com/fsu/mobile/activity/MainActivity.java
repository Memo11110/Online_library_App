package com.fsu.mobile.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fsu.mobile.LoginActivity;
import com.fsu.mobile.dao.BookDao;
import com.fsu.mobile.fragment.FindBookDialog;
import com.fsu.mobile.model.Book;
import com.fsu.mobile.util.AppController;
import com.fsu.mobile.util.Constants;
import com.fsu.mobile.util.Utils;
import com.fus.mobile.R;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements  FindBookDialog.SearchListener{

    private ListView mListBooks;
    List<Book> books = new ArrayList<Book>();
    ProgressDialog pProgressDialog;
    SharedPreferences prefs;
    BookDao bookDao;
    BooksAdapter booksAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pProgressDialog =  new ProgressDialog(this);
        pProgressDialog.setMessage("Loading books,please wait...");
        bookDao =  new BookDao(this);
       // books = bookDao.getAllBooks(pProgressDialog);
        mListBooks = (ListView)findViewById(R.id.booksList);

        mListBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),BookActivity.class);
                intent.putExtra("selectedBook",books.get(position));
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        pProgressDialog.show();
        books = bookDao.getAllBooks(pProgressDialog);
        booksAdapter = new BooksAdapter(this,books);
        mListBooks.setAdapter(booksAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.editProfile) {
            startActivity(new Intent(this,EditProfileActivity.class));
            finish();
            return true;
        }
        else if (id == R.id.logout) {
            SharedPreferences.Editor editor = AppController.sharedPreferences.edit();
            editor.putBoolean("loggedIn",false);
            editor.commit();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
            return true;
        }
        else if(id == R.id.refresh){
            books = new ArrayList<Book>();
            books = new BookDao(this).getAllBooks(pProgressDialog);
            pProgressDialog.hide();
            mListBooks = (ListView)findViewById(R.id.booksList);
            mListBooks.setAdapter(new BooksAdapter(this,books));
            return true;
        }
        else if(id == R.id.search){
            DialogFragment findDialogFragment = new FindBookDialog();
            findDialogFragment.show(getFragmentManager(),"Find a Book");
            return  true;
        }
        else if(id == R.id.returns){
            startActivity(new Intent(this,ReturnsActivity.class));
            return  true;
        }
        else if(id == R.id.borrows){
            startActivity(new Intent(this,BorrowsActivity.class));
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(int indexCreteria,String value) {
        Book book = null;

        if(indexCreteria == 0){
            book = bookDao.findBookByTitle(value);
        }
        else
            book = bookDao.findBookByISBN(value);

        books.clear();
        if(book == null){
            mListBooks.setEmptyView(getLayoutInflater().inflate(R.layout.nobooks_view, null));
            Toast.makeText(this,"No book found !!",Toast.LENGTH_LONG).show();
        }
        else{
            books.add(book);
        }
        booksAdapter.notifyDataSetInvalidated();
    }
}

class BooksAdapter extends ArrayAdapter<Book>{
    List<Book> books;
    Context context;
    public BooksAdapter(Context context,List<Book> books){
        super(context, R.layout.book_row,books);
        this.books = books;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.book_row, parent, false);
        TextView titleView = (TextView) rowView.findViewById(R.id.bookTitle);
        TextView autorView = (TextView) rowView.findViewById(R.id.bookAutor);
        TextView quantityView = (TextView) rowView.findViewById(R.id.availableCopiesNbr);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.coverImage);
        Book book =books.get(position);
        titleView.setText(book.getTitle());
        autorView.setText("By: "+book.getAutor());
        quantityView.setText(book.getQuantity() + "");

        Ion.with(context)
                .load(book.getImageCoverURL())
                .withBitmap()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .animateIn(android.R.anim.slide_out_right)
                .intoImageView(imageView);

        return rowView;
    }

}