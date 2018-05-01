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

    }
    //random value
    public Dice(Color col){

    }
    //random color
    public Dice(int val){

    }
    //random Dice
    public Dice(){

    }

    //***************************//
    //         Methods           //
    //***************************//

    //give the dice another random value
    public void rollDice(){

    }

    //give the dice a certain value
    public void assignValue(){

    }

    @Override
    public String toString(){
        return "String";
    }
}
