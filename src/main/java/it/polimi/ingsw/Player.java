package it.polimi.ingsw;

public class Player {

    //***************************//
    //        Attributes         //
    //***************************//

    private String nickname;
    private int points;
    private int tokens;
    //private Dice diceInHand = null;

    // PrivateOC will work with indexes;
    private int privateOC;

    //constructor
    public Player(String nick){

    }

    //***************************//
    //         Methods           //
    //***************************//

    // Private Objective Card picker
    public void pickOC(){
        //return still not defined
    }

    // Pick a dice from scheme
    public boolean extractDice(int x, int y){
        return false;
    }

    //Place a dice in the scheme
    public boolean placeDice(int x, int y){
        return false;
    }

    //Use a tool card
    public boolean useCard(int num){
        return false;
    }

    //End the turn
    public void endTurn(){

    }

    //Count the points, at game end or game left. Return -1 if unsuccessfull
    public int countPoints(){
        return -1;
    }
}
