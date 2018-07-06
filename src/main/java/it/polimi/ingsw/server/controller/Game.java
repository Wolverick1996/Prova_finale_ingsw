package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Enum;

import java.util.*;

/**
 * Game context in which players move from the beginning to the end of the match
 *
 * @author Matteo
 */
public class Game implements Observer {

    //***************************//
    //        Attributes         //
    //***************************//

    private List<Player> players;
    private Table table;
    private int active = -1;
    private int turn = 1;
    private static final String STATUS = "STATUS";
    private static final int MAX_ROUNDS = 10;
    private int count = 0;
    private boolean clockwise = true;
    private boolean toolUsed = false;

    /**
     * Constructor of the Game object
     *
     * @param users: list of users are playing this match
     * @param board: table related to the current match
     * @author Matteo
     */
    Game (List<Player> users, Table board){
        this.players = users;
        this.table = board;
    }

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Initializes Game class and performs the preparation phase (card distribution, chose of window pattern)
     *
     * @author Matteo
     */
    void begin(){
        int i;
        int j;

        if (Controller.getMyIO(this) == null)
            return;

        Controller.getMyIO(this).broadcast(this.players.get(0).getUsername() + " is deciding whether to add custom window patterns or not");
        Controller.getMyIO(this).notify(this.players.get(0).getUsername(), "Do you want to use custom window patterns?");
        if (Controller.getMyIO(this).yesOrNo(this.players.get(0).getUsername())){
            this.table.setCustom();
            Controller.getMyIO(this).broadcast("Custom window patterns enabled!");
        } else
            Controller.getMyIO(this).broadcast("Old school, only standard window patterns!");

        Integer[] schemes = new Integer[table.getNumSchemes()/2];
        for (int k = 0; k < schemes.length; k++)
            schemes[k] = k+1;

        Collections.shuffle(Arrays.asList(schemes));
        for (Player p:this.players){
            Controller.getMyIO(this).notify(p.getUsername(), "This is your Private Objective Card:\n" +
            PrivObjHandler.getColor(p).escape() + PrivObjHandler.getName(p) + "\n" + Enum.Color.RESET +
            PrivObjHandler.getDescription(p) + "\n");
            Controller.getMyIO(this).broadcast(p.getUsername() + " has to choose a scheme");

            i = schemes[this.players.indexOf(p)];
            j = schemes[this.players.indexOf(p) + 4];

            p.chooseScheme(Scheme.initialize(Controller.getMyIO(this).chooseScheme(i,j,i+(this.table.getNumSchemes()/2),j+(this.table.getNumSchemes()/2), p.getUsername()),
                    this.table.getCustom(), this.table.getNumSchemes()));
        }

        this.active = 0;
        Controller.getMyIO(this).broadcast(STATUS);
        Controller.getMyIO(this).broadcast("Game is starting!\n");

        setObservables();
        this.next();
    }

