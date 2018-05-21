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
        boolean on = false;
        String ipAddress;
        try {
            do {
                Scanner scanner = new Scanner(System.in);
                System.out.println("IP address of the Server: ");
                ipAddress = scanner.nextLine();
                if (ipAddress.equals(""))
                    System.out.println("Not a valid IP");
                else
                    on = true;
            } while (!on);
            do {
                boolean loginSuccess = false;
                boolean idTaken = false;
                boolean fullLobby = false;
                ClientIntRMI validRemoteRef = null;
                server = (ServerIntRMI) Naming.lookup("//" + ipAddress + "/MyServer");
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
                    System.out.println("[Players in the lobby: " + server.getConnected().size() + "]\nLogin:\t\t(to refresh the page type '*')");
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
                if (!on)
                    scanner.close();
            } while (on);

        } catch (MalformedURLException e) {
            System.err.println("URL not found!");
        } catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
        } catch (NotBoundException e) {
            System.err.println("This reference is not connected!");
        } catch (NoSuchElementException e) {}
    }
}

