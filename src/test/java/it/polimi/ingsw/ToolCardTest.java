package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Scheme;
import it.polimi.ingsw.server.model.Table;
import it.polimi.ingsw.server.model.ToolCard;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

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

    /*@Test
    void tool1Test() {
        int numP = (int)(Math.random()*3 + 2);
        Table instance = new Table(numP);

        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(1);
        p.chooseScheme(scheme);

        ToolCard tool = new ToolCard(1);

        String data = "1\r\n";
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            Scanner scanner = new Scanner(System.in);
            System.out.println(scanner.nextLine());
        } finally {
            System.setIn(stdin);
        }

        tool.toolEffect(p, instance);
    }*/

}