    /**
     * Contains references to actions the player can perform during the turn (dice placement, tool usage) and is called when preparation phase/turn ends
     *
     * @author Matteo
     */
    private void next(){
        if (this.turn > this.players.size()*2* MAX_ROUNDS || howManyActivePlayers() <= 1){
            //End Game
            gameEnding();
        } else {
            boolean end = false;
            Controller.getMyIO(this).broadcast(players.get(active).getUsername() + ", it's your turn!");
            if (this.players.get(active).getTool8()){
                end = true;
                Controller.getMyIO(this).broadcast("Used tool 8, so is passing the turn...");
                this.players.get(active).setTool8(false);
            }
            if (this.players.get(active).isDisconnected()){
                end = true;
                Controller.getMyIO(this).broadcast(players.get(active).getUsername() +
                        " it's disconnected, so is passing the turn...\nTurn passed");
            }
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
        endTurn();
    }

    /**
     * Updates the active player status and manages turns and rounds
     *
     * @author Matteo
     */
    private void endTurn(){
        //This is made to keep track of the active player
        if (this.count == this.players.size() - 1 && this.clockwise){
            this.clockwise = false;
        } else if (this.count == 0 && !this.clockwise){
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

        if (this.players.get(active).isDisconnected()){
            Controller.getMyIO(this).broadcast(players.get(active).getUsername() +
                    " is disconnected and misses his turn -.-\nTurn passed");
            endTurn();
        }

        this.turn++;
        this.table.nextTurn();
        Controller.getMyIO(this).broadcast(STATUS);
        next();
    }

    /**
     * Contains references to model classes and methods necessary to perform tool card usages
     *
     * @author Matteo
     */
    private void useTool(){
        if (toolUsed){
            Controller.getMyIO(this).broadcast("Someone is trying to use a tool card TWICE... YOU CAAAAAAAAAAAAAN'T");
            return; }

        int index = -2;
        toolUsed = true;

        Controller.getMyIO(this).notify(this.players.get(active).getUsername(), "\nTool Cards on table:\n");
        for (int i = 0; i<3; i++) {
            Controller.getMyIO(this).notify(this.players.get(active).getUsername(), Enum.Color.YELLOW.escape()
                    + ToolHandler.getName(i) + "\n" + Enum.Color.RESET + ToolHandler.getDescription(i) + Enum.Color.YELLOW.escape() +
                    "\n Tokens on: " + Enum.Color.RESET + ToolHandler.getTokens(i) + "\n");
        }
        while (index<0 || index>2){
            if (index == -1){
                toolUsed = false;
                Controller.getMyIO(this).broadcast("Nope, nothing done :(");
                return;
            }
            Controller.getMyIO(this).notify(this.players.get(active).getUsername(), "Insert number from 1 to 3");
            index = Controller.getMyIO(this).getTool(this.players.get(active).getUsername());
        }

        if (this.table.useToolCard(index, this.players.get(active), Controller.getMyIO(this))){
            Controller.getMyIO(this).broadcast("Player " + this.players.get(active).getUsername() +
            " has used " + ToolHandler.getName(index) + " correctly! :)");
                Controller.getMyIO(this).broadcast("HOORAY!");
        } else {
            toolUsed = false;
            Controller.getMyIO(this).broadcast("Something went wrong... :(");
        }
    }

    /**
     * Contains references to model classes and methods necessary to perform dice placements
     *
     * @author Matteo
     */
    private void putDiceStandard(){
        if (!table.getCanExtract()){
            Controller.getMyIO(this).broadcast("Someone is trying to place a dice TWICE... YOU CAAAAAAAAAAAAAN'T");
            return; }

        Dice dice = null;
        boolean check = false;
        int index = 0;
        while (!check){
            try {
                if (index != -1){
                    dice = null;
                    Controller.getMyIO(this).notify(this.players.get(active).getUsername(), players.get(active).getOwnScheme().toString());
                    Controller.getMyIO(this).notify(this.players.get(active).getUsername(), "Type '0' if you want to go back");
                    index = Controller.getMyIO(this).getDiceFromReserve(players.get(active).getUsername());
                }
                if (index == -1){
                    table.setCanExtract(true);
                    Controller.getMyIO(this).broadcast("Nope, nothing done");
                    return; }

                dice = this.table.checkDiceFromReserve(index);

                Controller.getMyIO(this).notify(this.players.get(active).getUsername(), "Insert the coordinates of the dice to be placed, one at a time (x, y)");
                int x = Controller.getMyIO(this).getCoordinate(this.players.get(active).getUsername());
                if (x != -1){
                    int y = Controller.getMyIO(this).getCoordinate(this.players.get(active).getUsername());
                    if (y != -1)
                        check = this.players.get(active).placeDice(x, y , this.table, index);
                    else
                        index = -1;
                }else
                    index = -1;

                if (!check){
                    Controller.getMyIO(this).broadcast("Player " + this.players.get(active).getUsername() + " didn't do it right, try again\n");
                    if (dice!= null && dice != this.table.checkDiceFromReserve(index))
                        this.table.putDiceInReserve(dice);
                }

            } catch (Exception e) {
                Controller.getMyIO(this).broadcast("EXCEPTION CAUGHT! Player " + this.players.get(active).getUsername() + " didn't do it right, try again\n");
                Controller.getMyIO(this).broadcast(e.getMessage());
                if (dice!= null) this.table.putDiceInReserve(dice);
            }

        }
        Controller.getMyIO(this).broadcast("Dice correctly placed!");
    }

    /**
     * Method to perform tool card 8 usage (necessary because tool card 8 affects turn logic)
     *
     * @author Matteo
     */
    public void useTool8(){
        Controller.getMyIO(this).broadcast("Player" + this.players.get(active).getUsername() + " is using Tool 8");
        if (this.table.getCanExtract())
            putDiceStandard();

        this.table.setCanExtract(true);
        Controller.getMyIO(this).notify(this.players.get(active).getUsername(), "Place the second dice: ");

        putDiceStandard();
        this.players.get(active).setTool8(true);
    }

    /**
     * Manages game ending and creates the match ranking
     *
     * @author Andrea
     */
    private void gameEnding(){
        Controller.getMyIO(this).broadcast("Game ended, calculating points...\n");

        Player winner = null;

        if (howManyActivePlayers() == 1){
            for (Player p : players){
                if (!p.isDisconnected())
                    winner = p;
            }
        }
        else{

            int highestMade = 0;
            for (Player p : this.players){
                p.countPoints();
                if (p.getPoints() > highestMade)
                    highestMade = p.getPoints();
                Controller.getMyIO(this).broadcast(p.getUsername() + ":" + p.getPoints());
            }

            ArrayList<Player> winners = new ArrayList<>();
            int highestWithPrivOC = 0;
            int highestNumOfTokens = 0;
            for (Player p : this.players){
                if (p.getPoints() == highestMade && p.pointsInPrivObj() >= highestWithPrivOC){
                    highestWithPrivOC = p.pointsInPrivObj();

                    if (p.pointsInPrivObj() == highestWithPrivOC && p.getTokens() >= highestNumOfTokens){
                        highestNumOfTokens = p.getTokens();

                        if (p.getTokens() == highestNumOfTokens)
                            winners.add(p);
                        else {
                            winners.clear();
                            winners.add(p);
                        }
                    }
                }
            }

            if (winners.size() > 1)
                winner = lastCheck(winners);
            else
                winner = winners.get(0);
        }

        Controller.getMyIO(this).broadcast("Congratulations " + winner.getUsername() + ", you won the game!");

        Controller.getMyIO(this).finishGameSocket();

        System.exit(-1);
    }

    /**
     * Checks the order in which players played in the last round (useful in case of double parity)
     *
     * @param winners: list of players (sorted by score)
     * @return the winner of the game
     * @author Andrea
     */
    private Player lastCheck(List<Player> winners) {
        int firstPlLastTurn = MAX_ROUNDS % this.players.size();
        int lastPlLastTurn;
        if (firstPlLastTurn == 0) {
            firstPlLastTurn = this.players.size() - 1;
            lastPlLastTurn = firstPlLastTurn - 1;
        } else {
            firstPlLastTurn--;
            if (firstPlLastTurn == 0)
                lastPlLastTurn = this.players.size() - 1;
            else
                lastPlLastTurn = firstPlLastTurn - 1;
        }

        int[] orderFinalRound = new int[this.players.size()];
        for (int i = 0; i < this.players.size(); i++) {
            if (lastPlLastTurn == 0) {
                orderFinalRound[i] = lastPlLastTurn;
                lastPlLastTurn = this.players.size() - 1;
            } else {
                orderFinalRound[i] = lastPlLastTurn;
                lastPlLastTurn--;
            }
        }

        for (int i = 0; i < this.players.size(); i++) {
            for (Player w : winners) {
                if (orderFinalRound[i] == this.players.indexOf(w)) {
                    winners.clear();
                    winners.add(w);
                }
            }
        }

        return winners.get(0);
    }

    /**
     * Get a Player with a specific username
     *
     * @param username: the name of the Player you need
     * @return the object Player with a specific username
     * @author Andrea
     */
    public Player getPlayer(String username) {
        for (Player p : players){
            if (p.getUsername().equals(username))
                return p;
        }
        return null;
    }

    /**
     * Returns how many players are still playing (no disconnected)
     *
     * @return number of active players
     */
    private int howManyActivePlayers(){
        int result = 0;
        for (Player p : players){
            if (!p.isDisconnected())
                result ++;
        }
        return result;
    }

    /**
     * Sets the observables
     *
     * @author Matteo
     */
    private void setObservables(){
        for (Player p: this.players){
            p.addObserver(this);
        }
    }

    /**
     * It's called by notify method of observables and passes a string object to clients to be printed
     *
     * @param o: observable calling the method
     * @param arg: object to be eventually converted into string to be printed
     * @author Matteo
     */
    @Override
    public void update(Observable o, Object arg) {
        Controller.getMyIO(this).notify(this.players.get(active).getUsername(), arg.toString());
    }

}