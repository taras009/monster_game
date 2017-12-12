package com.oreilly.demo.android.pa.uidemo.view;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.util.DisplayMetrics;

import com.oreilly.demo.android.pa.uidemo.model.Monsters;


public class Grid extends View {

    private Paint paint = new Paint();

    private boolean isInitialed = false;
    private int row;
    private int column;
    private int squareWidth;
    private int squareHeight;
    private int leftMargin;
    private int rightMargin;
    private int topMargin;
    private int bottomMargin;
    private int displayWidth;
    private int displayHeight;
    private int size = 25;

    private Monsters monsters;

    private boolean loaded = false;

    /**
     * @param context the rest of the application
     */
    public Grid(final Context context) {
        super(context);
        setFocusableInTouchMode(true);
    }

    /**
     * @param context
     * @param attrs
     */
    public Grid(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setFocusableInTouchMode(true);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public Grid(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        setFocusableInTouchMode(true);
    }

    /**
     * @param monsters
     */
    public void setMonsters(final Monsters monsters) { this.monsters = monsters; }

    /**
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override protected void onDraw(Canvas canvas) {
        if(!isInitialed){
            getScreenSpec();
            isInitialed = true;
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(leftMargin, topMargin, displayWidth - rightMargin, displayHeight
                - bottomMargin, paint);

        for(int i = 0 ; i < row - 1 ; i ++){
            canvas.drawLine(leftMargin, topMargin + (i+1) * squareWidth, displayWidth - rightMargin,
                    topMargin + (i+1) * squareWidth , paint);
        }

        for(int i = 0 ; i < column - 1 ; i ++){
            canvas.drawLine(leftMargin + (i+1) * squareWidth, topMargin , leftMargin + (i+1)
                    * squareWidth, displayHeight - bottomMargin, paint);
        }

        drawMonsters(canvas, paint);
    }

    public int getSquareWidth(){
        return squareWidth;
    }

    public int getSquareHeight(){
        return squareHeight;
    }

    public int getLeftMargin(){
        return leftMargin;
    }

    public int getTopMargin(){
        return topMargin;
    }
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    private void drawMonsters(Canvas canvas, Paint paint){
        for(int i = 0 ; i < monsters.getMonsters().size() ; i++)
            monsters.getMonsters().get(i).draw(canvas, getContext(), squareWidth, leftMargin, topMargin, paint);
    }

    public void getScreenSpec(){

        displayHeight = getHeight();
        displayWidth = getWidth();

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        squareWidth = Math.round(size * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        squareHeight = Math.round(size * (displayMetrics.ydpi / DisplayMetrics.DENSITY_DEFAULT));
        row = displayHeight / squareHeight;
        column = displayWidth / squareWidth;

        leftMargin = (displayWidth % squareWidth) / 2;
        rightMargin = (displayWidth % squareWidth) - (displayWidth % squareWidth) / 2;
        topMargin = (displayHeight % squareHeight) / 2;
        bottomMargin = (displayHeight % squareHeight) - (displayHeight % squareHeight) / 2;
        loaded = true;
    }
    public void stopMoving(){
        monsters.stopMoving();
        monsters.clearMonsters();
        invalidate();
    }

    public void startMoving(){
        monsters.initializeMonsters(column, row);
        monsters.startMoving();
    }
}
