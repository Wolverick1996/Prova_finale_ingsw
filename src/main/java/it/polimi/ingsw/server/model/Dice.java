package it.polimi.ingsw.server.model;

import java.util.Random;

/**
 * Dice has 6 faces and 5 possible colors
 *
 * @author Riccardo
 */
public class Dice extends Enum {

    //***************************//
    //        Attributes         //
    //***************************//

    private int value;
    private Color color;

    /**
     * Specific constructor for the dice: it creates a dice with predetermined color and value
     *
     * @param col: the color that the dice will have to take
     * @param val: the value that the dice will have to take
     * @author Riccardo
     */
    public Dice(Color col, int val){
        this.color = col;
        this.value = val;
    }

    /**
     * Constructor for the dice: it creates a dice with predetermined color and random value
     *
     * @param col: the color that the dice will have to take
     * @author Riccardo
     */
    public Dice(Color col){
        Random rand = new Random();
        this.color = col;
        this.value = rand.nextInt(6)+1;
    }

    /**
     * Constructor for the dice: it creates a dice with predetermined value and random color
     *
     * @param val: the value that the dice will have to take
     * @author Riccardo
     */
    public Dice(int val){
        this.color = Color.getRandomColor();
        this.value = val;
    }

    /**
     * Generic constructor for the dice: it creates a dice with random color and value
     *
     * @author Riccardo
     */
    public Dice(){
        Random rand = new Random();
        this.color = Color.getRandomColor();
        this.value = rand.nextInt(6)+1;
    }

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Gives the dice another random value rolling it
     *
     * @author Riccardo
     */
    void rollDice(){
        Random rand = new Random();
        this.value = rand.nextInt(6)+1;
    }

    /**
     * Gives the dice another specific value (useful for tool cards)
     *
     * @author Riccardo
     */
    public void assignValue(int val){
        this.value = val;
    }

    /**
     * Returns the dice value
     *
     * @return the dice value
     * @author Riccardo
     */
    public int getValue(){
        return this.value;
    }

    /**
     * Returns the dice color
     *
     * @return the dice color
     * @author Riccardo
     */
    public Color getColor(){
        return this.color;
    }

    /**
     * Turns the dice to the opposite face
     *
     * @author Riccardo
     */
    public void turnDice() {
        this.value = 7-this.value;
    }

    /**
     * Used to print a dice
     *
     * @return the string that represents the dice
     * @author Riccardo
     */
    @Override
    public String toString(){
        String escape = this.color.escape();
        return escape+this.value+Color.RESET;
    }

}