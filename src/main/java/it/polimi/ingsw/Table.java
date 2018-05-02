package it.polimi.ingsw;

import com.sun.xml.internal.bind.v2.TODO;

import java.awt.*;
import java.util.*;
import java.lang.*;

public class Table {

    //***************************//
    //        Attributes         //
    //***************************//

    private static Table instance = null;
    private ArrayList<Dice> reserve = new ArrayList<Dice>();
    private ArrayList<Dice> roundTrack = new ArrayList<Dice>();
    private int redExt = 0;
    private int greenExt = 0;
    private int purpleExt = 0;
    private int yellowExt = 0;
    private int bluExt = 0;
    private int round = 0; //must match ROUND in controller
    private int numPlayers;

    //constructor Singleton
    protected Table(){
        ToolHandler.setTools();
        PubObjHandler.setPubOC();
        //numPlayers = Controller.getNumPlayers(); //Request must be sent to right controller
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
        //set RoundTrack
        //set Reserve
        Dice temp = new Dice();
        int i = 0;

        for(Dice d:reserve){
            roundTrack.add(d);
        }
        reserve.clear();

        //TODO: add new dice to reserve

        round++;
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
