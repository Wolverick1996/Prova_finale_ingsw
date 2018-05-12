package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.*;

class DiceTest extends Enum {

    @Test
    void valueTest(){
        int value = (int)(Math.random()*6 + 1);
        Dice dice = new Dice(value);
        assertEquals(value, dice.getValue());

        value = (int)(Math.random()*6 + 1);
        dice.assignValue(value);
        assertEquals(value, dice.getValue());
    }

    @Test
    void colorTest(){
        Color color = Color.getRandomColor();
        Dice dice = new Dice(color);
        assertEquals(color, dice.getColor());
    }

    @Test
    void definedDiceTest(){
        int value = (int)(Math.random()*6 + 1);
        Color color = Color.getRandomColor();
        Dice dice = new Dice(color, value);

        assertEquals(value, dice.getValue());
        assertEquals(color, dice.getColor());
    }

    @Test
    void turnDiceTest(){
        Dice dice = new Dice();
        int old = dice.getValue();
        dice.turnDice();
        assertEquals(7, old + dice.getValue());
    }

}