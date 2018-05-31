package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Lobby;

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
        String string = scanner.nextLine();
        String ipAddress;
        if (string.equals("RMI") || string.equals("rmi") || string.equals("socket") || string.equals("Socket")){
            Boolean on = false;
            do {
                System.out.println("IP address of the Server: ");
                ipAddress = scanner.nextLine();
                if (ipAddress.equals(""))
                    System.out.println("Not a valid IP");
                else
                    on = true;
            } while (!on);
            ClientMain clientMain = new ClientMain(ipAddress);
            if (string.equals("RMI") || string.equals("rmi")){
                clientMain.startClientRMI();
            }else{
                try {
                    clientMain.startClientSocket();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }catch (NoSuchElementException e){
                    System.err.println("Nothing to read " +e.getMessage());
                }
            }
        }
        scanner.close();
    }

    private void startClientRMI(){
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
                    System.out.println("You are the first player of the lobby, please set a timer (min 15 s, max 60s)");
                    server.setDelay(Integer.parseInt(scanner.nextLine()));
                }
                boolean active = true;
                while (active) {

                    System.out.println("Waiting for other players...\n" +
                        "If you want to disconnect type 'e' or type any other character to refresh and check how many players there are in the lobby");
                    String text = scanner.nextLine();
                    if (text.equals("e")) {
                        server.logout(validRemoteRef);
                        active = false;
                    } else {
                        server.confirmConnections();
                        System.out.println("[Players in the lobby: " + server.playersInLobby() + "]");
                        if (server.playersInLobby() == Lobby.MAX_PLAYERS)
                            System.out.println("The game is starting...");
                    }

                }
                if (!on)
                    scanner.close();
            } while (on);

        } catch (MalformedURLException e) {
            System.err.println("URL not found!");
        } catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
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
                    System.out.println("You are the first player of the lobby, please set a timer (min 15 s, max 60s)");
                    out.println(scanner.nextLine());
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
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        finally {
            if (socket != null)
                socket.close();
        }
    }
}

