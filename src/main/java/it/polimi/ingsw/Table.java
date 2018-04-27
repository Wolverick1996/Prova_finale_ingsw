package it.polimi.ingsw;

import java.util.*;

public class Table {

    //***************************//
    //        Attributes         //
    //***************************//

    private static Table instance = null;
    int[] toolCards = new int[3];
    int[] publicOC = new int[3];
    ArrayList reserve;
    ArrayList roundTrack;

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
    //public Dice pickDice(int num){return null;}


}
