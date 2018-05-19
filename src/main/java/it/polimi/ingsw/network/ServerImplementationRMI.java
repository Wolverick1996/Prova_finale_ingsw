package it.polimi.ingsw.network;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class ServerImplementationRMI extends UnicastRemoteObject implements
        ServerIntRMI {

    private ArrayList<ClientIntRMI> clients = new ArrayList<>();

    protected ServerImplementationRMI() throws RemoteException {
        super(0);
    }

    public boolean login(ClientIntRMI a) throws RemoteException{

        if (clients.size() > 0){
            for (ClientIntRMI c : clients){
                if (c.getName().equals(a.getName())){
                    System.out.println("Connection failed, userID already used");
                    a.notify("Connection failed, userID already used");
                    return false;
                }
            }
        }

        System.out.println(a.getName() + "  got connected....");
        a.notify("You have Connected successfully.");
        send(a.getName()+ " has just connected.");
        clients.add(a);
        return true;
    }

    /*public void login(ClientIntRMI client) throws RemoteException {
        clients.add(client);
        System.out.println("Client "+ (clients.indexOf(client)+1) + " connesso!");
    }*/

    public boolean logout(ClientIntRMI a) throws RemoteException{
        clients.remove(a);
        a.notify("You have disconnected successfully");
        send(a.getName()+"has just disconnected");
        return true;
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

    public ArrayList<ClientIntRMI> getClients() {
        return clients;
    }
}
/*public class ServerImplementationRMI extends UnicastRemoteObject implements ServerIntRMI{

    private Vector v = new Vector();
    public ServerImplementationRMI() throws RemoteException {}

    public boolean login(ClientIntRMI a) throws RemoteException{
        System.out.println(a.getName() + "  got connected....");
        a.tell("You have Connected successfully.");
        publish(a.getName()+ " has just connected.");
        v.add(a);
        return true;
    }

    public boolean logout(ClientIntRMI a) throws RemoteException{
        v.removeElement(a);
        a.tell("You have disconnected successfully");
        publish(a.getName()+"has just disconnected");
        return true;
    }

    public void publish(String s) throws RemoteException{
        System.out.println(s);
        for(int i=0;i<v.size();i++){
            try{
                ClientIntRMI tmp=(ClientIntRMI) v.get(i);
                tmp.tell(s);
            }catch(Exception e){
                //problem with the client not connected.
                //Better to remove it
            }
        }
    }

    public Vector getConnected() throws RemoteException{
        return v;
    }
}
*/
