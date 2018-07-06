package it.polimi.ingsw.server.network_server;

import it.polimi.ingsw.client.network_client.ClientIntRMI;
import it.polimi.ingsw.server.controller.Lobby;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Implementation of the ServerIntRMI interface
 *
 * @author Andrea
 */
public class ServerImplementationRMI extends UnicastRemoteObject implements ServerIntRMI {

    private List<ClientIntRMI> clients = new ArrayList<>();
    private List<String> usernames = new ArrayList<>();
    private Lobby lobby;

    /**
     * Constructor of the ServerImplementationRMI object
     *
     * @param lobby: the lobby containing the active players
     * @throws RemoteException if connection fails
     * @author Andrea
     */
    ServerImplementationRMI(Lobby lobby) throws RemoteException {
        super(0);
        this.lobby = lobby;
        this.lobby.setServerRMI(this);
    }

    /**
     * Allows connection to the RMI server
     *
     * @param a: skeleton related to the specific client
     * @return true if login is successful, otherwise false
     * @throws RemoteException if connection to the skeleton fails
     * @author Andrea
     */
    public boolean login(ClientIntRMI a) throws RemoteException {
        if (lobby.hasStarted()){
            if (lobby.addPlayer(a.getName())){
                clients.add(a);
                usernames.add(a.getName());
                System.out.println("[RMI Server]\t" +a.getName()+ "  got connected again....");
                a.notify("Welcome back " +a.getName()+ ".\nYou have connected successfully.");
                lobby.rejoinedMatch(a.getName(), true);
                return true;
            } else {
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

    //TODO: JavaDoc
    public void confirmConnections() throws RemoteException {
        Iterator<ClientIntRMI> clientIterator = clients.iterator();
        ArrayList<ClientIntRMI> toBeDeleted = new ArrayList<>();
        ClientIntRMI c = null;
        int i = 0;
        while (clientIterator.hasNext()){
            String s = usernames.get(i);
            try {
                c = clientIterator.next();
                c.confirmConnection();
            } catch(ConnectException e) {
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
            for (Integer n : indexToRemove) {
                clients.remove(n.intValue());
                usernames.remove(n.intValue());
            }
        }
    }

    /**
     * Returns the list of skeleton saved
     *
     * @return the list of skeleton
     * @author Andrea
     */
    public List<ClientIntRMI> getConnected(){
        return this.clients;
    }

    /**
     * Returns the number of players in the lobby
     *
     * @return the number of players in the lobby
     * @author Andrea
     */
    public int playersInLobby(){
        return this.lobby.getPlayers().size();
    }

    /**
     * Communicates if the match started or not
     *
     * @return true if the match started, otherwise false
     * @author Andrea
     */
    public boolean hasStarted(){
        return this.lobby.hasStarted();
    }

    /**
     * Sets the delay of the lobby
     *
     * @param delay: number of seconds
     * @author Andrea
     */
    public void setDelay(int delay){
        this.lobby.setDelay(delay);
    }

    /**
     * Removes a target skeleton from the list of clients and a target username from the list of usernames
     *
     * @param c: ClientIntRMI to be removed
     * @param username: username string to be removed
     * @author Andrea
     */
    public void removeClientAndUsername(ClientIntRMI c, String username) {
        this.clients.remove(c);
        this.usernames.remove(username);
    }

    @Override
    public boolean equals(Object obj){
        return super.equals(obj);
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }

}