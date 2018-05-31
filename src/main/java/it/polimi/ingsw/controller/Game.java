package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.IOhandler;

import java.util.*;

public class Game {

    //***************************//
    //        Attributes         //
    //***************************//

    private List<Player> players = new ArrayList<>();
    private Table table = null;
    private int active = -1;
    private int turn = 1;
    private static final String STATUS = "STATUS";
    private int count = 0;
    private boolean clockwise = true;

    public Game(List<Player> users, Table board){

        this.players = users;
        this.table = board;

    }
    //***************************//
    //         Methods           //
    //***************************//

    public void begin(){

        int i,j;
        Integer[] schemes = new Integer[12];
        for (int k = 1; k < schemes.length; k++) {
            schemes[k] = k;
        }
        Collections.shuffle(Arrays.asList(schemes));
        //TODO: PLACE CORRECTLY SCHEMES IN FILE
        for(Player p:this.players){
            Controller.getMyIO(this).broadcast(p.getUsername() + " has to choose a scheme");

            i = schemes[this.players.indexOf(p)];
            j = schemes[this.players.indexOf(p) + 4];

            p.chooseScheme(Scheme.initialize(Controller.getMyIO(this).chooseScheme(i,j,i+12,j+12)));
        }



        this.active = 0; //TODO: add algorithm to define starting player
        Controller.getMyIO(this).broadcast(STATUS);
        Controller.getMyIO(this).broadcast("Game is starting!\n");
        this.next();
    }

    public void next(){
        if(this.turn > this.players.size()*2*10){
            Controller.getMyIO(this).broadcast("Game has ended! Closing..."); //TODO: implement end of game
            System.exit(0);
        } else {
            Controller.getMyIO(this).broadcast(players.get(active).getUsername() + ", it's your turn");
            String action = null;
            action = Controller.getMyIO(this).getStandardAction();
            switch (action){
                case "d": {
                    Dice dice = null;
                    boolean check = false;
                    while (!check){
                        try{
                            int index = Controller.getMyIO(this).getDice();
                            dice = this.table.checkDiceFromReserve(index);
                            check = this.players.get(active).placeDice(Controller.getMyIO(this).getCoordinate("x"),
                                    Controller.getMyIO(this).getCoordinate("y"),
                                    this.table, index);

                            if (!check){
                                Controller.getMyIO(this).broadcast("Player " + this.players.get(active).getUsername() + " didn't do it right, try again");
                                if (dice!= null && dice != this.table.checkDiceFromReserve(index)) this.table.putDiceInReserve(dice);
                                Controller.getMyIO(this).broadcast(this.table);
                            }

                        } catch (Exception e){
                            Controller.getMyIO(this).broadcast("Player " + this.players.get(active).getUsername() + " didn't do it right, try again");
                            if (dice!= null) this.table.putDiceInReserve(dice);
                            Controller.getMyIO(this).broadcast(this.table);
                        }
                    }
                    break;
                }
                case "q":{

                    break;
                }
                default: System.out.println("FATAL ERROR, UNKNOWN INPUT"); System.exit(-1); //TODO: implement input error manager
            }
        }

        //This is made to keep track of the active player
        if (this.count == players.size() - 1 && this.clockwise) {
            clockwise = false;
        } else if(this.count == 0 && !this.clockwise){
            if (this.active == this.players.size() - 1){
                this.active = 0;
            } else {
                this.active ++;
            }
            clockwise = true;
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

}
