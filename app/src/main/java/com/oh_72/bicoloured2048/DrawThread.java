package com.oh_72.bicoloured2048;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;

public class DrawThread extends Thread{

    private final static int GRID_WIDTH = 500;
    private final static int GRID_HEIGHT = 500;

    private final static int COLOR_ZERO = Color.rgb(200, 209, 182);
    private final static int COLOR_TWO = Color.rgb(255, 179, 128);
    private final static int COLOR_THREE = Color.rgb(117, 191, 255);
    private final static int COLOR_BACKGROUND = Color.rgb(204, 194, 177);

    private int tileWidth;
    private int tileHeight;

    private int grid_x;
    private int minDim;
    private int width;
    private int grid_y;

    private boolean running;
    private SurfaceHolder surfaceHolder;
    private Paint paint;

    private Integer grid[];

    private boolean gameOn;

    public DrawThread(SurfaceHolder surfaceHolder, Integer grid[]){
        this.surfaceHolder = surfaceHolder;
        this.grid = grid;

        paint = new Paint();

        tileHeight = GRID_HEIGHT / 3;
        tileWidth = GRID_WIDTH / 3;

        grid_x = 0;
        grid_y = 0;
    }

    public void setWidth(int width, int minDim){
        this.width = width;
        this.minDim = minDim;
        tileWidth = minDim / 3;
        tileHeight = minDim / 3;
        if(width > minDim){
            grid_x = (width - minDim) / 2;
        }
    }

    public void setGameOn(boolean gameOn){
        this.gameOn = gameOn;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (running){
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                if(canvas == null){
                    continue;
                }
                canvas.drawColor(COLOR_BACKGROUND);
                for(int i = 0; i < 3; i++){
                    for(int ii = 0; ii < 3; ii++){
                        int index = i * 3 + ii;
                        if(grid[index] == 0){
                            paint.setColor(COLOR_ZERO);
                        }else {
                            if(grid[index] % 2 == 0){
                                paint.setColor(COLOR_TWO);
                            }
                            if(grid[index] % 3 == 0){
                                paint.setColor(COLOR_THREE);
                            }
                        }
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(
                                ii * tileWidth + grid_x, i * tileHeight + grid_y,
                                ii * tileWidth + grid_x + tileWidth, i * tileHeight + grid_y + tileHeight,
                                paint
                        );
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setColor(Color.rgb(47, 48, 44));
                        paint.setStrokeWidth(10);
                        canvas.drawRect(
                                ii * tileWidth + grid_x, i * tileHeight + grid_y,
                                ii * tileWidth + grid_x + tileWidth, i * tileHeight + grid_y + tileHeight,
                                paint
                        );
                        if(grid[index] != 0) {
                            paint.setColor(Color.rgb(47, 48, 44));
                            paint.setTextSize(80);
                            canvas.drawText(grid[index] + "", ii * tileWidth + grid_x + 50, i * tileHeight + grid_y + 100, paint);
                        }
                    }
                }
                if(!gameOn){
                    paint.setColor(Color.argb(90, 0, 0, 0));
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(
                            grid_x, grid_y,
                            grid_x + minDim, grid_y + minDim,
                            paint
                    );
                    paint.setColor(Color.rgb(224, 224, 224));
                    paint.setTextSize(100);
                    canvas.drawText("Game over", grid_x + 10, grid_y + 100, paint);
                    paint.setTextSize(90);
                    canvas.drawText("click restart", grid_x + 10, grid_y + 190, paint);
                }
            }finally {
                if(canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
