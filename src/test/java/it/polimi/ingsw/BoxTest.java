package it.polimi.ingsw;

import it.polimi.ingsw.model.Box;
import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.Enum;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BoxTest is the class which contains tests related to box functions
 *
 * @author Riccardo
 */
class BoxTest {

    /**
     * Tests if a box is employable checking ONLY value restrictions
     * Placement and emptying methods are implicitly tested
     *
     * @author Riccardo
     */
    @Test
    void isEmployableNoColTest() {
        Random rand = new Random();
        Dice dice = new Dice();
        Box box = new Box();
        int otherValue;

        //box should be employable just the first time
        //test setDice method
        assertTrue(box.isEmployableNoCol(dice));
        box.setDice(dice);
        assertFalse(box.isEmployableNoCol(dice));

        //test free method
        assertNotNull(box.free());
        box.free();
        assertNull(box.free());

        //box should be employable because dice respects the number restriction
        box = new Box(dice.getValue());
        assertTrue(box.isEmployableNoCol(dice));
        box.free();

        //box should NOT be employable because dice's second assignment DOESN'T respects the number restriction
        dice = new Dice(rand.nextInt(6)+1);
        box = new Box(dice.getValue());
        do {
            otherValue = rand.nextInt(6) + 1;
        } while (otherValue == dice.getValue());
        dice.assignValue(otherValue);
        assertFalse(box.isEmployableNoCol(dice));
    }

    /**
     * Tests if a box is employable checking ONLY color restrictions
     * Placement and emptying methods are implicitly tested
     *
     * @author Riccardo
     */
    @Test
    void isEmployableNoNumTest() {
        Dice dice = new Dice();
        Box box = new Box();
        Enum.Color otherColor;

        //box should be employable just the first time
        assertTrue(box.isEmployableNoNum(dice));
        box.setDice(dice);
        assertFalse(box.isEmployableNoNum(dice));
        box.free();

        //box should be employable because dice respects the color restriction
        box = new Box(dice.getColor());
        assertTrue(box.isEmployableNoNum(dice));
        box.free();

        //box should NOT be employable because dice's second assignment DOESN'T respects the color restriction
        dice = new Dice(Enum.Color.getRandomColor());
        box = new Box(dice.getColor());
        do {
            otherColor = Enum.Color.getRandomColor();
        } while (otherColor == dice.getColor());
        dice = new Dice(otherColor);
        assertFalse(box.isEmployableNoNum(dice));
    }

    /**
     * Tests the method to view which dice is inside a box and methods to get restrictions of the box
     *
     * @author Riccardo
     */
    @Test
    void getDiceTest() {
        Dice dice = new Dice();
        Box box = new Box();

        assertNull(box.getDice());
        box.setDice(dice);
        assertEquals(dice, box.getDice());

        //test getRestrictions methods
        box = new Box(Enum.Color.RED);
        assertEquals(Enum.Color.RED, box.getRestrictionCol());
        box = new Box(1);
        assertEquals(1, box.getRestrictionNum());
    }

    /**
     * Tests the method to check if a box is full
     *
     * @author Riccardo
     */
    @Test
    void isFullTest() {
        Dice dice = new Dice();
        Box box = new Box();

        assertFalse(box.isFull());
        box.setDice(dice);
        assertTrue(box.isFull());
    }

}