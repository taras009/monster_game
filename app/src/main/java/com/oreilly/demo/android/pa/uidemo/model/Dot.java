package com.oreilly.demo.android.pa.uidemo.model;


/** A dot: the coordinates, color and size. */
public final class Dot {
    private final int x, y;
    private final int color;
    private final int diameter;

    /**
     * @param x horizontal coordinate.
     * @param y vertical coordinate.
     * @param color the color.
     * @param diameter dot diameter.
     */
    public Dot(final int x, final int y, final int color, final int diameter) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.diameter = diameter;
    }

    /** @return the horizontal coordinate. */
    public float getX() { return x; }

    /** @return the vertical coordinate. */
    public float getY() { return y; }

    /** @return the color. */
    public int getColor() { return color; }

    /** @return the dot diameter. */
    public int getDiameter() { return diameter; }

    public boolean equals(Object obj) {
        if (!(obj instanceof Dot))
            return false;
        if (obj == this)
            return true;
        if (((Dot) obj).getX() == getX() && ((Dot) obj).getY() == getY())
            return true;
        else
            return false;
    }
}