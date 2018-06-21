package it.polimi.ingsw.server.model;

/**
 * Box represents a single cell in the window pattern grid
 *
 * @author Andrea
 */
public class Box extends Enum {

    //***************************//
    //        Attributes         //
    //***************************//

    private Color restrictionCol;
    private int restrictionNum;
    private boolean isFull;
    private Dice diceInside = null;

    /**
     * Generic constructor for the box: it doesn't insert color or value restrictions
     *
     * @author Andrea
     */
    public Box(){
        this.restrictionCol = null;
        this.restrictionNum = 0;
        this.isFull = false;
    }

    /**
     * Constructor for the box which insert a color restriction
     *
     * @param restrictionCol: color restriction
     * @author Andrea
     */
    public Box(Color restrictionCol){
        this.restrictionCol = restrictionCol;
        this.restrictionNum = 0;
        this.isFull = false;
    }

    /**
     * Constructor for the box which insert a value restriction
     *
     * @param restrictionNum: value restriction
     * @author Andrea
     */
    public Box(int restrictionNum){
        this.restrictionCol = null;
        this.restrictionNum = restrictionNum;
        this.isFull = false;
    }

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Checks if box can be occupied by a certain dice checking ONLY value restrictions
     *
     * @param dice: the dice to be placed in the box
     * @return true if the dice can be placed, otherwise false
     * @author Andrea
     */
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

    /**
     * Checks if box can be occupied by a certain dice checking ONLY color restrictions
     *
     * @param dice: the dice to be placed in the box
     * @return true if the dice can be placed, otherwise false
     * @author Andrea
     */
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

    /**
     * Empties the box from the containing dice and returns it
     *
     * @return the dice that was in the box before the calling of the method, otherwise null
     * @author Andrea
     */
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

    /**
     * Fills the box with a dice
     *
     * @param dice: the dice to be placed in the box
     * @return true if the dice is correctly placed, false if the box is already full
     * @author Andrea
     */
    public boolean setDice(Dice dice){
        if (isFull)
            return false;
        else{
            this.diceInside = dice;
            this.isFull = true;
            return true;
        }
    }

    /**
     * Gets the dice in the box (read-only)
     *
     * @return the dice inside the box if present, otherwise null
     * @author Andrea
     */
    public Dice getDice(){
        if (this.diceInside == null || !isFull)
            return null;
        else
            return this.diceInside;
    }

    /**
     * Gets the color restriction of the box (if present)
     *
     * @return the color restriction of the box (null if the box has not color restrictions)
     * @author Andrea
     */
    public Color getRestrictionCol (){
        return this.restrictionCol;
    }

    /**
     * Gets the value restriction of the box (if present)
     *
     * @return the value restriction of the box (0 if the box has not value restrictions)
     * @author Andrea
     */
    public int getRestrictionNum (){
        return this.restrictionNum;
    }

    /**
     * Gets the occupation status of the box
     *
     * @return isFull boolean variable: false if the box is empty, true if the box is full
     * @author Andrea
     */
    public boolean isFull(){
        return this.isFull;
    }

    /**
     * Used to print a box
     *
     * @return the string that represents the box
     * @author Andrea
     */
    @Override
    public String toString(){
        String escape;
        if(this.diceInside == null || !isFull){
            if(this.restrictionCol == null && this.restrictionNum == 0)
                return "[ ]\t";
            else if(this.restrictionCol != null) {
                escape = this.restrictionCol.escape();
                return escape+"["+this.restrictionCol.toString().substring(0,1)+"]\t"+Color.RESET;
            } else
                return "["+this.restrictionNum+"]\t";
        } else
            return "["+this.diceInside+"]\t";
    }

}