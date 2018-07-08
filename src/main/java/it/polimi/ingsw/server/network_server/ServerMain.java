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

    private static int portrmi = 1099; //default port
    private static int portsocket = 1337; //default port
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
        try {
            portrmi = Integer.parseInt(args[0]);
            portsocket = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException n){
            portrmi = 1099;
            portsocket = 1337;
        }
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
        int i = 0;
        try {
            i++;
            LocateRegistry.createRegistry(portrmi);
        } catch (RemoteException e) {
            System.err.println("There is already a registry!");
            if (i < 2){
                portrmi = 1099;
                startServerRMI();
            }
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
        int i = 0;
        try {
            i++;
            serverSocket = new ServerSocket(portsocket);

            System.out.println("[Socket Server]\tServer is ready...");
            boolean on = true;
            while (on){
                try {
                    Socket socket = serverSocket.accept();
                    executor.submit(new ServerImplementationSocket(socket, this.lobby));
                } catch(IOException e) {
                    System.err.println(e.getMessage());
                    on = false;
                }
            }
            executor.shutdown();
        } catch (IOException e) {
            if (i < 2){
                portsocket = 1337; //default
                startServerSocket();
            }
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