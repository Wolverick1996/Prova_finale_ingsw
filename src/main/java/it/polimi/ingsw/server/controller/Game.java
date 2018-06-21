package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Enum;

import java.rmi.RemoteException;
import java.util.*;

class Game {

    //***************************//
    //        Attributes         //
    //***************************//

    private List<Player> players;
    private Table table;
    private int active = -1;
    private int turn = 1;
    private static final String STATUS = "STATUS";
    private int count = 0;
    private boolean clockwise = true;
    private boolean toolUsed = false;
    private boolean dicePlaced = false;

    Game(List<Player> users, Table board){
        this.players = users;
        this.table = board;
    }

    //***************************//
    //         Methods           //
    //***************************//

    void begin(){
        int i;
        int j;
        Integer[] schemes = new Integer[12];
        for (int k = 0; k < schemes.length; k++)
            schemes[k] = k+1;

        Collections.shuffle(Arrays.asList(schemes));
        //TODO: PLACE CORRECTLY SCHEMES IN FILE
        for (Player p:this.players){
            try {
                Controller.getMyIO(this).notify(p.getUsername(), "This is your Private Objective Card:\n" +
                        PrivObjHandler.getColor(p).escape() + PrivObjHandler.getName(p) + "\n" + Enum.Color.RESET +
                        PrivObjHandler.getDescription(p) + "\n");
            } catch (RemoteException e) {
                e.printStackTrace();
                //THIS PLAYER HAS DISCONNECTED
            }
            Controller.getMyIO(this).broadcast(p.getUsername() + " has to choose a scheme");

            i = schemes[this.players.indexOf(p)];
            j = schemes[this.players.indexOf(p) + 4];

            p.chooseScheme(Scheme.initialize(Controller.getMyIO(this).chooseScheme(i,j,i+12,j+12, p.getUsername())));
        }

        this.active = 0; //TODO: add algorithm to define starting player
        Controller.getMyIO(this).broadcast(STATUS);
        Controller.getMyIO(this).broadcast("Game is starting!\n");
        this.next();
    }

    private void next(){
        if (this.turn > this.players.size()*2*10){
            Controller.getMyIO(this).broadcast("Game has ended! Closing..."); //TODO: implement end of game
            System.exit(0);
        } else {
            Controller.getMyIO(this).broadcast(players.get(active).getUsername() + ", it's your turn!");
            Boolean end = false;
            while (!end){
                String action = Controller.getMyIO(this).getStandardAction(players.get(active).getUsername());
                switch (action){
                    case "d":
                        Controller.getMyIO(this).broadcast("Is putting a dice...");
                        putDiceStandard();
                        break;
                    case "q":
                        Controller.getMyIO(this).broadcast("Turn passed");
                        end = true;
                        toolUsed = false;
                        dicePlaced = false;
                        break;
                    case "t":
                        Controller.getMyIO(this).broadcast("Is using a tool...");
                        useTool();
                        break;
                    default:
                        System.err.println("FATAL ERROR, UNKNOWN INPUT");
                        System.exit(-1); //TODO: implement input error manager
                }
            }
        }

        //This is made to keep track of the active player
        if (this.count == this.players.size() - 1 && this.clockwise){
            this.clockwise = false;
        } else if(this.count == 0 && !this.clockwise){
            if (this.active == this.players.size() - 1){
                this.active = 0;
            } else {
                this.active ++;
            }
            this.clockwise = true;
        } else if (this.clockwise){
            if (this.active == this.players.size() - 1){
                this.active = 0;
            } else {
                this.active ++;
            }
            this.count++;
        } else {
            if (this.active == 0){
                this.active = this.players.size() - 1;
            } else {
                this.active --;
            }
            this.count--;
        }

        this.turn++;
        this.table.nextTurn();
        Controller.getMyIO(this).broadcast(STATUS);
        next();
    }

    private void useTool(){
        if (toolUsed){
            Controller.getMyIO(this).broadcast("Someone is trying to use a tool card TWICE... YOU CAAAAAAAAAAAAAN'T");
            return; }

        int index = -2;
        toolUsed = true;
        //Table temp = table;
        try {
            Controller.getMyIO(this).notify(this.players.get(active).getUsername(), "\nTool Cards on table:\n");
            for (int i = 0; i<3; i++) {
                Controller.getMyIO(this).notify(this.players.get(active).getUsername(), Enum.Color.YELLOW.escape()
                        + ToolHandler.getName(i) + "\n" + Enum.Color.RESET + ToolHandler.getDescription(i) + Enum.Color.YELLOW.escape() +
                        "\n Tokens on: " + Enum.Color.RESET + ToolHandler.getTokens(i) + "\n");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }
        while (index<0 || index>2){
            if (index == -1){
                toolUsed = false;
                Controller.getMyIO(this).broadcast("Nope, nothing done :(");
                return; }

            index = Controller.getMyIO(this).getTool(this.players.get(active).getUsername());
        }

        if (this.table.useToolCard(index, this.players.get(active), Controller.getMyIO(this))){
            Controller.getMyIO(this).broadcast("Player " + this.players.get(active).getUsername() +
            " has used " + ToolHandler.getName(index) + " correctly! :)");
        } else {
            toolUsed = false;
            Controller.getMyIO(this).broadcast("Something went wrong... :(");
            //table = temp;
        }
    }

    private void putDiceStandard(){
        if (dicePlaced){
            Controller.getMyIO(this).broadcast("Someone is trying to place a dice TWICE... YOU CAAAAAAAAAAAAAN'T");
            return; }

        dicePlaced = true;
        Dice dice = null;
        boolean check = false;
        while (!check){
            try {
                int index = Controller.getMyIO(this).getDiceFromReserve(players.get(active).getUsername());
                if (index == -1){
                    dicePlaced = false;
                    Controller.getMyIO(this).broadcast("Nope, nothing done");
                    return; }

                dice = this.table.checkDiceFromReserve(index);
                check = this.players.get(active).placeDice(Controller.getMyIO(this).getCoordinate("x", this.players.get(active).getUsername()),
                        Controller.getMyIO(this).getCoordinate("y", this.players.get(active).getUsername()),
                        this.table, index);

                if (!check){
                    Controller.getMyIO(this).broadcast("Player " + this.players.get(active).getUsername() + " didn't do it right, try again");
                    if (dice!= null && dice != this.table.checkDiceFromReserve(index)) this.table.putDiceInReserve(dice);
                    Controller.getMyIO(this).notify(this.players.get(active).getUsername(), this.table.toString());
                    Controller.getMyIO(this).notify(this.players.get(active).getUsername(), this.players.get(active).getOwnScheme().toString());
                }

            } catch (Exception e){
                Controller.getMyIO(this).broadcast("EXCEPTION CAUGHT! Player " + this.players.get(active).getUsername() + " didn't do it right, try again");
                Controller.getMyIO(this).broadcast(e.getMessage());
                if (dice!= null) this.table.putDiceInReserve(dice);
            }

        }
        Controller.getMyIO(this).broadcast("Dice correctly placed!");
    }

}