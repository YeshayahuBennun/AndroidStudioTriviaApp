package com.ybennun.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ybennun.trivia.controller.AppController;
import com.ybennun.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    ArrayList<Question> questionsArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            Log.d("Repo", "onCreate: " + response.toString());
        }, error -> {
            Log.d("Repo", "onCreate: is Failed!");
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return null;
    }
}
