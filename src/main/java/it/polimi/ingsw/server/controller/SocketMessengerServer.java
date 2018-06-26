package it.polimi.ingsw.server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class SocketMessengerServer {
    //SocketMessengerServer is intended for single-game Servers. Update this method to enable multi-games on same server

    //DICTIONARY
    private static final String FAILED = "";
    private static final String D_LEFT = "<";
    private static final String D_RIGHT = ">";
    private static final String GAMESTART = "gameStarts";
    private static final String NAME = "name";
    private static final String OK = "ok";
    private static final String FINISH = "finish";
    private static final String PRINT = "print";
    private static final String REQUEST = "requestData";

    static synchronized String getToKnow(Socket socket) throws IOException{
        String name = "";
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(GAMESTART);
            out.flush();

            name = in.readLine();

            String[] parts = name.split(D_LEFT);
            if (!parts[0].equals(NAME)){
                return FAILED;
            }
            parts = parts[1].split(D_RIGHT);
            name = parts[0];
            out.println(OK);
            out.flush();
            System.out.println(name + " was registered and is ready to play. He is " + socket);
        } catch (IOException e) {
            return FAILED;
        }
        return name;
    }

    static synchronized void write(Socket socket, String message) throws IOException{
        message = cleanString(message);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        String output = PRINT + D_LEFT + message + D_RIGHT;
        System.out.println("I'm sending this message: " + output + " to " + socket);
        out.println(output);
        out.flush();
        askIfReceived(socket);
    }

    static synchronized String get(Socket socket) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        String response;

        out.println(REQUEST);
        out.flush();

        response = in.readLine();

        out.println(OK);
        out.flush();

        return response;
    }

    private static synchronized void askIfReceived(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String request;
        System.out.println("I'm waiting for a response from " + socket);
        request = in.readLine();
        System.out.println("I got an answer from " + socket);
        if (request.equals(OK)){
            return;
        } else {
            throw new IOException();
        }
    }

    private static synchronized String cleanString(String s){
        return s.replaceAll("\\n", "%%%nnn%%%");
    }

}
