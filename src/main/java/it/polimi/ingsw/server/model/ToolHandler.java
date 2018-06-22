package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.IOhandler;

import java.rmi.RemoteException;
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
        } catch (NullPointerException e){
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
     * @param coord: the coordinate to ask (x or y)
     * @param player: the player who is using the tool card
     * @return the coordinate asked (integer)
     * @author Matteo
     */
    static int getCoordinates(String coord, Player player){
        return currentIO.getCoordinate(coord, player.getUsername());
    }

    /**
     * Calls the IOhandler to ask a dice position on the round track from input
     *
     * @param player: the player who is using the tool card
     * @return the dice position asked (integer)
     * @author Matteo
     */
    static int getFromRoundtrack(Player player){
        return currentIO.getDiceFromRoundtrack(player.getUsername());
    }

    /**
     * Calls the IOhandler to ask a dice position on the reserve from input
     *
     * @param player: the player who is using the tool card
     * @return the dice position asked (integer)
     * @author Matteo
     */
    static int getFromReserve(Player player){
        return currentIO.getDiceFromReserve(player.getUsername());
    }

    /**
     * Calls the IOhandler to ask a value from input
     *
     * @param player: the player who is using the tool card
     * @return the value asked
     * @author Matteo
     */
    static int getDiceValue(Boolean restricted, Player player){
        return currentIO.chooseDiceValue(player.getUsername(), restricted);
    }

    /**
     * Calls the IOhandler to send it messages to print out
     *
     * @param player: the player who is using the tool card
     * @param string: the string to print
     * @author Matteo
     */
    public static void notify(Player player, String string){
        try {
            currentIO.notify(player.getUsername(), string);
        } catch (RemoteException e) {
            System.err.println("ERROR IN TOOLHANDLER NOTIFY()" + e.getMessage());
        }
    }

    /**
     * Calls tool card 8 method in controller
     *
     * @author Riccardo
     */
    static void tool8(){
        //Controller.getMyGame(currentIO).useTool8();
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
    public static int getTokens(int index) { return activeID.get(index).getTokens(); }

    /**
     * Used to print all the tool cards in play
     *
     * @return the string that includes all the tool cards in play
     * @author Riccardo
     */
    @Override
    public String toString() {
        String s = "These are the active PublicOCs:\n";
        for(ToolCard t:activeID)
            s = s + t.toString() + "\n";
        return s;
    }

}