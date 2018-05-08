package it.polimi.ingsw;

public class Temp_View {

    /* This class is intended for alpha testing of the game mechanics, not for single-method testing.
    NOTE:
 */

    public static void main( String[] args ) {
        String s = new String("\nciao\nciao");
        s = s+"\nciaone";
        System.out.println(Enum.Color.RED.escape() + "HELLO " + Enum.Color.PURPLE.escape() + "WORLD" + Enum.Color.RESET+s );
    }

}
