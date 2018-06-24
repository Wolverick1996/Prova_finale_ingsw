package it.polimi.ingsw;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Enum;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ToolCardTest is the class which contains tests related to tool cards functions
 * This class affects ToolHandler class in model because needs special attributes and methods to simulate user inputs
 *
 * @author Riccardo
 */
class ToolCardTest {

    /**
     * Tests the constructor of the tool card, trying also to create inexistent cards
     *
     * @throws IllegalArgumentException if ID passed as a parameter is not valid
     * @author Riccardo
     */
    @Test
    void initializeTest() {
        Random rand = new Random();
        int id = (rand.nextInt(12)+1);
        ToolCard tool;

        assertThrows(IllegalArgumentException.class, () -> new ToolCard(0) );
        assertThrows(IllegalArgumentException.class, () -> new ToolCard(13) );

        tool = new ToolCard(id);
        assertNotNull(tool.getName());
        assertNotNull(tool.getDescription());
    }

    /**
     * Tests correct and wrong usages of tool card 1
     *
     * @author Riccardo
     */
    @Test
    void tool1Test() {
        Table instance = new Table(2);

        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(1);
        p.chooseScheme(scheme);

        int value = instance.checkDiceFromReserve(0).getValue();

        //If value is 1 or 6 I want to test incorrect operations (1-1, 6+1), otherwise I will sum +1
        if (value == 1)
            ToolHandler.simulateInput10("-1");
        else
            ToolHandler.simulateInput10("+1");

        ToolCard tool = new ToolCard(1);

        //Testing failing extractions from reserve
        ToolHandler.simulateInput9("-1");
        assertFalse(tool.toolEffect(p, instance));
        ToolHandler.simulateInput9("5");
        assertFalse(tool.toolEffect(p, instance));

        //Choosing first dice position in reserve
        ToolHandler.simulateInput9("0");

        //NOTE: Tokens increasing/decreasing is tested just for the first card because if "use" method returns true is always made correctly
        int tooltokens = tool.getTokens();
        int playertokens = p.getTokens();

        if (value == 1 || value == 6) {
            assertFalse(tool.toolEffect(p, instance));
            assertEquals(tooltokens, tool.getTokens());
            assertEquals(playertokens, p.getTokens());
            assertEquals(value, instance.checkDiceFromReserve(4).getValue());
        } else {
            assertTrue(tool.toolEffect(p, instance));
            assertEquals(tooltokens+1, tool.getTokens());
            assertEquals(playertokens-1, p.getTokens());
            assertEquals(value+1, instance.checkDiceFromReserve(4).getValue());
        }
    }

    /**
     * Tests correct and wrong usages of tool card 2 and 3
     *
     * @author Riccardo
     */
    @Test
    void tool23Test() {
        ToolHandler.testCounter = 1;
        Table instance = new Table(2);

        Player p = PublicOCTest.createPlayerScheme();
        PublicOCTest.editPlayerScheme(p);

        ToolCard tool = new ToolCard(2);

        Dice dice = p.getOwnScheme().checkBox(1, 3);

        //Testing extraction from an empty box
        ToolHandler.simulateInput1("1");
        ToolHandler.simulateInput2("2");
        assertFalse(tool.toolEffect(p, instance));

        //Testing placement failure (due to value restrictions)
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput2("3");
        ToolHandler.simulateInput3("1");
        ToolHandler.simulateInput4("4");
        assertFalse(tool.toolEffect(p, instance));
        assertEquals(dice, p.getOwnScheme().checkBox(1, 3));

        //Testing correct placement
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput3("2");
        ToolHandler.simulateInput4("1");
        assertTrue(tool.toolEffect(p, instance));
        assertEquals(dice, p.getOwnScheme().checkBox(2, 1));

        //Testing placement failure (due to value restrictions) and reintroduction of the dice in the last position
        //Placement should be allowed despite the previous placement was made using a tool
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput1("2");
        ToolHandler.simulateInput2("1");
        ToolHandler.simulateInput3("1");
        ToolHandler.simulateInput4("4");
        assertFalse(tool.toolEffect(p, instance));
        assertEquals(dice, p.getOwnScheme().checkBox(2, 1));
    }

