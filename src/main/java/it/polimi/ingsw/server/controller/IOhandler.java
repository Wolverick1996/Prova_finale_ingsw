package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Scheme;
import it.polimi.ingsw.server.model.Table;
import it.polimi.ingsw.server.network_server.ServerIntRMI;

import java.rmi.RemoteException;
import java.util.*;

public class IOhandler implements Observer{

    //***************************//
    //        Attributes         //
    //***************************//

    private ServerIntRMI server;
    private static List<Player> players = new ArrayList<>();
    private Observable ov = null;
    private static Table table = null;
    private static final String STATUS = "STATUS";
    private static final String DIVISOR = "\n\n---------------------------------------------\n\n";


    public IOhandler(List<Player> users, Table board/*, Observable observed*/){
        this.players = users;
        this.table = board;
        /*this.ov = observed;*/
    }

    //***************************//
    //         Methods           //
    //***************************//

    public void broadcast(Object message){
        try {
            System.out.println(message);
            if (message.equals(STATUS)){
                System.out.println(DIVISOR);
                server.broadcast(DIVISOR);
                System.out.println(table);
                server.broadcast(table.toString());
                for (Player p: players){
                    System.out.println(p);
                    server.broadcast(p.toString());
                }
                System.out.println(DIVISOR);
                server.broadcast(DIVISOR);
            }
            else
                server.broadcast(message.toString());
        }catch (RemoteException e){
            System.err.println("ERROR BROADCAST: "+e.getMessage());
        }

    }

    public String getStandardAction(String player){
        try {
            boolean send = false;
            String answer = null;
            while (!send){

                notify(player, "Insert action (d = place dice, t = use tool, q = pass turn)");
                answer = server.getInput(player).toLowerCase();

                if (answer.equals("d")){
                    return "d";
                } else if (answer.equals("t")){
                    return "t";
                } else if (answer.equals("q")){
                    return "q";
                }
                else{
                    notify(player, "Invalid input!");
                }
            }
        }catch (RemoteException e){
            System.err.println("GETSTDACTION ERROR: "+e.getMessage());
        }
        return null;
    }

    public int getDiceFromReserve(String player){
        int answer;
        try {
            notify(player, "Insert the place of the dice in the reserve or type '0' if you want to go back");
            answer = Integer.parseInt(server.getInput(player));
            return answer-1;
        }catch (RemoteException e){
            System.err.println("GETDICE ERROR: " +e.getMessage());
        }
       //TODO: CHECK THIS INPUT!
        return -1;
    }

    public int getCoordinate(String coor, String player){
        int answer;
        try {
            notify(player,"Insert coordinate " + coor + " of the dice");
            answer = Integer.parseInt(server.getInput(player));
            //TODO: CHECK THE INPUT!
            return answer-1;
        }catch (RemoteException e){
            System.err.println("GETCOORDINATE: "+e.getMessage());
        }
        return -1;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == ov)
        {
            System.out.println(ov);
        }
    }

    public int chooseScheme(int s1, int s2, int s3, int s4, String player){

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

            while(!isValid){
                notify(player, "Pick a scheme (write 1, 2, 3 or 4)");
                answer = Integer.parseInt(server.getInput(player));

                if (answer == 1 || answer == 2 || answer == 3 || answer == 4){
                    isValid = true;
                } else {
                    notify(player, "Not a scheme!");
                }
            }
            return schemes.get(answer-1);
        }catch (RemoteException e){
            System.err.println("CHOOSESCHEME ERROR: " +e.getMessage());
        }
        return -1;
    }

    public int getDiceFromRoundtrack(String player){
        int answer;
        try {
            notify(player,"Choose a dice from Round Track [from 1 to n]");
            answer = Integer.parseInt(server.getInput(player));
            //TODO: CHECK THE INPUT!
            return answer-1;
        }catch (RemoteException e){
            System.err.println("GETTOOL: "+e.getMessage());
        }
        return -1;
    }

    public int getTool(String player){
        int answer;
        try {
            notify(player,"Choose a tool card [1, 2, 3] or type '0' if you want to go back");
            answer = Integer.parseInt(server.getInput(player));
            //TODO: CHECK THE INPUT!
            return answer-1;
        }catch (RemoteException e){
            System.err.println("GETTOOL: "+e.getMessage());
        }
        return -1;
    }

    public int chooseDiceValue(String player, boolean restricted){
        int answer;
        try {
            if (restricted){
                notify(player,"Increment or decrement the value typing '+1' or '-1'");
            }
            else{
                notify(player,"Insert the new value [1-6]");
            }
            answer = Integer.parseInt(server.getInput(player));
            //TODO: CHECK THE INPUT!
            return answer;
        }catch (RemoteException e){
            System.err.println("GETTOOL: "+e.getMessage());
        }
        return -1;
    }

    public void setServer(ServerIntRMI server) {
        this.server = server;
    }

    public void notify(String player, String message)throws RemoteException{
        server.notify(player, message);
    }
}
