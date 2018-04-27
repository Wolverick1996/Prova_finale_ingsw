package it.polimi.ingsw;

public class Box extends Enum {

    //***************************//
    //        Attributes         //
    //***************************//

    private Color restrictionCol;
    private int restrictionNum;
    private boolean isFull;
    private Dice diceInside;

    //constructor
    public Box(){
        isFull = false;
    }

    //***************************//
    //         Methods           //
    //***************************//

    //Check if Box can be used
    public boolean isEmployable(Dice dice){
        return false;
    }

    //Free the box
    public boolean free(){
        return false;
    }

    //Fill the box
    public boolean set(Dice dice){
        return false;
    }

    //Get the dice (ReadOnly)
    public Dice getDice(){
        if (isFull == false) {return null;}
        return null;
    }

}
