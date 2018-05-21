package it.polimi.ingsw;

import it.polimi.ingsw.model.Enum;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PrivateOC;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrivateOCTest {

    @Test
    void initializeTest() {
        PrivateOC priv = new PrivateOC(Enum.Color.getRandomColor());

        assertNotNull(priv.getName());
        assertNotNull(priv.getDescription());
    }

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