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
    private static final String FINISH = "finish";
    private static final String PRINT = "print";
    private static final String REQUEST = "requestData";

    public SocketMessengerClient(Socket s, String n) {
        try{
            this.username = n;
            this.handler = new IOHandlerClient(this.username, cli);
            this.socket = s;
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream());
            this.waitStart();
        } catch (IOException e) {
            this.handler.send("Server is down, I repeat, server is down!");
            this.handler.send(e.getMessage());
        }
    }

    private void waitStart() throws IOException{
        String request;
        //this method waits until the server gives a "GAMESTART" signal
        do {
            request = this.in.readLine();
        } while (!request.equals(GAMESTART));
        this.handler.send(request);
        this.handler.send("Trying to send: " + NAME + D_LEFT + this.username + D_RIGHT);
        this.out.println(NAME + D_LEFT + this.username + D_RIGHT);
        this.out.flush();
        this.askIfReceived();
        this.game();
    }

    private void game() throws IOException{
        this.handler.send("We're ready to roll! " + this.username + ", let's go! Even if you are playing with " +
                "Socket, I'm with you");
        String input;
        String request;
        do{
            this.handler.send("LISTENING THE SERVER");
            input = this.in.readLine();
            request = this.getRequest(input);
            this.handler.send("HEARD THE SERVER");
            this.handler.send(request + "\n" + input);
            switch (request){
                case PRINT:
                    this.send(input);
                    this.out.println(OK);
                    this.out.flush();
                    break;
                case REQUEST:
                    this.out.println(this.get());
                    this.out.flush();
                    this.askIfReceived();
                    break;
                case FINISH:
                    break;
                default:
                    this.unexpectedMessageFromServer();
                    break;
            }

        }while (!request.equals(FINISH));

        //CountPoints here

        this.close();
    }

    private String getRequest (String input){
        String[] splittedInput = input.split(D_LEFT);
        return splittedInput[0];
    }

    private String getFirstParameter (String input){
        try{
            String[] splittedInput = input.split(D_LEFT);
            splittedInput = splittedInput[1].split(D_RIGHT);
            return splittedInput[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return"";
        }
    }

    private void unexpectedMessageFromServer() throws IOException{
        this.handler.send("Something went horribly wrong on the Server, I'm sorry :(");
        throw new IOException();
    }

    private void send(String input){
        String message = this.getFirstParameter(input);
        message = message.replaceAll("%%%nnn%%%", "\n");
        this.handler.send(message);
    }

    private String get(){
        String s = "";
        s = this.handler.request();
        return s;
    }

    private void askIfReceived() throws IOException {
        String request;
        this.handler.send("I'm checking if the server got my answer");
        request = this.in.readLine();
        this.handler.send("I read " + request);
        if (request.equals(OK)){
            this.handler.send("He got it!");
            return;
        } else {
            this.unexpectedMessageFromServer();
        }
    }

    public void close() {
        if(this.handler!=null)
        this.handler.send("Bye bye <3");
    }
}
