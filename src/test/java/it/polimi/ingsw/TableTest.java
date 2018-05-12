package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import java.lang.*;

class TableTest {

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
    void pickFromReserveTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = Table.initialize(numP);

        //check that reserve size is numP*2+1
        assertNotNull(instance.checkDiceFromReserve(numP*2));
        assertNull(instance.checkDiceFromReserve(numP*2+1));

        //test sequential extraction in the same turn
        instance.pickDiceFromReserve(numP*2);
        assertNull(instance.pickDiceFromReserve(numP*2-1));
    }

    /*@Test
    void putInBagTest(){
        int numP = (int)(Math.random()*3 + 2);
        Table instance = Table.initialize(numP);

        for (int i=0; i<numP-1; i++)
            instance.nextTurn();
        assertEquals(numP-1, instance.getTurn());

        for (int i=0; i<numP; i++)
            instance.nextTurn();

        instance.nextTurn();

        assertNotNull(instance.checkDiceFromRoundtrack(0));
        instance.putDiceInBag(0);
        assertNull(instance.checkDiceFromRoundtrack(0));

    }*/

}