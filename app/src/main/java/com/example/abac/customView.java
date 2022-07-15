package com.example.abac;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
    private int amtRows;
    private int cellWidth;
    private int curPolicy;
    dbHelper dbHelper;
    SQLiteDatabase db;
    private final Paint paint = new Paint();


    public customView(Context context) {
        super(context);
        init(context);
    }

    public customView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public customView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        dbHelper = new dbHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    public void initGrid(int size){
        this.amtRows = size;
        paint.setColor(Color.BLACK);
    }


    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    // Ran every time invalidate() is called.
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
        cellWidth = getWidth() / amtRows;

        canvas.drawColor(Color.GREEN);
        drawSquares(canvas);
        drawLines(canvas);
    }

    // If the value in the grid is 1, paint it red.
    private void drawSquares(Canvas canvas) {
        paint.setColor(Color.RED);
        for (int i = 0 ; i < amtRows; i++){
            for (int j = 0; j <amtRows; j++){

                if (dbHelper.getValue(1,i,j) == 0) { // FIX: Add function to pass policyID? I'm not sure how to get the policy id otherwise.
                    canvas.drawRect(
                            i * cellWidth,
                            j * cellWidth,
                            (i * cellWidth) + cellWidth,
                            (j * cellWidth) + cellWidth,
                            paint);
                }
            }
        }

    }

    // Used to separate the squares
    private void drawLines(Canvas canvas) {
        paint.setColor(Color.BLACK);
        for(int i = 1 ; i < amtRows; i ++){
            int offset = (i * cellWidth);
            // Draw the vertical lines
            canvas.drawLine(offset, 0f, offset, getHeight(), paint  );

            // Draw the horizontal lines
            canvas.drawLine(0f,offset , getWidth(), offset, paint  );
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d(TAG, "onTouch X:" + event.getX() + " Y:" + event.getY());

        // When a square is touched, get the position in the matrix and change its value
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int i = (int) (event.getX() / cellWidth);
            int j = (int) (event.getY() / cellWidth);
            Log.d(TAG,"i:" + i + "j:" + j);

            // Toggle between 0 and 1
            if(dbHelper.getValue(1,i,j)==1){ // Once again, pass policyID somehow
                dbHelper.updateMatrix(1,i,j,0);
            } else if (dbHelper.getValue(1,i,j)==0) {
                dbHelper.updateMatrix(1,i,j,1);
            }
            // Redraw the grid
            invalidate();
        }

        return super.onTouchEvent(event);
    }
}
