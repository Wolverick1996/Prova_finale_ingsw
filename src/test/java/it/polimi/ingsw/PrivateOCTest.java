package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Enum;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.PrivateOC;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PrivateOCTest is the class which contains tests related to private objective card functions
 *
 * @author Riccardo
 */
class PrivateOCTest {

    /**
     * Tests the constructor of the private objective card
     *
     * @author Riccardo
     */
    @Test
    void initializeTest() {
        PrivateOC priv = new PrivateOC(Enum.Color.getRandomColor());

        assertNotNull(priv.getName());
        assertNotNull(priv.getDescription());
    }

    /**
     * Tests the counting of points related to the private objective
     *
     * @author Riccardo
     */
    @Test
    void countPointsTest() {
        Player p = PublicOCTest.createPlayerScheme();

        PrivateOC priv = new PrivateOC(Enum.Color.YELLOW);
        assertEquals(14, priv.countPoints(p));

        priv = new PrivateOC(Enum.Color.BLUE);
        assertEquals(11, priv.countPoints(p));

        priv = new PrivateOC(Enum.Color.RED);
        assertEquals(12, priv.countPoints(p));

        priv = new PrivateOC(Enum.Color.PURPLE);
        assertEquals(11, priv.countPoints(p));

        priv = new PrivateOC(Enum.Color.GREEN);
        assertEquals(20, priv.countPoints(p));

        p = PublicOCTest.editPlayerScheme(p);

        priv = new PrivateOC(Enum.Color.YELLOW);
        assertEquals(10, priv.countPoints(p));

        priv = new PrivateOC(Enum.Color.BLUE);
        assertEquals(6, priv.countPoints(p));

        priv = new PrivateOC(Enum.Color.RED);
        assertEquals(12, priv.countPoints(p));

        priv = new PrivateOC(Enum.Color.PURPLE);
        assertEquals(9, priv.countPoints(p));

        priv = new PrivateOC(Enum.Color.GREEN);
        assertEquals(16, priv.countPoints(p));
    }

}