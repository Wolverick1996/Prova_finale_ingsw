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
            // leggo e scrivo nella connessione finche' non ricevo "quit"
            boolean on = true;
            boolean ok = false;
            while (on) {
                String line = in.nextLine();
                if (line.equals("andrea")) {
                    out.println(ok);
                    out.flush();
                    //on = false;
                } else {
                    out.println(!ok);
                    out.flush();
                }
            }
            // chiudo gli stream e il socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
