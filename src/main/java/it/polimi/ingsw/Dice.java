package it.polimi.ingsw;

import java.lang.*;

public class Dice extends Enum{

    //***************************//
    //        Attributes         //
    //***************************//

    private int value;
    private Color color;

    //constructors
    //defined Dice
    public Dice(Color col, int val){
        this.color = col;
        this.value = val;
    }

    //random value
    public Dice(Color col){
        this.color = col;
        this.value = (int)(Math.random()*6 + 1);
    }

    //random color
    public Dice(int val){
        this.color = Color.getRandomColor();
        this.value = val;
    }

    //random Dice
    public Dice(){
        this.color = Color.getRandomColor();
        this.value = (int)(Math.random()*6 + 1);
    }

    //***************************//
    //         Methods           //
    //***************************//

    //give the dice another random value
    public void rollDice(){
        this.value = (int)(Math.random()*6 + 1);
    }

    //give the dice a certain value
    public void assignValue(int val){
        this.value = val;
    }

    //return the dice value
    public int getValue(){
        return this.value;
    }

    //return the dice color
    public Color getColor(){
        return this.color;
    }


    @Override
    public String toString(){
        return "This is a "+this.color+" "+this.value+" dice";
    }
}
