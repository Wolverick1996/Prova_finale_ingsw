package it.polimi.ingsw.server.network_server;

import it.polimi.ingsw.client.network_client.ClientIntRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerIntRMI extends Remote {
    boolean login(ClientIntRMI client) throws RemoteException;
    void logout (ClientIntRMI client) throws RemoteException;
    void confirmConnections () throws RemoteException;
    List<ClientIntRMI> getConnected() throws RemoteException;
    int playersInLobby() throws RemoteException;
    boolean hasStarted() throws RemoteException;
    void setDelay(int delay) throws RemoteException;
    void notify(String username, String message) throws RemoteException;
    void broadcast (String message) throws RemoteException;
    String getInput (String username) throws RemoteException;
}