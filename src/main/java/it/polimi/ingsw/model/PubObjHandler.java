package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PubObjHandler {

    //***************************//
    //        Attributes         //
    //***************************//

    private static ArrayList<PublicOC> activeID = new ArrayList<>();
    private static final int NUM_PUB_OC = 10;

    //***************************//
    //         Methods           //
    //***************************//

    private PubObjHandler(){
    }

    //Set the PubOC to be handled in the right position
    public static void setPubOC(){
        try {
            for (int i:  Utility.returnRandomInts(3,1,NUM_PUB_OC)){
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
        }catch (NullPointerException e){
            //Unhandled Exception
            System.out.println("Failed setPubOC");
            System.exit(-1);
        }
    }

    //Call the method or the right PubOC for counting points
    public static int countPoints(Player player, int index){
        return activeID.get(index).countPoints(player);
    }

    //Get the name (on table)
    public static String getName(int index){
        return activeID.get(index).getName();
    }

    //Get the description (on table)
    public static String getDescription(int index){
        return activeID.get(index).getDescription();
    }

    /*@Override
    public String toString() {
        String s = "This are the active PrivateOC (from player 1):\n";
        for(PrivateOC p:activeID)
            s = s + p.toString() + "\n";
        return s;
    }*/

}
