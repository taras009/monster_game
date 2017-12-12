package com.oreilly.demo.android.pa.uidemo.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import com.oreilly.demo.android.pa.uidemo.view.Grid;



/** A list of Monsters. */
public class Monsters implements Observer{
    /** DotChangeListener. */
    public interface MonsterChangeListener {
        /** @param monsters the monsters that changed. */
        void onDotsChange(Monsters monsters);
    }

    private final LinkedList<Monster> monsters = new LinkedList<>();
    private final List<Monster> safeDots = Collections.unmodifiableList(monsters);
    public Monster[][] positions;

    public Grid grid;
    private int monsterPop;
    public static int currentGroup =0;
    public int deadMonsters = 0;
    public static  Random ran = new Random();
    private int vulnerableProb;

    private MonsterChangeListener monsterChangeListener;

    /** @param l set the change listener. */
    public void setMonsterChangeListener(final MonsterChangeListener l) {
        monsterChangeListener = l;
    }

    public int getVulnerableProb() {
        return vulnerableProb;
    }

    public void setVulnerableProb(int vulnerableProb) {
        this.vulnerableProb = vulnerableProb;
    }


    /** @return the most recently added dot. */
    public Monster getLastDot() {
        return (monsters.size() <= 0) ? null : monsters.getLast();
    }

    /** @return immutable list of monsters. */
    public List<Monster> getMonsters() {
        return monsters;
    }

    public Monsters(int monsterPop, int vulnerableProb){
        this.monsterPop = monsterPop;
        this.vulnerableProb = vulnerableProb;
    }
    /**
     * @param newMonster is a monster
     */
    public Monster addMonster(Monster newMonster) {
        newMonster.addObserver(grid);
        monsters.add(newMonster);
        positions[newMonster.getX()][newMonster.getY()] = newMonster;

        return newMonster;
    }

    public void stopMoving(){
        currentGroup = (currentGroup +1)%2;
    }

    public void startMoving(){
        for(Monster monster : monsters){
            Object[] params=new Object[2];
            params[0] = positions;
            params[1] = monster;
            monster.async = new Monster.Async();
            monster.async.group = currentGroup;
            monster.async.execute(params);
        }
    }

    public  boolean removeMonster(Monster monster){
        positions[monster.getX()][monster.getY()] = null;
        deadMonsters++;
        return monsters.remove(monster);
    }

    public void setMonsterPop(int monsterPop) {

            this.monsterPop = monsterPop;

        }

    /** Remove all the monsters from set */
    public void clearMonsters() {
        monsters.clear();
        deadMonsters = 0;
        notifyListener();
    }

    private void notifyListener() {
        if (null != monsterChangeListener) {
            monsterChangeListener.onDotsChange(this);
        }
    }
//    public void setTotalDotCount(int totalDotCount) {
//        this.totalDotCount = totalDotCount;
//    }


    @Override
    public synchronized void update(Observable o, Object arg){ }

    public void initializeMonsters(int column, int row){

        positions = new Monster[column][row];
        for(int i = 0 ; i < column ; i++)
            for(int j = 0 ; j < row ; j++)
                positions[i][j] = null;

        for(int i = 0; i < monsterPop; i++){
            boolean exist = true;
            while (exist){
                int x = ran.nextInt(column);
                int y = ran.nextInt(row);
                if (positions[x][y] == null){
                    exist = false;

                    Monster newMonster = new Monster(x,y,vulnerableProb);
                    newMonster.monsterGroup =Monsters.currentGroup;
                    addMonster(newMonster);
                }
            }
        }
        deadMonsters = 0;
    }

}
