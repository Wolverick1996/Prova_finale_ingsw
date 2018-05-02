package it.polimi.ingsw;

import java.lang.*;

public class PubObjHandler {

    //***************************//
    //        Attributes         //
    //***************************//

    private static int[] activeID = new int[3];
    public static int numPubOC = 0; //Will have a function that extract number of Cards

    //***************************//
    //         Methods           //
    //***************************//

    //Set the PubOC to be handled in the right position
    public static void setPubOC(){
        try {
            activeID = Utility.returnRandomInts(3,0,numPubOC);
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

    @Override
    public String toString(){
        return "String";
    }

}
