package com.example.abac;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class customView extends View {
    private static final String TAG = customView.class.getSimpleName();
    private boolean[][] grid;
    private int amtOfRows;
    private int cellWidth;

    private final Paint paint = new Paint();

    public customView(Context context) {
        super(context);
    }

    public customView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public customView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initGrid(int size){
        this.amtOfRows = size;
        grid = new boolean[size][size];
        paint.setColor(Color.BLACK);
    }

    public void updateGrid(boolean[][] newGrid){
        this.grid = newGrid;
    }

    public boolean[][] getGrid() {
        return grid;
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");

        cellWidth = getWidth() / amtOfRows;

        canvas.drawColor(Color.GREEN);
        drawSquares(canvas);
        drawLines(canvas);
    }

    private void drawSquares(Canvas canvas) {

        for (int i = 0 ; i < amtOfRows; i++){
            for (int j = 0; j <amtOfRows; j++){

                if (!grid[i][j]) {
                    canvas.drawColor(Color.RED);
                } else{
                    canvas.drawColor(Color.GREEN);
                }
                canvas.drawRect(
                        i * cellWidth,
                        j * cellWidth,
                        (i * cellWidth) + cellWidth,
                        (j * cellWidth) + cellWidth,
                        paint);
            }
        }

    }

    private void drawLines(Canvas canvas) {
        paint.setColor(Color.BLACK);
        for(int i = 1 ; i < amtOfRows; i ++){
            int offset = (i * cellWidth);
            // Draw the vertical lines
            canvas.drawLine(offset, 0f, offset, getHeight(), paint  );

            // Draw the horizontal lines
            canvas.drawLine(0f,offset , getWidth(), offset, paint  );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d(TAG, "onTouch X:" + event.getX() + " Y:" + event.getY());

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int i = (int) (event.getX() / cellWidth);
            int j = (int) (event.getY() / cellWidth);

            grid[i][j] = !grid[i][j];

            // redraw the grid
            invalidate();
        }

        return super.onTouchEvent(event);
    }
}
