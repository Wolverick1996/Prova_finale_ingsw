package it.polimi.ingsw.client.view;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import static it.polimi.ingsw.client.view.IOHandlerClient.Interface.*;

public class SocketMessengerClient{

    private String username;
    private Socket socket;
    private Scanner scanner = new Scanner(System.in);
    private PrintWriter out;
    private BufferedReader in;
    private IOHandlerClient handler;

    //DICTIONARY:
    private static final String FAILED = "";
    private static final String D_LEFT = "<";
    private static final String D_RIGHT = ">";
    private static final String GAMESTART = "gameStarts";
    private static final String NAME = "name";
    private static final String OK = "ok";

    public SocketMessengerClient(Socket s, String n) {
        try{
            this.username = n;
            this.handler = new IOHandlerClient(username, cli);
            this.socket = s;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream());
            waitStart();
        } catch (IOException e) {
            handler.send("Server is down, I repeat, server is down!");
            handler.send(e.getMessage());
        }
    }

    private void waitStart() throws IOException{

        String request = "";
        //this method waits until the server gives a "GAMESTART" signal
        do {
            request = this.in.readLine();
        } while (!request.equals(GAMESTART));
        this.handler.send(request);
        this.handler.send("Trying to send: " + NAME + D_LEFT + this.username + D_RIGHT);
        this.out.println(NAME + D_LEFT + this.username + D_RIGHT);
        this.out.flush();

        this.handler.send("MUEVE LA COLITTA");
        do {
            request = this.in.readLine();
        } while (!request.equals(OK));

        this.handler.send("PORCODDDIO SI SBOCCIA DON PERO");
        game();
    }

    private void game() throws IOException{
        this.scanner.nextLine();
    }

    private void unexpectedMessageFromServer() throws IOException{
        //TODO: make this method more clear
        throw new IOException();
    }

    public void close() {
        if(this.handler!=null)
        this.handler.send("Bye bye <3");
    }
}
