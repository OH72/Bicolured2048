package com.oh_72.bicoloured2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private final static int TWO = 40;
    private final static int THREE = 40;
    private final static int FOUR = 20;

    private DrawThread thread;

    private Integer grid[];
    private Random random;

    private int countNumbers;
    private int score;

    private int width;
    private int height;

    private int minDim;


    private boolean gameOn;

    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        grid = new Integer[]{
                0, 0, 0,
                0, 0, 0,
                0, 0, 0
        };
        random = new Random();
        thread = new DrawThread(this.getHolder(), grid);
        width = this.getWidth();
        height = this.getHeight();
        minDim = Math.min(width, height);
        thread.setWidth(width, minDim);
        startGame();
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while(retry){
            try{
                thread.join();
                retry = false;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void addNewNumber(){
        int index_random = random.nextInt(9 - countNumbers);
        int number = random.nextInt(100) + 1;

        int index = 0;
        for(int i = 0; i < 9; i++){
            if(grid[i] == 0){
                index_random--;
                if(index_random <= 0){
                    index = i;
                    break;
                }
            }
        }

        System.out.println(index + " " + number);

        if(number <= TWO){
            grid[index] = 2;
        }else if(number <= TWO + THREE){
            grid[index] = 3;
        }else if(number <= TWO + THREE + FOUR){
            grid[index] = 4;
        }

        countNumbers++;
        if(countNumbers == 9){
            gameOn = gameOver();
            thread.setGameOn(gameOn);
        }
    }

    public void moveRight(){
        if(!gameOn){
            return;
        }
        boolean makeMove = false;
        for(int i = 0; i < 3; i++){
            for(int ii = 1; ii >= 0; ii--){
                if(grid[i * 3 + ii] == 0){
                    continue;
                }
                int newPosition = -1;
                for(int iii = ii + 1; iii < 3; iii++){
                    if(grid[i * 3 + iii] == 0){
                        newPosition = iii;
                        makeMove = true;
                    }else{
                        if(grid[i * 3 + iii] == grid[i * 3 + ii]){
                            grid[i * 3 + iii] = grid[i * 3 + ii] * 2;
                            grid[i * 3 + ii] = 0;
                            newPosition = -1;
                            countNumbers--;
                            makeMove = true;
                            score += grid[i * 3 + iii];
                        }
                        break;
                    }
                }
                if(newPosition != -1){
                    grid[i * 3 + newPosition] = grid[i * 3 + ii];
                    grid[i * 3 + ii] = 0;
                }
            }
        }
        if(makeMove) {
            addNewNumber();
        }
        Log.d(PlayActivity.LOG_TAG, "filled tiles = " + countNumbers);
    }

    public void moveLeft(){
        if(!gameOn){
            return;
        }
        boolean makeMove = false;
        for(int i = 0; i < 3; i++){
            for(int ii = 1; ii < 3; ii++){
                if(grid[i * 3 + ii] == 0){
                    continue;
                }
                int newPosition = -1;
                for(int iii = ii - 1; iii >= 0; iii--){
                    if(grid[i * 3 + iii] == 0){
                        newPosition = iii;
                        makeMove = true;
                    }else{
                        if(grid[i * 3 + iii] == grid[i * 3 + ii]){
                            grid[i * 3 + iii] = grid[i * 3 + ii] * 2;
                            grid[i * 3 + ii] = 0;
                            newPosition = -1;
                            countNumbers--;
                            makeMove = true;
                            score += grid[i * 3 + iii];
                        }
                        break;
                    }
                }
                if(newPosition != -1){
                    grid[i * 3 + newPosition] = grid[i * 3 + ii];
                    grid[i * 3 + ii] = 0;
                }
            }
        }
        if(makeMove) {
            addNewNumber();
        }
        Log.d(PlayActivity.LOG_TAG, "filled tiles = " + countNumbers);
    }

    public void moveBottom(){
        if(!gameOn){
            return;
        }
        boolean makeMove = false;
        for(int i = 0; i < 3; i++){
            for(int ii = 1; ii >= 0; ii--){
                if(grid[ii * 3 + i] == 0){
                    continue;
                }
                int newPosition = -1;
                for(int iii = ii + 1; iii < 3; iii++){
                    if(grid[iii * 3 + i] == 0){
                        newPosition = iii;
                        makeMove = true;
                    }else{
                        if(grid[iii * 3 + i] == grid[ii * 3 + i]){
                            grid[iii * 3 + i] = grid[ii * 3 + i] * 2;
                            grid[ii * 3 + i] = 0;
                            newPosition = -1;
                            countNumbers--;
                            makeMove = true;
                            score += grid[iii * 3 + i];
                        }
                        break;
                    }
                }
                if(newPosition != -1){
                    grid[i + newPosition * 3] = grid[ii * 3 + i];
                    grid[ii * 3 + i] = 0;
                }
            }
        }
        if(makeMove) {
            addNewNumber();
        }
        Log.d(PlayActivity.LOG_TAG, "filled tiles = " + countNumbers);
    }

    public void moveTop(){
        if(!gameOn){
            return;
        }
        boolean makeMove = false;
        for(int i = 0; i < 3; i++){
            for(int ii = 1; ii < 3; ii++){
                if(grid[ii * 3 + i] == 0){
                    continue;
                }
                int newPosition = -1;
                for(int iii = ii - 1; iii >= 0; iii--){
                    if(grid[iii * 3 + i] == 0){
                        newPosition = iii;
                        makeMove = true;
                    }else{
                        if(grid[iii * 3 + i] == grid[ii * 3 + i]){
                            grid[iii * 3 + i] = grid[ii * 3 + i] * 2;
                            grid[ii * 3 + i] = 0;
                            newPosition = -1;
                            countNumbers--;
                            makeMove = true;
                            score += grid[iii * 3 + i];
                        }
                        break;
                    }
                }
                if(newPosition != -1){
                    grid[i + newPosition * 3] = grid[ii * 3 + i];
                    grid[ii * 3 + i] = 0;
                }
            }
        }
        if(makeMove) {
            addNewNumber();
        }
        Log.d(PlayActivity.LOG_TAG, "filled tiles = " + countNumbers);
    }

    public int getScore(){
        return score;
    }

    public void startGame(){
        for(int i = 0; i < 9; i++){
            grid[i] = 0;
        }
        score = 0;
        gameOn = true;
        thread.setGameOn(gameOn);
        countNumbers = 0;
        addNewNumber();
    }

    private boolean gameOver(){
        boolean play = false;
        if(countNumbers < 9){
            return true;
        }
        for(int i = 0; i < 3; i ++){
            for(int ii = 0; ii < 3; ii++){
                if(i - 1 >= 0){
                    if(grid[(i - 1) * 3 + ii] == grid[i * 3 + ii]){
                        play = true;
                    }
                }
                if(i + 1 < 3){
                    if(grid[(i + 1) * 3 + ii] == grid[i * 3 + ii]){
                        play = true;
                    }
                }
                if(ii - 1 >= 0){
                    if(grid[i * 3 + ii - 1] == grid[i * 3 + ii]){
                        play = true;
                    }
                }
                if(ii + 1 < 3){
                    if(grid[i * 3 + ii + 1] == grid[i * 3 + ii]){
                        play = true;
                    }
                }
                if(play){
                    return play;
                }
            }
        }
        return play;
    }

}
