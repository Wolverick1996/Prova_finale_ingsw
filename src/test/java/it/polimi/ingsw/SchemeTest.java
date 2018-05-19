package it.polimi.ingsw;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.Enum;
import it.polimi.ingsw.model.Scheme;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SchemeTest {

    @Test
    void initializeTest() {
        Random rand = new Random();
        int id = -1;

        Scheme scheme = Scheme.initialize(id);
        assertNull(scheme);

        id = (rand.nextInt(24)+1);
        assertNotNull(scheme.initialize(id));
    }

    @Test
    void empty_removeTest() {
        Scheme scheme = Scheme.initialize(1);
        Dice dice = new Dice(Enum.Color.YELLOW, 3);

        assertTrue(scheme.isGridEmpty());

        //placement in correct box respecting color restrictions
        assertTrue(scheme.placeDice(0,0, dice));

        assertFalse(scheme.isGridEmpty());

        //test removeDice method
        assertEquals(dice, scheme.removeDice(0, 0));
        assertNull(scheme.removeDice(0, 0));

        assertTrue(scheme.isGridEmpty());
    }

    @Test
    void wrongBoxesTest() {
        Scheme scheme = Scheme.initialize(1);
        Dice dice = new Dice(Enum.Color.YELLOW, 3);

        //checking not existing boxes
        assertFalse(scheme.placeDice(-1,-1, dice));
        assertFalse(scheme.placeDice(0,5, dice));
        assertFalse(scheme.placeDice(4,0, dice));
    }

    @Test
    void firstDiceTest() {
        Scheme scheme = Scheme.initialize(1);
        System.out.println(scheme);
        Dice dice = new Dice(Enum.Color.YELLOW, 3);

        //can't place the first dice out of border
        assertFalse(scheme.placeDice(2,1, dice));
        //can't place the dice ignoring color restrictions
        assertFalse(scheme.placeDice(1,0, dice));
        //can't place the dice ignoring value restrictions
        assertFalse(scheme.placeDice(0,4, dice));

        //placement in correct box respecting color restrictions
        assertTrue(scheme.placeDice(0,0, dice));
        System.out.println(scheme);
    }

    @Test
    void isPlaceableNoAdjTest() {
        Scheme scheme = Scheme.initialize(1);
        System.out.println(scheme);
        Dice dice = new Dice(Enum.Color.YELLOW, 3);

        //placement in correct box respecting color restrictions
        assertTrue(scheme.placeDice(0,0, dice));
        System.out.println(scheme);

        //can't place in a full box
        dice = new Dice();
        assertFalse(scheme.placeDice(0,0, dice));

        //placement in correct box without restrictions
        dice = new Dice(Enum.Color.RED, 4);
        assertTrue(scheme.placeDice(1,1, dice));
        System.out.println(scheme);

        //can't place in a box not adjacent to another
        assertFalse(scheme.placeDice(1,4, dice));
        assertFalse(scheme.placeDice(3,1, dice));

        dice = new Dice(Enum.Color.PURPLE, 1);
        //can't place the dice ignoring value restrictions
        assertFalse(scheme.placeDice(1,2, dice));
        //can't place the dice ignoring color restrictions
        assertFalse(scheme.placeDice(0,1, dice));
        //placement in correct box without restrictions
        assertTrue(scheme.placeDice(2,1, dice));
        System.out.println(scheme);
    }

    @Test
    void valueAdjacencesTest() {
        Scheme scheme = Scheme.initialize(1);
        System.out.println(scheme);
        Dice dice = new Dice(Enum.Color.YELLOW, 3);

        //placement in correct box respecting color restrictions
        assertTrue(scheme.placeDice(0,0, dice));
        System.out.println(scheme);

        //adjacent with same VALUE
        dice = new Dice(Enum.Color.GREEN, 3);
        assertFalse(scheme.placeDice(1,0, dice));
        assertFalse(scheme.placeDice(0,1, dice));
        assertTrue(scheme.placeDice(1,1, dice));
        System.out.println(scheme);
    }

    @Test
    void colorAdjacencesTest() {
        Scheme scheme = Scheme.initialize(1);
        System.out.println(scheme);
        Dice dice = new Dice(Enum.Color.YELLOW, 3);

        //placement in correct box
        assertTrue(scheme.placeDice(0,3, dice));
        System.out.println(scheme);

        //adjacent with same COLOR
        dice = new Dice(Enum.Color.YELLOW, 4);
        assertFalse(scheme.placeDice(0,2, dice));
        assertFalse(scheme.placeDice(1,3, dice));
        assertTrue(scheme.placeDice(1,4, dice));
        System.out.println(scheme);
    }

    @Test
    void placementTool2Test() {
        Scheme scheme = Scheme.initialize(1);
        System.out.println(scheme);
        Dice dice = new Dice(Enum.Color.GREEN, 3);

        //first dice placement
        assertTrue(scheme.placeFromTool(0,2, 2, dice));
        System.out.println(scheme);

        //test placement from tool 2
        dice = new Dice(Enum.Color.GREEN, 5);
        assertTrue(scheme.placeFromTool(0, 1, 2, dice));
        assertTrue(scheme.placeFromTool(0, 3, 2, dice));
        assertTrue(scheme.placeFromTool(1, 2, 2, dice));
        System.out.println(scheme);

        //placement should not be possible cause of value restrictions
        assertFalse(scheme.placeFromTool(1, 3, 2, dice));
        dice = new Dice(Enum.Color.GREEN, 6);
        assertFalse(scheme.placeFromTool(0, 4, 2, dice));

        //can't place in a full box
        assertFalse(scheme.placeDice(0,2, dice));
        //can't place in a box not adjacent to another
        assertFalse(scheme.placeFromTool(3, 1, 2, dice));
    }

    @Test
    void placementTool3Test() {
        Scheme scheme = Scheme.initialize(1);
        System.out.println(scheme);
        Dice dice = new Dice(Enum.Color.GREEN, 3);

        //first dice placement
        assertTrue(scheme.placeFromTool(0,2, 3, dice));
        System.out.println(scheme);

        //test placement from tool 3
        dice = new Dice(Enum.Color.BLUE, 3);
        assertTrue(scheme.placeFromTool(0, 1, 3, dice));
        assertTrue(scheme.placeFromTool(0, 3, 3, dice));
        assertTrue(scheme.placeFromTool(1, 2, 3, dice));
        System.out.println(scheme);

        //placement should not be possible cause of color restrictions
        assertFalse(scheme.placeFromTool(1, 1, 3, dice));
        dice = new Dice(Enum.Color.GREEN, 3);
        assertFalse(scheme.placeFromTool(2, 2, 3, dice));

        //can't place in a full box
        assertFalse(scheme.placeDice(0,2, dice));
        //can't place in a box not adjacent to another
        assertFalse(scheme.placeFromTool(3, 1, 2, dice));
    }

    @Test
    void placementTool9Test() {
        Scheme scheme = Scheme.initialize(1);
        System.out.println(scheme);
        Dice dice = new Dice(Enum.Color.GREEN, 3);

        //first dice placement
        assertTrue(scheme.placeFromTool(0,2, 9, dice));
        System.out.println(scheme);

        //test placement from tool 9
        assertTrue(scheme.placeFromTool(3, 1, 9, dice));
        System.out.println(scheme);
        dice = new Dice(Enum.Color.PURPLE, 1);
        //tool 9 doesn't allow adjacent placements
        assertFalse(scheme.placeFromTool(3, 2, 9, dice));

        //placement should not be possible cause of color restrictions
        assertFalse(scheme.placeFromTool(3, 3, 9, dice));
        //placement should not be possible cause of value restrictions
        assertFalse(scheme.placeFromTool(1, 4, 9, dice));
    }

}