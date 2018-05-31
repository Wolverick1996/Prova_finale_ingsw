package it.polimi.ingsw.controller;

import java.util.*;

public class Temp_ServerCaller {

    public static void main(String[] args) {
        Lobby lobby = new Lobby();
        boolean startgame = false;
        int i = 0;
        while (!startgame){
            System.out.println("Type name of the new player or '*' to start.");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            if(s.equals("*")){
                if (i == 0 || i == 1)
                    System.out.println("Add more players! (Min 2)");
                else
                    startgame = true;
            }
            else{
                lobby.addPlayer(s);
                i++;
                if (i == 4)
                    startgame = true;
            }
        }
        lobby.startGame();
    }
}
