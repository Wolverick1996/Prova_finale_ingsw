package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Table;
import it.polimi.ingsw.server.network_server.ServerIntRMI;

import java.util.*;

public class Controller {

    private static List<Lobby> lobbies = new ArrayList<>();
    private static List<Game> games = new ArrayList<>();
    private static List<IOhandler> speakers = new ArrayList<>();
    /* These attributes only serve as controller's addresses */

    public static void startGame(List<String> nicknames, Lobby lobby, ServerIntRMI server){

        try {
            lobbies.add(lobby);
            int i = lobbies.size() - 1;
            String s = "";
            Table table = new Table(nicknames.size());
            table.setPlayers(nicknames);
            speakers.add(new IOhandler(table.getActivePlayers(), table)); //TODO: add on Table extends observable
            speakers.get(i).setServer(server);
            speakers.get(i).broadcast("\nOK\nGame is ready!");

            //TODO: Implementation of prep phase
            games.add(new Game(table.getActivePlayers(), table));

            speakers.get(i).broadcast(s);
            //TODO: print status of game
            speakers.get(i).broadcast("\nThis is the lobby: \n" + lobby);
            speakers.get(i).broadcast(table);
        } catch (NullPointerException n){
            System.err.println("Null Pointer exception");
            n.printStackTrace();
        } catch (Exception e){
            System.err.println("Exception caught");
            e.printStackTrace();
        }

    }

    public static void switchContext(Lobby lobby){
        try {
            games.get(lobbies.indexOf(lobby)).begin();
        } catch (NullPointerException n){
            System.err.println("Null Pointer exception");
            n.printStackTrace();
        } catch (Exception e){
            System.err.println("Exception caught");
            e.printStackTrace();
        }

    }

    public static IOhandler getMyIO (Object caller){
        if (caller.getClass() == Lobby.class){
            return speakers.get(lobbies.indexOf(caller));
        } else if (caller.getClass() == Game.class){
            return speakers.get(games.indexOf(caller));
        } else {
            return null;
        }
    }

    public static Game getMyGame (Object caller){
        if (caller.getClass() == Lobby.class){
            return games.get(lobbies.indexOf(caller));
        } else if (caller.getClass() == IOhandler.class){
            return games.get(speakers.indexOf(caller));
        } else {
            return null;
        }
    }

}