package it.polimi.ingsw;

import java.util.ArrayList;
import static java.lang.System.out;

public class PrivObjHandler {

    //***************************//
    //        Attributes         //
    //***************************//

    //sorted for players [p1, p2, p3, p4]
    private ArrayList<PrivateOC> activeID;

    //***************************//
    //         Methods           //
    //***************************//

    //Constructor
    public PrivObjHandler (int numPlayers){
        this.activeID = new ArrayList<>();
        int redExt = 0;
        int purpleExt = 0;
        int bluExt = 0;
        int greenExt = 0;
        int yellowExt = 0;

        for(int i = 0; i<numPlayers; i++){
            boolean isAVB = false;
            Enum.Color color;
            while(!isAVB){
                color = Enum.Color.getRandomColor();
                switch (color){
                    case RED:
                        if (redExt < 1){
                            this.activeID.add(new PrivateOC(color));
                            isAVB = true;
                            redExt++;
                        }break;
                    case PURPLE:
                        if (purpleExt < 1) {
                            this.activeID.add(new PrivateOC(color));
                            isAVB = true;
                            purpleExt++;
                        }break;
                    case BLUE:
                        if (bluExt < 1) {
                            this.activeID.add(new PrivateOC(color));
                            isAVB = true;
                            bluExt++;
                        }break;
                    case GREEN:
                        if (greenExt < 1) {
                            this.activeID.add(new PrivateOC(color));
                            isAVB = true;
                            greenExt++;
                        }break;
                    case YELLOW:
                        if (yellowExt < 1) {
                            this.activeID.add(new PrivateOC(color));
                            isAVB = true;
                            yellowExt++;
                        }break;
                    default:
                        out.println("ERROR");
                        break;
                }
            }
        }
    }

    //Call the method or the right PrivOC for counting points
    public int countPoints(Player player, int num){
        PrivateOC tempPrivateOC = activeID.get(num);
        return tempPrivateOC.countPoints(player);
    }

    //Get the name (on table)
    public String getName(int num){
        return activeID.get(num).getName();
    }

    //Get the description (on table)
    public String getDescription(int num){
        return activeID.get(num).getDescription();
    }
}
