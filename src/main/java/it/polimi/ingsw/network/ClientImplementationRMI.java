package it.polimi.ingsw.network;

import java.rmi.RemoteException;

public class ClientImplementationRMI implements ClientIntRMI {

    private String name;

    ClientImplementationRMI (String n) throws RemoteException {
        name=n;
    }

    public void notify(String message) throws RemoteException {
        System.out.println(message);
    }

    public String getName() throws RemoteException{
        return name;
    }

    /*public void confirmConnection () throws RemoteException{
        return;
    }*/
}

