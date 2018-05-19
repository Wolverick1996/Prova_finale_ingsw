package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*public interface ClientIntRMI {
    public void tell (String name)throws RemoteException ;
    public String getName()throws RemoteException;
}*/
public interface ClientIntRMI extends Remote {

    public void notify(String message) throws RemoteException;
    public String getName()throws RemoteException;

}
