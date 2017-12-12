package com.oreilly.demo.android.pa.uidemo.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/** A list of Monsters. */
public class Monsters {
    /** DotChangeListener. */
    public interface MonsterChangeListener {
        /** @param monsters the monsters that changed. */
        void onDotsChange(Monsters monsters);
    }

    private final LinkedList<Monster> dots = new LinkedList<>();
    private final List<Monster> safeDots = Collections.unmodifiableList(dots);
    public Monster[][] positions;
    private int totalDotCount;
    public Grid grid;


    private MonsterChangeListener monsterChangeListener;

    /** @param l set the change listener. */
    public void setMonsterChangeListener(final MonsterChangeListener l) {
        monsterChangeListener = l;
    }

    /** @return the most recently added dot. */
    public Monster getLastDot() {
        return (dots.size() <= 0) ? null : dots.getLast();
    }

    /** @return immutable list of dots. */
    public List<Monster> getDots() { return safeDots; }
    public Monsters(int totalMonsterNumberProb, int vulnerableProb){
        this.totalNumberOfMonsters = totalMonsterNumberProb;
        this.vulnerableProb = vulnerableProb;
    }
    /**
     * @param newMonster is a monster
     */
    public Monster addMonster(Monster newMonster) {
        newMonster.addObserver(monsterGrid);
        monsters.add(newMonster);
        positions[newMonster.getX()][newMonster.getY()] = newMonster;

        return newMonster;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    /** Remove all dots. */
    public void clearDots() {
        dots.clear();
        notifyListener();
    }

    private void notifyListener() {
        if (null != monsterChangeListener) {
            monsterChangeListener.onDotsChange(this);
        }
    }
    public void setTotalDotCount(int totalDotCount) {
        this.totalDotCount = totalDotCount;
    }


    public void initializeDots(int column, int row){

        positions = new Monster[column][row];
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
