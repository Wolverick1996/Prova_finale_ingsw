package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Table;
import it.polimi.ingsw.client.view.IOhandler;

import java.util.*;

public class Controller {

    private static List<Lobby> lobbies = new ArrayList<>();
    private static List<Game> games = new ArrayList<>();
    private static List<IOhandler> speakers = new ArrayList<>();
    private static final String STATUS = "status";
/* These attributes only serve as controller's addresses
 */

    public static void startGame(List<String> nicknames, Lobby lobby){

        try {

            lobbies.add(lobby);
            int i = lobbies.size() - 1;
            String s = "";
            Table table = new Table(nicknames.size());
            table.setPlayers(nicknames);
            speakers.add(new IOhandler(table.getActivePlayers(), table/*, table*/  )); //TODO: add on Table extends observable
            speakers.get(i).broadcast("OK");
            speakers.get(i).broadcast("These are the players: ");
            for(String player:nicknames){speakers.get(i).broadcast(player);}

            //TODO: Implementation of prep phase
            games.add(new Game(table.getActivePlayers(), table));

            speakers.get(i).broadcast("Game is ready");

            speakers.get(i).broadcast("Printing reserve...");
            for (int k=0; k<nicknames.size()*2 + 1; k++){
                s += table.checkDiceFromReserve(k) + "\t";
            }
            speakers.get(i).broadcast(s);
            //TODO: print status of game
            speakers.get(i).broadcast("This is the lobby: \n" + lobby);
            speakers.get(i).broadcast(table);

            return;

        }catch (NullPointerException n){
            System.out.println("Null Pointer exception");
            n.printStackTrace();
            return;
        }
        catch (Exception e){
            System.out.println("Exception caught");
            e.printStackTrace();
            return;
        }

    }

    public static void switchContext(Lobby lobby){
        try {

            games.get(lobbies.indexOf(lobby)).begin();

        }
        catch (NullPointerException n){
            System.out.println("Null Pointer exception");
            n.printStackTrace();
            return;
        }
        catch (Exception e){
            System.out.println("Exception caught");
            e.printStackTrace();
            return;
        }

    }

    public static IOhandler getMyIO (Object caller){
        if(caller.getClass() == Lobby.class){
            return speakers.get(lobbies.indexOf(caller));
        } else if (caller.getClass() == Game.class){
            return speakers.get(games.indexOf(caller));
        } else {
            return null;
        }
    }
}
