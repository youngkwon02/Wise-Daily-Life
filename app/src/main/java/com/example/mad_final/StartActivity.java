package com.example.mad_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class StartActivity extends AppCompatActivity {

    private TextView wdQuiz, wdQuizNo;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3, rb4, rb5;
    private Button btnNext;

    int totalQuiz;
    int quizCounter = 0;
    int score;

    ColorStateList dfRbColor;

    private QuizModel currentQuiz;

    private List<QuizModel> questionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        questionsList = new ArrayList<>();
        wdQuiz = findViewById(R.id.wdQuiz);
        wdQuizNo = findViewById(R.id.wdQuizNo);

        radioGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        rb5 = findViewById(R.id.rb5);
        btnNext = findViewById(R.id.btnNext);

        dfRbColor = rb1.getTextColors();

        addQuestions();

        totalQuiz = questionsList.size();
        showNextQuiz();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()
                        || rb5.isChecked()) {
                    if(rb1.isChecked()) {
                        score += 10;
                    } else if(rb2.isChecked()) {
                        score += 20;
                    } else if(rb3.isChecked()) {
                        score += 30;
                    } else if(rb4.isChecked()) {
                        score += 40;
                    } else if(rb5.isChecked()) {
                        score += 50;
                    }
                    showNextQuiz();
                } else {
                    Toast.makeText(StartActivity.this, "Please answer the question!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showNextQuiz() {

        radioGroup.clearCheck();
        rb1.setTextColor(dfRbColor);
        rb2.setTextColor(dfRbColor);
        rb3.setTextColor(dfRbColor);
        rb4.setTextColor(dfRbColor);
        rb5.setTextColor(dfRbColor);

        if(quizCounter < totalQuiz) {
            currentQuiz = questionsList.get(quizCounter);
            wdQuiz.setText(currentQuiz.getQuestion());
            rb1.setText(currentQuiz.getOption1());
            rb2.setText(currentQuiz.getOption2());
            rb3.setText(currentQuiz.getOption3());
            rb4.setText(currentQuiz.getOption4());
            rb5.setText(currentQuiz.getOption5());

            quizCounter++;
            btnNext.setText("Next");
            wdQuizNo.setText("Question: " + quizCounter + " / " + totalQuiz);

        } else if(quizCounter == totalQuiz){
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int date = cal.get(Calendar.DATE);
            int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
            String dowArr[] = {"일", "월", "화", "수", "목", "금", ""};
            String dow = dowArr[day_of_week - 1];
            wdQuizNo.setText("");
            wdQuiz.setText(year + "년 " + month + " 월" + date + " 일 (" + dow + ")\nScore: " + score);
            radioGroup.setVisibility(View.INVISIBLE);
        } else {
            finish();
        }
    }

    private void addQuestions() {
        questionsList.add(new QuizModel("Pineapple pizza, are you serious?", "Yes",
                "No", "wtf", "Not worth mentioning", "Mint pizza would" +
                " be better"));
        questionsList.add(new QuizModel("Mint pizza, are you serious?", "Yes",
                "No", "wtf", "Not worth mentioning", "Mint pizza would" +
                " be better"));
        questionsList.add(new QuizModel("Chocolate pizza, are you serious?", "Yes",
                "No", "wtf", "Not worth mentioning", "Mint pizza would" +
                " be better"));
        questionsList.add(new QuizModel("Sugar pizza, are you serious?", "Yes",
                "No", "wtf", "Not worth mentioning", "Mint pizza would" +
                " be better"));
        questionsList.add(new QuizModel("Spicy pizza, are you serious?", "Yes",
                "No", "wtf", "Not worth mentioning", "Mint pizza would" +
                " be better"));
    }
}