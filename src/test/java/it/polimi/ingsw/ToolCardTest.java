package it.polimi.ingsw;

import it.polimi.ingsw.server.model.ToolCard;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

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

}