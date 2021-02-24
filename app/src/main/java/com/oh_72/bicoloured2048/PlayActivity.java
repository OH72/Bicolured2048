package com.oh_72.bicoloured2048;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity{

    public final static String LOG_TAG = "2048";

    private int height;
    private int width;

    private DrawView drawView;
    private TextView score;
    private TextView record;
    private ImageButton restart;

    private int highScore;
    private int nowScore;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.play_activity);

        getWindow().
                getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        init();

    }

    @SuppressLint("WrongViewCast")
    private void init(){
        preferences = this.getSharedPreferences("2048_PREF", Context.MODE_PRIVATE);
        editor = preferences.edit();
        highScore = preferences.getInt("HighScore", 0);

        drawView  = findViewById(R.id.surface);
        drawView.setOnTouchListener(new OnSwipeTouchListener(PlayActivity.this){
            @Override
            public void onSwipeRight() {
                Log.d(LOG_TAG, "RIGHT");
                drawView.moveRight();
                nowScore = drawView.getScore();
                score.setText("" + nowScore);
            }

            @Override
            public void onSwipeLeft() {
                Log.d(LOG_TAG, "LEFT");
                drawView.moveLeft();
                nowScore = drawView.getScore();
                score.setText("" + nowScore);
            }

            @Override
            public void onSwipeBottom() {
                Log.d(LOG_TAG, "BOTTOM");
                nowScore = drawView.getScore();
                drawView.moveBottom();
                score.setText("" + nowScore);
            }

            @Override
            public void onSwipeTop() {
                Log.d(LOG_TAG, "TOP");
                drawView.moveTop();
                nowScore = drawView.getScore();
                score.setText("" + nowScore);
            }
        });
        //window.addView(drawView);

        //widgets = new LinearLayout(this);
        //widgets.setOrientation(LinearLayout.HORIZONTAL);

        score = findViewById(R.id.score);
        //score = new TextView(this);
        score.setText("0");
        score.setPadding(10, 10, 10, 10);
        //widgets.addView(score);

        //record = new TextView(this);
        record = findViewById(R.id.record);
        record.setText("" + highScore);
       // widgets.addView(record);

        //restart = new Button(this);
        restart = findViewById(R.id.restart);
        //restart.setText("restart");
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.startGame();
                if(nowScore > highScore){
                    highScore = nowScore;
                    editor.putInt("HighScore", highScore);
                }
                nowScore = 0;
                score.setText("" + nowScore);
                record.setText("" + highScore);
            }
        });
        //widgets.addView(restart);

        //window.addView(widgets);
    }


}
