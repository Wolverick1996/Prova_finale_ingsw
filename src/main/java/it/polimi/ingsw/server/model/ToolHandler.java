package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * ToolHandler represents a kind of tool card's controller, whose function is mainly to extract and place 3 different cards on the table
 *
 * @author Matteo
 */
public class ToolHandler {

    //***************************//
    //        Attributes         //
    //***************************//

    private static ArrayList<ToolCard> activeID = new ArrayList<>();
    static final int NUM_TOOLS = 12;
    private static IOhandler currentIO;

    //***************************//
    //      Test attributes      //
    //***************************//

    private static String simulatedInput1 = null;
    private static String simulatedInput2 = null;
    private static String simulatedInput3 = null;
    private static String simulatedInput4 = null;
    private static String simulatedInput5 = null;
    private static String simulatedInput6 = null;
    private static String simulatedInput7 = null;
    private static String simulatedInput8 = null;
    private static String simulatedInput9 = null;
    private static String simulatedInput10 = null;
    private static String simulatedInput11 = null;
    private static boolean simulatedInput12;
    public static int testCounter = 1;

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Selects 3 random different tool cards to be placed on the table
     *
     * @throws NullPointerException if there are problems to set tool cards
     * @author Riccardo
     */
    static void setTools(){
        Random rand = new Random ();
        int[] numOnTable = new int[3];

        while (numOnTable[0] == numOnTable[1] || numOnTable[1] == numOnTable[2] || numOnTable[0] == numOnTable[2]){
            numOnTable[0] = rand.nextInt(NUM_TOOLS)+1;
            numOnTable[1] = rand.nextInt(NUM_TOOLS)+1;
            numOnTable[2] = rand.nextInt(NUM_TOOLS)+1;
        }

        try {
            for (int i:  numOnTable){
                switch (i){
                    case 1: activeID.add(new ToolCard(i)); break;
                    case 2: activeID.add(new ToolCard(i)); break;
                    case 3: activeID.add(new ToolCard(i)); break;
                    case 4: activeID.add(new ToolCard(i)); break;
                    case 5: activeID.add(new ToolCard(i)); break;
                    case 6: activeID.add(new ToolCard(i)); break;
                    case 7: activeID.add(new ToolCard(i)); break;
                    case 8: activeID.add(new ToolCard(i)); break;
                    case 9: activeID.add(new ToolCard(i)); break;
                    case 10: activeID.add(new ToolCard(i)); break;
                    case 11: activeID.add(new ToolCard(i)); break;
                    case 12: activeID.add(new ToolCard(i)); break;
                    default: throw new NullPointerException();
                }
            }
        } catch (NullPointerException e) {
            //Unhandled Exception
            System.err.println("Failed setTools");
            System.exit(-1);
        }
    }

    /**
     * Calls the method of the right tool card to use the effect of the card
     *
     * @param index: the value that indicates the position of the card in the vector of the tool cards in play
     * @param player: the player who wants to use the tool card
     * @param table: the instance of table (useful for certain tool cards)
     * @return ToolCard toolEffect method result (true if the card is correctly used, otherwise false)
     * @author Riccardo
     */
    static boolean useTool(int index, Player player, Table table, IOhandler out){
        currentIO = out;
        return activeID.get(index).toolEffect(player, table);
    }

    /**
     * Calls the IOhandler to ask coordinates from input
     *
     * @param player: the player who is using the tool card
     * @return the coordinate asked (integer)
     * @author Matteo
     */
    static int getCoordinates(Player player){
        if (simulatedInput1 == null)
            return currentIO.getCoordinate(player.getUsername());

        /* TEST SECTION ONLY */

        switch (testCounter){
            case 1: testCounter++; return Integer.parseInt(simulatedInput1);
            case 2: testCounter++; return Integer.parseInt(simulatedInput2);
            case 3: testCounter++; return Integer.parseInt(simulatedInput3);
            case 4: testCounter++; return Integer.parseInt(simulatedInput4);
            case 5: testCounter++; return Integer.parseInt(simulatedInput5);
            case 6: testCounter++; return Integer.parseInt(simulatedInput6);
            case 7: testCounter++; return Integer.parseInt(simulatedInput7);
            case 8: testCounter++; return Integer.parseInt(simulatedInput8);
            default: System.err.println("Error");
        }

        return 0;
    }

