package it.polimi.ingsw.controller;

import com.sun.org.apache.xerces.internal.xs.StringList;
import it.polimi.ingsw.model.Table;

import java.util.*;

public class Temp_ServerCaller {

    public static void main(String[] args) {
        List<String> nn = new ArrayList<String>();

        nn.add("Duco");
        nn.add("Davide");
        nn.add("Valentina");

        Temp_Lobby lobby = new Temp_Lobby();

        Table table = Controller.startGame(nn, lobby);
    }
}
