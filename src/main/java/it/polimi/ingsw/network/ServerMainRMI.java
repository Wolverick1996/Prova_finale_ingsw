package it.polimi.ingsw.network;

import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMainRMI {

    private static final int PORTRMI = 1099; // porta di default
    private static final int PORTSOCKET = 1337; // porta di default

    private ServerMainRMI (){
        super();
    }

    public static void main(String[] args) {
        ServerMainRMI serverMainRMI = new ServerMainRMI();
        serverMainRMI.startServerRMI();
        serverMainRMI.startServerSocket();
    }

    private void startServerRMI (){
        try {
            LocateRegistry.createRegistry(PORTRMI);

        } catch (RemoteException e) {
            System.out.println("There is already a registry!");
        }

        try {

            ServerImplementationRMI serverImplementation = new ServerImplementationRMI();
            InetAddress ip;
            ip = InetAddress.getLocalHost();
            Naming.rebind("//" +ip.getHostAddress()+ "/MyServer", serverImplementation);
            System.out.println("[RMI Server]\tServer is ready...");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.err.println("Impossible to register this object!");
        } catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
        }
    }

    private void startServerSocket (){
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORTSOCKET);

            System.out.println("[Socket Server]\tServer is ready...");
            boolean on = true;
            while (on) {
                try {
                    Socket socket = serverSocket.accept();
                    executor.submit(new ServerImplementationSocket(socket));
                } catch(IOException e) {
                    on = false;
                }
            }
            executor.shutdown();
        } catch (IOException e) {
            System.err.println(e.getMessage()); // porta non disponibile
        }
        finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}