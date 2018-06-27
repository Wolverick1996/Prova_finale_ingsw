package it.polimi.ingsw;

import it.polimi.ingsw.server.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

/**
 * PlayerTest is the class which contains tests related to player functions
 * PrivObjHandler and PubObjHandler are also tested predominantly in this class and in the TableTest one
 *
 * @author Riccardo
 */
class PlayerTest {

    /**
     * Tests the choice of a window pattern and the correct attribution of tokens
     *
     * @author Riccardo
     */
    @Test
    void chooseSchemeTest() {
        Random rand = new Random();
        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(rand.nextInt(24)+1, false, 24);
        p.chooseScheme(scheme);

        assertEquals(p.getTokens(), scheme.getDifficulty());
        assertEquals(p.getOwnScheme(), scheme);
    }

    /**
     * Tests the placement of a dice (inexistent position, right position, wrong position)
     * Table methods to initialize a table and to check from reserve are implicitly tested
     *
     * @author Riccardo
     */
    @Test
    void placeDiceTest() {
        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(1, false, 24);
        p.chooseScheme(scheme);
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);
        Dice dice = instance.checkDiceFromReserve(0);

        //trying to place a dice in an inexistent position
        assertFalse(p.placeDice(0, 2, instance, 10));

        assertTrue(p.placeDice(0, 2, instance, 0));
        //reserve should be decreased by 1
        assertNull(instance.checkDiceFromReserve(numP*2));
        //dice in the first position of reserve should be now in (0,2)
        assertEquals(dice, p.getOwnScheme().checkBox(0,2));

        //trying to place in a wrong position
        assertFalse(p.placeDice(0, 0, instance, 0));
        //placement should not have happened
        assertNull(p.getOwnScheme().checkBox(0, 0));
        //reserve should not be decreased
        assertNotNull(instance.checkDiceFromReserve(numP*2-1));
    }

    /**
     * Tests the extraction of a dice from the window pattern
     * Table methods to initialize a table and to check from reserve are implicitly tested
     * Methods to place dice and to get dice in hand are implicitly tested
     *
     * @author Riccardo
     */
    @Test
    void extractDiceTest() {
        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(1, false, 24);
        p.chooseScheme(scheme);
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);
        Dice dice = instance.checkDiceFromReserve(0);

        assertFalse(p.extractDice(0, 2));
        p.placeDice(0, 2, instance, 0);
        assertTrue(p.extractDice(0, 2));
        assertEquals(dice, p.getDiceInHand());
    }

    /**
     * Tests the counting of points
     * Table method to initialize a table is implicitly tested
     * PrivObjHandler and PubObjHandler methods to count points are implicitly tested
     *
     * @author Riccardo
     */
    @Test
    void countPointsTest() {
        Player p1 = PublicOCTest.createPlayerScheme();

        Player p2 = new Player("n1zzo", 1);
        Scheme scheme = Scheme.initialize(2, false, 24);
        p2.chooseScheme(scheme);

        PrivObjHandler.setPrivOC(2);
        PubObjHandler.setPubOC();

        p1.countPoints();

        //p1 points should be equals to the sum of points obtained from his privOC and from pubOCs
        assertEquals(p1.getPoints(), PrivObjHandler.countPoints(p1) + PubObjHandler.countPoints(p1, 0) +
                PubObjHandler.countPoints(p1, 1) + PubObjHandler.countPoints(p1, 2) + p1.getTokens());

        p1 = PublicOCTest.editPlayerScheme(p1);
        p1.countPoints();

        //p1 points should be decreased cause of grid changes and free boxes
        //4 free boxes in the grid = 4 points less
        assertEquals(p1.getPoints(), PrivObjHandler.countPoints(p1) + PubObjHandler.countPoints(p1, 0) +
                PubObjHandler.countPoints(p1, 1) + PubObjHandler.countPoints(p1, 2) + p1.getTokens() -4);
    }

}