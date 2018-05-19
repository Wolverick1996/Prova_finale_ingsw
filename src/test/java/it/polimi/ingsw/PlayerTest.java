package it.polimi.ingsw;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.Enum;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Scheme;
import it.polimi.ingsw.model.Table;
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

}