package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Table;
import it.polimi.ingsw.view.IOhandler;
import it.polimi.ingsw.view.Temp_View;

import java.util.*;

public class Controller {

    private static List<Lobby> lobbies = new ArrayList<>();
    private static List<Game> games = new ArrayList<>();
    private static List<IOhandler> speakers = new ArrayList<>();
/* These attributes only serve as contrller's addresses
 */

    public static Table startGame(List<String> nicknames, Lobby lobby){

        try {

            lobbies.add(lobby);
            int i = lobbies.size() - 1;
            String s = "";
            Table table = new Table(nicknames.size());
            table.setPlayers(nicknames);
            speakers.add(new IOhandler(table.getActivePlayers()));
            speakers.get(i).broadcast("OK");
            speakers.get(i).broadcast("These are the players: ");
            for(String player:nicknames){speakers.get(i).broadcast(player);}

            //TODO: Implementation of prep phase
            games.add(new Game(table.getActivePlayers()));

            speakers.get(i).broadcast("Game is ready");

            speakers.get(i).broadcast("Printing reserve...");
            for (int k=0; k<nicknames.size()*2 + 1; k++){
                s += table.checkDiceFromReserve(k) + "\t";
            }
            speakers.get(i).broadcast(s);
            //TODO: print status of game
            speakers.get(i).broadcast("This is the lobby: \n" + lobby);
            speakers.get(i).broadcast(table);

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
