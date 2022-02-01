package com.ybennun.trivia.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String HIGHEST_SCORE = "highest_score";
    private SharedPreferences preferences;

    public Prefs(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighestScore(int score) {
        int currentScore = score;

        int lastScore = preferences.getInt("highest_score", 0);

        if (currentScore > lastScore) {
            //we have a new highest score.
            preferences.edit().putInt(HIGHEST_SCORE, currentScore).apply();
        }


    }

    public int getHighestScore() {
        return preferences.getInt(HIGHEST_SCORE, 0);
    }
}
