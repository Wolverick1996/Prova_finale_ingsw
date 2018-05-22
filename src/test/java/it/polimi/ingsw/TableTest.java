package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PrivObjHandler;
import it.polimi.ingsw.model.PubObjHandler;
import it.polimi.ingsw.model.Table;
import org.junit.jupiter.api.Test;

import java.lang.*;
import java.util.Arrays;
import java.util.List;

class TableTest {

    //SHOULD BE COMPLETED WHEN TOOL CARDS WILL BE IMPLEMENTED
    @Test
    void initializeTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);

        assertNotNull(PubObjHandler.getName(0));
        assertNotNull(PubObjHandler.getName(1));
        assertNotNull(PubObjHandler.getName(2));
        assertNotEquals(PubObjHandler.getName(0), PubObjHandler.getName(1), PubObjHandler.getName(2));
    }

    @Test
    void setPlayersTest(){
        Player p1 = new Player("ingconti", 0);
        Player p2 = new Player("n1zzo", 1);
        Player p3 = new Player("michele-bertoni", 2);
        Player p4 = new Player("valerio-castelli", 3);
        List<String> nicknames = Arrays.asList("ingconti", "n1zzo", "michele-bertoni", "valerio-castelli");

        Table instance = new Table(4);

        instance.setPlayers(nicknames);

        assertThrows(IllegalArgumentException.class, () -> PrivObjHandler.setPrivOC(5) );

        assertNotNull(PrivObjHandler.getName(p1));
        assertNotNull(PrivObjHandler.getName(p2));
        assertNotNull(PrivObjHandler.getName(p3));
        assertNotNull(PrivObjHandler.getName(p4));
        assertNotEquals(PrivObjHandler.getName(p1), PrivObjHandler.getName(p2), PrivObjHandler.getName(p3));
        assertNotEquals(PrivObjHandler.getName(p2), PrivObjHandler.getName(p3), PrivObjHandler.getName(p4));
        assertNotEquals(PrivObjHandler.getName(p1), PrivObjHandler.getName(p4));
    }

    @Test
    void nextTurnTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);

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
        Table instance = new Table(numP);

        for (int i=0; i<90-numP*2-1; i++)
            assertNotNull(instance.pickDiceFromBag());

        assertNull(instance.pickDiceFromBag());
    }

    @Test
    void pickFromReserveTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);

        //check that reserve size is numP*2+1
        assertNotNull(instance.checkDiceFromReserve(numP*2));
        assertNull(instance.checkDiceFromReserve(numP*2+1));

        //test sequential extraction in the same turn
        assertNotNull(instance.pickDiceFromReserve(numP*2));
        assertNull(instance.pickDiceFromReserve(numP*2-1));
    }

    @Test
    void roundTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);

        for (int i=0; i<numP*2-1; i++)
            instance.nextTurn();

        //BEFORE the end of the first round: round number should be 0 and roundtrack should be empty
        assertEquals(0, instance.getRound());
        assertNull(instance.checkDiceFromRoundtrack(0));
        assertNotNull(instance.checkDiceFromReserve(0));

        //AFTER the end of the first round: round number should be 1 and roundtrack should not be empty
        //checking also if reserve is filled between one round and another
        instance.nextTurn();
        assertEquals(1, instance.getRound());
        assertNotNull(instance.checkDiceFromRoundtrack(0));
        assertNotNull(instance.checkDiceFromReserve(0));

        //trying to reach round 10
        for (int i=0; i<9*numP*2; i++)
            instance.nextTurn();
        assertEquals(10, instance.getRound());

        //trying to pass round 10 (a WARNING should be returned and round number should remain 10)
        //NOTE: if numP = 4 "Bag is empty" should be printed 8 times
        for (int i=0; i<numP*2; i++)
            instance.nextTurn();
        assertEquals(10, instance.getRound());
    }

    @Test
    void pickFromRoundtrack (){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);

        for (int i=0; i<numP*2-1; i++)
            instance.nextTurn();

        //check that the dice is inserted into the roundtrack exactly at the end of the round
        assertNull(instance.checkDiceFromRoundtrack(numP*2));
        instance.nextTurn();
        assertNotNull(instance.checkDiceFromRoundtrack(numP*2));

        //test pickDiceFromRoundtrack method
        instance.pickDiceFromRoundtrack(numP*2);
        assertNull(instance.checkDiceFromRoundtrack(numP*2));
    }

    @Test
    void putInBagTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);

        //test the movement of a single dice from reserve to bag
        assertNotNull(instance.checkDiceFromReserve(numP*2));
        instance.putDiceInBag(numP*2);
        assertNull(instance.checkDiceFromReserve(numP*2));

        //try moving all dices from reserve to bag
        for (int i=numP*2-1; i>=0; i--)
            instance.putDiceInBag(i);
        assertNull(instance.checkDiceFromReserve(0));
    }

    @Test
    void rerollTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);
        int[] temp = new int[numP*2];
        int flag1 = 0, flag2 = 0, flag3 = 0;

        //can't reroll during the first turn of the round
        assertFalse(instance.rerollReserve());

        for (int i=0; i<numP; i++)
            instance.nextTurn();

        assertTrue(instance.rerollReserve());

        //temp maintains values of the reserve before the reroll
        for (int i=0; i<numP*2; i++)
            temp[i] = instance.checkDiceFromReserve(i).getValue();

        //test rerolling (3 loops to minimize the probability of equity)
        instance.rerollReserve();
        for (int i=0; i<numP*2; i++){
            if (temp[i] == instance.checkDiceFromReserve(i).getValue())
                flag1++; }
        if (flag1 == numP*2)
            flag1 = -1;

        instance.rerollReserve();
        for (int i=0; i<numP*2; i++){
            if (temp[i] == instance.checkDiceFromReserve(i).getValue())
                flag2++; }
        if (flag2 == numP*2)
            flag2 = -1;

        instance.rerollReserve();
        for (int i=0; i<numP*2; i++){
            if (temp[i] == instance.checkDiceFromReserve(i).getValue())
                flag3++; }
        if (flag3 == numP*2)
            flag3 = -1;

        if (flag1 == -1 && flag2 == -1 && flag3 == -1)
            assertTrue(false);
        else
            assertTrue(true);
    }

}