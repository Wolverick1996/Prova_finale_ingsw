package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Table;

import java.util.*;

public class Temp_ServerCaller {

    public static void main(String[] args) {
        Lobby lobby = new Lobby();
        lobby.addPlayer("Duco");
        lobby.addPlayer("Crive");
        lobby.addPlayer("Ricky");
        lobby.startGame();
    }
}
