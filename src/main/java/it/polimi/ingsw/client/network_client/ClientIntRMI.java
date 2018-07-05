package it.polimi.ingsw.client.network_client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientIntRMI extends Remote {

    void notify(String message) throws RemoteException;
    String getName()throws RemoteException;
    void confirmConnection () throws RemoteException;
    String getInput () throws RemoteException;
    void startInterface() throws RemoteException;
}
