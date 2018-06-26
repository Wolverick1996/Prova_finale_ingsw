package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Dice;
import it.polimi.ingsw.server.model.Enum;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.PublicOC;
import it.polimi.ingsw.server.model.Scheme;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PublicOCTest is the class which contains tests related to public objective card functions
 *
 * @author Riccardo
 */
class PublicOCTest {

    /**
     * Associates a player with a window pattern and fills the grid with dice correctly placed
     * The method was created to be used only in tests
     *
     * @return the player, with his completely filled window pattern
     * @author Riccardo
     */
    static Player createPlayerScheme(){
        Player p = new Player("ingconti", 0);
        Scheme scheme = Scheme.initialize(1, false, 24);
        p.chooseScheme(scheme);

        scheme.placeDice(0, 0, new Dice(Enum.Color.YELLOW, 6));
        scheme.placeDice(0, 1, new Dice(Enum.Color.BLUE, 2));
        scheme.placeDice(0, 2, new Dice(Enum.Color.RED, 3));
        scheme.placeDice(0, 3, new Dice(Enum.Color.GREEN, 4));
        scheme.placeDice(0, 4, new Dice(Enum.Color.PURPLE, 1));

        scheme.placeDice(1, 0, new Dice(Enum.Color.GREEN, 2));
        scheme.placeDice(1, 1, new Dice(Enum.Color.PURPLE, 6));
        scheme.placeDice(1, 2, new Dice(Enum.Color.BLUE, 5));
        scheme.placeDice(1, 3, new Dice(Enum.Color.PURPLE, 2));
        scheme.placeDice(1, 4, new Dice(Enum.Color.YELLOW, 4));

        scheme.placeDice(2, 0, new Dice(Enum.Color.RED, 3));
        scheme.placeDice(2, 1, new Dice(Enum.Color.GREEN, 4));
        scheme.placeDice(2, 2, new Dice(Enum.Color.RED, 6));
        scheme.placeDice(2, 3, new Dice(Enum.Color.YELLOW, 1));
        scheme.placeDice(2, 4, new Dice(Enum.Color.GREEN, 5));

        scheme.placeDice(3, 0, new Dice(Enum.Color.PURPLE, 2));
        scheme.placeDice(3, 1, new Dice(Enum.Color.YELLOW, 1));
        scheme.placeDice(3, 2, new Dice(Enum.Color.GREEN, 5));
        scheme.placeDice(3, 3, new Dice(Enum.Color.BLUE, 4));
        scheme.placeDice(3, 4, new Dice(Enum.Color.YELLOW, 2));

        System.out.println(p);
        return p;
    }

    /**
     * Edits the window pattern of the player removing some dice
     * The method was created to be used only in tests
     *
     * @param p: the player whose window pattern should be modified
     * @return the player, with his modified window pattern
     * @author Riccardo
     */
    static Player editPlayerScheme(Player p){
        Scheme scheme = p.getOwnScheme();

        scheme.removeDice(1, 2);
        scheme.removeDice(1, 4);
        scheme.removeDice(2, 1);
        scheme.removeDice(3, 0);

        System.out.println(p);
        return p;
    }

    /**
     * Tests the constructor of the public objective card, trying also to create inexistent cards
     *
     * @throws IllegalArgumentException if ID passed as a parameter is not valid
     * @author Riccardo
     */
    @Test
    void initializeTest() {
        Random rand = new Random();
        int id = (rand.nextInt(10)+1);
        PublicOC pub;

        assertThrows(IllegalArgumentException.class, () -> new PublicOC(0) );
        assertThrows(IllegalArgumentException.class, () -> new PublicOC(11) );

        pub = new PublicOC(id);
        assertNotNull(pub.getName());
        assertNotNull(pub.getDescription());
    }

    /**
     * Tests the counting of points related to the public objective card 1
     *
     * @author Riccardo
     */
    @Test
    void pubOC1Test() {
        Player p = createPlayerScheme();
        PublicOC pub = new PublicOC(1);

        //1 occurence in the scheme * 6 points
        assertEquals(6, pub.countPoints(p));

        p = editPlayerScheme(p);

        //1 occurence in the scheme * 6 points
        assertEquals(6, pub.countPoints(p));
    }

