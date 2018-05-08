package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import java.lang.*;

class AppTest{

    @Test
    void myFirstTest() {
        assertEquals(2, 1 + 1);
    }

    //control Table.initialize() (should first return a Table, then null)

    //control Player.extractDice() , Player.placeDice() , Player.useCard() (should return false if schemes and PrOC are not assigned)

    //control ToolHandler.setTools() (after initialization, names and descriptions should be availables)

    //control Box (if isFull, you should be able to get diceInside using right methods)

    //control Scheme.placeDice() , Scheme.checkBox() (check consistency)

    //control ROUND. Must be < 10
}

class DiceTest extends Enum {

    @Test
    void valueTest(){
        Dice dice;
        int value;

        value = (int)(Math.random()*6 + 1);
        dice = new Dice(value);
        assertEquals(value, dice.getValue());

        value = (int)(Math.random()*6 + 1);
        dice.assignValue(value);
        assertEquals(value, dice.getValue());
    }

    @Test
    void colorTest(){
        Dice dice;
        Color color;

        color = Color.getRandomColor();
        dice = new Dice(color);
        assertEquals(color, dice.getColor());
    }

    @Test
    void definedDiceTest(){
        Dice dice;
        int value;
        Color color;

        value = (int)(Math.random()*6 + 1);
        color = Color.getRandomColor();
        dice = new Dice(color, value);

        assertEquals(value, dice.getValue());
        assertEquals(color, dice.getColor());
    }

    @Test
    void turnDiceTest(){
        Dice dice;
        int old;

        dice = new Dice();
        old = dice.getValue();
        dice.turnDice();
        assertEquals(7, old + dice.getValue());
    }
}

class TableTest{

    @Test
    void singleTableTest(){
        int numP;
        Table instance = null;

        numP = (int)(Math.random()*3 + 2);
        instance = Table.initialize(numP);
        instance = Table.initialize(numP);
        assertNull(instance);
    }
}