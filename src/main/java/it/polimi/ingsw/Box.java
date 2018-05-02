package it.polimi.ingsw;

import java.lang.*;

public class Box extends Enum {

    //***************************//
    //        Attributes         //
    //***************************//

    private Color restrictionCol;
    private int restrictionNum;
    private boolean isFull;
    private Dice diceInside = null;

    //constructor
    public Box(Color restrictionCol, int restrictionNum){
        this.restrictionCol = restrictionCol;
        this.restrictionNum = restrictionNum;
        isFull = false;
    }

    //***************************//
    //         Methods           //
    //***************************//

    //Check if Box can be used
    public boolean isEmployable(Dice dice){
        if(this.restrictionNum == 0 || this.restrictionCol == Color.RESET ||
                dice.getColor() == this.restrictionCol || dice.getValue() == this.restrictionNum)
            return true;
        else
            return false;
    }

    //Free the box
    public boolean free(){
        if (!this.isFull)
            return false;
        else{
            this.isFull = false;
            this.diceInside = null;
            return true;
        }
    }

    //Fill the box
    public boolean set(Dice dice){
        if (!this.free() || !this.isEmployable(dice))
            return false;
        else{
            this.diceInside = dice;
            this.isFull = true;
            return true;
        }
    }

    //Get the dice (ReadOnly)
    public Dice getDice(){
        if (this.diceInside == null || !isFull)
            return null;
        else
            return this.diceInside;
    }

    @Override
    public String toString(){
        if(this.diceInside == null || !isFull){
            if(this.restrictionCol == RESET && this.restrictionNum == 0)
                return "The box is empty and has no restrictions";
            else if(this.restrictionCol != RESET)
                return "The box is empty and has a color restriction ("+restrictionCol+")";
            else
                return "The box is empty and has a number restriction ("+restrictionNum+")";
        }
        else
            return "In the box there is a "+this.diceInside.getColor()+" "+this.diceInside.getValue()+" dice";
    }

}
