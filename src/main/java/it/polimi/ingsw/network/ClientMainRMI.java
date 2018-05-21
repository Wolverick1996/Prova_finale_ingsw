package it.polimi.ingsw.network;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ClientMainRMI {

    public static void main(String[] args) {
        ServerIntRMI server;
        boolean on = true;
        try {
            do {
                boolean loginSuccess = false;
                boolean idTaken = false;
                boolean fullLobby = false;
                ClientIntRMI validRemoteRef = null;
                server = (ServerIntRMI) Naming.lookup("//localhost/MyServer");
                do {
                    if (fullLobby)
                        System.out.println("Retry later...");

                    if (idTaken)
                        System.out.println("Login failed, this userID is already used");

                    Scanner scanner = new Scanner(System.in);
                    System.out.println("[Players in the lobby: " + server.getConnected().size() + "]\nLogin:\t\t(to refresh the page type 'r')");
                    String text = scanner.nextLine();

                    if (!text.equals("r")) {
                        ClientImplementationRMI client = new ClientImplementationRMI(text);

                        ClientIntRMI remoteRef = (ClientIntRMI) UnicastRemoteObject.exportObject(client, 0);
                        validRemoteRef = remoteRef;

                        if (server.login(remoteRef))
                            loginSuccess = true;
                        else {
                            if (server.getConnected().size() >= ServerImplementationRMI.MAX_PLAYERS)
                                fullLobby = true;
                            else
                                idTaken = true;
                        }
                    }
                } while (!loginSuccess);

                Scanner scanner = new Scanner(System.in);
                boolean active = true;
                while (active) {
                    System.out.println("Waiting for other players...if you want to disconnect type 'e'");
                    String text = scanner.nextLine();
                    if (!text.equals("e")) {
                        if (server.getConnected().size() == ServerImplementationRMI.MAX_PLAYERS)
                            System.out.println("The game is starting...");
                    } else {
                        server.logout(validRemoteRef);
                        active = false;
                    }
                }
                if(!on)
                    scanner.close();
            }while (on);

        } catch (MalformedURLException e) {
            System.err.println("URL not found!");
        } catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
        } catch (NotBoundException e) {
            System.err.println("This reference is not connected!");
        } catch (NoSuchElementException e){}
    }
}

