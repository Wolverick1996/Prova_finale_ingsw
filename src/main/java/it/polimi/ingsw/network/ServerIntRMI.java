package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

public interface ServerIntRMI extends Remote {
    boolean login(ClientIntRMI client) throws RemoteException;
    void logout (ClientIntRMI client) throws RemoteException;
    void send(String message) throws RemoteException;
    List<ClientIntRMI> getConnected() throws RemoteException;
}
/*
public interface ServerIntRMI extends Remote {
    public boolean login (ClientIntRMI a)throws RemoteException;
    public boolean logout (ClientIntRMI a) throws RemoteException;
    public Vector getConnected() throws RemoteException ;
}

public interface ServerIntRMI extends Remote{
    public boolean login (ClientIntRMI a)throws RemoteException ;
    public boolean logout (ClientIntRMI a) throws RemoteException;
    public void publish (String s)throws RemoteException ;
    public Vector getConnected() throws RemoteException ;
}
*/