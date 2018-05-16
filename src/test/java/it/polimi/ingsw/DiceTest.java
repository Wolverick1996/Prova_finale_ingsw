package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.Enum;
import org.junit.jupiter.api.Test;

import java.lang.*;
import java.util.Random;

class DiceTest extends Enum {

    @Test
    void valueTest(){
        Random rand = new Random();
        int value = rand.nextInt(6)+1;
        Dice dice = new Dice(value);
        assertEquals(value, dice.getValue());

        value = rand.nextInt(6)+1;
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
        Random rand = new Random();
        int value = rand.nextInt(6)+1;
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