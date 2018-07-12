package it.polimi.ingsw.client.network_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Allows the connection between the game and the socket (client-side)
 *
 * @author Andrea
 * @author Matteo
 */
class ClientImplementationSocket {

    private Socket socket;
    private boolean gameStarted;

    /**
     * Constructor of the ClientImplementationSocket class
     *
     * @param socket: socket of the client
     * @author Andrea
     */
    ClientImplementationSocket(Socket socket){
        this.socket = socket;
    }

    /**
     * Allows the login of a socket user (CLI, client-side)
     *
     * @return the username of the player logged in
     * @throws IOException if client has connection issues
     * @author Andrea
     */
    String login() throws IOException {
        boolean success = false;
        int playersInLobby;
        Scanner scanner = new Scanner(System.in);
        String string = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter out = new PrintWriter(this.socket.getOutputStream());
            do {
                playersInLobby = Integer.parseInt(in.readLine());
                System.out.println("[Players in the lobby: " + playersInLobby + "]\nLogin:\t\t(to refresh the page type '*')");
                string = scanner.nextLine();
                out.println(string);
                out.flush();
                if (!string.equals("*")){
                    String result = in.readLine();
                    if (result.equals("invalid"))
                        System.out.println("Invalid name, your ID should be an alphanumeric of at least 1 character");
                    if (result.equals("true")){
                        System.out.println("Welcome " +string+ ".\nYou have connected successfully.");
                        success = true;
                    } else {
                        if (result.equals("same"))
                            System.out.println("Login failed, this userID is already used");
                        else if (result.equals("started")){
                            this.gameStarted = true;
                            if (in.readLine().equals("true")){
                                return string;
                            }
                            System.out.println("Your friends started without you :(\n\nGet better friends");
                        }
                        else if (result.equals("max"))
                            System.out.println("Retry later...");
                    }
                }
            } while (!success);
        } catch (NoSuchElementException e) {
            System.err.println("LOGIN "+e.getMessage());
        }
        return string;
    }

    /**
     * Allows the login of a socket user (GUI, client-side)
     *
     * @return the username of the player logged in
     * @throws IOException if client has connection issues
     * @author Matteo
     */
    String loginGUI(String name) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        PrintWriter out = new PrintWriter(this.socket.getOutputStream());
        int numPlayers = Integer.parseInt(in.readLine()); //Here we read the number of players (sent by the Server)
        out.println(name);
        out.flush();
        String result = in.readLine();
        System.out.println("I'm waiting the response from the server");

        if (result.equals("same"))
            return "Login failed, this userID is already used";
        else if (result.equals("started"))
            return "Your friends started without you :(\n\nGet better friends";
        else if (result.equals("max"))
            return "Retry later...";
        else if (result.equals("invalid"))
            return "Invalid name, your ID should be an alphanumeric of at least 1 character";
        else if (result.equals("true"))
            return "OK";

        return "SOMETHING WENT HORRIBLY WRONG";
    }

    /**
     * Gets the current value of gameStarted flag
     *
     * @return true if game started, otherwise false
     * @author Andrea
     */
    boolean isGameStarted(){
        return this.gameStarted;
    }

}