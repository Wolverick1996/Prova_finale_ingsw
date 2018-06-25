package it.polimi.ingsw.server.network_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketMessengerServer {
    //SocketMessengerServer is intended for single-game Servers. Update this method to enable multi-games on same server

    //DICTIONARY
    private static final String FAILED = "";
    private static final String D_LEFT = "<";
    private static final String D_RIGHT = ">";
    private static final String GAMESTART = "gameStarts";
    private static final String NAME = "name";
    private static final String OK = "ok";

    public synchronized static String getToKnow(Socket socket){
        String name = "";
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            System.out.println("cazzo");
            out.println(GAMESTART);
            out.flush();

            name = in.readLine();
            System.out.println(name);

            String[] parts = name.split(D_LEFT);
            if (!parts[0].equals(NAME)){
                return FAILED;
            }
            parts = parts[1].split(D_RIGHT);
            name = parts[0];
            out.println(OK);
            out.flush();
        } catch (IOException e) {
            return FAILED;
        }
        System.out.println(name);
        return name;
    }


}
