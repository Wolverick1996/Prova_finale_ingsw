package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.network_client.ClientIntRMI;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.PrivObjHandler;
import it.polimi.ingsw.server.model.Scheme;
import it.polimi.ingsw.server.model.Table;
import it.polimi.ingsw.server.network_server.ServerIntRMI;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Manages communications between the controller and all clients
 *
 * @author Matteo
 */
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

    /**
     * IOhandler constructor: combines the list of players of the game and the game table
     *
     * @param users: list of players
     * @param board: game table
     * @author Matteo
     */
    IOhandler (List<Player> users, Table board){
        this.players = users;
        this.table = board;
    }

    //***************************//
    //         Methods           //
    //***************************//

    //**********SETUP**********//

    /**
     * Extracts RMI users from RMI server
     *
     * @param server: RMI server
     * @author Matteo
     */
    void setServer(ServerIntRMI server){
        try {
            usersRMI = server.getConnected();
            for (ClientIntRMI c : usersRMI)
                c.startIterface();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    /**
     * Matches every socket received from the constructor with the correct username
     *
     * @param sockets: list of sockets
     * @author Matteo
     */
    synchronized void setSockets(List<Socket> sockets){
        for(Socket s: sockets){
            String name;
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

    /**
     * Subclass which allows to combine all sockets with usernames
     *
     * @author Matteo
     */
    private class SocketUser {
        //NEVER MODIFY SINGLE ATTRIBUTES OF THIS SUB CLASS
        private String name;
        private Socket socket;

        /**
         * Constructor of SocketUser objects
         * NOTE: To call only when USERNAME has been read by the SocketMessenger system
         *
         * @param username: socket's username
         * @param s: socket with which IOhandler should communicates
         */
        SocketUser (String username, Socket s){
            this.name = username;
            this.socket = s;
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Sends a specific message to notify client (Socket) the game has ended
     *
     * @author Andrea
     */
    void finishGameSocket(){
        for (SocketUser s : this.socketUserList){
            try {
                SocketMessengerServer.sendFinish(s.socket);
                s.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends notifies to all clients combined with IOhandler
     *
     * @param message: the message to be printed to all users
     * @author Matteo
     */
    void broadcast(Object message){
        try {
            System.out.println(message);
            if (message.equals(STATUS)){
                System.out.println(DIVISOR);
                for (Player p: this.players) this.notify(p.getUsername(), DIVISOR);
                System.out.println(table);
                for (Player p: this.players){
                    this.notify(p.getUsername(), table.toString());
                    this.notify(p.getUsername(), PrivObjHandler.getCard(p));
                }
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

    /**
     * Requires a specific input for the standard turn phase (input accepted: "d", "t", "q")
     *
     * @param player: username of the player who is playing the current turn
     * @return the action chosen
     * @author Matteo
     */
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

    /**
     * Asks the player to choose a dice from the reserve
     *
     * @param player: the player who has to choose the dice
     * @return dice position in the reserve (-1 if extraction failed)
     * @author Matteo
     */
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

    /**
     * Asks the player to choose a dice from the round track
     *
     * @param player: the player who has to choose the dice
     * @return dice position in the round track (-1 if extraction failed)
     * @author Matteo
     */
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

    /**
     * Asks the player to choose a coordinate
     *
     * @param player: the player who has to choose the dice
     * @return the coordinate chosen (-1 if it not exists)
     * @author Matteo
     */
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

    /**
     * Asks the player to choose a window pattern between the four purposed before the game starting
     *
     * @param s1: first window pattern purposed
     * @param s2: second window pattern purposed
     * @param s3: third window pattern purposed
     * @param s4: fourth window pattern purposed
     * @param player: the player who has to choose the window pattern
     * @return the window pattern chosen
     * @author Matteo
     */
    int chooseScheme(int s1, int s2, int s3, int s4, String player){
        int answer = -1;
        boolean isValid = false;
        ArrayList<Integer> schemes = new ArrayList<>();
        schemes.add(s1);
        schemes.add(s2);
        schemes.add(s3);
        schemes.add(s4);
        try {
            notify(player, "CHOOSE A SCHEME :");
            for (Integer i:schemes){
                notify(player, "Scheme " + (schemes.indexOf(i)+1));
                notify(player, Scheme.initialize(i, this.table.getCustom(), this.table.getNumSchemes()).toString());
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

    /**
     * Asks the player to choose a tool card to be used
     *
     * @param player: the player who has to choose the tool card
     * @return the index position of the tool card to be used
     * @author Matteo
     */
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

    /**
     * Asks the player to choose a dice value (useful for tool cards 1 and 11)
     *
     * @param player: the player who has to choose the dice value
     * @param restricted: boolean flag useful to understand which action should be performed (true = tool 1; false = tool 11)
     * @return the dice value chosen (-1 if values are not allowed)
     * @author Matteo
     */
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

    /**
     * Sends the message to be printed to a specific player
     *
     * @param player: the player whom message needs to be sent to
     * @param message: the message to be printed
     * @throws RemoteException when client RMI disconnected
     * @author Matteo
     */
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
                } catch (IOException e){
                    //TODO: SOMETHING WRONG
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /**
     * Asks a boolean decision to the player (just "y" or "n" are allowed)
     *
     * @param player: the player who has to choose
     * @return a boolean value (true for yes, false for no)
     * @throws RemoteException when client RMI disconnected
     * @author Matteo
     */
    public boolean yesOrNo(String player) throws RemoteException{
        boolean check = true;
        String answer;
        do {
            if (!check)
                notify(player,"I don't know what you mean");

            try {
                notify(player, "Insert y/n");
                answer = getInput(player).toLowerCase();
                System.out.println("I read in YES or NO: " + answer);
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

    /**
     * Asks for a generic input from a specific player
     *
     * @param player: the player who has to insert an input
     * @return the inserted input
     * @throws RemoteException when client RMI disconnected
     * @author Matteo
     */
    private synchronized String getInput(String player) throws RemoteException{

        for (ClientIntRMI r: usersRMI){
            if (r.getName().equals(player)){
                return r.getInput();
            }
        }
        for (SocketUser u: socketUserList){
            if (u.name.equals(player)){
                try {
                    return SocketMessengerServer.get(u.socket);
                } catch (IOException e){
                    //TODO: SOMETHING WRONG
                    e.printStackTrace();
                    return "";
                }
            }
        }
        System.err.println("COULD NOT FIND PLAYER FOR THE INPUT");
        return "INVALID";
    }

    //TODO: Realize JavaDoc comment when GUI will be implemented
    @Override
    public void update(Observable o, Object arg) {
        if (o == ov)
            System.out.println(ov);
    }

}