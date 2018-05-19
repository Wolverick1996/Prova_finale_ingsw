package it.polimi.ingsw.model;

public class Box extends Enum {

    //***************************//
    //        Attributes         //
    //***************************//

    private Color restrictionCol;
    private int restrictionNum;
    private boolean isFull;
    private Dice diceInside = null;

    //constructors
    public Box(){
        this.restrictionCol = null;
        this.restrictionNum = 0;
        this.isFull = false;
    }

    public Box(Color restrictionCol){
        this.restrictionCol = restrictionCol;
        this.restrictionNum = 0;
        this.isFull = false;
    }

    public Box(int restrictionNum){
        this.restrictionCol = null;
        this.restrictionNum = restrictionNum;
        this.isFull = false;
    }

    //***************************//
    //         Methods           //
    //***************************//

    //Check if Box can be used without color restrictions
    public boolean isEmployableNoCol(Dice dice){
        if(((this.restrictionNum == 0) || dice.getValue() == this.restrictionNum) && !isFull)
            return true;
        else {
            if (this.restrictionNum != 0 && dice.getValue() != this.restrictionNum)
                System.out.println("You can't place the dice here cause of value restrictions");
            else
                System.out.println("Box is full");
            return false;
        }
    }

    //Check if Box can be used without value restrictions
    public boolean isEmployableNoNum(Dice dice){
        if(((this.restrictionCol == null) || dice.getColor() == this.restrictionCol) && !isFull)
            return true;
        else {
            if (this.restrictionCol != null && dice.getColor() != this.restrictionCol)
                System.out.println("You can't place the dice here cause of color restrictions");
            else
                System.out.println("Box is full");
            return false;
        }
    }

    //Free the box
    public Dice free(){
        Dice tempDice = this.diceInside;
        if (!this.isFull)
            return null;
        else{
            this.isFull = false;
            this.diceInside = null;
            return tempDice;
        }
    }

    //Fill the box
    public boolean setDice(Dice dice){
        if (isFull)
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

    //Get the Color restriction
    public Color getRestrictionCol (){
        return this.restrictionCol;
    }

    //Get the Value restriction
    public int getRestrictionNum (){
        return this.restrictionNum;
    }

    //is free?
    public boolean isFull(){
        return this.isFull;
    }

    @Override
    public String toString(){
        String escape;
        if(this.diceInside == null || !isFull){
            if(this.restrictionCol == null && this.restrictionNum == 0)
                return "[#]\t";
            else if(this.restrictionCol != null) {
                escape = this.restrictionCol.escape();
                return escape+"["+this.restrictionCol.toString().substring(0,1)+"]\t"+Color.RESET;
            } else
                return "["+this.restrictionNum+"]\t";
        } else
            return "["+this.diceInside+"]\t";
    }

}