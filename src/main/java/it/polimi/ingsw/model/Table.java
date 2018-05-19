package it.polimi.ingsw.model;

import java.util.*;

public class Table {

    //***************************//
    //        Attributes         //
    //***************************//

    private ArrayList<Dice> reserve = new ArrayList<Dice>();
    private boolean canExtract = true;
    private ArrayList<Dice> roundTrack = new ArrayList<Dice>();
    private int redExt = 0;
    private int greenExt = 0;
    private int purpleExt = 0;
    private int yellowExt = 0;
    private int bluExt = 0;
    private int round = -1; //must match ROUND in controller
    private boolean clockwise = true;
    private int turn = 0;
    private int numPlayers; //must receive data

    //constructor Singleton (?)
    public Table(int numP){
        ToolHandler.setTools();
        PubObjHandler.setPubOC();
        //TODO: numPlayers = controller.getNumPlayers(); //Request must be sent to right controller
        numPlayers = numP;
        nextRound();
    }

    //***************************//
    //         Methods           //
    //***************************//

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
        } else {
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
        } else if (round != -1) {System.out.println("WARNING reserve is empty"); return;}

        for (int i=0;i<numPlayers*2+1;i++){ reserve.add(pickDiceFromBag()); }

        round++;
    }

    //Pick dice from Bag (TOOL 11)
    public Dice pickDiceFromBag(){
        boolean isAVB = false;
        Enum.Color color = Enum.Color.getRandomColor();
        if (redExt==18 && purpleExt==18 && bluExt==18 && greenExt==18 && yellowExt==18){System.out.println("Bag is empty");
        return null;}
        while(!isAVB){
            color = Enum.Color.getRandomColor();
            switch (color){
                case RED: {if (redExt < 18) { isAVB = true; redExt++;} break; }
                case PURPLE: {if (purpleExt < 18) { isAVB = true; purpleExt++;} break; }
                case BLUE: {if (bluExt < 18) { isAVB = true; bluExt++;} break; }
                case GREEN: {if (greenExt < 18) { isAVB = true; greenExt++;} break; }
                case YELLOW: {if (yellowExt < 18) { isAVB = true; yellowExt++;} break; }
                //default: throw new Exception();
            }
        }
        return new Dice(color);
    }

    //Pick dice from reserve (num is the position on the table of the dice)
    //NOTE: CALL THIS METHOD ONLY AFTER MOVE HAS BEEN CONFIRMED
    public Dice pickDiceFromReserve(int dicePos){
        if (dicePos >= reserve.size()) {return null;}
        if (canExtract){
            Dice temp = reserve.get(dicePos);
            reserve.remove(dicePos);
            canExtract = false;
            return temp;
        } else {
            return null;
        }
    }

    //Pick dice from Roundtrack
    //NOTE: CALL THIS METHOD ONLY AFTER MOVE HAS BEEN CONFIRMED
    public Dice pickDiceFromRoundtrack(int dicePos){
        Dice temp = roundTrack.get(dicePos);
        roundTrack.remove(dicePos);
        return temp;
    }

    //pick dice from reserve, put it in the bag (TOOL 11)
    public void putDiceInBag(int dicePos){
        if(!canExtract){System.out.println("Tool card 11 was called without permission (dice already extracted)"); return;}
        Enum.Color color = reserve.get(dicePos).getColor();
        switch (color){
            //TODO: colorExt must never be <0
            case RED: { redExt--; break; }
            case PURPLE: {purpleExt--; break; }
            case BLUE: {bluExt--; break; }
            case GREEN: {greenExt--; break; }
            case YELLOW: {yellowExt--; break; }
            //default: throw new Exception();
        }
        reserve.remove(dicePos);
    }

    //Reroll Reserve (tool 7)
    public  boolean rerollReserve(){

        if (clockwise) return false;

        for(Dice d:reserve){
            d.rollDice();
        }
        return true;
    }

    //Get the dice to check position restrictions
    public Dice checkDiceFromReserve(int dicePos){if(dicePos<reserve.size()) return reserve.get(dicePos);return null;}
    public Dice checkDiceFromRoundtrack(int dicePos){if(dicePos<roundTrack.size()) return roundTrack.get(dicePos);
    return null;}

    //put dice in a particular spot
    public void putDiceInReserve(Dice d){reserve.add(d);}
    public void putDiceInRoundtrack(Dice d){roundTrack.add(d);}

    public int getTurn(){ return this.turn; }

    public int getRound(){ return this.round; }

    @Override
    public String toString(){
        return "Table: \n" + "Dice in reserve: " + reserve.size() + "\nReserve: " + reserve.toString() + "\nRoundtrack: " +
                roundTrack.toString() + "\nTurn: " + turn + "\nRound: " + round + "\nClockwise: " + clockwise;
    }

}
