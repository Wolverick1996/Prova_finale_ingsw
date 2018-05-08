package it.polimi.ingsw;

import java.awt.*;
import java.util.*;
import java.lang.*;

public class Table {

    //***************************//
    //        Attributes         //
    //***************************//

    private static Table instance = null;
    private ArrayList<Dice> reserve = new ArrayList<Dice>();
    private boolean canExtract = true;
    private ArrayList<Dice> roundTrack = new ArrayList<Dice>();
    private int redExt = 0;
    private int greenExt = 0;
    private int purpleExt = 0;
    private int yellowExt = 0;
    private int bluExt = 0;
    private int round = 0; //must match ROUND in controller
    private boolean clockwise = true;
    private int turn = 0;
    private int numPlayers; //must receive data

    //constructor Singleton
    protected Table(int numP){
        ToolHandler.setTools();
        PubObjHandler.setPubOC();
        //TODO: numPlayers = Controller.getNumPlayers(); //Request must be sent to right controller
        numPlayers = numP;
    }

    //***************************//
    //         Methods           //
    //***************************//

    //constructor and initializer
    public Table initialize(int numP){

        if (instance == null){
            instance = new Table(numP);
            return instance;
        }

        return null;
    }

    //Change turn. TURN GOES FROM 0 TO 3, either clockwise or anticlockwise
    public void nextTurn(){
        canExtract = true;
        //TODO: call controller to active nextPlayer;
        if (clockwise){
            if (turn == numPlayers-1){
                clockwise = false;
            } else {
                turn++;
            }
        }else {
            if (turn == 0){
                clockwise = true;
                nextRound();
            } else {
                turn--;
            }
        }
    }

    //Change round
    private void nextRound(){
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
    // NOTE: CALL THIS METHOD ONLY AFTER MOVE HAS BEEN CONFIRMED
    public Dice pickDice(int num){
        if (canExtract){
            Dice temp = new Dice();
            temp = reserve.get(num);
            reserve.remove(num);
            return temp;
        } else {
            return null;
        }
    }

    @Override
    public String toString(){
        return "String";
    }

}
