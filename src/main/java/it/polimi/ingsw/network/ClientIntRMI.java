package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientIntRMI extends Remote {

    void notify(String message) throws RemoteException;
    String getName()throws RemoteException;
    void confirmConnection () throws RemoteException;
}
