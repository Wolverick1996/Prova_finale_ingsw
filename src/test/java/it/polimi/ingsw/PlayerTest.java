package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void chooseSchemeTest() {
        Random rand = new Random();
        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(rand.nextInt(24)+1);
        p.chooseScheme(scheme);

        assertEquals(p.getTokens(), scheme.getDifficulty());
        assertEquals(p.getOwnScheme(), scheme);
    }

    @Test
    void placeDiceTest() {
        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(1);
        p.chooseScheme(scheme);
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);
        Dice dice = instance.checkDiceFromReserve(0);

        //trying to pick a dice from inexistent position
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

    @Test
    void extractDiceTest() {
        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(1);
        p.chooseScheme(scheme);
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);
        Dice dice = instance.checkDiceFromReserve(0);

        assertFalse(p.extractDice(0, 2));
        p.placeDice(0, 2, instance, 0);
        assertTrue(p.extractDice(0, 2));
        assertEquals(dice, p.getDiceInHand());
    }

    @Test
    void countPointsTest() {
        Player p1 = PublicOCTest.createPlayerScheme();

        Player p2 = new Player("n1zzo", 1);
        Scheme scheme = Scheme.initialize(2);
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