    /**
     * Calls the IOhandler to ask a dice position on the round track from input
     *
     * @param player: the player who is using the tool card
     * @return the dice position asked (integer)
     * @author Matteo
     */
    static int getFromRoundtrack(Player player){
        if (simulatedInput11 == null)
            return currentIO.getDiceFromRoundtrack(player.getUsername());

        /* TEST SECTION ONLY */
        return Integer.parseInt(simulatedInput11);
    }

    /**
     * Calls the IOhandler to ask a dice position on the reserve from input
     *
     * @param player: the player who is using the tool card
     * @return the dice position asked (integer)
     * @author Matteo
     */
    static int getFromReserve(Player player){
        if (simulatedInput9 == null)
            return currentIO.getDiceFromReserve(player.getUsername());

        /* TEST SECTION ONLY */
        return Integer.parseInt(simulatedInput9);
    }

    /**
     * Calls the IOhandler to ask a value from input
     *
     * @param player: the player who is using the tool card
     * @return the value asked
     * @author Matteo
     */
    static int getDiceValue(boolean restricted, Player player){
        if (simulatedInput10 == null)
            return currentIO.chooseDiceValue(player.getUsername(), restricted);

        /* TEST SECTION ONLY */
        return Integer.parseInt(simulatedInput10);
    }

    /**
     * Calls the IOhandler to ask a char (y/n) and returns true if char inserted is 'y', false if is 'n'
     *
     * @param player: the player who is using the tool card
     * @return true if the char inserted is 'y', false if is 'n'
     * @author Matteo
     */
    static boolean getYesOrNo(Player player){
        if (simulatedInput1 == null){
            return currentIO.yesOrNo(player.getUsername());
        }

        /* TEST SECTION ONLY */
        return simulatedInput12;
    }

    /**
     * Calls the IOhandler to send it messages to print out
     *
     * @param player: the player who is using the tool card
     * @param string: the string to print
     * @author Matteo
     */
    static void notify(Player player, String string){
        if (simulatedInput1 == null && simulatedInput2 == null && simulatedInput3 == null && simulatedInput4 == null && simulatedInput5 == null
                && simulatedInput6 == null && simulatedInput7 == null && simulatedInput8 == null && simulatedInput9 == null &&
                simulatedInput10 == null && simulatedInput11 == null){
            currentIO.notify(player.getUsername(), string);
        }

        /* TEST SECTION ONLY */
        System.out.println(string);
    }

    /**
     * Calls tool card 8 method in controller
     *
     * @author Riccardo
     */
    static void tool8(){
        if (simulatedInput1 == null)
            Controller.getMyGame(currentIO).useTool8();

        /* TEST SECTION ONLY */
    }

    /**
     * Gets the name of the tool card (on table)
     *
     * @return tool card's name
     * @author Riccardo
     */
    public static String getName(int index){
        return activeID.get(index).getName();
    }

    /**
     * Gets the description of the tool card (on table)
     *
     * @return tool card's description
     * @author Riccardo
     */
    public static String getDescription(int index){
        return activeID.get(index).getDescription();
    }

    /**
     * Gets the number of tokens currently on the tool card (on table)
     *
     * @return tool card's tokens
     * @author Riccardo
     */
    public static int getTokens(int index){
        return activeID.get(index).getTokens();
    }

    /**
     * Used to print all the tool cards in play
     *
     * @return the string that includes all the tool cards in play
     * @author Riccardo
     */
    @Override
    public String toString() {
        String s = "These are the active Tool Cards:\n";
        for(ToolCard t:activeID)
            s = s + t.toString() + "\n";
        return s;
    }

    //***************************//
    //       Test methods        //
    //***************************//

    public static void simulateInput1(String message){ simulatedInput1 = message; }
    public static void simulateInput2(String message){ simulatedInput2 = message; }
    public static void simulateInput3(String message){ simulatedInput3 = message; }
    public static void simulateInput4(String message){ simulatedInput4 = message; }
    public static void simulateInput5(String message){ simulatedInput5 = message; }
    public static void simulateInput6(String message){ simulatedInput6 = message; }
    public static void simulateInput7(String message){ simulatedInput7 = message; }
    public static void simulateInput8(String message){ simulatedInput8 = message; }
    public static void simulateInput9(String message){ simulatedInput9 = message; }
    public static void simulateInput10(String message){ simulatedInput10 = message; }
    public static void simulateInput11(String message){ simulatedInput11 = message; }
    public static void simulateInput12(boolean message){ simulatedInput12 = message; }

}