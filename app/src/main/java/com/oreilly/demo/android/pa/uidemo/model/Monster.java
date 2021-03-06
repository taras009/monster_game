package com.oreilly.demo.android.pa.uidemo.model;


import java.util.Observable;
import java.util.Random;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.oreilly.demo.android.pa.uidemo.R;


/** A dot: the coordinates, color and size. */
public final class Monster extends Observable{
    private int x;
    private int y;
    private int vulnChance;
    private boolean isVuln;
    public boolean moved = false;
    public int monsterGroup; //this will prob be used to interact with our group of monsters
    public static Random randomNum = new Random(); // random num generator for general use in class


    /**
     * @param x horizontal coordinate.
     * @param y vertical coordinate.
     * @param vulnChance for calculating how vulnerable our monster will be
     */
    public Monster(int x, int y, int vulnChance) {
        this.x = x;
        this.y = y;
        this.vulnChance = vulnChance;
        //random number generator is used to give
        //us a randomness to our monster vulnerability
        this.isVuln = randomNum.nextInt(100) < vulnChance;
    }

    public Async async=new Async();

    public static class Async extends AsyncTask<Object,Void,Monster> {

        public int group;

        @Override
        protected void onPreExecute (){
            if(Monsters.currentGroup!= group){
                cancel(false);
            }
        }

        @Override
        protected Monster doInBackground(Object... params) {

            Monster[][] positions = (Monster[][]) params[0];
            Monster m = (Monster) params[1];
            m.moved = false;

            try {
                //0.08 second intervals
                Thread.sleep(80);

            } catch (InterruptedException e) {
            }
            m.move(positions);
            return m;
        }

        @Override
        protected void onPostExecute(Monster m) {
            if (m.isMoved()) {
                m.setChanged();
                m.notifyObservers(m);//tell the Grid monster has moved
            }
        }
    }

    //this method is meant to draw our monster on the android canvas
    public void drawMonster(Canvas canvas, Context context, int squareWidth, int leftMargin, int topMargin, Paint paint) {
        Bitmap image;

        //used bitmapfactory to set up our custom image on the canvas
        if (isVulnerable()) {image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ytroop);}
        else {image = BitmapFactory.decodeResource(context.getResources(), R.drawable.gtroop);}

        Bitmap imageScaled = Bitmap.createScaledBitmap(image, squareWidth, squareWidth, false);
        canvas.drawBitmap(imageScaled, getX() * squareWidth + leftMargin, getY() * squareWidth + topMargin, paint);

    }

    /** @return whether the monster is currently vulnerable*/
    public boolean isVulnerable() {
        return isVuln;
    }

    /** @return the horizontal coordinate. */
    public int getX() { return x; }

    /** @return the vertical coordinate. */
    public int getY() { return y; }

    public boolean isMoved() {
        return moved;
    }

    //comparisons
    public boolean equals(Object obj) {
        if (!(obj instanceof Monster))
            return false;
        if (obj == this)
            return true;
        if (((Monster) obj).getX() == getX() && ((Monster) obj).getY() == getY())
            return true;
        else
            return false;
    }
    //move takes a matrix of monsters
    public synchronized Object[] move (Monster[][] positions){

        Object[] result=new Object[4];
        int lx=positions.length; //lx is the number of rows in matrix
        int ly=positions[0].length; // ly is the number of columns in matrix
        int count=7;

        while(true){
            int newX = x + randomNum.nextInt(3) - 1;
            int newY = y + randomNum.nextInt(3) - 1;
            if(newX >= 0 && newX < lx && newY >= 0 && newY < ly && positions[newX][newY]==null){
                result[0] = newX;
                result[1] = newY;
                moved=true;
            }
            count--;
            if(count==-1 || moved)
                break;
        }
        if(count==-1){//stay at the same place
            result[0] = x;
            result[1] = y;
        }
        positions[x][y] = null;
        result[2] = randomNum.nextInt(100)<vulnChance; //some probability to change to vulnerable
        result[3] = this;
        x=(int)(result[0]); //change x coordinate
        y=(int)(result[1]); //change y coordinate
        isVuln=(boolean)result[2];
        positions[x][y]=this;
        return  result;

    }

}