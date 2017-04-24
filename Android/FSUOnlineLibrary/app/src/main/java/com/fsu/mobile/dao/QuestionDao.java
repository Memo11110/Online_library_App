package com.fsu.mobile.dao;

import android.content.Context;
import android.widget.Toast;

import com.fsu.mobile.model.Book;
import com.fsu.mobile.model.Question;
import com.fsu.mobile.util.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class QuestionDao {

    public static List<Question> getAllQuestions(int bookId,Context context){
        List<Question> res = new ArrayList<Question>();
        JsonObject jsonObject ;
        try {
            jsonObject = Ion.with(context, Constants.SERVER_URL + "book/list/answredQuestions/"+bookId)
                    .asJsonObject()
                    .get();
            if(!jsonObject.toString().equals("{}")) {
                JsonArray jsonArray = jsonObject.get("values").getAsJsonArray();
                int i = 0;
                while (i < jsonArray.size()) {
                    Question q = new Question();
                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                    q.setQuestionId(jsonObject1.get("questionId").getAsInt());
                    q.setAnswer(jsonObject1.get("response").getAsString());
                    q.setValue(jsonObject1.get("value").getAsString());
                    res.add(q);
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

    public static void sendQuestion(int studentId,int bookId, String value,Context context){
        Ion.with(context, Constants.SERVER_URL + "book/send/question")
                .setBodyParameter("studentId", studentId + "")
                .setBodyParameter("bookId", bookId + "")
                .setBodyParameter("value", value)
                .asJsonObject()
                ;
    }
}
