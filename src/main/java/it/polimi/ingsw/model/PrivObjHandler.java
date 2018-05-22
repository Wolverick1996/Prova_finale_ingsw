package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PrivObjHandler {

    //***************************//
    //        Attributes         //
    //***************************//

    //sorted for players [p1, p2, p3, p4]
    private static ArrayList<PrivateOC> activeID = new ArrayList<>();

    //***************************//
    //         Methods           //
    //***************************//

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
                        System.out.println("ERROR");
                        break;
                }
            }
        }
    }

    //Call the method or the right PrivOC for counting points
    public static int countPoints(Player player){
        PrivateOC tempPrivateOC = activeID.get(player.getIDplayer());
        return tempPrivateOC.countPoints(player);
    }

    //Get the name (on table)
    public static String getName(Player player){
        return activeID.get(player.getIDplayer()).getName();
    }

    //Get the description (on table)
    public static String getDescription(Player player){
        return activeID.get(player.getIDplayer()).getDescription();
    }

    @Override
    public String toString() {
        String s = "This are the active PrivateOC (from player 1):\n";
        for(PrivateOC p: activeID)
            s = s + p.toString() + "\n";
        return s;
    }
}