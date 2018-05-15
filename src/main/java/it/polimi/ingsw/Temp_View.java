package it.polimi.ingsw;

public class Temp_View {

    /* This class is intended for alpha testing of the game mechanics, not for single-method testing.
    NOTE:
    */

    public static void main( String[] args ) {
        Scheme temp;
        temp = new Scheme(3);
        System.out.println(temp.toString());

        System.out.println(Enum.Color.RED.escape() + "HELLO " + Enum.Color.PURPLE.escape() + "WORLD" + Enum.Color.RESET);
    }
}
