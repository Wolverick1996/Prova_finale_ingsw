package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Lobby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerImplementationSocket implements Runnable {
    private Socket socket;
    private Lobby lobby;
    private String playerConnected;

    ServerImplementationSocket(Socket socket, Lobby lobby) {
        this.socket = socket;
        this.lobby = lobby;
    }
    public void run() {
        try {
            BufferedReader in;
            PrintWriter out;
            String string;
            boolean gameCanStart;
            do{
                login();
                in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                out = new PrintWriter(this.socket.getOutputStream());
                gameCanStart = true;
                while (gameCanStart){
                    string = in.readLine();
                    if (string.equals("e")){
                        logout();
                        gameCanStart = false;
                    }else{
                        out.println(this.lobby.getPlayers().size());
                        out.flush();
                    }
                }
            }while (!gameCanStart);

            while (!gameCanStart){
                Scanner s = new Scanner(System.in);
                String t = s.nextLine();
            }
            in.close();
            out.close();
            this.socket.close();
        }catch (IOException e){
            System.err.println("Connection lost from a client");
            if (this.playerConnected != null){
                System.out.println("[Socket Server]\t" +this.playerConnected+ "  disconnected....");
                this.lobby.removePlayer(this.playerConnected);
                this.playerConnected = null;
            }
        }
    }

    private void login() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        PrintWriter out = new PrintWriter(this.socket.getOutputStream());
        boolean loginSuccess = false;

        while (!loginSuccess) {
            out.println(this.lobby.getPlayers().size());
            out.flush();
            String string = in.readLine();
            if (!string.equals("*")){
                if (this.lobby.addPlayer(string)){
                    loginSuccess = true;
                    System.out.println("[Socket Server]\t" +string+ "  got connected....");
                    this.playerConnected = string;
                    out.println("true");
                    out.flush();
                }else {
                    boolean sameUsername = false;
                    for (String s : this.lobby.getPlayers())
                        if (s.equals(string))
                            sameUsername = true;
                    if (sameUsername){
                        out.println("same");
                        out.flush();
                    }
                    else {
                        out.println("max");
                        out.flush();
                    }
                }
            }
        }
    }

    private void logout() throws IOException{
        PrintWriter out = new PrintWriter(this.socket.getOutputStream());
        if(this.lobby.removePlayer(this.playerConnected)){
            out.println("ok");
            out.flush();
            System.out.println("[Socket Server]\t" +this.playerConnected+ "  disconnected....");
        }
        else{
            out.println("ko");
            out.flush();
        }
    }
}
