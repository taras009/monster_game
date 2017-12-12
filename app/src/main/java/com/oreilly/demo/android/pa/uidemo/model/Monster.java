package com.oreilly.demo.android.pa.uidemo.model;


import java.util.Observable;
import java.util.Random;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.oreilly.demo.android.pa.uidemo.R;


/** A dot: the coordinates, color and size. */
public final class Monster extends Observable{
    private final int x, y;
   // private final int color;
  //  private final int diameter;
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

    //this method is meant to draw our monster on the android canvas
    public void drawMonster(Canvas canvas, Context context, int squareWidth, int leftMargin, int topMargin, Paint paint) {
        Bitmap image;

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
    public float getX() { return x; }

    /** @return the vertical coordinate. */
    public float getY() { return y; }


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

}