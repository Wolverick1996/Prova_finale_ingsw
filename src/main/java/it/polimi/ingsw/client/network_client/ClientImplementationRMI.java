package it.polimi.ingsw.client.network_client;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientImplementationRMI implements ClientIntRMI {

    private String name;

    ClientImplementationRMI (String n){
        name=n;
    }

    public void notify(String message) throws RemoteException {
        System.out.println(message);
    }

    public String getName() throws RemoteException{
        return name;
    }

    public void freeScanner() throws RemoteException{
        Scanner scanner = new Scanner(System.in);
        scanner.reset();
    }

    public String getInput() throws RemoteException{
        Scanner scanner = new Scanner(System.in);
        scanner.reset();
        //TODO: solve "FLUSH" problem
        return scanner.nextLine();
    }

    public void confirmConnection() throws RemoteException{
        //used to ping and check connection
    }
}

