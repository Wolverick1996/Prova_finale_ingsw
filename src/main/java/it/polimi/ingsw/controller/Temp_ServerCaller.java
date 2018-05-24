package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Table;

import java.util.*;

public class Temp_ServerCaller {

    public static void main(String[] args) {
        List<String> nn = new ArrayList<String>();

        nn.add("Duco");
        nn.add("Davide");
        nn.add("Valentina");

        Lobby lobby = new Lobby();

        Table table = Controller.startGame(nn, lobby);
    }
}
