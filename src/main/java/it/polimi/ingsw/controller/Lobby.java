package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.ServerIntRMI;

import java.rmi.RemoteException;
import java.util.*;

public class Lobby /*extends Observer*/ {

    //***************************//
    //        Attributes         //
    //***************************//

    private ServerIntRMI server;

    public static final int MAX_PLAYERS = 4;
    private int delay = 20000;
    private List<String> players = new ArrayList<>();
    private Boolean streetlight;
    private Timer timer = new Timer();
    private boolean isGoing = false;
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            try {
                server.confirmConnections();
                startGame();
            } catch (RemoteException re){
                System.err.println("ERROR" + re.getMessage());
            }

        }
    };

    public Lobby(){
        streetlight = true;
    }
    //***************************//
    //         Methods           //
    //***************************//

    //TODO: define this method (is now equal to controller's startGame)
    public void startGame(){
        Controller.startGame(players, this);
        Controller.getMyIO(this).broadcast("\n\n\tSwitching from lobby to Game ... \n\n");
        Controller.switchContext(this);
    }

    public boolean addPlayer(String username){
        while(!canIGo()){
            //TODO: add feedback for user
        }

        if (this.players.size()<= MAX_PLAYERS){

            for (String s : this.players){
                if (s.equals(username)){
                    this.streetlight = true;
                    return false;
                }
            }

            this.players.add(username);

            if (this.players.size() == 2){
                this.timer.schedule(this.task, delay);
            }

            this.streetlight = true;
            return true;
        }
        this.streetlight = true;
        return false;
    }

    public boolean removePlayer(String username){
        while(!canIGo()){
            //TODO: add feedback for user
        }
        for (String s : this.players){
            if (s.equals(username)){
                this.players.remove(s);
                if (this.players.size() < 2){
                    if(this.isGoing){
                        this.timer.cancel();
                        this.isGoing = false;
                    }
                }
                this.streetlight = true;
                return true;
            }
        }
        this.streetlight = true;
        return false;
    }

    private boolean canIGo(){
        while (this.streetlight==false){
            //TODO: add feedback for user
        }
        this.streetlight = false;
        return true;
    }

    public void setServerRMI(ServerIntRMI server){
        this.server = server;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public List<String> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 1; i <= players.size(); i++){
            string += "Player " +i+ ":\t" +players.get(i - 1) + "\n";
        }
        return string;
    }
}
