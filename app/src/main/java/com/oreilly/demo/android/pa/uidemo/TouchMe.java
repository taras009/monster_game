package com.oreilly.demo.android.pa.uidemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;

import com.oreilly.demo.android.pa.uidemo.model.Monster;
import com.oreilly.demo.android.pa.uidemo.model.Monsters;
import com.oreilly.demo.android.pa.uidemo.view.Grid;


/** Android UI demo program */
public class TouchMe extends Activity {
    /** Monster diameter */
    public static final int DOT_DIAMETER = 6;
    final Monsters monstersModel = new Monsters(6);
    /** Listen for taps. */
    private static final class TrackingTouchListener implements View.OnTouchListener {
        private final Monsters mDots;
        Grid grid;
        private List<Integer> tracks = new ArrayList<>();

        TrackingTouchListener(final Monsters dots) { mDots = dots; }

        @Override public boolean onTouch(final View v, final MotionEvent evt) {
            final int action = evt.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    float x = evt.getX();
                    float y = evt.getY();
                    x = x - grid.getLeftMargin();
                    y = y - grid.getTopMargin();

                    x = x / grid.getSquareHeight();
                    y = y / grid.getSquareWidth();



                    break;


                default:
                    return false;
            }

            for (final Integer i: tracks) {
                final int idx = evt.findPointerIndex(i);
                addDot(
                    mDots,
                        (int)evt.getX(idx),
                        (int)evt.getY(idx),
                    evt.getPressure(idx),
                    evt.getSize(idx));
            }

            return true;
        }

       //removedot here
    }

    private final Random rand = new Random();

    /** The application model */
    private Monsters dotModel = new Monsters(6);


    /** The application view */
    private Grid grid;

    /** The dot generator */
    private Timer dotGenerator;

    /** Called when the activity is first created. */
    @Override public void onCreate(final Bundle state) {
        super.onCreate(state);

        // install the view
        setContentView(R.layout.main);

        // find the dots view
        grid = (Grid) findViewById(R.id.dots);
        grid.setMstrs(dotModel);

        grid.setOnCreateContextMenuListener(this);
        grid.setOnTouchListener(new TrackingTouchListener(dotModel));


        dotModel.dotView = grid;

        grid.setMstrs(monstersModel);
        grid.setOnCreateContextMenuListener(this);
        grid.setOnTouchListener(new TrackingTouchListener(dotModel, grid));


        grid.setOnKeyListener((final View v, final int keyCode, final KeyEvent event) -> {
            if (KeyEvent.ACTION_DOWN != event.getAction()) {
                return false;
            }

            int color;
            switch (keyCode) {
                case KeyEvent.KEYCODE_SPACE:
                    color = Color.MAGENTA;
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    color = Color.BLUE;
                    break;
                default:
                    return false;
            }

            makeDot(dotModel, grid, color);

            return true;
        });

        // wire up the controller
        findViewById(R.id.button1).setOnClickListener((final View v) ->
            makeDot(dotModel, grid, Color.RED)
        );
        findViewById(R.id.button2).setOnClickListener((final View v) ->
            makeDot(dotModel, grid, Color.GREEN)
        );

        final EditText tb1 = (EditText) findViewById(R.id.text1);
        final EditText tb2 = (EditText) findViewById(R.id.text2);
        dotModel.setMonsterChangeListener((final Monsters dots) -> {
            final Monster d = dots.getLastDot();
            tb1.setText((null == d) ? "" : String.valueOf(d.getX()));
            tb2.setText((null == d) ? "" : String.valueOf(d.getY()));
            grid.invalidate();
        });
    }

    @Override public void onResume() {
        super.onResume();
        if (dotGenerator == null) {
            dotGenerator = new Timer();
            // generate new dots, one every two seconds
            dotGenerator.schedule(new TimerTask() {
                @Override
                public void run() {
                    // must invoke makeDot on the UI thread to avoid
                    // ConcurrentModificationException on list of dots
                    runOnUiThread(() -> makeDot(dotModel, grid, Color.BLACK));
                }
            }, /*initial delay*/ 0, /*periodic delay*/ 2000);
        }
    }

    @Override public void onPause() {
        super.onPause();
        if (dotGenerator != null) {
            dotGenerator.cancel();
            dotGenerator = null;
        }
    }

    /** Install an options menu. */
    @Override public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }

    /** Respond to an options menu selection. */
//    @Override public boolean onOptionsItemSelected(final MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_clear:
//                dotModel.clearDots();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    /** Install a context menu. */
    @Override public void onCreateContextMenu(
            final ContextMenu menu,
            final View v,
            final ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Clear").setAlphabeticShortcut('x');
    }

    /** Respond to a context menu selection. */
    @Override public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                dotModel.clearDots();
                return true;
            default:
                return false;
        }
    }

    /**
     * @param dots the dots we're drawing
     * @param view the view in which we're drawing dots
     * @param color the color of the dot
     */
    void makeDot(final Monsters dots, final Grid view, final int color) {
        final int pad = (DOT_DIAMETER + 2) * 2;
        dots.addDot(
                (int)(DOT_DIAMETER + (rand.nextFloat() * (view.getWidth() - pad))),
                (int)(DOT_DIAMETER + (rand.nextFloat() * (view.getHeight() - pad))),
            color,
            DOT_DIAMETER);
    }
}