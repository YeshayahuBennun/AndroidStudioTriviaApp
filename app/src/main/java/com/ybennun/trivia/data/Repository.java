package com.ybennun.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ybennun.trivia.controller.AppController;
import com.ybennun.trivia.model.Question;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    ArrayList<Question> questionsArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    Question question = new Question(
                            response.getJSONArray(i).get(0).toString(),
                            response.getJSONArray(i).getBoolean(1)
                    );
                    //Add questions to arrayList/list
                    questionsArrayList.add(question);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {

        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionsArrayList;
    }
}
