package it.polimi.ingsw;

import java.lang.*;

public class ToolHandler {

    //***************************//
    //        Attributes         //
    //***************************//

    private static int activeID[] = new int[3];
    public static int numTools = 0; //Will have a function that extract number of Cards

    //***************************//
    //         Methods           //
    //***************************//

    //Set the toolcards to be handled in the right position
    public static void setTools(){
        try {
            activeID = Utility.returnRandomInts(3,0,numTools);
        }catch (Exception e){
            //Unhandled exception
            System.out.println("Failed setTools");
            System.exit(-1);
        }
    }

    //Call the method or the right tool
    public static void useTool(int num){

    }

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
