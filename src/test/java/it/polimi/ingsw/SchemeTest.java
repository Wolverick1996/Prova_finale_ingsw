package it.polimi.ingsw;

import it.polimi.ingsw.model.Scheme;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SchemeTest {

    @Test
    void initializeTest() {
        Random rand = new Random();
        int id = -1;

        Scheme scheme = Scheme.initialize(id);
        assertNull(scheme);

        id = (rand.nextInt(24)+1);
        assertNotNull(scheme.initialize(id));
    }

    @Test
    void checkBox() {
    }

    @Test
    void isPlaceableNoCol() {
    }

    @Test
    void isPlaceableNoNum() {
    }

    @Test
    void isPlaceableNoDiceNear() {
    }

    @Test
    void isGridFree() {
    }

    @Test
    void removeDice() {
    }

    @Test
    void placeDice() {
    }

    @Test
    void getName() {
    }

    @Test
    void getDifficulty() {
    }

    @Test
    void getGrid() {
    }

}