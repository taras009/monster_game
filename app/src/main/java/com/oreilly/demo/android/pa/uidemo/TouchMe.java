package com.oreilly.demo.android.pa.uidemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oreilly.demo.android.pa.uidemo.model.Monster;
import com.oreilly.demo.android.pa.uidemo.model.Monsters;
import com.oreilly.demo.android.pa.uidemo.view.Grid;

import java.util.concurrent.TimeUnit;


public class TouchMe extends Activity {

    /** The application model */
    final Monsters monstersModel = new Monsters(totalNumberProbArrays[0], vulnerableProbArrays[0]);

    /** The application view */
    Grid grid;

    private CountDownTimer timer;
    TextView clockView;
    TextView pointView;
    Button buttonStart, buttonStop;

    public static DisplayMetrics displayMetrics = new DisplayMetrics();

    public static final int[] totalNumberProbArrays = {15, 20, 25, 30, 35};
    public static final int[] vulnerableProbArrays = {25, 20, 15, 10, 5};

    private boolean isStopped = true;

    private static final String FORMAT = "%02d";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        setContentView(R.layout.main);
        pointView = (TextView)findViewById(R.id.pointsView);

        this.setTitle(getResources().getText(R.string.app_name) + " - " + getResources().getText(R.string.menuLevel1));

        clockView = (TextView) findViewById(R.id.clockView);
        buttonStart = (Button) findViewById(R.id.start);
        buttonStop = (Button) findViewById(R.id.stop);
        clockView.setText("45");

        grid = (Grid) findViewById(R.id.monsterView);

        monstersModel.grid = grid;

        grid.setMonsters(monstersModel);
        grid.setOnCreateContextMenuListener(this);
        grid.setOnTouchListener(new TrackingTouchListener(monstersModel, grid));

        timer = new CountDownTimer(45000, 1000){
            public void onTick(long millisUntilFinished){
                clockView.setText(""+String.format(FORMAT, TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
                if(monstersModel.getMonsters().size()==0){
                    onFinish();
                }
            }
            public void onFinish(){
                grid.stopMoving();
                clockView.setText("00");
                isStopped = true;
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);

            }
        };

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStopped) {
                    timer.start();
                    grid.startMoving();
                    pointView.setText("0");
                    isStopped = false;
                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);
                }
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStopped) {
                    grid.stopMoving();
                    pointView.setText("0");
                    timer.cancel();
                    clockView.setText("45");
                    isStopped = true;
                    buttonStop.setEnabled(false);
                    buttonStart.setEnabled(true);
                }
            }
        });
    }

    /**
     * Install an options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }

    /** Respond to an options menu selection. */
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        this.setTitle(getResources().getText(R.string.app_name) + " - " + item.getTitle());
        isStopped = true;
        buttonStop.setEnabled(false);
        buttonStart.setEnabled(true);
        grid.stopMoving();
        pointView.setText("0");
        clockView.setText("45");
        timer.cancel();

        switch (item.getItemId()) {
            case R.id.menuLevel1:
                monstersModel.setMonsterPop(totalNumberProbArrays[0]);
                monstersModel.setVulnerableProb(vulnerableProbArrays[0]);
                return true;
            case R.id.menuLevel2:
                monstersModel.setMonsterPop(totalNumberProbArrays[1]);
                monstersModel.setVulnerableProb(vulnerableProbArrays[1]);
                return true;
            case R.id.menuLevel3:
                monstersModel.setMonsterPop(totalNumberProbArrays[2]);
                monstersModel.setVulnerableProb(vulnerableProbArrays[2]);
                return true;
            case R.id.menuLevel4:
                monstersModel.setMonsterPop(totalNumberProbArrays[3]);
                monstersModel.setVulnerableProb(vulnerableProbArrays[3]);
                return true;
            case R.id.menuLevel5:
                monstersModel.setMonsterPop(totalNumberProbArrays[4]);
                monstersModel.setVulnerableProb(vulnerableProbArrays[4]);
                return true;
            // case R.id.menuHighScores:
            //   Snackbar highScores = Snackbar.make( VIEW , "hi, scores", 1000);
            // highScores.show();
            //return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Our touch . */
    private final class TrackingTouchListener implements View.OnTouchListener {

        private final Monsters mMonsters;
        private final Grid grid;

        TrackingTouchListener(Monsters mMonsters, Grid grid) {
            this.mMonsters = mMonsters;
            this.grid = grid;
        }

        @Override public boolean onTouch(View v, MotionEvent evt) {
            int action = evt.getAction();

            switch (action & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    float x = evt.getX();
                    float y = evt.getY();
                    x = x - grid.getLeftMargin();
                    y = y - grid.getTopMargin();

                    x = x / grid.getSquareHeight();
                    y = y / grid.getSquareWidth();

                    int indexX = (int)x;
                    int indexY = (int)y;

                    if(indexX >= 0 && indexX < grid.getColumn() && indexY >= 0 && indexY < grid.getRow()
                            && mMonsters.positions[indexX][indexY]!=null && mMonsters.positions[indexX][indexY].isVulnerable()){
                        mMonsters.removeMonster(new Monster(indexX, indexY, monstersModel.getVulnerableProb()));
                        grid.invalidate();
                        pointView.setText(Integer.toString(mMonsters.deadMonsters));
                    }
                    break;
                default:
                    return false;
            }
            return true;
        }
    }
}
