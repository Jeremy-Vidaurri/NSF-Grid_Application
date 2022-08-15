package com.example.abac;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
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


    public void initGrid(int size, int curPolicy){
        this.amtRows = size;
        this.curPolicy =  curPolicy;
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

        cellWidth = getWidth() / amtRows;
        //Log.d(TAG, "cellWidth: " + cellWidth);
        canvas.drawColor(Color.GREEN);
        drawSquares(canvas);
        drawLines(canvas);
    }

    // If the value is 0, paint it red.
    // If the value is 2, paint it yellow.
    // Otherwise, there should be no square so that it appears green.
    private void drawSquares(Canvas canvas) {
        db = dbHelper.getWritableDatabase();

        // Get all cords for where there is not a green zone
        try (Cursor cur = db.query("Matrix", new String[]{"ColumnID", "RowID", "Value"},
                "PolicyID=" + curPolicy + " AND Value!=1",
                null, null, null, null)) {
            int row, column, val;

            // For each red/yellow zone, grab the column, row, and value.
            while (cur.moveToNext()) {
                column = cur.getInt(0);
                row = cur.getInt(1);
                val = cur.getInt(2);
                //Log.d(TAG, "drawSquares: " + row + " " +column + " Value: " + val);

                // Color the square according to its value
                if (val == 0) {
                    paint.setColor(Color.RED);
                } else{
                    paint.setColor(Color.YELLOW);
                }

                canvas.drawRect(
                        column * cellWidth,
                        (amtRows - row-1) * cellWidth,
                        (column+1) * cellWidth,
                        (amtRows - row) * cellWidth,
                        paint);
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

        //Log.d(TAG, "onTouch X:" + event.getX() + " Y:" + event.getY());

        // When a square is touched, get the position in the matrix and change its value
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int i = (int) (amtRows - event.getY() / cellWidth );
            int j = (int) (event.getX() / cellWidth);
            //Log.d(TAG,"i:" + i + " j:" + j);

            // Toggle between 0, 1, and 2 based on the current value.
            int val = dbHelper.getValue(curPolicy,i,j);

            switch(val){
                case 0:
                    val=1;
                    break;
                case 1:
                    val=2;
                    break;
                case 2:
                    val=0;
                    break;
            }
            dbHelper.updateMatrix(curPolicy,i,j,val);

            // Redraw the grid
            invalidate();
        }

        return super.onTouchEvent(event);
    }
}
