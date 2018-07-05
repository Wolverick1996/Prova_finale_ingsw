package it.polimi.ingsw.server.network_server;

import it.polimi.ingsw.client.network_client.ClientIntRMI;
import it.polimi.ingsw.server.controller.Lobby;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

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

    public synchronized boolean login(ClientIntRMI a) throws RemoteException{

        if(!lobby.hasStarted())
            confirmConnections();

        if (lobby.getPlayers().size() >= Lobby.MAX_PLAYERS){
            System.out.println("Max number of players reached!");
            a.notify("The lobby is full!");
            return false;
        }

        if (lobby.hasStarted()){
            if (lobby.addPlayer(a.getName())){
                clients.add(a);
                usernames.add(a.getName());
                System.out.println("[RMI Server]\t" +a.getName()+ "  got connected again....");
                a.notify("Welcome back " +a.getName()+ ".\nYou have connected successfully.");
                lobby.rejoinedMatch(a.getName());
                return true;
            }else{
                a.notify("The game started without you :(\n\nGet better friends dude");
                return false;
            }
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

    public synchronized void confirmConnections() throws RemoteException{

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

        ArrayList<Integer> indexToRemove = new ArrayList<>();
        for (ClientIntRMI clientIntRMI : toBeDeleted){
            for (ClientIntRMI usr : this.clients){
                if (clientIntRMI ==  usr){
                    indexToRemove.add(this.clients.indexOf(usr));
                }
            }
        }

        for (Integer n : indexToRemove){
            clients.remove(n.intValue());
            usernames.remove(n.intValue());
        }
    }

    public void notify(String username, String message){
        try{
            for (ClientIntRMI c : clients){
                if(c.getName().equals(username)){
                    c.notify(message);
                }
            }
        } catch (RemoteException e){
            System.err.println("NOTIFY FAILED");
        } catch (NullPointerException e){
            System.err.println("CLIENTRMI DOES NOT EXIST");
        }
    }

    public void broadcast(String message) throws RemoteException{
        for(ClientIntRMI user:clients){
            user.notify(message);
        }
    }

    public String getInput(String username) throws RemoteException{
        String s = null;
        for (ClientIntRMI c : clients){
            if(c.getName().equals(username)){
                s = c.getInput();
            }
        }
        return s;
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

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
