package it.polimi.ingsw.client.network_client;

import java.rmi.RemoteException;
import java.util.Scanner;

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

    public String getInput() throws RemoteException{
        Scanner scanner = new Scanner(System.in);
        //TODO: solve "FLUSH" problem
        return scanner.nextLine();
    }

    public void confirmConnection() throws RemoteException{
    }
}

