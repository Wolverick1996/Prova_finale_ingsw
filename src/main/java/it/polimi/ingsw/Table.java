package it.polimi.ingsw;

import com.sun.xml.internal.bind.v2.TODO;
import org.w3c.dom.events.EventException;

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
        if (round>9){ System.out.println("WARNING nextRound called, round is already 10"); return;}

        Enum.Color color = Enum.Color.getRandomColor();

        if(!reserve.isEmpty()){
            for(Dice d:reserve){
                roundTrack.add(d);
            }
            reserve.clear();
        }

        //TODO: control this algorithm
        for (int i=0;i<numPlayers*2+1;i++){
            boolean isAVB = false;
            while(!isAVB){
                color = Enum.Color.getRandomColor();
                switch (color){
                    case RED: {if (redExt <= 18) { isAVB = true;} break; }
                    case PURPLE: {if (purpleExt <= 18) { isAVB = true;} break; }
                    case BLUE: {if (bluExt <= 18) { isAVB = true;} break; }
                    case GREEN: {if (greenExt <= 18) { isAVB = true;} break; }
                    case YELLOW: {if (yellowExt <= 18) { isAVB = true;} break; }
                    //default: throw new Exception();
                }
            }
            reserve.add(new Dice(color));
        }

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
