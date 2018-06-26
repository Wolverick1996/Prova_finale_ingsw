package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.network_server.ServerIntRMI;

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

    private ServerIntRMI server;

    public static final int MAX_PLAYERS = 4;
    private static final int ONE_SEC = 1000;
    private int delay = 20000;
    private List<String> players = new ArrayList<>();
    private List<Socket> sockets = new ArrayList<>();
    private Boolean streetlight;
    private Boolean hasStarted = false;
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
        hasStarted = true;
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

            if (this.players.size() >= 2) {
                if (this.timer != null)
                    this.timer.cancel();

                this.timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            List<String> check = players;
                            server.confirmConnections();
                            canIGo();
                            streetlight = true;
                            if (players.size() >= 2 && check.equals(players))
                                startGame();

                        } catch (RemoteException re){
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
     * Adds a socket to sockets array to be passed to the controller
     *
     * @param s: the socket to be added
     * @author Matteo
     */
    public synchronized void addSocket(Socket s){
        sockets.add(s);
    }

    /**
     * Removes the player from the lobby
     *
     * @param username: username of the player to be removed
     * @return true if the player can be removed to the lobby, otherwise false
     * @author Matteo
     */
    public boolean removePlayer(String username){
        canIGo();
        for (String s : this.players){
            if (s.equals(username)){
                this.players.remove(s);
                if (this.players.size() == 1){
                        this.timer.cancel();
                }
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
        while (!this.streetlight){
            assert true;
        }
        this.streetlight = false;
    }

    /**
     * Sets the RMI stub
     *
     * @param server: the server to be set up
     * @author Andrea
     */
    public void setServerRMI(ServerIntRMI server){
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
    public boolean hasStarted(){ return hasStarted; }

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