package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Lobby;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerImplementationSocket implements Runnable {
    private Socket socket;
    private Lobby lobby;
    ServerImplementationSocket(Socket socket, Lobby lobby) {
        this.socket = socket;
        this.lobby = lobby;
    }
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            String string;
            boolean gameCanStart = false;
            do{
                login();
                gameCanStart = true;
                while (gameCanStart){
                    string = in.nextLine();
                    if (string.equals("e")){
                        logout();
                        gameCanStart = false;
                    }
                    out.println(this.lobby.getPlayers().size());
                    out.flush();
                }
            }while (!gameCanStart);

            while (!gameCanStart){
                Scanner s = new Scanner(System.in);
                String t = s.nextLine();
            }
            in.close();
            out.close();
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    private void login() throws IOException{
        Scanner in = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        boolean loginSuccess = false;
        while (!loginSuccess) {
            out.println(this.lobby.getPlayers().size());
            out.flush();
            String string = in.nextLine();
            if (!string.equals("*")){
                if (this.lobby.addPlayer(string)){
                    loginSuccess = true;
                    System.out.println("[Socket Server]\t" +string+ "  got connected....");
                    out.println(loginSuccess);
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
        // chiudo gli stream
        in.close();
        out.close();
    }

    private void logout(){
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            String playerDisconnected;
            playerDisconnected = in.nextLine();
            if(this.lobby.removePlayer(playerDisconnected)){
                out.println("ok");
                out.flush();
                System.out.println("[RMI Server]\t" +playerDisconnected+ "  disconnected....");
            }
            else{
                out.println("ko");
                out.flush();
            }
            in.close();
            out.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
