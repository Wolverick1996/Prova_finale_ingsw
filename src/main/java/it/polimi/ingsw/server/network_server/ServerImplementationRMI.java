package it.polimi.ingsw.server.network_server;

import it.polimi.ingsw.client.network_client.ClientIntRMI;
import it.polimi.ingsw.server.controller.Lobby;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerImplementationRMI extends UnicastRemoteObject implements
        ServerIntRMI{

    private ArrayList<ClientIntRMI> clients = new ArrayList<>();
    private ArrayList<String> usernames = new ArrayList<>();
    private Lobby lobby;

    ServerImplementationRMI(Lobby lobby) throws RemoteException {
        super(0);
        this.lobby = lobby;
        this.lobby.setServerRMI(this);
    }

    public boolean login(ClientIntRMI a) throws RemoteException{

        confirmConnections();

        if (lobby.getPlayers().size() >= Lobby.MAX_PLAYERS){
            System.out.println("Max number of players reached!");
            a.notify("The lobby is full!");
            return false;
        }

        if (!lobby.addPlayer(a.getName()))
            return false;
        else {
            clients.add(a);
            usernames.add(a.getName());
            System.out.println("[RMI Server]\t" +a.getName()+ "  got connected....");
            a.notify("Welcome " +a.getName()+ ".\nYou have connected successfully.");
            return true;
        }
    }

    public void logout(ClientIntRMI a) throws RemoteException{

        confirmConnections();

        System.out.println("[RMI Server]\t" +a.getName()+ "  disconnected....");
        lobby.removePlayer(a.getName());
        usernames.remove(a.getName());
        clients.remove(a);
        a.notify("You have disconnected successfully");
    }


    public void confirmConnections() throws RemoteException{
        Iterator<ClientIntRMI> clientIterator = clients.iterator();
        int i = 0;
        while(clientIterator.hasNext()){
            String s = usernames.get(i);
            try{
                clientIterator.next().confirmConnection();
            }catch(ConnectException e) {
                clientIterator.remove();
                lobby.removePlayer(s);
                System.out.println("[RMI Server]\t" +s+ "  disconnected....");
            }
            i++;
        }
    }

    public List<ClientIntRMI> getConnected() throws RemoteException{
        return clients;
    }

    public int playersInLobby() throws RemoteException{
        return this.lobby.getPlayers().size();
    }

    public void setDelay(int delay) throws RemoteException{
        this.lobby.setDelay(delay);
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
