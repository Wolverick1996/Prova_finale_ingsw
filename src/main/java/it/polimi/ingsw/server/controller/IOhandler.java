package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.network_client.ClientIntRMI;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Scheme;
import it.polimi.ingsw.server.model.Table;
import it.polimi.ingsw.server.network_server.ServerIntRMI;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

public class IOhandler implements Observer{

    //***************************//
    //        Attributes         //
    //***************************//

    private List<ClientIntRMI> usersRMI = new ArrayList<>();
    private List<SocketUser> socketUserList = new ArrayList<>(); //this is the list of socket BY socketUser->name
    private List<Player> players;
    private Observable ov = null;
    private Table table;
    private static final String STATUS = "STATUS";
    private static final String DIVISOR = "\n\n---------------------------------------------\n\n";

    IOhandler(List<Player> users, Table board){
        this.players = users;
        this.table = board;
    }

    //***************************//
    //         Methods           //
    //***************************//

    //*********SETUP***************

    void setServer(ServerIntRMI server) {
        try {
            usersRMI = server.getConnected();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    synchronized void setSockets(List<Socket> sockets){
        for(Socket s: sockets){
            String name = null;
            try {
                name = SocketMessengerServer.getToKnow(s);
            } catch (IOException e) {
                //TODO: DISCONNECTION/ERROR
                e.printStackTrace();
                sockets.remove(s);
                continue;
            }
            if (!name.equals(""))
                socketUserList.add(new SocketUser(name, s));
            else
                sockets.remove(s);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////
    private class SocketUser{
        //NEVER MODIFY SINGLE ATTRIBUTES OF THIS SUB CLASS
        private String name;
        private Socket socket;

        //To call only when USERNAME has been read by the SocketMessenger system
        SocketUser(String username, Socket s) {
            this.name = username;
            this.socket = s;
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////


    void broadcast(Object message){
        try {
            System.out.println(message);
            if (message.equals(STATUS)){
                System.out.println(DIVISOR);
                for (Player p: this.players) this.notify(p.getUsername(), DIVISOR);
                System.out.println(table);
                for (Player p: this.players) this.notify(p.getUsername(), table.toString());
                for (Player p: this.players){
                    System.out.println(p);
                    for (Player pl: this.players) this.notify(pl.getUsername(), p.toString());
                }
                System.out.println(DIVISOR);
                for (Player p: this.players) this.notify(p.getUsername(), DIVISOR);
            } else
                for (Player p: this.players) this.notify(p.getUsername(), message.toString());
        } catch (RemoteException e){
            System.err.println("ERROR BROADCAST: "+e.getMessage());
        }
    }

    String getStandardAction(String player){
        try {
            boolean send = false;
            String answer;
            while (!send){
                notify(player, "Insert action (d = place dice, t = use tool, q = pass turn)");
                answer = getInput(player).toLowerCase();

                if (answer.equals("d")){
                    return "d";
                } else if (answer.equals("t")){
                    return "t";
                } else if (answer.equals("q")){
                    return "q";
                } else {
                    notify(player, "Invalid input!");
                }
            }
        } catch (IllegalArgumentException e){
            try {
                notify(player, "What did you write, sorry?");
            } catch (RemoteException e1){
                e1.printStackTrace();
            }
            return getStandardAction(player);
        } catch (RemoteException e){
            System.err.println("GETSTDACTION ERROR: " +e.getMessage());
        }
        return null;
    }

    public int getDiceFromReserve(String player){
        int answer;
        boolean check = true;
        do {
            try {
                if (!check)
                    notify(player, "Try again: ");

                notify(player, "Insert the place of the dice in the reserve");
                notify(player, table.printReserve());
                answer = Integer.parseInt(getInput(player));
                return answer-1;
            } catch (IllegalArgumentException i){
                try {
                    notify(player, "You don't know what you just said");
                } catch (RemoteException e){
                    e.printStackTrace();
                }
            } catch (RemoteException e){
                e.printStackTrace();
            }
            check = false;
        } while (!check);
        return -1;
    }

    public int getDiceFromRoundtrack(String player){
        int answer;
        try {
            notify(player,"Choose a dice from round track [from 1 to N]");
            notify(player, table.printRoundtrack());
            answer = Integer.parseInt(getInput(player));
            return answer-1;
        } catch (IllegalArgumentException e){
            try {
                notify(player,"I do not understand. Am I the only one here?");
            } catch (RemoteException e1){
                e1.printStackTrace();
            }
        } catch (RemoteException e){
            System.err.println("GETDICE: " + e.getMessage());
        }
        return -1;
    }

    public int getCoordinate(String player){
        int answer;
        try {
            answer = Integer.parseInt(getInput(player));
            return answer-1;
        } catch (IllegalArgumentException i){
            try {
                notify(player, "What did you mean? I have no idea");
            } catch (RemoteException e){
                e.printStackTrace();
            }
        } catch (RemoteException e){
            System.err.println("GETCOORDINATE: " +e.getMessage());
        }
        return -1;
    }

    int chooseScheme(int s1, int s2, int s3, int s4, String player){
        int answer = -1;
        boolean isValid = false;

        ArrayList<Integer> schemes = new ArrayList<>();
        schemes.add(s1);
        schemes.add(s2);
        schemes.add(s3);
        schemes.add(s4);
        try {
            for (Integer i:schemes){
                notify(player, "Scheme " + (schemes.indexOf(i)+1));
                notify(player, Scheme.initialize(i).toString());
            }

            while (!isValid){
                notify(player, "Pick a scheme (write 1, 2, 3 or 4)");
                answer = Integer.parseInt(getInput(player));

                if (answer == 1 || answer == 2 || answer == 3 || answer == 4){
                    isValid = true;
                } else {
                    notify(player, "Not a scheme!");
                }
            }
            return schemes.get(answer-1);
        } catch (NumberFormatException n){
            try {
                notify(player, "In my town, we do not call that a number");
            } catch (RemoteException e){
                e.printStackTrace();
            }
        } catch (IllegalArgumentException i){
            try {
                notify(player, "What is that character? Just write a number, is it that hard?");
            } catch (RemoteException e){
                e.printStackTrace();
            }
        } catch (RemoteException e){
            System.err.println("CHOOSESCHEME: " +e.getMessage());
        }
        return chooseScheme(s1, s2, s3, s4, player);
    }

    int getTool(String player){
        int answer;
        try {
            notify(player,"\nChoose a tool card [1, 2, 3] or type '0' if you want to go back");
            answer = Integer.parseInt(getInput(player));
            return answer-1;
        } catch (NumberFormatException n){
            try {
                notify(player, "I may be a computer, but I don't think that is a number");
            } catch (RemoteException e){
                e.printStackTrace();
            }
        } catch (IllegalArgumentException i){
            try {
                notify(player, "I DON'T UNDERSTAAAAND");
            } catch (RemoteException e){
                e.printStackTrace();
            }
        } catch (RemoteException e){
            System.err.println("GETTOOL: " + e.getMessage());
        }
        return -1;
    }

    public int chooseDiceValue(String player, boolean restricted){
        int answer;
        try {
            if (restricted)
                notify(player,"Increment or decrement the value typing '+1' or '-1'");
            else
                notify(player,"Insert the new value [1-6]");
            answer = Integer.parseInt(getInput(player));
            return answer;
        } catch (NumberFormatException n){
            try {
                notify(player, "That is no number, not even in Wakanda");
            } catch (RemoteException e){
                e.printStackTrace();
            }
        } catch (IllegalArgumentException i){
            try {
                notify(player, "Sorry, what?");
            } catch (RemoteException e){
                e.printStackTrace();
            }
        } catch (RemoteException e){
            System.err.println("CHOOSEDICEVALUE: " + e.getMessage());
        }
        return -1;
    }

    public synchronized void notify(String player, String message) throws RemoteException {
        for (ClientIntRMI r: usersRMI){
            if (r.getName().equals(player)){
                r.notify(message);
                return;
            }
        }
        for (SocketUser u: socketUserList){
            if (u.name.equals(player)){
                try {
                    SocketMessengerServer.write(u.socket, message);
                    return;
                } catch (IOException e) {
                    //TODO: SOMETHING WRONG
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public boolean yesOrNo(String player) throws RemoteException{
        boolean check = true;
        String answer;
        do {
            if (!check)
                notify(player,"I don't know what you mean");

            try {
                notify(player, "Insert y/n");
                answer = getInput(player).toLowerCase();
                if (answer.equals("y")){
                    return true;
                } else if (answer.equals("n")){
                    return false;
                }
            } catch (IllegalArgumentException e){
                notify(player, "Strange thing you wrote there...");
            }
            check = false;
        } while (!check);
        return false;
    }

    private synchronized String getInput(String player) throws RemoteException {
        for (ClientIntRMI r: usersRMI){
            if (r.getName().equals(player)){
                return r.getInput();
            }
        }
        for (SocketUser u: socketUserList){
            if (u.name.equals(player)){
                try {
                    return SocketMessengerServer.get(u.socket);
                } catch (IOException e) {
                    //TODO: SOMETHING WRONG
                    e.printStackTrace();
                    return "";
                }
            }
        }
        System.err.println("COULD NOT FIND PLAYER FOR THE INPUT");
        return "INVALID";
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == ov)
            System.out.println(ov);
    }
}