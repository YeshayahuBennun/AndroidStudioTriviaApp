package com.ybennun.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.snackbar.Snackbar;
import com.ybennun.trivia.data.Repository;
import com.ybennun.trivia.databinding.ActivityMainBinding;
import com.ybennun.trivia.model.Question;
import com.ybennun.trivia.model.Score;
import com.ybennun.trivia.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentQuestionIndex = 0;
    List<Question> questionList;
    private int scoreCounter = 0;
    private Score score;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        score = new Score();
        prefs = new Prefs(MainActivity.this);

        // Retrieve the last state
        currentQuestionIndex = prefs.getState();

        binding.scoreText.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        binding.highestScoreText.setText(MessageFormat.format("Highest: {0}", String.valueOf(prefs.getHighestScore())));

        questionList = new Repository().getQuestions(this::updateCounter
        );

        binding.buttonNext.setOnClickListener(view -> {
            getNextQuestion();
        });

        binding.buttonTrue.setOnClickListener(view -> {
            checkAnswer(true);
            updateQuestion();

        });

        binding.buttonFalse.setOnClickListener(view -> {
            checkAnswer(false);
            updateQuestion();
        });
    }

    private void getNextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();
    }

    private void checkAnswer(boolean userChoseCorrect) {
        boolean answer = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMessageId = 0;
        if (userChoseCorrect == answer) {
            snackMessageId = R.string.correct_answer;
            fadeAnimation();
            addPoints();
        } else {
            snackMessageId = R.string.incorrect_answer;
            shakeAnimation();
            deductPoints();
        }

        Snackbar.make(binding.cardView, snackMessageId, Snackbar.LENGTH_SHORT).show();

    }

    private void updateCounter(ArrayList<Question> questionArrayList) {
        binding.questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
        binding.textViewOutOf.setText(String.format(getString(R.string.text_formatted), currentQuestionIndex, questionArrayList.size()));
    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 1.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        binding.questionTextView.setText(question);
        updateCounter((ArrayList<Question>) questionList);
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);

        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void deductPoints() {

        if (scoreCounter > 0) {
            scoreCounter -= 100;
            score.setScore(scoreCounter);
            binding.scoreText.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));

        } else {
            scoreCounter = 0;
            score.setScore(scoreCounter);

        }
    }

    private void addPoints() {
        scoreCounter += 100;
        score.setScore(scoreCounter);
        binding.scoreText.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
    }

    @Override
    protected void onPause() {
        prefs.saveHighestScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        Log.d("State", "onPause: saving state " + prefs.getState());
        Log.d("Pause", "onPaused: saving score " + prefs.getHighestScore());
        super.onPause();
    }
}