package it.polimi.ingsw.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerImplementationSocket implements Runnable {
    private Socket socket;
    ServerImplementationSocket(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            // leggo e scrivo nella connessione finche' non ricevo "quit"
            boolean on = true;
            while (on) {
                String line = in.nextLine();
                if (line.equals("quit")) {
                    on = false;
                } else {
                    out.println("Received: " + line);
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
