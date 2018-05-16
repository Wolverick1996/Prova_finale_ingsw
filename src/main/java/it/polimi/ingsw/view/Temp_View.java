package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.Enum;
import it.polimi.ingsw.model.Scheme;

public class Temp_View {

    /* This class is intended for alpha testing of the game mechanics, not for single-method testing.
    NOTE:
    */

    public static void main( String[] args ) {
        Scheme temp = Scheme.initialize(3);
        System.out.println(temp);

        Dice d = new Dice(Enum.Color.RED, 5);
        if (temp.placeDice(0,0, d))
            System.out.println(temp);

        System.out.println(Enum.Color.RED.escape() + "HELLO " + Enum.Color.PURPLE.escape() + "WORLD" + Enum.Color.RESET);
    }

}