package it.polimi.ingsw.server.network_server;

import it.polimi.ingsw.client.network_client.ClientIntRMI;
import it.polimi.ingsw.server.controller.Lobby;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerImplementationRMI extends UnicastRemoteObject implements
        ServerIntRMI{

    private List<ClientIntRMI> clients = new ArrayList<>();
    private List<String> usernames = new ArrayList<>();
    private Lobby lobby;

    ServerImplementationRMI(Lobby lobby) throws RemoteException {
        super(0);
        this.lobby = lobby;
        this.lobby.setServerRMI(this);
    }

    public boolean login(ClientIntRMI a) throws RemoteException{

        if (lobby.hasStarted()){
            if (lobby.addPlayer(a.getName())){
                clients.add(a);
                usernames.add(a.getName());
                System.out.println("[RMI Server]\t" +a.getName()+ "  got connected again....");
                a.notify("Welcome back " +a.getName()+ ".\nYou have connected successfully.");
                lobby.rejoinedMatch(a.getName(), true);
                return true;
            }else{
                a.notify("The game started without you :(\n\nGet better friends dude");
                return false;
            }
        }

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

    public void confirmConnections() throws RemoteException{

        Iterator<ClientIntRMI> clientIterator = clients.iterator();
        ArrayList<ClientIntRMI> toBeDeleted = new ArrayList<>();
        ClientIntRMI c = null;
        int i = 0;
        while(clientIterator.hasNext()){
            String s = usernames.get(i);
            try{
                c = clientIterator.next();
                c.confirmConnection();
            }catch(ConnectException e) {
                toBeDeleted.add(c);
                lobby.removePlayer(s);
                System.out.println("[RMI Server]\t" +s+ "  disconnected....");
            }
            i++;
        }
        if (!toBeDeleted.isEmpty()){
            ArrayList<Integer> indexToRemove = new ArrayList<>();
            for (ClientIntRMI clientIntRMI : toBeDeleted){
                for (ClientIntRMI usr : this.clients){
                    if (clientIntRMI ==  usr){
                        indexToRemove.add(this.clients.indexOf(usr));
                    }
                }
            }
            for (Integer n : indexToRemove){
                this.clients.remove(n.intValue());
                this.usernames.remove(n.intValue());
            }
        }
    }

    public List<ClientIntRMI> getConnected(){
        return this.clients;
    }

    public int playersInLobby(){
        return this.lobby.getPlayers().size();
    }

    public boolean hasStarted() {
        return this.lobby.hasStarted();
    }

    public void setDelay(int delay){
        this.lobby.setDelay(delay);
    }

    /**
     * Removes a target skeleton from the list of clients and a target username from the list of usernames
     *
     * @param c: ClientIntRMI to be removed
     * @param username: String to be removed
     */
    public void removeClientAndUsername(ClientIntRMI c, String username) {
        this.clients.remove(c);
        this.usernames.remove(username);
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
