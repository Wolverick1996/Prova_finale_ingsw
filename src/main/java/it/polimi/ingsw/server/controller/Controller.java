package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Table;
import it.polimi.ingsw.server.network_server.ServerImplementationRMI;

import java.net.Socket;
import java.util.*;

/**
 * Manages other controller classes and allows them to communicate
 *
 * @author Matteo
 */
public class Controller {

    private static List<Lobby> lobbies = new ArrayList<>();
    private static List<Game> games = new ArrayList<>();
    private static List<IOhandler> speakers = new ArrayList<>();
    /* These attributes only serve as controller's addresses */

    /**
     * Private constructor
     * @author Andrea
     */
    private Controller(){
        super();
    }

    /**
     * Initializes Game and IOhandler at the request of Lobby and combines classes
     *
     * @param nicknames: list of players nicknames
     * @param lobby: Lobby object which called startGame method and acts as index for the classes combination
     * @param server: reference to RMI server containing all users connected with RMI
     * @param sockets: list of users connected with socket
     * @author Matteo
     */
    static void startGame(List<String> nicknames, Lobby lobby, ServerImplementationRMI server, List<Socket> sockets){

        try {
            lobbies.add(lobby);
            int i = lobbies.size() - 1;
            String s = "";
            Table table = new Table(nicknames.size());
            table.setPlayers(nicknames);
            speakers.add(new IOhandler(table.getActivePlayers(), table));
            speakers.get(i).setServer(server);
            speakers.get(i).setSockets(sockets);
            speakers.get(i).broadcast("\nOK\nGame is ready!");

            games.add(new Game(table.getActivePlayers(), table));

            speakers.get(i).broadcast(s);

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

    /**
     * Allows all clients to communicate with Game class combined with the Lobby one: game starts
     *
     * @param lobby: Lobby object which called switchContext method and acts as index for the classes combination
     * @author Matteo
     */
    static void switchContext(Lobby lobby){
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

    /**
     * Returns the right IOhandler to the caller
     *
     * @param caller: method caller
     * @return the IOhandler combined with the caller
     * @author Matteo
     */
    static IOhandler getMyIO (Object caller){
        if (caller.getClass() == Lobby.class){
            return speakers.get(lobbies.indexOf(caller));
        } else if (caller.getClass() == Game.class){
            return speakers.get(games.indexOf(caller));
        } else {
            return null;
        }
    }

    /**
     * Returns the right Game to the caller
     *
     * @param caller: method caller
     * @return the Game combined with the caller
     * @author Matteo
     */
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