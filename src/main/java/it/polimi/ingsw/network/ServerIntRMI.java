package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerIntRMI extends Remote {
    boolean login(ClientIntRMI client) throws RemoteException;
    void logout (ClientIntRMI client) throws RemoteException;
    void send(String message) throws RemoteException;
    List<ClientIntRMI> getConnected() throws RemoteException;
}