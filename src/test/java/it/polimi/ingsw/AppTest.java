package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AppTest {

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