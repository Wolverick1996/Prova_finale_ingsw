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
    /*private ArrayList<String> clientsName = new ArrayList<>();
    private ClientIntRMI disconnected = null;
    private String disconnectedClient = null;*/
    static final int MAX_PLAYERS = 4;

    ServerImplementationRMI() throws RemoteException {
        super(0);
    }

    /*public void run(){
        boolean gameStart = false;
        while (!gameStart){
            try {
                if (!checkConnection()){
                    System.out.println("User " +disconnectedClient+ "interrupted his connection!");
                    disconnectedClient = null;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkConnection (){

        if (!clients.isEmpty()) {
            Iterator<ClientIntRMI> clientIterator = clients.iterator();
            while(clientIterator.hasNext()){
                ClientIntRMI c = null;
                String s = null;
                try{
                    c = clientIterator.next();
                    s = c.getName();
                    c.confirmConnection();
                } catch(ConnectException e) {
                    clientIterator.remove();
                    System.out.println("Client removed!");
                } catch (RemoteException e) {
                    disconnectedClient = s;
                    clients.remove(c);
                    return false;
                }
            }
        }
        if (clients.isEmpty())
            return true;
        return false;
    }*/

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

        System.out.println(a.getName() + "  got connected....");
        a.notify("Welcome " +a.getName()+ ".\nYou have connected successfully.");
        send(a.getName()+ " has just connected.");
        clients.add(a);
        //clientsName.add(a.getName());
        send("[Players in the lobby: "+clients.size()+"]");
        return true;
    }

    public void logout(ClientIntRMI a) throws RemoteException{
        clients.remove(a);
        //clientsName.remove(a.getName());
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
