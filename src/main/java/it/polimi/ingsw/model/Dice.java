package it.polimi.ingsw.model;
import java.util.Random;

public class Dice extends Enum {

    //***************************//
    //        Attributes         //
    //***************************//

    private int value;
    private Color color;
    private String face;
    private static final String[] faces = { "\u2680", "\u2681", "\u2682", "\u2683", "\u2684", "\u2685" };

    //constructors
    //defined Dice
    public Dice(Color col, int val){
        this.color = col;
        this.value = val;
        this.face = faces[this.value-1];
    }

    //random value
    public Dice(Color col){
        Random rand = new Random();
        this.color = col;
        this.value = rand.nextInt(6)+1;
        this.face = faces[this.value-1];
    }

    //random color
    public Dice(int val){
        this.color = Color.getRandomColor();
        this.value = val;
        this.face = faces[this.value-1];
    }

    //random Dice
    public Dice(){
        Random rand = new Random();
        this.color = Color.getRandomColor();
        this.value = rand.nextInt(6)+1;
        this.face = faces[this.value-1];
    }

    //***************************//
    //         Methods           //
    //***************************//

    //give the dice another random value
    public void rollDice(){
        Random rand = new Random();
        this.value = rand.nextInt(6)+1;
        this.face = faces[this.value-1];
    }

    //give the dice a certain value
    public void assignValue(int val){
        this.value = val;
        this.face = faces[this.value-1];
    }

    //return the dice value
    public int getValue(){
        return this.value;
    }

    //return the dice color
    public Color getColor(){
        return this.color;
    }

    //turn the dice to the opposite face
    public void turnDice() {
        this.value = 7-this.value;
        this.face = faces[this.value-1];
    }

    @Override
    public String toString(){
        String escape = this.color.escape();
        return escape+""+face+""+Color.RESET;
    }

}