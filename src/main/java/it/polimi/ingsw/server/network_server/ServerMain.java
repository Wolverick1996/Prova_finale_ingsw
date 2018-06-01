package it.polimi.ingsw.server.network_server;

import it.polimi.ingsw.server.controller.Lobby;

import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    private static final int PORTRMI = 1099; // porta di default
    private static final int PORTSOCKET = 1337; // porta di default
    private Lobby lobby;

    private ServerMain(){
        this.lobby = new Lobby();
    }

    public static void main(String[] args) {
        ServerMain serverMain = new ServerMain();
        serverMain.startServerRMI();
        serverMain.startServerSocket();
    }

    private void startServerRMI (){
        try {
            LocateRegistry.createRegistry(PORTRMI);

        } catch (RemoteException e) {
            System.out.println("There is already a registry!");
        }
        try {
            ServerImplementationRMI serverImplementation = new ServerImplementationRMI(lobby);
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
                    executor.submit(new ServerImplementationSocket(socket, lobby));
                } catch(IOException e) {
                    System.err.println(e.getMessage());
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