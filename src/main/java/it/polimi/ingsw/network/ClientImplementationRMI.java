package it.polimi.ingsw.network;

import java.rmi.RemoteException;

/*class ClientImplementationRMI  extends UnicastRemoteObject implements ClientIntRMI{

    private String name;
    public ClientImplementationRMI (String n) throws RemoteException {
        name=n;
    }

    public void tell(String st) throws RemoteException{
        System.out.println(st);
    }
    public String getName() throws RemoteException{
        return name;
    }

}*/

public class ClientImplementationRMI implements ClientIntRMI {

    private String name;

    public ClientImplementationRMI (String n) throws RemoteException {
        name=n;
    }

    public void notify(String message) throws RemoteException {
        System.out.println(message);
    }

    public String getName() throws RemoteException{
        return name;
    }

}

