package it.polimi.ingsw.controller;

import com.sun.org.apache.xerces.internal.xs.StringList;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Table;

import java.util.*;

public class Controller {

/* This class is intended for alpha testing of the game mechanics, not for single-method testing.
    NOTE:
 */

    public static Table startGame(List<String> nicknames, Temp_Lobby lobby){

        try {

            lobby.broadcast("Creating table ... ");
            Table table = new Table(nicknames.size());
            lobby.broadcast("OK");
            lobby.broadcast("This are the players: ");
            for(String player:nicknames){lobby.broadcast(player);}
            lobby.broadcast("Setting players ...");
            table.setPlayers(nicknames);
            lobby.broadcast("OK");
            //TODO: Implementation of prep phase
            lobby.broadcast("Game is ready");

            lobby.broadcast("Printing reserve...");
            for (int i=0; i<nicknames.size()*2 + 1; i++){lobby.broadcast(table.checkDiceFromReserve(i));}
            //TODO: print status of game

            return table;

        }catch (NullPointerException n){
            System.out.println("Null Pointer exception");
            n.printStackTrace();
            return null;
        }
        catch (Exception e){
            System.out.println("Exception caught");
            e.printStackTrace();
            return null;
        }

    }
}