    /**
     * Tests correct and wrong usages of tool card 4
     *
     * @author Riccardo
     */
    @Test
    void tool4Test() {
        ToolHandler.testCounter = 1;
        Table instance = new Table(2);

        Player p = PublicOCTest.createPlayerScheme();
        PublicOCTest.editPlayerScheme(p);

        ToolCard tool = new ToolCard(4);

        Dice dice1 = p.getOwnScheme().checkBox(1, 3);

        //Testing first extraction failure
        ToolHandler.simulateInput1("1");
        ToolHandler.simulateInput2("2");
        assertFalse(tool.toolEffect(p, instance));

        //Testing first placement failure (due to value restrictions)
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput2("3");
        ToolHandler.simulateInput3("1");
        ToolHandler.simulateInput4("4");
        assertFalse(tool.toolEffect(p, instance));
        assertEquals(dice1, p.getOwnScheme().checkBox(1, 3));

        //Testing second extraction failure
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput3("3");
        ToolHandler.simulateInput4("0");
        ToolHandler.simulateInput5("1");
        ToolHandler.simulateInput6("2");
        assertFalse(tool.toolEffect(p, instance));
        assertEquals(dice1, p.getOwnScheme().checkBox(1, 3));
        assertNull(p.getOwnScheme().checkBox(3, 0));

        Dice dice2 = p.getOwnScheme().checkBox(0, 3);

        //Testing second placement failure (due to color restrictions)
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput5("0");
        ToolHandler.simulateInput6("3");
        ToolHandler.simulateInput7("1");
        ToolHandler.simulateInput8("4");
        assertFalse(tool.toolEffect(p, instance));
        assertEquals(dice1, p.getOwnScheme().checkBox(1, 3));
        assertNull(p.getOwnScheme().checkBox(3, 0));
        assertEquals(dice2, p.getOwnScheme().checkBox(0, 3));

        //Testing correct tool card using
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput8("3");
        assertTrue(tool.toolEffect(p, instance));
        assertEquals(dice1, p.getOwnScheme().checkBox(3, 0));
        assertNull(p.getOwnScheme().checkBox(0, 3));
        assertEquals(dice2, p.getOwnScheme().checkBox(1, 3));
    }

    /**
     * Tests correct and wrong usages of tool card 5
     *
     * @author Riccardo
     */
    @Test
    void tool5Test() {
        Table instance = new Table(2);

        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(1);
        p.chooseScheme(scheme);

        //Putting a dice in round track
        Dice d = new Dice();
        instance.putDiceInRoundtrack(d);

        ToolCard tool = new ToolCard(5);

        //Testing failing extractions from reserve
        ToolHandler.simulateInput9("-1");
        assertFalse(tool.toolEffect(p, instance));

        //Choosing first dice position in reserve
        ToolHandler.simulateInput9("0");
        Dice dice1 = instance.checkDiceFromReserve(0);

        //Testing failing extractions from round track
        ToolHandler.simulateInput11("-1");
        assertFalse(tool.toolEffect(p, instance));
        assertEquals(dice1, instance.checkDiceFromReserve(4));

        dice1 = instance.checkDiceFromReserve(0);
        //Choosing first dice position in round track
        ToolHandler.simulateInput11("0");
        Dice dice2 = instance.checkDiceFromRoundtrack(0);

        assertTrue(tool.toolEffect(p, instance));
        assertEquals(dice2, instance.checkDiceFromReserve(4));
        assertEquals(dice1, instance.checkDiceFromRoundtrack(0));
    }

    /**
     * Tests correct and wrong usages of tool card 6
     *
     * @author Riccardo
     */
    @Test
    void tool6Test() {
        ToolHandler.testCounter = 1;
        Table instance = new Table(2);

        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(4);
        p.chooseScheme(scheme);

        ToolCard tool = new ToolCard(6);

        //Testing failing extractions from reserve
        ToolHandler.simulateInput9("-1");
        assertFalse(tool.toolEffect(p, instance));

        //Choosing first dice position in reserve
        ToolHandler.simulateInput9("0");
        Enum.Color color = instance.checkDiceFromReserve(0).getColor();

        //Using tool card correctly
        ToolHandler.simulateInput1("0");
        ToolHandler.simulateInput2("1");
        assertTrue(tool.toolEffect(p, instance));
        assertEquals(color, p.getOwnScheme().checkBox(0, 1).getColor());

        //Using tool card correctly but without resetting canExtract: dice should be putted into the reserve
        color = instance.checkDiceFromReserve(0).getColor();
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput1("1");
        ToolHandler.simulateInput2("0");
        assertTrue(tool.toolEffect(p, instance));
        assertEquals(color, instance.checkDiceFromReserve(3).getColor());

        //Using tool card correctly with a wrong placement: dice should be putted into the reserve
        instance.setCanExtract(true);
        color = instance.checkDiceFromReserve(0).getColor();
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput1("0");
        ToolHandler.simulateInput2("1");
        assertTrue(tool.toolEffect(p, instance));
        assertEquals(color, instance.checkDiceFromReserve(3).getColor());

        assertEquals(0, p.getTokens());
        assertEquals(5, tool.getTokens());

        //Tool card use should not be allowed cause player has 0 tokens
        assertFalse(tool.toolEffect(p, instance));
    }

