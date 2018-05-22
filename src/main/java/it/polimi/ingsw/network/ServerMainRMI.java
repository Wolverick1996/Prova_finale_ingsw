package it.polimi.ingsw.network;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

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

            File inputFile = new File("src/main/resources/server_sagrada.txt");
            Scanner scan = null;
            String ip = null;
            try {
                scan = new Scanner(inputFile);
                ip = scan.nextLine();
            } catch (IOException e){
                System.out.println("Error");
            }
            finally {
                if (scan != null) {
                    try {
                        scan.close();
                    } catch (Exception e1) {
                        System.out.println("Error");
                    }
                }
            }
            Naming.rebind("//" +ip+ "/MyServer", serverImplementation);

            System.out.println("[Server] Server is ready...");

        } catch (MalformedURLException e) {
            System.err.println("Impossible to register this object!");
        } catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
        }
    }
}