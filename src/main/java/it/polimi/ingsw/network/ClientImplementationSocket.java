package it.polimi.ingsw.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

class ClientImplementationSocket {

    private Socket socket;
    ClientImplementationSocket (Socket socket){
        this.socket = socket;
    }

    void login() throws IOException {
        boolean success = false;
        int playersInLobby;
        Scanner scanner = new Scanner(System.in);
        String string;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter out = new PrintWriter(this.socket.getOutputStream());
            do {
                playersInLobby = Integer.parseInt(in.readLine());
                System.out.println("[Players in the lobby: " + playersInLobby + "]\nLogin:\t\t(to refresh the page type '*')");
                string = scanner.nextLine();
                out.println(string);
                out.flush();
                if (!string.equals("*")){
                    String result = in.readLine();
                    if (result.equals("true")){
                        System.out.println("Welcome " +string+ ".\nYou have connected successfully.");
                        success = true;
                    }
                    else{
                        if (result.equals("same"))
                            System.out.println("Login failed, this userID is already used");
                        else
                            System.out.println("Retry later...");
                    }
                }
            }while (!success);
        }catch (NoSuchElementException e){
            System.err.println("ERROR "+e.getMessage());
        }
    }

    void logout() throws IOException{
        String result;
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        result = in.readLine();
        if (result.equals("ok")){
            System.out.println("You have disconnected successfully");
        }else
            System.out.println("Error in your disconnection!!");
    }
}
