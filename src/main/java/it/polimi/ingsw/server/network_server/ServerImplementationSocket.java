package it.polimi.ingsw.server.network_server;

import it.polimi.ingsw.server.controller.Lobby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Allows the connection between the game and the socket (server-side)
 *
 * @author Andrea
 * @author Matteo
 */
public class ServerImplementationSocket implements Runnable {
    private Socket socket;
    private Lobby lobby;
    private String playerConnected;

    /**
     * Constructor of the ServerImplementationSocket class
     *
     * @param socket: socket of the client
     * @param lobby: lobby related to the current match
     * @author Andrea
     */
    ServerImplementationSocket(Socket socket, Lobby lobby){
        this.socket = socket;
        this.lobby = lobby;
    }

    /**
     * Instantiates a thread which waits for a delay to set
     *
     * @author Matteo
     */
    class WaitForDelay implements Runnable {

        /**
         * (Empty) constructor of the WaitForDelay class
         *
         * @author Matteo
         */
        WaitForDelay(){
            //TRY AND USE EXECUTORSERVICE HERE
            super();
        }

        /**
         * Override of the run method of the Runnable interface, waits to receive an input from the client and sets the delay of the lobby when received
         *
         * @author Matteo
         */
        @Override
        public void run(){
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!in.ready())
                    Thread.sleep(200);

                lobby.setDelay(Integer.parseInt(in.readLine()));
                System.out.println("GOT IT!");
            } catch (IOException e) {
                System.err.println("Connection lost from a client");
                if (playerConnected != null){
                    System.out.println("[Socket Server]\t" +playerConnected+ "  disconnected....");
                    lobby.removePlayer(playerConnected);
                    playerConnected = null;
                }
            } catch (InterruptedException i) {
                System.out.println("WAITFORDELAY WAS INTERRUPTED");
            }
        }

    }

    /**
     * Override of the run method of the Runnable interface
     * Waiting room before the game start (socket, server-side)
     *
     * @author Andrea
     */
    @Override
    public void run(){
        try {
            PrintWriter out;
            boolean gameCanStart = false;
            do {
                login();
                if (this.lobby.hasStarted()){
                    reconnection();
                    System.out.println("[Socket Server]\t" + this.playerConnected + "  got connected again....");
                    System.out.println("HERE I CLOSE THE SOCKET IMPLEMENTATION THREAD :\t" + Thread.currentThread().getName());
                    return;
                }
                out = new PrintWriter(this.socket.getOutputStream());
                out.println(this.lobby.getPlayers().size());
                out.flush();
                Thread waitForInput = new Thread(new WaitForDelay());
                if (this.lobby.getPlayers().size() == 1)
                    waitForInput.start();

                while (!this.lobby.hasStarted()){
                    out.println(this.lobby.getPlayers().size());
                    out.flush();
                    gameCanStart = true;
                }
                if (!waitForInput.isInterrupted())
                    waitForInput.interrupt();
                out.println(999);
                out.flush();
                System.out.println("HERE I CLOSE THE SOCKET IMPLEMENTATION THREAD :\t" + Thread.currentThread().getName());
            } while (!gameCanStart);
        } catch (IOException e) {
            System.err.println("Connection lost from a client");
            if (this.playerConnected != null){
                System.out.println("[Socket Server]\t" +this.playerConnected+ "  disconnected....");
                this.lobby.removePlayer(this.playerConnected);
                this.playerConnected = null;
            }
        }
    }

    /**
     * Allows the login of a socket user (server-side)
     *
     * @throws IOException if client has connection issues
     * @author Andrea
     */
    private synchronized void login() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        PrintWriter out = new PrintWriter(this.socket.getOutputStream());
        boolean loginSuccess = false;

        while (!loginSuccess) {
            out.println(this.lobby.getPlayers().size());
            out.flush();
            String string = in.readLine();
            if (!string.equals("*")){
                if (string.equals("")){
                    out.println("invalid");
                    out.flush();
                } else if (this.lobby.addPlayer(string)){
                    this.lobby.addSocket(this.socket);
                    loginSuccess = true;
                    System.out.println("[Socket Server]\t" +string+ "  got connected....");
                    this.playerConnected = string;
                    out.println("true");
                    out.flush();
                } else {
                    boolean sameUsername = false;
                    for (String s : this.lobby.getPlayers())
                        if (s.equals(string))
                            sameUsername = true;
                    if (this.lobby.hasStarted()){
                        out.println("started");
                        out.flush();
                        if (this.lobby.willingToReconnectPlayer(string)){
                            this.playerConnected = string;
                            out.println("true");
                            out.flush();
                            return;
                        } else{
                            out.println("false");
                            out.flush();
                            return;
                        }
                    } else if (sameUsername){
                        out.println("same");
                        out.flush();
                    } else {
                        out.println("max");
                        out.flush();
                    }
                }
            }
        }
    }

    /**
     * Re-connect player to the match
     *
     * @author Andrea
     */
    private void reconnection(){
        lobby.rejoinMatch(this.playerConnected, this.socket);
    }

    /**
     * Allows the logout of a socket user (server-side)
     *
     * @throws IOException if client has connection issues
     * @author Andrea
     */
    private synchronized void logout() throws IOException {
        PrintWriter out = new PrintWriter(this.socket.getOutputStream());
        if (this.lobby.removePlayer(this.playerConnected)){
            out.println("ok");
            out.flush();
            System.out.println("[Socket Server]\t" + this.playerConnected + "  disconnected....");
        } else {
            out.println("ko");
            out.flush();
        }
    }

}