    /**
     * Tests the counting of points related to the public objective card 2
     *
     * @author Riccardo
     */
    @Test
    void pubOC2Test() {
        Player p = createPlayerScheme();
        PublicOC pub = new PublicOC(2);

        //3 occurences in the scheme * 5 points
        assertEquals(15, pub.countPoints(p));

        p = editPlayerScheme(p);

        //1 occurence in the scheme * 5 points
        assertEquals(5, pub.countPoints(p));
    }

    /**
     * Tests the counting of points related to the public objective card 3
     *
     * @author Riccardo
     */
    @Test
    void pubOC3Test() {
        Player p = createPlayerScheme();
        PublicOC pub = new PublicOC(3);

        //2 occurences in the scheme * 5 points
        assertEquals(10, pub.countPoints(p));

        p = editPlayerScheme(p);

        //1 occurence in the scheme * 5 points
        assertEquals(5, pub.countPoints(p));
    }

    /**
     * Tests the counting of points related to the public objective card 4
     *
     * @author Riccardo
     */
    @Test
    void pubOC4Test() {
        Player p = createPlayerScheme();
        PublicOC pub = new PublicOC(4);

        //2 occurences in the scheme * 4 points
        assertEquals(8, pub.countPoints(p));

        p = editPlayerScheme(p);

        //0 occurences in the scheme * 4 points
        assertEquals(0, pub.countPoints(p));
    }

    /**
     * Tests the counting of points related to public objective cards 5, 6 and 7
     *
     * @author Riccardo
     */
    @Test
    void pubOC567Test() {
        Player p = createPlayerScheme();
        PublicOC pub = new PublicOC(5);

        //3 occurences in the scheme * 2 points
        assertEquals(6, pub.countPoints(p));
        pub = new PublicOC(6);

        //2 occurences in the scheme * 2 points
        assertEquals(4, pub.countPoints(p));
        pub = new PublicOC(7);

        //3 occurences in the scheme * 2 points
        assertEquals(6, pub.countPoints(p));

        p = editPlayerScheme(p);
        pub = new PublicOC(5);

        //3 occurences in the scheme * 2 points
        assertEquals(6, pub.countPoints(p));
        pub = new PublicOC(6);

        //2 occurences in the scheme * 2 points
        assertEquals(4, pub.countPoints(p));
        pub = new PublicOC(7);

        //2 occurences in the scheme * 2 points
        assertEquals(4, pub.countPoints(p));
    }

    /**
     * Tests the counting of points related to the public objective card 8
     *
     * @author Riccardo
     */
    @Test
    void pubOC8Test() {
        Player p = createPlayerScheme();
        PublicOC pub = new PublicOC(8);

        //2 occurences in the scheme * 5 points
        assertEquals(10, pub.countPoints(p));

        p = editPlayerScheme(p);

        //2 occurences in the scheme * 5 points
        assertEquals(10, pub.countPoints(p));
    }

    /**
     * Tests the counting of points related to the public objective card 9
     *
     * @author Riccardo
     */
    @Test
    void pubOC9Test() {
        Player p = createPlayerScheme();
        PublicOC pub = new PublicOC(9);

        //10 dice of the same color diagonally adjacent
        assertEquals(10, pub.countPoints(p));

        p = editPlayerScheme(p);

        //4 dice of the same color diagonally adjacent
        assertEquals(4, pub.countPoints(p));
    }

    /**
     * Tests the counting of points related to the public objective card 10
     *
     * @author Riccardo
     */
    @Test
    void pubOC10Test() {
        Player p = createPlayerScheme();
        PublicOC pub = new PublicOC(10);

        //3 occurences in the scheme * 4 points
        assertEquals(12, pub.countPoints(p));

        p = editPlayerScheme(p);

        //2 occurences in the scheme * 4 points
        assertEquals(8, pub.countPoints(p));
    }

}