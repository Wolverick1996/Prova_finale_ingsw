package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Table;

import java.util.ArrayList;

public class Controller {

/* This class is intended for alpha testing of the game mechanics, not for single-method testing.
    NOTE:
 */

    public boolean startGame(ArrayList<Player> players){

        try {

            System.out.println("Creating players ... ");
            Table table = new Table(players.size());
            System.out.println("OK");

        }catch (NullPointerException n){
            System.out.println("Null Pointer exception");
            n.printStackTrace();
            return false;
        }
        catch (Exception e){
            System.out.println("Exception caught");
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
