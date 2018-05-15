package it.polimi.ingsw;

import java.lang.*;

public class Player {

    //***************************//
    //        Attributes         //
    //***************************//

    private String nickname;
    private int points;
    private int tokens;
    private Dice diceInHand = null;
    private Scheme ownScheme;

    // PrivateOC will work with indexes;
    private int privateOC;

    //constructor
    public Player(String nick){
        this.nickname = nick;
        this.points = 0;
    }

    //***************************//
    //         Methods           //
    //***************************//

    // Private Objective Card picker
    public void pickOC(){

    }

    //Assign Scheme to a player
    public void chooseScheme(Scheme scheme){
        this.ownScheme = scheme;
        this.tokens = scheme.getDifficulty();
    }

    // Pick a dice from scheme
    public boolean extractDice(int x, int y){
        if(ownScheme.checkBox(x, y) != null){
            this.diceInHand = ownScheme.removeDice(x, y);
            return true;
        }
        else
            return false;
    }

    //Is a dice picked from reserve placeable in the scheme?
    public boolean isPlaceableDiceFromReserve(int x, int y, Table table, int indexDice){
        Dice tempDice;
        if (table ==  null)
            return false;
        tempDice = table.checkDiceFromReserve(indexDice);
        if(ownScheme.isPlaceableAllRestr(x, y, tempDice))
            return true;
        else
            return false;
    }

    //Place a dice (!!CALL IT AFTER CHECKING THE PLACEMENT CONDITIONS!!)
    public void placeDice (int x, int y, Table table, int indexDice){
        Dice tempDice;

        tempDice = table.pickDiceFromReserve(indexDice);
        ownScheme.placeDice(x, y, tempDice);
    }

    //Use a tool card
    public boolean useCard(int indexToolCard){
        return false;
    }

    //End the turn
    public void endTurn(){

    }

    //Count the points, at game end or game left. Return -1 if unsuccessfull
    public int countPoints(Table table){

        return -1;
    }

    public Scheme getOwnScheme() {
        return ownScheme;
    }

    @Override
    public String toString(){
        return "String";
    }
}
