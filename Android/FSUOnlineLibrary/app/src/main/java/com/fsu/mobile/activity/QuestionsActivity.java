package com.fsu.mobile.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fsu.mobile.dao.QuestionDao;
import com.fsu.mobile.fragment.SendNewQuestionFragment;
import com.fsu.mobile.model.Question;
import com.fus.mobile.R;

import java.util.List;

public class QuestionsActivity extends Activity {

    ListView quesListView;
    int bookId;
    List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        bookId = getIntent().getIntExtra("bookId",0);

        questions = QuestionDao.getAllQuestions(bookId,this);

        quesListView = (ListView)findViewById(R.id.bookQuestions);

        quesListView.setAdapter(new QuestionAdapter(this,questions));

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.questions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this,BookActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        else if(id == R.id.sendQuestion){
            DialogFragment dialogFragment = new SendNewQuestionFragment();
            dialogFragment.show(getFragmentManager(),"Send new question");
        }
        return super.onOptionsItemSelected(item);
    }
}

class QuestionAdapter extends ArrayAdapter<Question>{
    List<Question> questions;
    Context context;
    public QuestionAdapter(Context context,List<Question> questions){
        super(context, R.layout.question_row,questions);
        this.questions = questions;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.question_row, parent, false);

        TextView indexTextView = (TextView)rowView.findViewById(R.id.questionIndex);
        TextView value = (TextView)rowView.findViewById(R.id.question);
        TextView answer  = (TextView)rowView.findViewById(R.id.answer);

        Question question = questions.get(position);
        indexTextView.setText((position+1)+"");
        value.setText("Q.: "+question.getValue());
        answer.setText("Ans.: "+question.getAnswer());

        return rowView;
    }
}
