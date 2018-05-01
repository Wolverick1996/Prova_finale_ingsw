package it.polimi.ingsw;

public class Utility {

    // This class contains Utility methods. All the methods here must be STATIC.
    public static int[] returnRandomInts(int tot, int min, int max){

        if (tot == 0 || max<=min || max - min + 1 < tot){
            return null;
        }

        int intArray[] = new int[tot];

        for (int i=0; i<tot; i++){
            intArray[i] = 0;
        }

        return intArray;
    }
}
