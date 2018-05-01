package it.polimi.ingsw;

import java.util.*;
import java.lang.*;

public class Table {

    //***************************//
    //        Attributes         //
    //***************************//

    private static Table instance = null;
    private int[] toolCards = new int[3];
    private int[] publicOC = new int[3];
    private ArrayList reserve;
    private ArrayList roundTrack;

    //constructor Singleton
    protected Table(){

    }

    //***************************//
    //         Methods           //
    //***************************//

    //constructor and initializer
    public Table initialize(){

        if (instance == null){
            instance = new Table();
            return instance;
        }

        return null;
    }

    //Change turn
    public void nextTurn(){

    }

    //Change round
    public void nextRound(){

    }

    //Pick dice from reserve (num is the position on the table of the dice)
    public Dice pickDice(int num){
        return null;
    }

    @Override
    public String toString(){
        return "String";
    }

}
