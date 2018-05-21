package it.polimi.ingsw.network;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerImplementationRMI extends UnicastRemoteObject implements
        ServerIntRMI {

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
                if (c.getName().equals(a.getName())){
                    System.out.println("Connection failed, userID already used");
                    return false;
                }
            }
        }

        System.out.println(a.getName() + "  got connected....");
        a.notify("You have Connected successfully.");
        send(a.getName()+ " has just connected.\t[Players in the lobby: "+clients.size()+"]");
        clients.add(a);
        return true;
    }

    public void logout(ClientIntRMI a) throws RemoteException{
        clients.remove(a);
        a.notify("You have disconnected successfully");
        send(a.getName()+"has just disconnected.\t[Players in the lobby: "+clients.size()+"]");
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

    public List<ClientIntRMI> getConnected() {
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
