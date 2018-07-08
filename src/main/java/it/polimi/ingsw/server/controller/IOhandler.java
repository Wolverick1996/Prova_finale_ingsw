package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.network_client.ClientIntRMI;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Enum;
import it.polimi.ingsw.server.network_server.ServerImplementationRMI;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Manages communications between the controller and all clients
 *
 * @author Matteo
 */
public class IOhandler{

    //***************************//
    //        Attributes         //
    //***************************//

    private List<ClientIntRMI> usersRMI = new ArrayList<>();
    private List<DisconnectedPlayers> disconnectedRMI = new ArrayList<>();
    private ServerImplementationRMI server;
    private List<SocketUser> socketUserList = new ArrayList<>(); //this is the list of socket BY socketUser->name
    private List<DisconnectedPlayers> disconnectedSocket = new ArrayList<>();
    private List<Player> players;
    private Table table;
    private static final String DISCONNECTION = "disconnection";
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
    void setServer(ServerImplementationRMI server){
        try {
            this.server = server;
            this.usersRMI = server.getConnected();
            for (ClientIntRMI c : usersRMI)
                c.startInterface();
        } catch (RemoteException e){
            //Strange behave of the skeleton
        }
    }

    /**
     * Matches every socket received from the constructor with the correct username
     *
     * @param sockets: list of sockets
     * @author Matteo
     */
    synchronized void setSockets(List<Socket> sockets){
        for (Socket s: sockets){
            String name;
            name = SocketMessengerServer.getToKnow(s);
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
     * Method used by Lobby to re-add a client to the list of users (RMI or socket)
     *
     * @param username: player's username
     * @param o: object useful for socket re-connection (it will be the actual reference to that client's socket), useless for RMI
     * @author Andrea
     */
    void rejoinMatch(String username, Object o){
        boolean wasRmi = false;
        for (DisconnectedPlayers d : disconnectedRMI)
            if (d.name.equals(username))
                wasRmi = true;
        if (wasRmi)
            reconnectRmi(username);
        else
            reconnectSocket(username, o);
    }

    /**
     * Re-adds the player to the list of usersRMI in the same position he was before he got disconnected
     *
     * @param username: name of the RMI user who wants to re-connect
     * @author Andrea
     */
    private void reconnectRmi(String username){
        ClientIntRMI uRMI = null;
        try {
            for (ClientIntRMI c : server.getConnected()){
                if (c.getName().equals(username))
                    uRMI = c;
            }
        } catch (RemoteException e){
            System.out.println("RECONNECTRMI ERROR!");
        }
        if (uRMI != null){
            int index = -1;
            for (DisconnectedPlayers d : disconnectedRMI){
                if (d.name.equals(username))
                    index = d.index;
            }

            ArrayList<ClientIntRMI> fooCopy = new ArrayList<>(usersRMI);
            this.usersRMI.clear();
            boolean setted = false;
            for (int i = 0; i < fooCopy.size() + 1 ; i++){
                if (!setted){
                    if (i == index){
                        this.usersRMI.add(uRMI);
                        setted = true;
                    } else
                        this.usersRMI.add(fooCopy.get(i));
                } else
                    this.usersRMI.add(fooCopy.get(i-1));
            }
            try {
                DisconnectedPlayers d = null;
                for (DisconnectedPlayers dP : disconnectedRMI){
                    if (dP.name.equals(username))
                        d = dP;
                }
                if (d != null)
                    disconnectedRMI.remove(d);
                uRMI.startInterface();
            } catch (RemoteException e){
                System.out.println("RECONNECTRMI ERROR (STARTINTERFACE)");
            }
        }
    }

    /**
     * Re-adds the player to the list of socketUsers in the same position he was before he got disconnected
     *
     * @param username: name of the socket user who wants to re-connect
     * @param o: Socket of the user who wants to re-connect
     * @author Andrea
     */
    private void reconnectSocket(String username, Object o){
        Socket socketToReJoin;
        try {
            socketToReJoin = (Socket) o;
        } catch (ClassCastException c){
            System.err.println("ERROR IN CASTING!! Obj cannot be cast into a Socket");
            return;
        }
        if (socketToReJoin != null){
            int index = -1;
            for (DisconnectedPlayers d : disconnectedSocket){
                if (d.name.equals(username))
                    index = d.index;
            }

            SocketUser userToRejoin = new SocketUser(username, socketToReJoin);

            ArrayList<SocketUser> fooCopy = new ArrayList<>(socketUserList);
            this.socketUserList.clear();
            boolean setted = false;
            for (int i = 0; i < fooCopy.size() + 1; i++){
                if (!setted){
                    if (i == index){
                        this.socketUserList.add(userToRejoin);
                        setted = true;
                    } else
                        this.socketUserList.add(fooCopy.get(i));
                } else
                    this.socketUserList.add(fooCopy.get(i - 1));
            }
            DisconnectedPlayers d = null;
            for (DisconnectedPlayers dP : disconnectedSocket){
                if (dP.name.equals(username))
                    d = dP;
            }
            if (d != null)
                disconnectedSocket.remove(d);
        }
    }

    /**
     * Sends notifies to all clients combined with IOhandler
     *
     * @param message: the message to be printed to all users
     * @author Matteo
     */
    void broadcast(Object message){
        System.out.println(message);
        if (message.equals(STATUS)){
            System.out.println(DIVISOR);
            for (Player p: getListOfActive()) this.notify(p.getUsername(), DIVISOR);
            for (Player p: getListOfActive()) this.notify(p.getUsername(), "Here is the status: ");
            System.out.println(table);
            for (Player p: getListOfActive()){
                this.notify(p.getUsername(), table.toString());
                String tools = "\nTool Cards on table:\n";
                for (int i = 0; i<3; i++) {
                    tools = tools + Enum.Color.YELLOW.escape() + ToolHandler.getName(i) + "\n" + Enum.Color.RESET +
                            ToolHandler.getDescription(i) + Enum.Color.YELLOW.escape() + "\n Tokens on: " +
                            Enum.Color.RESET + ToolHandler.getTokens(i) + "\n";
                }
                this.notify(p.getUsername(), tools);
                this.notify(p.getUsername(), PrivObjHandler.getCard(p));
            }
            for (Player p: getListOfActive()){
                System.out.println(p);
                for (Player pl: getListOfActive()) this.notify(pl.getUsername(), p.toString());
            }
            System.out.println(DIVISOR);
            for (Player p: getListOfActive()) this.notify(p.getUsername(), DIVISOR);
        } else
            for (Player p: getListOfActive()) this.notify(p.getUsername(), message.toString());
    }

    /**
     * Returns the list of players not disconnected
     *
     * @return the list of the active players
     * @author Andrea
     */
    private List<Player> getListOfActive(){
        ArrayList<Player> actives = new ArrayList<>();
        for (Player p : players){
            if (!p.isDisconnected())
                actives.add(p);
        }
        return actives;
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
                } else if (answer.equals("q") || answer.equals(DISCONNECTION)){
                    return "q";
                } else {
                    notify(player, "Invalid input!");
                }
            }
        } catch (IllegalArgumentException e){
            notify(player, "What did you write, sorry?");
            return getStandardAction(player);
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
                String answerString = getInput(player);
                //if disconnected return to the previous status asking for an action(d,t,q)
                if (answerString.equals(DISCONNECTION))
                    return -1;
                answer = Integer.parseInt(answerString);
                return answer-1;
            } catch (IllegalArgumentException i){
                notify(player, "You don't know what you just said");
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
            String answerString = getInput(player);
            if (answerString.equals(DISCONNECTION))
                return -1;
            answer = Integer.parseInt(answerString);
            return answer-1;
        } catch (IllegalArgumentException e){
            notify(player,"I do not understand. Am I the only one here?");
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
            String answerString = getInput(player);
            if (answerString.equals(DISCONNECTION))
                return -1;
            answer = Integer.parseInt(answerString);
            return answer-1;
        } catch (IllegalArgumentException i){
            notify(player, "What did you mean? I have no idea");
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
                String answerString = getInput(player);
                if (answerString.equals(DISCONNECTION))
                    return 1;
                answer = Integer.parseInt(answerString);

                if (answer == 1 || answer == 2 || answer == 3 || answer == 4){
                    isValid = true;
                } else {
                    notify(player, "Not a scheme!");
                }
            }
            return schemes.get(answer-1);
        } catch (NumberFormatException n){
            notify(player, "In my town, we do not call that a number");
        } catch (IllegalArgumentException i){
            notify(player, "What is that character? Just write a number, is it that hard?");
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
            String answerString = getInput(player);
            if (answerString.equals(DISCONNECTION))
                return -1;
            answer = Integer.parseInt(answerString);
            return answer-1;
        } catch (NumberFormatException n){
            notify(player, "I may be a computer, but I don't think that is a number");
        } catch (IllegalArgumentException i){
            notify(player, "I DON'T UNDERSTAAAAND");
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
            String answerString = getInput(player);
            if (answerString.equals(DISCONNECTION))
                return -1;
            answer = Integer.parseInt(answerString);
            return answer;
        } catch (NumberFormatException n){
            notify(player, "That is no number, not even in Wakanda");
        } catch (IllegalArgumentException i){
            notify(player, "Sorry, what?");
        }
        return -1;
    }

    /**
     * Sends the STATUS message to a single player
     *
     * @param player: the player whom message needs to be sent to
     * @author Matteo
     */
    private synchronized void notifyStatus(String player) {
        this.notify(player, DIVISOR);
        this.notify(player, "Here is the status: ");
        this.notify(player, table.toString());
        String tools = "\nTool Cards on table:\n";
        for (int i = 0; i<3; i++) {
            tools = tools + Enum.Color.YELLOW.escape() + ToolHandler.getName(i) + "\n" + Enum.Color.RESET +
                    ToolHandler.getDescription(i) + Enum.Color.YELLOW.escape() + "\n Tokens on: " +
                    Enum.Color.RESET + ToolHandler.getTokens(i) + "\n";
        }
        this.notify(player, tools);
        this.notify(player, PrivObjHandler.getCard(Controller.getMyGame(this).getPlayer(player)));
        for (Player p: getListOfActive()){
            this.notify(player, p.toString());
        }
        this.notify(player, DIVISOR);
    }

    /**
     * Sends the message to be printed to a specific player
     *
     * @param player: the player whom message needs to be sent to
     * @param message: the message to be printed
     * @author Matteo
     */
    public synchronized void notify(String player, String message) {

        if (message.equals(STATUS)) {
            notifyStatus(player);
            return;
        }

        int index = -1;
        int oneToBeDelete = -1;
        for (int i = 0; i < usersRMI.size(); i++){
            try {
                if (usersRMI.get(i).getName().equals(player)){
                    usersRMI.get(i).notify(message);
                    return;
                }
            } catch (RemoteException e){
                oneToBeDelete = i;
                index = disconnectPlayer(i);
            }
        }

        if (oneToBeDelete != -1)
            disconnectedRMI.add(new DisconnectedPlayers(oneToBeDelete, players.get(index).getUsername()));

        oneToBeDelete = -1;
        for (SocketUser u: socketUserList){
            if (u.name.equals(player)){
                try {
                    SocketMessengerServer.write(u.socket, message);
                    return;
                } catch (IOException e){
                    oneToBeDelete = socketUserList.indexOf(u);
                    index = findDisconnectedSocket(u.name);
                    players.get(index).setDisconnected(true);
                }
            }
        }
        if (oneToBeDelete != -1){
            disconnectedSocket.add(new DisconnectedPlayers(oneToBeDelete, players.get(index).getUsername()));
            socketUserList.remove(oneToBeDelete);
            System.out.println("[Socket Server]\t" + players.get(index).getUsername() + "  disconnected....");
        }
    }

    /**
     * Asks a boolean decision to the player (just "y" or "n" are allowed)
     *
     * @param player: the player who has to choose
     * @return a boolean value (true for yes, false for no)
     * @author Matteo
     */
    public boolean yesOrNo(String player){
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
                } else if (answer.equals(DISCONNECTION)){
                    broadcast("First player got disconnected!");
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
     * @author Matteo
     */
    private synchronized String getInput(String player){
        int oneToBeDelete = -1;
        int index = -1;
        for (int i = 0; i < usersRMI.size(); i++){
            try {
                if (usersRMI.get(i).getName().equals(player))
                    return usersRMI.get(i).getInput();
            } catch (RemoteException e){
                oneToBeDelete = i;
                index = disconnectPlayer(i);
            }
        }
        if (oneToBeDelete != -1){
            disconnectedRMI.add(new DisconnectedPlayers(oneToBeDelete, players.get(index).getUsername()));
            if (players.get(index).getUsername().equals(player)){
                return DISCONNECTION;
            }
        }
        oneToBeDelete = -1;
        for (SocketUser u: socketUserList){
            if (u.name.equals(player)){
                try {
                    return SocketMessengerServer.get(u.socket);
                } catch (IOException e){
                    oneToBeDelete = socketUserList.indexOf(u);
                    index = findDisconnectedSocket(u.name);
                    players.get(index).setDisconnected(true);
                }
            }
        }
        if (oneToBeDelete != -1){
            disconnectedSocket.add(new DisconnectedPlayers(oneToBeDelete, players.get(index).getUsername()));
            socketUserList.remove(oneToBeDelete);
            System.out.println("[Socket Server]\t" + players.get(index).getUsername() + "  disconnected....");
            return DISCONNECTION;
        }
        System.err.println("COULD NOT FIND PLAYER FOR THE INPUT");
        return DISCONNECTION;
    }

    /**
     * Private method to reduce complexity of try/catch in case of a RemoteException
     *
     * @param disconnected: index of the ClientIntRmi in usersRMI that should be removed from active players
     * @return index of the player that have to be set as "disconnected"
     * @author Andrea
     */
    private int disconnectPlayer (int disconnected){
        int index = findDisconnectedRmi(usersRMI.get(disconnected));
        this.players.get(index).setDisconnected(true);
        this.server.removeClientAndUsername(this.usersRMI.get(disconnected), this.players.get(index).getUsername());
        return index;
    }

    /**
     * Used to find out who is the player disconnected (index in ArrayList<> players)
     *
     * @param o: SocketUser.name disconnected
     * @return index of disconnected player
     * @author Andrea
     */
    private int findDisconnectedSocket(String o){
        int index = 0;
        ArrayList<String> names = new ArrayList<>();

        //Socket case
        for (SocketUser s : socketUserList){
            if (!s.name.equals(o))
                names.add(s.name);
        }
        //RMI case
        for (ClientIntRMI c : usersRMI){
            try {
                names.add(c.getName());
            } catch (RemoteException e){
                //WE CAN MANAGE ONE DISCONNECTION PER TIME
            }
        }

        for (int i = 0; i < players.size(); i++){
            boolean found = false;
            for (String n : names){
                if (players.get(i).getUsername().equals(n))
                    found = true;
            }
            if (!found)
                index = i;
        }
        return index;
    }

    /**
     * Used to find out who is the player disconnected (index in ArrayList<> players)
     *
     * @param o: ClientIntRmi disconnected
     * @return index of disconnected player
     * @author Andrea
     */
    private int findDisconnectedRmi(ClientIntRMI o){
        int index = 0;
        ArrayList<String> names = new ArrayList<>();

        //RMI case
        for (ClientIntRMI c : usersRMI){
            try {
                if (c != o)
                    names.add(c.getName());
            } catch (RemoteException e){
                //WE CAN MANAGE ONE DISCONNECTION PER TIME
            }
        }
        //Socket case
        for (SocketUser s : socketUserList)
            names.add(s.name);

        for (int i = 0; i < players.size(); i++){
            boolean found = false;
            for (String n : names){
                if (players.get(i).getUsername().equals(n))
                    found = true;
            }
            if (!found)
                index = i;
        }
        return index;
    }

    ////////////////////////////////////////////////////////////////

    /**
     * Class used to keep trace of a disconnected player
     *
     * @author Andrea
     */
    class DisconnectedPlayers {

        int index;
        String name;

        /**
         * Constructor of the DisconnectedPlayer object
         *
         * @param index: index in list usersRMI or socketUserList
         * @param name: name of the disconnected player
         * @author Andrea
         */
        DisconnectedPlayers(int index, String name){
            this.index = index;
            this.name = name;
        }

    }

    ////////////////////////////////////////////////////////////////

}