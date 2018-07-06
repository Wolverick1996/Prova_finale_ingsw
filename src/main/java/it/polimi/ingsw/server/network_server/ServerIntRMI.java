package it.polimi.ingsw.server.network_server;

import it.polimi.ingsw.client.network_client.ClientIntRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Stub of RMI server
 *
 * @author Andrea
 */
public interface ServerIntRMI extends Remote {

    /**
     * Allows connection to the RMI server
     *
     * @param client: skeleton related to the specific client
     * @return true if login is successful, otherwise false
     * @throws RemoteException if connection to the skeleton fails
     * @author Andrea
     */
    boolean login(ClientIntRMI client) throws RemoteException;

    //TODO: JavaDoc
    void confirmConnections() throws RemoteException;

    /**
     * Returns the list of skeleton saved
     *
     * @return the list of skeleton
     * @throws RemoteException if connection to the skeleton fails
     * @author Andrea
     */
    List<ClientIntRMI> getConnected() throws RemoteException;

    /**
     * Returns the number of players in the lobby
     *
     * @return the number of players in the lobby
     * @throws RemoteException if connection to the skeleton fails
     * @author Andrea
     */
    int playersInLobby() throws RemoteException;

    /**
     * Communicates if the match started or not
     *
     * @return true if the match started, otherwise false
     * @throws RemoteException if connection to the skeleton fails
     * @author Andrea
     */
    boolean hasStarted() throws RemoteException;

    /**
     * Sets the delay of the lobby
     *
     * @param delay: number of seconds
     * @throws RemoteException if connection to the skeleton fails
     * @author Andrea
     */
    void setDelay(int delay) throws RemoteException;
}