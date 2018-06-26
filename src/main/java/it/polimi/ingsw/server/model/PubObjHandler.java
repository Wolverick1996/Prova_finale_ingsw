package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * PubObjHandler represents a kind of public objective card's controller, whose function is mainly to extract and place 3 different cards on the table
 *
 * @author Matteo
 */
public class PubObjHandler {

    //***************************//
    //        Attributes         //
    //***************************//

    private static ArrayList<PublicOC> activeID = new ArrayList<>();
    static final int NUM_PUB_OC = 10;

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Selects 3 random different public objective cards to be placed on the table
     *
     * @throws NullPointerException if there are problems to set public objective cards
     * @author Matteo
     */
    public static void setPubOC(){
        Random rand = new Random ();
        int[] numOnTable = new int[3];

        while (numOnTable[0] == numOnTable[1] || numOnTable[1] == numOnTable[2] || numOnTable[0] == numOnTable[2]){
            numOnTable[0] = rand.nextInt(NUM_PUB_OC)+1;
            numOnTable[1] = rand.nextInt(NUM_PUB_OC)+1;
            numOnTable[2] = rand.nextInt(NUM_PUB_OC)+1;
        }

        try {
            for (int i:  numOnTable){
                switch (i){
                    case 1: activeID.add(new PublicOC(i)); break;
                    case 2: activeID.add(new PublicOC(i)); break;
                    case 3: activeID.add(new PublicOC(i)); break;
                    case 4: activeID.add(new PublicOC(i)); break;
                    case 5: activeID.add(new PublicOC(i)); break;
                    case 6: activeID.add(new PublicOC(i)); break;
                    case 7: activeID.add(new PublicOC(i)); break;
                    case 8: activeID.add(new PublicOC(i)); break;
                    case 9: activeID.add(new PublicOC(i)); break;
                    case 10: activeID.add(new PublicOC(i)); break;
                    default: throw new NullPointerException();
                }
            }
        } catch (NullPointerException e) {
            //Unhandled Exception
            System.err.println("Failed setPubOC");
            System.exit(-1);
        }
    }

    /**
     * Calls the method of the right public objective card to count the points
     *
     * @param player: the player whose window pattern is asked to count the points
     * @param index: the value that indicates the position of the card in the vector of the public objective cards in play
     * @return PublicOC countPoints method result (the number of points earned by the card)
     * @author Matteo
     */
    public static int countPoints(Player player, int index){
        return activeID.get(index).countPoints(player);
    }

    /**
     * Gets the name of the public objective card (on table)
     *
     * @return public objective card's name
     * @author Matteo
     */
    public static String getName(int index){
        return activeID.get(index).getName();
    }

    /**
     * Gets the description of the public objective card (on table)
     *
     * @return public objective card's description
     * @author Matteo
     */
    public static String getDescription(int index){
        return activeID.get(index).getDescription();
    }

    /**
     * Used to print all the public objective cards in play
     *
     * @return the string that includes all the public objective cards in play
     * @author Matteo
     */
    @Override
    public String toString() {
        String s = "These are the active PublicOCs:\n";
        for(PublicOC p:activeID)
            s = s + p.toString() + "\n";
        return s;
    }

}