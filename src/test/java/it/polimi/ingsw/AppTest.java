package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import java.io.OutputStream;
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

class TableTest{

    @Test
    void singleTableTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = Table.initialize(numP);
        instance = Table.initialize(numP);
        assertNull(instance);
    }

    @Test
    void nextTurnTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = Table.initialize(numP);

        for (int i=0; i<numP-1; i++)
            instance.nextTurn();
        assertEquals(numP-1, instance.getTurn());

        for (int i=0; i<numP; i++)
            instance.nextTurn();

        assertEquals(0, instance.getTurn());
    }

    @Test
    void allExtractedTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = Table.initialize(numP);

        for (int i=0; i<90-numP*2-1; i++)
            assertNotNull(instance.pickDiceFromBag());

        assertNull(instance.pickDiceFromBag());
    }

    @Test
    void pickFromBagTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = Table.initialize(numP);

        //check that reserve size is numP*2+1
        assertNotNull(instance.checkDiceFromReserve(numP*2));
        assertNull(instance.checkDiceFromReserve(numP*2+1));

        //test sequential extraction in the same turn
        instance.pickDiceFromReserve(numP*2);
        assertNull(instance.pickDiceFromReserve(numP*2-1));
    }

}