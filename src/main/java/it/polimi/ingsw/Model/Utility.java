package it.polimi.ingsw.Model;

import java.util.*;

public class Utility {

    // This class contains Utility methods. All the methods here must be STATIC.

    //Return an array with tot ints WITHOUT REPETITIONS
    public static int[] returnRandomInts(int tot, int min, int max){

        Random rand = new Random();
        int temp = rand.nextInt(max - min +1) + min;

        if (tot == 0 || max<=min || max - min + 1 < tot){
            return null;
        }

        int intArray[] = new int[tot];

        for (int i=0; i<tot; i++){
            intArray[i] = -1;
            for (int j=0; ;j++){
                if (intArray[j]==-1){
                    intArray[j] = temp;
                    break;
                } else if (intArray[j] == temp){
                    temp = rand.nextInt(max - min +1) + min;
                    j = -1;
                }
            }
        }

        return intArray;
    }
}
