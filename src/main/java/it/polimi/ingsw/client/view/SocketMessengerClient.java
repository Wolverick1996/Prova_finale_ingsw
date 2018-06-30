package it.polimi.ingsw.client.view;

import java.io.*;
import java.net.Socket;
import static it.polimi.ingsw.client.view.IOHandlerClient.Interface.*;

public class SocketMessengerClient implements Runnable{

    private String username;
    private PrintWriter out;
    private BufferedReader in;
    private IOHandlerClient handler;
    private Socket socket;
    private String ip;
    private int port;

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
    private static final String NEWLINE = "%%%nnn%%%";

    public SocketMessengerClient(String ip, int port, Socket s, String n, IOHandlerClient.Interface ui) {
        try{
            this.ip = ip;
            this.port = port;
            this.socket = s;
            this.username = n;
            this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.out = new PrintWriter(s.getOutputStream());
            if (ui == cli){
                this.handler = new IOHandlerClient(this.username, cli);
                this.handler.startInterface();
                this.waitStart();
            } else {
                //GUI
                this.handler = new IOHandlerClient(this.username, cli);
                this.handler.startInterface();
            }
        } catch (IOException e) {
            this.handler.send("Server is down, I repeat, server is down!");
            this.handler.send(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            this.waitStart();
        } catch (IOException e) {
            this.handler.send("Server is down, I repeat, server is down!");
            this.handler.send(e.getMessage());
        }
    }

    private void waitStart() throws IOException{
        String request;
        //this method waits until the server gives a "GAMESTART" signal
        this.handler.send("I'm wating for the server to tell me that I can start...");

        do {
            request = this.in.readLine();
        } while (!request.equals(GAMESTART));
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
            input = this.in.readLine();
            request = this.getRequest(input);
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
                    this.out.println(OK);
                    this.out.flush();
                    break;
                default:
                    this.unexpectedMessageFromServer();
                    break;
            }

        }while (!request.equals(FINISH));

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
        message = message.replaceAll(NEWLINE, "\n");
        this.handler.send(message);
    }

    private String get(){
        String s = "";
        s = this.handler.request();
        return s;
    }

    private void askIfReceived() throws IOException {
        String request;
        this.handler.send("I'm waiting to know if the server understood...");
        request = this.in.readLine();
        this.handler.send("I read : " + request);
        if (request.equals(OK)){
            return;
        } else {
            this.unexpectedMessageFromServer();
        }
    }

    public void close() throws IOException {
        if(this.handler!=null)
        this.handler.send("Bye bye <3");

        this.socket.close();
    }
}
