package it.polimi.ingsw.controller;

import java.util.*;

public class Lobby /*extends Observer*/ {

    //***************************//
    //        Attributes         //
    //***************************//

    List<String> players = new ArrayList<>();
    Boolean streetlight;

    public Lobby(){
        streetlight = true;
    }
    //***************************//
    //         Methods           //
    //***************************//

    public boolean addPlayer(String username){
        while(!canIGo()){
            //TODO: add feedback for user
        }
        if (this.players.size()<=4 && !this.players.contains(username)){
            this.players.add(username);
            return true;
        }
        return false;
    }

    public boolean removePlayer(String username){
        while(!canIGo()){
            //TODO: add feedback for user
        }
        if (this.players.contains(username)){
            this.players.remove(username);
            return true;
        }
        return false;
    }

    private boolean canIGo(){
        while (this.streetlight==false){
            //TODO: add feedback for user
        }
        this.streetlight = false;
        return true;
    }

    public void broadcast(Object message){
        System.out.println(message);
    }
}
