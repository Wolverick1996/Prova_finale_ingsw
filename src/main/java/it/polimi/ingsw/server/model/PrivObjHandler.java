package it.polimi.ingsw.server.model;

import java.util.ArrayList;

/**
 * PrivObjHandler represents a kind of private objective card's controller, whose function is mainly to associate each player with a different card
 *
 * @author Andrea
 */
public class PrivObjHandler {

    //***************************//
    //        Attributes         //
    //***************************//

    //sorted for players [p1, p2, p3, p4]
    private static ArrayList<PrivateOC> activeID = new ArrayList<>();

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Gives each player a different private objective card
     *
     * @param numPlayers: number of players
     * @throws IllegalArgumentException if number of players is not valid
     * @author Andrea
     */
    public static void setPrivOC (int numPlayers){
        int redExt = 0;
        int purpleExt = 0;
        int blueExt = 0;
        int greenExt = 0;
        int yellowExt = 0;

        if (numPlayers < 2 || numPlayers > 4)
            throw new IllegalArgumentException("Number of players not valid!");

        for (int i = 0; i<numPlayers; i++){
            boolean isAVB = false;
            Enum.Color color;
            while(!isAVB){
                color = Enum.Color.getRandomColor();
                switch (color){
                    case RED:
                        if (redExt < 1){
                            activeID.add(new PrivateOC(color));
                            isAVB = true;
                            redExt++;
                        } break;
                    case PURPLE:
                        if (purpleExt < 1) {
                            activeID.add(new PrivateOC(color));
                            isAVB = true;
                            purpleExt++;
                        } break;
                    case BLUE:
                        if (blueExt < 1) {
                            activeID.add(new PrivateOC(color));
                            isAVB = true;
                            blueExt++;
                        } break;
                    case GREEN:
                        if (greenExt < 1) {
                            activeID.add(new PrivateOC(color));
                            isAVB = true;
                            greenExt++;
                        } break;
                    case YELLOW:
                        if (yellowExt < 1) {
                            activeID.add(new PrivateOC(color));
                            isAVB = true;
                            yellowExt++;
                        } break;
                    default:
                        System.err.println("Failed setPrivOC");
                        break;
                }
            }
        }
    }

    /**
     * Calls the method of the right private objective card to count the points
     *
     * @param player: the player whose window pattern is asked to count the points
     * @return PrivateOC countPoints method result (the number of points earned by the card)
     * @author Andrea
     */
    public static int countPoints(Player player){
        PrivateOC tempPrivateOC = activeID.get(player.getIDplayer());
        return tempPrivateOC.countPoints(player);
    }

    /**
     * Gets the name of the private objective card (on table)
     *
     * @param player: the player whose card is asked for the name
     * @return private objective card's name
     * @author Andrea
     */
    public static String getName(Player player){
        return activeID.get(player.getIDplayer()).getName();
    }

    /**
     * Gets the description of the private objective card (on table)
     *
     * @param player: the player whose card is asked for the description
     * @return private objective card's description
     * @author Andrea
     */
    public static String getDescription(Player player){
        return activeID.get(player.getIDplayer()).getDescription();
    }

    /**
     * Gets the color of the private objective card (on table)
     *
     * @param player: the player whose card is asked for the color
     * @return private objective card's color
     * @author Andrea
     */
    public static Enum.Color getColor(Player player){
        return activeID.get(player.getIDplayer()).getColor();
    }

    /**
     * Used to print all the private objective cards in play
     *
     * @return the string that includes all the private objective cards in play
     * @author Andrea
     */
    @Override
    public String toString() {
        String s = "These are the active PrivateOCs (from player 1):\n";
        for(PrivateOC p: activeID)
            s = s + p.toString() + "\n";
        return s;
    }

}