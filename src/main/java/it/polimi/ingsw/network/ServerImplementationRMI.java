package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Lobby;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerImplementationRMI extends UnicastRemoteObject implements
        ServerIntRMI{

    private ArrayList<ClientIntRMI> clients = new ArrayList<>();
    private Lobby lobby;

    ServerImplementationRMI(Lobby lobby) throws RemoteException {
        super(0);
        this.lobby = lobby;
    }

    public boolean login(ClientIntRMI a) throws RemoteException{

        if (lobby.getPlayers().size() >= Lobby.MAX_PLAYERS){
            System.out.println("Max number of players reached!");
            a.notify("The lobby is full!");
            return false;
        }

        if (!lobby.addPlayer(a.getName()))
            return false;
        else {
            clients.add(a);
            System.out.println("[RMI Server]\t" +a.getName()+ "  got connected....");
            a.notify("Welcome " +a.getName()+ ".\nYou have connected successfully.");
            send(a.getName()+ " has just connected.");
            send("[Players in the lobby: "+lobby.getPlayers().size()+"]");
            return true;
        }
    }

    public void logout(ClientIntRMI a) throws RemoteException{
        System.out.println("[RMI Server]\t" +a.getName()+ "  disconnected....");
        lobby.removePlayer(a.getName());
        clients.remove(a);
        a.notify("You have disconnected successfully");
        send(a.getName()+" has just disconnected.\t[Players in the lobby: "+lobby.getPlayers().size()+"]");
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

    public int playersInLobby() {
        return this.lobby.getPlayers().size();
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