    /**
     * Tests correct and wrong usages of tool card 7
     *
     * @author Riccardo
     */
    @Test
    void tool7Test() {
        Player p1 = new Player("ingconti", 0);
        Player p2 = new Player("n1zzo", 1);
        Player p3 = new Player("michele-bertoni", 2);
        Player p4 = new Player("valerio-castelli", 3);
        List<String> nicknames = Arrays.asList("ingconti", "n1zzo", "michele-bertoni", "valerio-castelli");

        Table instance = new Table(4);
        instance.setPlayers(nicknames);

        Scheme scheme = Scheme.initialize(1);
        p1.chooseScheme(scheme);

        ToolCard tool = new ToolCard(7);

        String reserve = instance.printReserve();

        for (int i=0; i<3; i++)
            instance.nextTurn();

        //Tool card should fail because is not already the second part of the round
        assertFalse(tool.toolEffect(p1, instance));
        assertEquals(reserve, instance.printReserve());

        instance.nextTurn();

        //Tool card should be used successfully
        assertTrue(tool.toolEffect(p1, instance));
        assertNotEquals(reserve, instance.printReserve());
    }

    /**
     * Tests correct and wrong usages of tool card 8
     * NOTE: This test covers just a portion of code related to tool card 8 because its implementation is predominantly controller-side
     *
     * @author Riccardo
     */
    @Test
    void tool8Test() {
        ToolHandler.simulateInput1("Something to use test methods");
        Player p1 = new Player("ingconti", 0);
        Player p2 = new Player("n1zzo", 1);
        Player p3 = new Player("michele-bertoni", 2);
        Player p4 = new Player("valerio-castelli", 3);
        List<String> nicknames = Arrays.asList("ingconti", "n1zzo", "michele-bertoni", "valerio-castelli");

        Table instance = new Table(4);
        instance.setPlayers(nicknames);

        Scheme scheme = Scheme.initialize(1);
        p1.chooseScheme(scheme);

        ToolCard tool = new ToolCard(8);

        for (int i=0; i<3; i++)
            instance.nextTurn();

        //Tool card should be used successfully
        assertTrue(tool.toolEffect(p1, instance));

        instance.nextTurn();

        //Tool card should fail because is not anymore the first part of the round
        assertFalse(tool.toolEffect(p1, instance));
    }

    /**
     * Tests correct and wrong usages of tool card 9
     *
     * @author Riccardo
     */
    @Test
    void tool9Test() {
        ToolHandler.testCounter = 1;
        Table instance = new Table(2);

        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(1);
        p.chooseScheme(scheme);

        ToolCard tool = new ToolCard(9);

        p.placeDice(0, 3, instance, 0);

        //Tool card should fail cause player has already placed in this turn
        ToolHandler.simulateInput9("0");
        ToolHandler.simulateInput1("0");
        ToolHandler.simulateInput2("2");
        assertFalse(tool.toolEffect(p, instance));

        instance.setCanExtract(true);

        //Testing failing extractions from reserve
        ToolHandler.simulateInput9("-1");
        assertFalse(tool.toolEffect(p, instance));

        Dice dice = instance.checkDiceFromReserve(0);

        //Trying to place in a position adjacent to another one (should fails)
        ToolHandler.simulateInput9("0");
        assertFalse(tool.toolEffect(p, instance));
        assertEquals(dice, instance.checkDiceFromReserve(3));

        dice = instance.checkDiceFromReserve(0);

        //Tool card should be used successfully
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput1("1");
        ToolHandler.simulateInput2("1");
        assertTrue(tool.toolEffect(p, instance));
        assertEquals(dice, p.getOwnScheme().checkBox(1, 1));
        assertFalse(instance.getCanExtract());
    }

    /**
     * Tests correct and wrong usages of tool card 10
     *
     * @author Riccardo
     */
    @Test
    void tool10Test() {
        Table instance = new Table(2);

        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(1);
        p.chooseScheme(scheme);

        ToolCard tool = new ToolCard(10);

        //Testing failing extractions from reserve
        ToolHandler.simulateInput9("-1");
        assertFalse(tool.toolEffect(p, instance));

        int value = instance.checkDiceFromReserve(0).getValue();

        //Tool card should be used successfully
        ToolHandler.simulateInput9("0");
        assertTrue(tool.toolEffect(p, instance));
        assertEquals(7-value, instance.checkDiceFromReserve(4).getValue());
    }

