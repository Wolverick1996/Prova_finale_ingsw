package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.network_server.ServerImplementationRMI;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Lobby is the class in which are saved players that will play the current match
 *
 * @author Matteo
 */
public class Lobby /*extends Observer*/ {

    //***************************//
    //        Attributes         //
    //***************************//

    private ServerImplementationRMI server;

    public static final int MAX_PLAYERS = 4;
    private static final int ONE_SEC = 1000;
    private int delay = 20000;
    private List<String> players = new ArrayList<>();
    private List<Socket> sockets = new ArrayList<>();
    private boolean streetlight;
    private boolean hasStarted = false;
    private Timer timer;

    /**
     * Constructor of the Lobby class
     *
     * @author Matteo
     */
    public Lobby(){
        streetlight = true;
    }

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Communicates to the controller that timer expired and the match should starts
     *
     * @author Matteo
     */
    void startGame(){
        this.hasStarted = true;
        Controller.startGame(players, this, this.server, sockets);
        Controller.getMyIO(this).broadcast("\n\tSwitching from lobby to Game ... \n\n");
        Controller.switchContext(this);
    }

    /**
     * Adds the player to the lobby
     * After each player except the first joins the lobby, a timer starts
     *
     * @param username: username of the player to be added
     * @return true if the player can be added to the lobby, otherwise false
     * @author Matteo
     */
    public synchronized boolean addPlayer(String username){
        canIGo();

        if (this.players.size()<= MAX_PLAYERS){
            for (String s : this.players){
                if (s.equals(username)){
                    this.streetlight = true;
                    return false;
                }
            }

            this.players.add(username);

            if (this.players.size() >= 2){
                if (this.timer != null)
                    this.timer.cancel();

                this.timer = new Timer();
                TimerTask task = new TimerTask(){

                    @Override
                    public void run(){
                        try {
                            List<String> check = players;
                            server.confirmConnections();
                            canIGo();
                            streetlight = true;
                            if (players.size() >= 2 && check.equals(players))
                                startGame();
                        } catch (RemoteException re) {
                            System.err.println("ERROR" + re.getMessage());
                        }
                    }

                };
                this.timer.schedule(task, delay);
            }

            this.streetlight = true;
            return true;
        }

        this.streetlight = true;
        return false;
    }

    /**
     * Checks if a player is re-connectible
     *
     * @param username: player's username
     * @return true if player is re-connectible, otherwise false
     * @author Andrea
     */
    public boolean willingToReconnectPlayer(String username){
        canIGo();
        if (this.hasStarted){
            this.streetlight = true;
            return checkReConnection(username);
        }
        return false;
    }

    /**
     * Checks if there is a disconnected player in the current game, if the answer is positive he joins the match
     *
     * @param username: the username to be checked
     * @return true if there is a disconnected player in the game, otherwise false
     * @author Andrea
     */
    private boolean checkReConnection(String username){
        boolean wasConnected = false;
        for (String p : players){
            if (p.equals(username))
                wasConnected = true;
        }
        return wasConnected && Controller.getMyGame(this).getPlayer(username).isDisconnected();
    }

    /**
     * Allows user to re-join current match (calls IOhandler's rejoinMatch method)
     *
     * @param username: username to be re-activate
     * @param o: useful to allow socket connection, useless if RMI
     * @author Andrea
     */
    public void rejoinMatch(String username, Object o){
        Controller.getMyGame(this).getPlayer(username).setDisconnected(false);
        Controller.getMyIO(this).rejoinMatch(username, o);
    }

    /**
     * Adds a socket to sockets array to be passed to the controller
     *
     * @param s: the socket to be added
     * @author Matteo
     */
    public synchronized void addSocket(Socket s){
        sockets.add(s);
    }

    /**
     * Removes the player from the lobby or setDisconnected a player during the match
     *
     * @param username: username of the player to be removed
     * @return true if the player can be removed to the lobby, otherwise false
     * @author Andrea
     */
    public boolean removePlayer(String username){
        canIGo();
        if (hasStarted){
            Controller.getMyGame(this).getPlayer(username).setDisconnected(true);
            this.streetlight = true;
            return true;
        }
        for (String s : this.players){
            if (s.equals(username)){
                this.players.remove(s);
                if (this.players.size() == 1)
                        this.timer.cancel();

                this.streetlight = true;
                return true;
            }
        }
        this.streetlight = true;
        return false;
    }

    /**
     * Used as a lock (to be sure resources are not accessed at the same time)
     *
     * @author Matteo
     */
    private void canIGo(){
        while (!this.streetlight)
            assert true;

        this.streetlight = false;
    }

    /**
     * Sets the RMI stub
     *
     * @param server: the server to be set up
     * @author Andrea
     */
    public void setServerRMI(ServerImplementationRMI server){
        this.server = server;
    }

    /**
     * Sets a delay for the lobby timer
     *
     * @param delay: value (seconds) that will be timer's delay
     * @author Andrea
     */
    public void setDelay(int delay){
        this.delay = delay*ONE_SEC;
    }

    /**
     * Returns the list of players usernames
     *
     * @return the list of players usernames
     * @author Matteo
     */
    public List<String> getPlayers(){
        return players;
    }

    /**
     * Returns hasStarted boolean flag, which is set up true when game starts
     *
     * @return true if game already started, otherwise false
     * @author Matteo
     */
    public boolean hasStarted(){
        return hasStarted;
    }

    /**
     * Used to print active players in the lobby
     *
     * @return the string that represents the list of players
     * @author Matteo
     */
    @Override
    public String toString(){
        String string = "";
        for (int i = 1; i <= players.size(); i++){
            string += "Player " +i+ ":\t" +players.get(i - 1) + "\n";
        }
        return string;
    }

}