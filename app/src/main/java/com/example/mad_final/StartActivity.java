package com.example.mad_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_final.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import java.util.Random;

public class StartActivity extends AppCompatActivity {

    private TextView wdQuiz, wdQuizNo, wdResult, reDate, reScore;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3, rb4, rb5;
    private Button btnNext;

    int totalQuiz;
    int quizCounter = 0;
    int score;
    int year;
    int month;
    int week;

    ColorStateList dfRbColor;

    private QuizModel currentQuiz;

    private List<QuizModel> questionsList;

    private ActivityMainBinding binding;

    public class myDBHelper extends SQLiteOpenHelper{
        public myDBHelper(StartActivity context){
            super(context, "wdlDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table wdlTBL (year char(10), month char(10), week char(10), " +
                    "score integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("drop table if exists wdlTBL;");
            onCreate(db);
        }

    }

    public void initializeDB(View view){
        myDBHelper myHelper = new myDBHelper(this);
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        myHelper.onUpgrade(sqlDB,1,2);
        sqlDB.close();
        Toast.makeText(getApplicationContext(),"Initialized",Toast.LENGTH_LONG).show();
    }

    public void insertDB(){
        myDBHelper myHelper = new myDBHelper(this);
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        sqlDB.execSQL("delete from wdlTBL where year = " + year + " and month = " + month + " and week =" + week);
        sqlDB.execSQL("insert into wdlTBL values (" + year + ", " + month + "," + week+
                "," + score +")");
        sqlDB.close();
    }

    public void searchDB(){
        myDBHelper myHelper = new myDBHelper(this);
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select * from wdlTBL;",null);

        String string1 = "날짜\n\n";
        String string2 = "점수\n\n";

        while(cursor.moveToNext()){
            String re_year = cursor.getString(0);
            String re_month = cursor.getString(1);
            String re_week = cursor.getString(2);
            String re_score = cursor.getString(3);
            string1 += re_year + "년 " + re_month + "월 " + re_week +"주차\n";
            string2 += re_score + "점\n";
        }

        reDate.setText(string1);
        reScore.setText(string2);

        cursor.close();
        sqlDB.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        questionsList = new ArrayList<>();
        wdQuiz = findViewById(R.id.wdQuiz);
        wdQuizNo = findViewById(R.id.wdQuizNo);
        wdResult = findViewById(R.id.resultText);
        reDate = findViewById(R.id.resultDate);
        reScore = findViewById(R.id.resultScore);

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
                        score += 4;
                    } else if(rb2.isChecked()) {
                        score += 8;
                    } else if(rb3.isChecked()) {
                        score += 12;
                    } else if(rb4.isChecked()) {
                        score += 16;
                    } else if(rb5.isChecked()) {
                        score += 20;
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
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH) + 1;
            int date = cal.get(Calendar.DATE);
            int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
            int offset = 7 - day_of_week;
            int satDate = date + offset;
            week = (satDate / 7) + 1;
            String dowArr[] = {"일", "월", "화", "수", "목", "금", "토"};
            String dow = dowArr[day_of_week - 1];
            wdQuizNo.setText("");
            wdQuiz.setText("");
            wdResult.setText("\n" + month + " 월 " + week + "주차 응답 완료!" + "\n" +
                    "이번주 슬기로움: " + score + "점");
            insertDB();
            searchDB();
            btnNext.setText("Finish");
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            radioGroup.setVisibility(View.INVISIBLE);
        } else {
            finish();
        }
    }

    private void addQuestions() {
        QuizModel[] quizList = {
                new QuizModel("이번주에 계획한 시간에 일어난 일수", " 0 ~ 1일",
                        " 2 ~ 3일", " 4일", " 5 ~ 6일", " 7일 모두"),
                new QuizModel("이번주에 친구와의 약속에 늦지 않은 횟수", " 1회 이하",
                        " 2회", " 3회", " 4회", " 5회 이상"),
                new QuizModel(" 이번주에 내가 좋아하는 일을 한 횟수", " 1회 이하",
                        " 2회", " 3회", " 4회", " 5회 이상"),
                new QuizModel("이번주에 누군가를 웃으면서 대한 횟수", " 2회 이하",
                        " 3회", " 4회", "5회", "6회 이상"),
                new QuizModel("이번주에 누군가에게 받은 칭찬 횟수", " 없음",
                        " 1회", " 2회", " 3회", " 4회 이상"),
                new QuizModel("이번주에 친구에게 먼저 연락한 횟수", " 없음",
                        " 1회", " 2회", " 3회", " 4회 이상"),
                new QuizModel("이번주에 강의나 과제를 제떄 수행한 횟수", " 없음",
                        " 1회", " 2회", " 3회", " 4회 이상"),
                new QuizModel("이번주에 내가 행복하다고 느낀 횟수", " 1회 이하",
                        " 2회", " 3회", " 4회", " 5회 이상"),
                new QuizModel("이번주에 누군가를 웃게 만든 횟수", " 1회 이하",
                        " 2회", " 3회", " 4회", " 5회 이상"),
                new QuizModel("이번주에 착한일을 한 횟수", " 1회 이하",
                        " 2회", " 3회", " 4회", " 5회 이상")
        };

        Random rand = new Random();
        int []randomIndexList = {-1, -1, -1, -1, -1};
        for(int i = 0; i < 5; i++) {
            int rValue = rand.nextInt(quizList.length);
            for(int k = 0; k < i; k++) {
                if(randomIndexList[k] == rValue) {
                    i--;
                    continue;
                }
            }
            randomIndexList[i] = rValue;
        }
        for(int i = 0; i < 5; i++) {
            questionsList.add(quizList[randomIndexList[i]]);
        }
    }

}