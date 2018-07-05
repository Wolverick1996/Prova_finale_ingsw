package it.polimi.ingsw.client.network_client;

import it.polimi.ingsw.client.view.IOHandlerClient;

import java.rmi.RemoteException;
import java.util.Observable;

import static it.polimi.ingsw.client.view.IOHandlerClient.Interface.*;

public class ClientImplementationRMI extends Observable implements ClientIntRMI {

    private String name;
    private IOHandlerClient handler;

    ClientImplementationRMI (String n, IOHandlerClient.Interface handlerType){
        name=n;
        this.handler = new IOHandlerClient(n, handlerType); //TODO: LET CHOOSE BETWEEN CLI AND GUI
    }

    public void notify(String message) throws RemoteException {
        try {
            this.handler.send(message);
        }catch (NullPointerException e){
            //The cli or the GUI is not active yet
            System.out.println(message);
        }

    }

    public String getName() throws RemoteException{
        return this.name;
    }

    public String getInput() throws RemoteException{
        return this.handler.request();
    }

    public void confirmConnection() throws RemoteException{
        //used to ping and check connection
    }

    public void startInterface() throws RemoteException{
        this.handler.startInterface();
    }
}

