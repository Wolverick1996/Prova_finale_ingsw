package it.polimi.ingsw.network;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerImplementationRMI extends UnicastRemoteObject implements
        ServerIntRMI{

    private ArrayList<ClientIntRMI> clients = new ArrayList<>();
    static final int MAX_PLAYERS = 4;

    ServerImplementationRMI() throws RemoteException {
        super(0);
    }

    public boolean login(ClientIntRMI a) throws RemoteException{

        if (clients.size() >= 4){
            System.out.println("Max number of players reached!");
            a.notify("The lobby is full!");
            return false;
        }

        if (!clients.isEmpty()){
            for (ClientIntRMI c : clients){
                if (c.getName().equals(a.getName()))
                    return false;
            }
        }

        System.out.println("[RMI Server]\t" +a.getName()+ "  got connected....");
        a.notify("Welcome " +a.getName()+ ".\nYou have connected successfully.");
        send(a.getName()+ " has just connected.");
        clients.add(a);
        send("[Players in the lobby: "+clients.size()+"]");
        return true;
    }

    public void logout(ClientIntRMI a) throws RemoteException{
        System.out.println("[RMI Server]\t" +a.getName()+ "  disconnected....");
        clients.remove(a);
        a.notify("You have disconnected successfully");
        send(a.getName()+" has just disconnected.\t[Players in the lobby: "+clients.size()+"]");
    }

    public void send(String message) throws RemoteException {
        Iterator<ClientIntRMI> clientIterator = clients.iterator();
        while(clientIterator.hasNext()){
            try{
                clientIterator.next().notify(message);
            }catch(ConnectException e) {
                clientIterator.remove();
                System.out.println("Client rimosso!");
            }
        }
    }

    public List<ClientIntRMI> getConnected() throws RemoteException{
        return clients;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
