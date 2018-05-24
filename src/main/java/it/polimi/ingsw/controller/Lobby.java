package it.polimi.ingsw.controller;

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

    public List<String> getPlayers() {
        return players;
    }

    public void broadcast(Object message){
        System.out.println(message);
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < players.size(); i++){
            string += "Player " +i+ ":\t" +players.get(i);
        }
        return string;
    }
}
