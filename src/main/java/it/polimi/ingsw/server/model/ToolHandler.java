package it.polimi.ingsw.server.model;

import java.util.Random;

public class ToolHandler {

    //***************************//
    //        Attributes         //
    //***************************//

    private static int activeID[] = new int[3];
    public static final int numTools = 0; //Will have a function that extract number of cards

    //***************************//
    //         Methods           //
    //***************************//

    //Set the toolcards to be handled in the right position
    public static void setTools(){
        Random rand = new Random ();
        int[] numOnTable = new int[3];

        while (numOnTable[0] == numOnTable[1] || numOnTable[1] == numOnTable[2] || numOnTable[0] == numOnTable[2]){
            numOnTable[0] = rand.nextInt(12)+1;
            numOnTable[1] = rand.nextInt(12)+1;
            numOnTable[2] = rand.nextInt(12)+1;
        }

        try {
            activeID = numOnTable;
        } catch (Exception e){
            //Unhandled exception
            System.out.println("Failed setTools");
            System.exit(-1);
        }
    }

    //Call the method or the right tool
    public static void useTool(int num){ }

    //Get the name of the tool (on table)
    public static String getName(int num){
        return null;
    }

    //Get the description of the tool (on table)
    public static String getDescription(int num){
        return null;
    }

    @Override
    public String toString(){
        return "String";
    }

}