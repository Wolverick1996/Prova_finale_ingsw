package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.network_server.ServerIntRMI;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

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

    public Lobby(){
        streetlight = true;
    }
    //***************************//
    //         Methods           //
    //***************************//

    public void startGame(){
        hasStarted = true;
        Controller.startGame(players, this, this.server, sockets);
        Controller.getMyIO(this).broadcast("\n\tSwitching from lobby to Game ... \n\n");
        Controller.switchContext(this);
    }

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

    public synchronized void addSocket(Socket s){
        sockets.add(s);
    }

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

    private void canIGo(){
        while (!this.streetlight){
            assert true;
        }
        this.streetlight = false;
    }

    public void setServerRMI(ServerIntRMI server){
        this.server = server;
    }

    public void setDelay(int delay) {
        this.delay = delay*ONE_SEC;
    }

    public List<String> getPlayers() {
        return players;
    }

    public boolean hasStarted(){ return hasStarted; }

    @Override
    public String toString() {
        String string = "";
        for (int i = 1; i <= players.size(); i++){
            string += "Player " +i+ ":\t" +players.get(i - 1) + "\n";
        }
        return string;
    }
}