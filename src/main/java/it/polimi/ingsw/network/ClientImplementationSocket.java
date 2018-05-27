package it.polimi.ingsw.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ClientImplementationSocket {

    private Socket socket;
    private String username;

    ClientImplementationSocket (Socket socket){
        this.socket = socket;
    }

    void login (){
        boolean success = false;
        int playersInLobby;
        Scanner scanner = new Scanner(System.in);
        String string;
        try {
            Scanner in = new Scanner(this.socket.getInputStream());
            PrintWriter out = new PrintWriter(this.socket.getOutputStream());
            do {
                playersInLobby = Integer.parseInt(in.nextLine());
                System.out.println("[Players in the lobby: " + playersInLobby + "]\nLogin:\t\t(to refresh the page type '*')");
                string = scanner.nextLine();
                if (!string.equals("*")){
                    out.println(string);
                    out.flush();
                    String result = in.nextLine();
                    if (result.equals("true")){
                        System.out.println("Welcome " +string+ ".\nYou have connected successfully.");
                        this.username = string;
                        success = true;
                    }
                    else{
                        if (result.equals("same"))
                            System.out.println("Login failed, this userID is already used");
                        else
                            System.out.println("Retry later...");
                    }
                }
                else {
                    out.println("*");
                    out.flush();
                }
            }while (!success);
            in.close();
            out.close();
            scanner.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    void logout(){
        String result;
        try {
            Scanner in = new Scanner(this.socket.getInputStream());
            PrintWriter out = new PrintWriter(this.socket.getOutputStream());

            out.println(this.username);
            result = in.nextLine();
            if (result.equals("ok")){
                System.out.println("You have disconnected successfully");
                this.username = null;
            }else
                System.out.println("Error in your disconnection!!");
            in.close();
            out.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
