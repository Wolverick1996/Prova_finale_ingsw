package it.polimi.ingsw.network;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerMainRMI {

    private static final int PORT = 1099; // porta di default

    public static void main(String[] args) {

        try {
            LocateRegistry.createRegistry(PORT);

        } catch (RemoteException e) {
            System.out.println("There is already a registry!");
        }

        try {

            ServerImplementationRMI serverImplementation = new ServerImplementationRMI();

            Naming.rebind("//127.0.0.1/MyServer", serverImplementation);

            System.out.println("[Server] Server is ready...");

        } catch (MalformedURLException e) {
            System.err.println("Impossible to register this object!");
        } catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
        }
    }
}