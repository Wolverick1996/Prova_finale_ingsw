package it.polimi.ingsw.client.view;

import java.io.*;
import java.net.Socket;
import static it.polimi.ingsw.client.view.IOHandlerClient.Interface.*;

/**
 * Static class used to initialize socket communication (client-side)
 *
 * @author Matteo
 */
public class SocketMessengerClient implements Runnable {

    private boolean gameHasStarted = false;
    private boolean debug = false;
    private String username;
    private PrintWriter out;
    private BufferedReader in;
    private IOHandlerClient handler;
    private Socket socket;

    //DICTIONARY
    private static final String D_LEFT = "<";
    private static final String D_RIGHT = ">";
    private static final String GAMESTART = "gameStarts";
    private static final String NAME = "name";
    private static final String OK = "ok";
    private static final String FINISH = "finish";
    private static final String PRINT = "print";
    private static final String REQUEST = "requestData";
    private static final String NEWLINE = "%%%nnn%%%";

    /**
     * Constructor of SocketMessengerClient
     *
     * @param s: socket of the client
     * @param n: player's username
     * @param ui: type of interface (CLI/GUI)
     * @author Matteo
     */
    public SocketMessengerClient(Socket s, String n, IOHandlerClient.Interface ui){
        try {
            this.socket = s;
            this.username = n;
            this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.out = new PrintWriter(s.getOutputStream());
            if (ui == CLI){
                this.handler = new IOHandlerClient(this.username, CLI);
                this.handler.startInterface();
                this.waitStart();
            } else {
                //GUI
                this.handler = new IOHandlerClient(this.username, GUI);
                this.handler.startInterface();
            }
        } catch (IOException e) {
            this.handler.send("Server is down, I repeat, server is down!");
            this.handler.send(e.getMessage());
        }
    }

    /**
     * Public constructor of SocketMessengerClient with isReconnecting parameter
     *
     * @param s: socket of the client
     * @param n: player's username
     * @param ui: type of interface (CLI/GUI)
     * @param isReconnecting: flag that represents the status of reconnecting
     * @author Andrea
     */
    public SocketMessengerClient (Socket s, String n, IOHandlerClient.Interface ui, boolean isReconnecting){
        if (isReconnecting){
            try{
                this.socket = s;
                this.username = n;
                this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                this.out = new PrintWriter(s.getOutputStream());
                this.gameHasStarted = true;
                if (ui == CLI){
                    this.handler = new IOHandlerClient(this.username, CLI);
                    this.handler.startInterface();
                    this.game();
                } else {
                    //GUI
                    this.handler = new IOHandlerClient(this.username, GUI);
                    this.handler.startInterface();
                }
            } catch (IOException e) {
                this.handler.send("Server is down, I repeat, server is down!");
                this.handler.send(e.getMessage());
            }
        }
    }

    /**
     * Override of the run method of the Runnable interface
     *
     * @author Matteo
     */
    @Override
    public void run(){
        try {
            this.waitStart();
        } catch (IOException e) {
            this.handler.send("Server is down, I repeat, server is down!");
            this.handler.send(e.getMessage());
        }
    }

    /**
     * Waits until the server gives a "GAMESTART" signal
     *
     * @throws IOException if server disconnects
     * @author Matteo
     */
    private void waitStart() throws IOException {
        String request;
        if (debug)
            this.handler.send("I'm wating for the server to tell me that I can start...");

        do {
            request = this.in.readLine();
        } while (!request.equals(GAMESTART));
        setGameHasStarted();
        this.out.println(NAME + D_LEFT + this.username + D_RIGHT);
        this.out.flush();

        this.askIfReceived();
        this.game();
    }

    /**
     * Handles games input
     *
     * @throws IOException if server disconnects
     * @author Matteo
     */
    private void game() throws IOException {
        this.handler.send("We're ready to roll! " + this.username + ", let's go! Even if you are playing with " +
                "Socket, I'm with you");
        String input;
        String request;
        do {
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
        } while (!request.equals(FINISH));

        this.close();
    }

    /**
     * Splits a string received from the server and returns the string fragment corresponding to the request
     *
     * @param input: the string to be split
     * @return the string corresponding to the request (first of the splittedInput array)
     * @author Matteo
     */
    private String getRequest(String input){
        String[] splittedInput = input.split(D_LEFT);
        return splittedInput[0];
    }

    /**
     * Splits a string received from the server and returns the string fragment corresponding to the first parameter
     *
     * @param input: the string to be split
     * @return the string corresponding to the request (second of the splittedInput array)
     * @author Matteo
     */
    private String getFirstParameter(String input){
        try {
            String[] splittedInput = input.split(D_LEFT);
            splittedInput = splittedInput[1].split(D_RIGHT);
            return splittedInput[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }

    /**
     * Throws an IOException if server disconnects
     *
     * @throws IOException if server disconnects
     * @author Matteo
     */
    private void unexpectedMessageFromServer() throws IOException {
        this.handler.send("Something went horribly wrong on the Server, I'm sorry :(");
        throw new IOException();
    }

    /**
     * Sends a message to the user
     *
     * @param input: the string to be sent to the user
     * @author Matteo
     */
    private void send(String input){
        String message = this.getFirstParameter(input);
        message = message.replaceAll(NEWLINE, "\n");
        this.handler.send(message);
    }

    /**
     * Requests an input from the user
     *
     * @return the input inserted
     * @author Matteo
     */
    private String get(){
        return this.handler.request();
    }

    /**
     * Gets an input from the server and returns if the received input is "OK", otherwise calls the unexpectedMessageFromServer method
     *
     * @throws IOException if server disconnects
     * @author Matteo
     */
    private void askIfReceived() throws IOException {
        String request;
        if (debug)
            this.handler.send("I'm waiting to know if the server understood...");
        request = this.in.readLine();
        if (debug)
            this.handler.send("I read : " + request);
        if (request.equals(OK)){
            return;
        } else {
            this.unexpectedMessageFromServer();
        }
    }

    /**
     * Closes the socket connection if server disconnects
     *
     * @throws IOException if server disconnects
     * @author Matteo
     */
    public void close() throws IOException {
        if (this.handler!=null)
            this.handler.send("Bye bye <3");

        this.socket.close();
    }

    /**
     * Sets the delay of the lobby (GUI)
     *
     * @param delay: number of seconds
     * @author Matteo
     */
    void GUIsetTimer(int delay){
        if (delay>=15 && delay <=60){
            this.out.println(delay);
            this.out.flush();
        }
    }

    /**
     * Sets the gameHasStarted flag true
     *
     * @author Matteo
     */
    private synchronized void setGameHasStarted(){
        gameHasStarted = true;
    }

    /**
     * Returns 999 if game already started, otherwise returns the number of players in the lobby
     *
     * @return 999 if game already started, otherwise the number of players in the lobby
     * @throws IOException if server disconnects
     * @author Matteo
     */
    synchronized int waitForGameStart() throws IOException {
        if (gameHasStarted) return 999;
        return Integer.parseInt(this.in.readLine());
    }

}