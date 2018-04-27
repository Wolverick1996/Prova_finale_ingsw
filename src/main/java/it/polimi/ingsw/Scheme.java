package it.polimi.ingsw;

public class Scheme {

    //***************************//
    //        Attributes         //
    //***************************//

    private String name;
    private int difficulty;
    private Box[][] grid;

    //constructor, takes the ID and call a HandlerMethod in Controller
    public Scheme(int id){

    }

    //***************************//
    //         Methods           //
    //***************************//

    //Check what dice is in a box
    public Dice checkBox(int x, int y){
        return null;
    }

    //Place a dice in a box
    public boolean placeDice(Dice dice, int x, int y){
        return false;
    }

    //Get name of the scheme
    public String getName(){
        return null;
    }

    //Get the difficulty
    public int getDifficulty(){
        return -1;
    }
}
