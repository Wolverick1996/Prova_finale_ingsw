package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.Enum;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * DiceTest is the class which contains tests related to dice functions
 *
 * @author Riccardo
 */
class DiceTest extends Enum {

    /**
     * Tests one of dice constructors (defined value) and methods to assign and get the value of a dice
     *
     * @author Riccardo
     */
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

    /**
     * Tests one of dice constructors (defined color) and the method to get the color of a dice
     *
     * @author Riccardo
     */
    @Test
    void colorTest(){
        Color color = Color.getRandomColor();
        Dice dice = new Dice(color);
        assertEquals(color, dice.getColor());
    }

    /**
     * Tests the specific dice constructor (defined color and value)
     *
     * @author Riccardo
     */
    @Test
    void definedDiceTest(){
        Random rand = new Random();
        int value = rand.nextInt(6)+1;
        Color color = Color.getRandomColor();
        Dice dice = new Dice(color, value);

        assertEquals(value, dice.getValue());
        assertEquals(color, dice.getColor());
    }

    /**
     * Tests the method to turn a dice to the opposite face
     *
     * @author Riccardo
     */
    @Test
    void turnDiceTest(){
        Dice dice = new Dice();
        int old = dice.getValue();
        dice.turnDice();
        assertEquals(7, old + dice.getValue());
    }

}