    /**
     * Tests correct and wrong usages of tool card 11
     *
     * @author Riccardo
     */
    @Test
    void tool11Test() {
        ToolHandler.testCounter = 1;
        Table instance = new Table(2);

        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(4);
        p.chooseScheme(scheme);

        ToolCard tool = new ToolCard(11);

        //Testing failing extractions from reserve
        ToolHandler.simulateInput9("-1");
        assertFalse(tool.toolEffect(p, instance));
        assertNotNull(instance.checkDiceFromReserve(4));

        //Testing not valid input value: method should returns true and dice should be putted into the reserve
        ToolHandler.simulateInput9("0");
        ToolHandler.simulateInput10("0");
        assertTrue(tool.toolEffect(p, instance));
        assertNotNull(instance.checkDiceFromReserve(4));

        //Tool card should be used successfully
        ToolHandler.simulateInput10("1");
        ToolHandler.simulateInput1("0");
        ToolHandler.simulateInput2("1");
        assertTrue(tool.toolEffect(p, instance));
        assertNull(instance.checkDiceFromReserve(4));
        assertNotNull(p.getOwnScheme().checkBox(0, 1));

        //Using tool card correctly but without resetting canExtract: dice should be putted into the reserve
        ToolHandler.testCounter = 1;
        assertTrue(tool.toolEffect(p, instance));
        assertNotNull(instance.checkDiceFromReserve(3));
    }

    /**
     * Tests correct and wrong usages of tool card 12
     * NOTE: This test doesn't check color matches between dice chosen from round track and dice chosen from window pattern, matches are always correct
     *
     * @author Riccardo
     */
    @Test
    void tool12Test() {
        ToolHandler.testCounter = 1;
        Table instance = new Table(2);

        Player p = PublicOCTest.createPlayerScheme();
        PublicOCTest.editPlayerScheme(p);

        //Putting a dice in round track
        Dice d = new Dice(Enum.Color.PURPLE);
        instance.putDiceInRoundtrack(d);

        ToolCard tool = new ToolCard(12);

        //Testing failing choice from round track
        ToolHandler.simulateInput11("-1");
        assertFalse(tool.toolEffect(p, instance));

        ToolHandler.simulateInput11("0");
        Dice dice1 = p.getOwnScheme().checkBox(1, 3);

        //Testing first extraction failure
        ToolHandler.simulateInput1("1");
        ToolHandler.simulateInput2("2");
        assertFalse(tool.toolEffect(p, instance));

        //Testing first placement failure (due to value restrictions)
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput2("3");
        ToolHandler.simulateInput3("1");
        ToolHandler.simulateInput4("4");
        assertFalse(tool.toolEffect(p, instance));
        assertEquals(dice1, p.getOwnScheme().checkBox(1, 3));

        //Testing placement of just one dice (correctly)
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput3("3");
        ToolHandler.simulateInput4("0");
        ToolHandler.simulateInput12(false);
        assertTrue(tool.toolEffect(p, instance));

        p = PublicOCTest.createPlayerScheme();
        PublicOCTest.editPlayerScheme(p);
        dice1 = p.getOwnScheme().checkBox(1, 3);
        ToolHandler.simulateInput12(true);

        //Testing second extraction failure
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput5("1");
        ToolHandler.simulateInput6("2");
        assertFalse(tool.toolEffect(p, instance));
        assertEquals(dice1, p.getOwnScheme().checkBox(1, 3));
        assertNull(p.getOwnScheme().checkBox(3, 0));

        Dice dice2 = p.getOwnScheme().checkBox(1, 1);

        //Testing second placement failure (due to value restrictions)
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput6("1");
        ToolHandler.simulateInput7("1");
        ToolHandler.simulateInput8("4");
        assertFalse(tool.toolEffect(p, instance));
        assertEquals(dice1, p.getOwnScheme().checkBox(1, 3));
        assertNull(p.getOwnScheme().checkBox(3, 0));
        assertEquals(dice2, p.getOwnScheme().checkBox(1, 1));

        //Testing correct tool card using (with two placements)
        ToolHandler.testCounter = 1;
        ToolHandler.simulateInput8("3");
        assertTrue(tool.toolEffect(p, instance));
        assertEquals(dice1, p.getOwnScheme().checkBox(3, 0));
        assertNull(p.getOwnScheme().checkBox(1, 1));
        assertEquals(dice2, p.getOwnScheme().checkBox(1, 3));
    }

}