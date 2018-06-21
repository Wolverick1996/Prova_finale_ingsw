package it.polimi.ingsw.client.network_client;

import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.network_server.ServerIntRMI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientMain {

    private String ip;
    private static final int PORT = 1337;

    private ClientMain(String ip){
        this.ip = ip;
    }

    public static void main(String[] args) {
        System.out.println("Which kind of connection do you want to use? [RMI or socket]");
        Scanner scanner = new Scanner(System.in);
        String string;
        Boolean check = false;
        while (!check) {
            string = scanner.nextLine();
            string = string.toLowerCase();
            if (string.equals("rmi") || string.equals("socket")) {
                check = true;
                boolean ipOK = false;
                while(!ipOK){
                    ClientMain clientMain = new ClientMain(askIP());
                    if (string.equals("rmi")) {
                        try {
                            clientMain.startClientRMI();
                            ipOK = true; //unreachable
                        } catch (MalformedURLException | RemoteException e){
                            System.out.println("IP not correct");
                        }
                    } else {
                        try {
                            clientMain.startClientSocket();
                            ipOK = true; //unreachable
                        } catch (IOException e) {
                            System.out.println("IP not correct");
                        } catch (NoSuchElementException e) {
                            System.err.println("Nothing to read " + e.getMessage());
                        }
                    }
                }

            }
            if (!check){
                System.out.println("Invalid name. Type rmi or socket");
            }
        }
        scanner.close();
    }

    private void startClientRMI() throws MalformedURLException, RemoteException{
        ServerIntRMI server;
        boolean on = true;
        try {
            do {
                boolean loginSuccess = false;
                boolean idTaken = false;
                boolean fullLobby = false;
                ClientIntRMI validRemoteRef = null;
                server = (ServerIntRMI) Naming.lookup("//" + this.ip + "/MyServer");
                do {
                    if (fullLobby) {
                        System.out.println("Retry later...");
                        fullLobby = false;
                    }

                    if (idTaken) {
                        System.out.println("Login failed, this userID is already used");
                        idTaken = false;
                    }

                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Connection established");
                    System.out.println("[Players in the lobby: " + server.playersInLobby() + "]\n" +
                            "Login:\t\t(to refresh the page type '*')");
                    String text = scanner.nextLine();

                    if (text.equals("")){
                        System.out.println("Invalid name, your ID should be an alphanumeric of at least 1 character");
                        text = "*";
                    }
                    if (!text.equals("*")) {
                        ClientImplementationRMI client = new ClientImplementationRMI(text);

                        ClientIntRMI remoteRef = (ClientIntRMI) UnicastRemoteObject.exportObject(client, 0);
                        validRemoteRef = remoteRef;

                        if (server.login(remoteRef))
                            loginSuccess = true;
                        else {
                            if (server.getConnected().size() >= Lobby.MAX_PLAYERS)
                                fullLobby = true;
                            else
                                idTaken = true;
                        }
                    }
                } while (!loginSuccess);

                Scanner scanner = new Scanner(System.in);
                if (server.playersInLobby() == 1){
                    System.out.println("You are the first player of the lobby!");
                    int delay = 0;
                    while(delay < 15 || delay > 60){
                        System.out.println("Please set a timer (min 15s, max 60s)");
                        delay = Integer.parseInt(scanner.nextLine());
                    }
                    server.setDelay(delay);
                }
                boolean active = true;
                System.out.println("Waiting for other players...\n" +
                        "If you want to disconnect type 'e' or type any other character to refresh and check how many players there are in the lobby");

                while (active) {

                    /*String text = scanner.nextLine();
                    if (text.equals("e")) {
                        server.logout(validRemoteRef);
                        active = false;
                    } else {
                        server.confirmConnections();
                        System.out.println("[Players in the lobby: " + server.playersInLobby() + "]");
                        if (server.playersInLobby() == Lobby.MAX_PLAYERS)
                            System.out.println("The game is starting...");
                    }*/

                }
                if (!on)
                    scanner.close();
            } while (on);

        } catch (NotBoundException e) {
            System.err.println("This reference is not connected!");
        } catch (NoSuchElementException e) {
            System.err.println("NOTHING TO READ " +e.getMessage());
        }
    }

    private void startClientSocket() throws IOException{
        Socket socket = null;
        int activePlayers;
        boolean success;
        try {
            socket = new Socket(ip, PORT);
            Scanner scanner = new Scanner(System.in);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connection established");
            ClientImplementationSocket clientImplementationSocket = new ClientImplementationSocket(socket);
            do {
                clientImplementationSocket.login();
                activePlayers = Integer.parseInt(in.readLine());
                if (activePlayers == 1){
                    System.out.println("You are the first player of the lobby!");
                    int delay = 0;
                    String delayString = null;
                    while (delay < 15 || delay > 60){
                        System.out.println("Please set a timer (min 15s, max 60s)");
                        delayString = scanner.nextLine();
                        delay = Integer.parseInt(delayString);
                    }
                    out.println(delayString);
                    out.flush();
                }
                success = true;
                while (success){
                    System.out.println("Waiting for other players...\n" +
                            "If you want to disconnect type 'e' or type any other character to refresh and check how many players there are in the lobby");

                    String string = scanner.nextLine();
                    out.println(string);
                    out.flush();
                    if (string.equals("e")){
                        clientImplementationSocket.logout();
                        success = false;
                    }
                    else {
                        activePlayers = Integer.parseInt(in.readLine());
                        System.out.println("[Players in the lobby: " + activePlayers + "]");
                    }
                }
            }while (!success);

            while (!success){
                Scanner s = new Scanner(System.in);
                String t = s.nextLine();
                System.err.println(t);
            }
            scanner.close();
            in.close();
            out.close();
        }catch (NoSuchElementException e){
            System.err.println("NOTHING TO READ "+e.getMessage());
        }
        finally {
            if (socket != null)
                socket.close();
        }
    }

    private static String askIP(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("IP address of the Server: ");
        return scanner.nextLine();
    }
}