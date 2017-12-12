package com.oreilly.demo.android.pa.uidemo.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/** A list of dots. */
public class Dots {
    /** DotChangeListener. */
    public interface DotsChangeListener {
        /** @param dots the dots that changed. */
        void onDotsChange(Dots dots);
    }

    private final LinkedList<Dot> dots = new LinkedList<>();
    private final List<Dot> safeDots = Collections.unmodifiableList(dots);
    public Dot[][] positions;
    private int totalDotCount;

    private DotsChangeListener dotsChangeListener;

    /** @param l set the change listener. */
    public void setDotsChangeListener(final DotsChangeListener l) {
        dotsChangeListener = l;
    }

    /** @return the most recently added dot. */
    public Dot getLastDot() {
        return (dots.size() <= 0) ? null : dots.getLast();
    }

    /** @return immutable list of dots. */
    public List<Dot> getDots() { return safeDots; }

    /**
     * @param x dot horizontal coordinate.
     * @param y dot vertical coordinate.
     * @param color dot color.
     * @param diameter dot size.
      */
    public void addDot(final int x, final int y, final int color, final int diameter) {
        dots.add(new Dot(x, y, color, diameter));
        notifyListener();
    }

    /** Remove all dots. */
    public void clearDots() {
        dots.clear();
        notifyListener();
    }

    private void notifyListener() {
        if (null != dotsChangeListener) {
            dotsChangeListener.onDotsChange(this);
        }
    }
    public void setTotalDotCount(int totalDotCount) {
        this.totalDotCount = totalDotCount;
    }


    public void initializeDots(int column, int row){

        positions = new Dot[column][row];
        for(int i = 0 ; i < column ; i++)
            for(int j = 0 ; j < row ; j++)
                positions[i][j] = null;

        for(int i = 0 ; i < totalDotCount ; i++){
            boolean exist = true;
            while (exist){
                int x = new Random().nextInt(column);
                int y = new Random().nextInt(row);
                if (positions[x][y] == null){
                    exist = false;

                    addDot(x,y,5,6);
                }
            }
        }
    }

}
