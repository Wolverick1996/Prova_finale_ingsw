package it.polimi.ingsw.network;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/*public class ServerMainRMI {
    public static final int PORT = 1234;

    public static void main( String[] args ){
        try{
        //System.setSecurityManager(new RMISecurityManager());
        ServerIntRMI b = new ServerRMI();
        Registry registry = LocateRegistry.createRegistry(PORT);

        registry.bind("Sagrada_project", b);
        System.out.println("[System] Server is ready.");
        }catch (Exception e) {
            System.out.println("Sagrada Server failed: " + e);
        }
    }
}

class ServerRMI extends UnicastRemoteObject implements ServerIntRMI{

    private Vector v = new Vector();
    public ServerRMI() throws RemoteException {}

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

public class ServerMainRMI {
    public static void main(String[] args) {
        try {
            //System.setSecurityManager(new RMISecurityManager());
            java.rmi.registry.LocateRegistry.createRegistry(1099);

            ServerIntRMI b = new ServerRMI();
            Naming.rebind("Sagrada", b);
            System.out.println("[System] Chat Server is ready.");
        }catch (Exception e) {
            System.out.println("Chat Server failed: " + e);
        }
    }
}

class ServerRMI extends UnicastRemoteObject implements ServerIntRMI{

    private Vector v = new Vector();
    public ServerRMI() throws RemoteException{}

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
                ClientIntRMI tmp=(ClientIntRMI)v.get(i);
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