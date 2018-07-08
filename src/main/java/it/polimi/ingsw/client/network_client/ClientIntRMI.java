package it.polimi.ingsw.client.network_client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Skeleton of the RMI client
 *
 * @author Andrea
 */
public interface ClientIntRMI extends Remote {

    /**
     * Shows to the client a specified message
     *
     * @param message: the string to be showed
     * @throws RemoteException if client has connection issues
     * @author Andrea
     */
    void notify(String message) throws RemoteException;

    /**
     * Gets the player's username
     *
     * @return the player's username
     * @throws RemoteException if client has connection issues
     * @author Andrea
     */
    String getName() throws RemoteException;

    /**
     * Throws an exception if client has connection issues (the exception will be caught from the server)
     *
     * @throws RemoteException if client has connection issues
     * @author Andrea
     */
    void confirmConnection() throws RemoteException;

    /**
     * Calls the IOhandler getInput method
     *
     * @return the input inserted by the player
     * @throws RemoteException if client has connection issues
     * @author Andrea
     */
    String getInput() throws RemoteException;

    /**
     * Calls the IOhandler method to start the CLI
     *
     * @throws RemoteException if client has connection issues
     * @author Andrea
     */
    void startInterface() throws RemoteException;

    /**
     * Calls Sys.exit() to exit from the application when game has ended
     *
     * @author Andrea
     */
    void finishGame() throws RemoteException;

}