package it.polimi.ingsw.server.network_server;

import it.polimi.ingsw.server.controller.Lobby;

import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Entry point of the application: lets the program start (server-side)
 *
 * @author Andrea
 */
public class ServerMain {

    private static final int PORTRMI = 1099; //default port
    private static final int PORTSOCKET = 1337; //default port
    private Lobby lobby;

    /**
     * Constructor of the ServerMain class
     *
     * @author Andrea
     */
    private ServerMain(){
        this.lobby = new Lobby();
    }

    /**
     * Launches the application
     *
     * @param args: list of strings received from the user during the JAR execution
     * @author Andrea
     */
    public static void main(String[] args){
        ServerMain serverMain = new ServerMain();
        serverMain.startServerRMI();
        serverMain.startServerSocket();
    }

    /**
     * Allows the game to work with an RMI connection (server-side)
     *
     * @author Andrea
     */
    private void startServerRMI(){
        try {
            LocateRegistry.createRegistry(PORTRMI);
        } catch (RemoteException e) {
            System.err.println("There is already a registry!");
        }
        try {
            ServerImplementationRMI serverImplementation = new ServerImplementationRMI(this.lobby);
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

    /**
     * Allows the game to work with a socket connection (server-side)
     *
     * @author Andrea
     */
    private void startServerSocket (){
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORTSOCKET);

            System.out.println("[Socket Server]\tServer is ready...");
            boolean on = true;
            while (on){
                try {
                    Socket socket = serverSocket.accept();
                    executor.submit(new ServerImplementationSocket(socket, this.lobby));
                } catch(IOException e) {
                    System.err.println(e.getMessage());
                    //on = false;
                }
            }
            executor.shutdown();
        } catch (IOException e) {
            System.err.println(e.getMessage()); //port not available
        } finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

}