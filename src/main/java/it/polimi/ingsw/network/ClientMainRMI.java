package it.polimi.ingsw.network;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;


public class ClientMainRMI {

    public static void main(String[] args) {
        ServerIntRMI server;
        try {
            boolean loginSuccess = false;
            boolean loginFailed = false;
            server = (ServerIntRMI) Naming.lookup("//localhost/MyServer");
            do {

                if (loginFailed)
                    System.out.println("Login failed, this userID is already used");

                Scanner scanner = new Scanner(System.in);
                System.out.println("Login: ");
                String text = scanner.nextLine();

                ClientImplementationRMI client = new ClientImplementationRMI(text);

                ClientIntRMI remoteRef = (ClientIntRMI) UnicastRemoteObject.exportObject(client, 0);

                if (server.login(remoteRef))
                    loginSuccess = true;
                else
                    loginFailed = true;
            }while (!loginSuccess);

            Scanner scanner = new Scanner(System.in);
            boolean active = true;
            while(active){
                System.out.println("Type a message");
                String text = scanner.nextLine();

                // Invio l'evento
                server.send(text);
            }
            scanner.close();

        } catch (MalformedURLException e) {
            System.err.println("URL not found!");
        } catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
        } catch (NotBoundException e) {
            System.err.println("This reference is not connected!");
        }

    }

}

