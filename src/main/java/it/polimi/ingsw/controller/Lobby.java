package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Table;
import it.polimi.ingsw.view.IOhandler;
import it.polimi.ingsw.view.Temp_View;

import java.util.*;

public class Lobby /*extends Observer*/ {

    //***************************//
    //        Attributes         //
    //***************************//


    public static final int MAX_PLAYERS = 4;
    List<String> players = new ArrayList<>();
    Boolean streetlight;

    public Lobby(){
        streetlight = true;
    }
    //***************************//
    //         Methods           //
    //***************************//

    //TODO: define this method (is now equal to controller's startGame)
    public void startGame(){
        Table table = Controller.startGame(players, this);
        Controller.getMyIO(this).broadcast("\n\n\tSwitching from lobby to Game ... \n\n");
        Controller.switchContext(this);
    }

    public boolean addPlayer(String username){
        while(!canIGo()){
            //TODO: add feedback for user
        }
        if (this.players.size()<= MAX_PLAYERS){

            for (String s : this.players)
                if (s.equals(username)){
                this.streetlight = true;
                    return false;
                }

            this.players.add(username);
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
