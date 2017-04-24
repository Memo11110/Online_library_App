package com.fsu.mobile.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fsu.mobile.fragment.BorrowCopyDialog;
import com.fsu.mobile.fragment.ReportDamageFragment;
import com.fsu.mobile.fragment.ReturnBookFragment;
import com.fsu.mobile.model.Book;
import com.fus.mobile.R;
import com.koushikdutta.ion.Ion;

public class BookActivity extends Activity {
    Book book = null;
    Button returnBtn,borrowBtn,questionsBtn,reportDamageBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        book = (Book) getIntent().getSerializableExtra("selectedBook");

        TextView TitleTxt = (TextView)findViewById(R.id.bookTitle);
        TextView autorBook = (TextView)findViewById(R.id.bookAutor);
        TextView avaQty = (TextView)findViewById(R.id.bookAvQuantity);
        ImageView coverImage = (ImageView)findViewById(R.id.imageCover);
        TextView category = (TextView)findViewById(R.id.bookCategory);
        TextView bookISBN = (TextView)findViewById(R.id.bookISBN);

        borrowBtn = (Button)findViewById(R.id.btnBorrow);
        returnBtn = (Button)findViewById(R.id.btnReturn);
        questionsBtn = (Button)findViewById(R.id.btnQuestions);
        reportDamageBtn = (Button)findViewById(R.id.btnReportDamage);

        TitleTxt.setText("Title: "+book.getTitle());
        autorBook.setText("Autor: "+book.getAutor());
        avaQty.setText("Available copies: "+book.getQuantity());
        category.setText("Category: "+book.getCategoryName());
        bookISBN.setText("ISBN: "+book.getIsbn());

        Ion.with(coverImage)
                .error(R.drawable.placeholder)
                .animateLoad(android.R.anim.fade_out)
                .load(book.getImageCoverURL());


        borrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new BorrowCopyDialog();
                dialog.show(getFragmentManager(),"Borrow a copy");
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new ReturnBookFragment();
                dialogFragment.show(getFragmentManager(),"Return a Book");
            }
        });
        questionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QuestionsActivity.class);
                intent.putExtra("bookId",book.getBookId());
                startActivity(intent);
            }
        });
        reportDamageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new ReportDamageFragment();
                dialogFragment.show(getFragmentManager(),"Report a damage");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
