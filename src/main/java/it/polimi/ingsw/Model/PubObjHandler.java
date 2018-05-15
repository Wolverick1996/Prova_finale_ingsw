package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.PubOC.*;

import java.lang.*;
import java.util.ArrayList;

public class PubObjHandler {

    //***************************//
    //        Attributes         //
    //***************************//

    private static ArrayList<Object> activeID = new ArrayList<>();
    private static final int numPubOC = 10;

    //***************************//
    //         Methods           //
    //***************************//

    //Set the PubOC to be handled in the right position
    public static void setPubOC(){
        try {
            for (int i:  Utility.returnRandomInts(3,0,numPubOC)){
                switch (i){
                    case 1: activeID.add(new PublicOC1()); break;
                    case 2: activeID.add(new PublicOC2()); break;
                    case 3: activeID.add(new PublicOC3()); break;
                    case 4: activeID.add(new PublicOC4()); break;
                    case 5: activeID.add(new PublicOC5()); break;
                    case 6: activeID.add(new PublicOC6()); break;
                    case 7: activeID.add(new PublicOC7()); break;
                    case 8: activeID.add(new PublicOC8()); break;
                    case 9: activeID.add(new PublicOC9()); break;
                    case 10: activeID.add(new PublicOC10()); break;
                    default: throw new Exception();
                }
            }
        }catch (Exception e){
            //Unhandled Exception
            System.out.println("Failed setPubOC");
            System.exit(-1);
        }
    }

    //Call the method or the right PubOC for counting points
    public static void countPoints(int num){
    }

    //Get the name (on table)
    public static String getName(int num){
        return null;
    }

    //Get the description (on table)
    public static String getDescription(int num){
        return null;
    }

    /*@Override
    public String toString() {
        String s = "This are the active PrivateOC (from player 1):\n";
        for(PrivateOC p:activeID)
            s = s + p.toString() + "\n";
        return s;
    }*/

}
