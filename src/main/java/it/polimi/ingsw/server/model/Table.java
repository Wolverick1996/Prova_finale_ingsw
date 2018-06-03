package it.polimi.ingsw.server.model;

import java.util.*;

/**
 * Table represents the current situation of the game: it contains the dice reserve and round track and it is responsible of turns and rounds
 *
 * @author Matteo
 */
public class Table {

    //***************************//
    //        Attributes         //
    //***************************//

    private static final int NUM_CARDS = 3;
    private ArrayList<Dice> reserve = new ArrayList<>();
    private ArrayList<Player> activePlayers = new ArrayList<>();
    private boolean canExtract = true;
    private ArrayList<Dice> roundTrack = new ArrayList<>();
    private int redExt = 0;
    private int greenExt = 0;
    private int purpleExt = 0;
    private int yellowExt = 0;
    private int bluExt = 0;
    private int round = -1; //must match ROUND in controller
    private boolean clockwise = true;
    private int turn = 0;
    private int numPlayers; //must receive data
    private int realTurn = 1;

    /**
     * Constructor of the table which sets tool cards and public objective cards of the game and starts the first round
     *
     * @param numP: number of players
     * @author Matteo
     */
    public Table(int numP){
        //ToolHandler.setTools();
        PubObjHandler.setPubOC();
        //TODO: numPlayers = controller.getNumPlayers(); //Request must be sent to right controller
        this.numPlayers = numP;
        nextRound();
    }

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Sets active players in the activePlayers ArrayList
     *
     * @param nicknames: list of player nicknames passed by the controller
     * @author Andrea
     */
    public void setPlayers (List<String> nicknames){
        for (int i = 0; i < this.numPlayers; i++)
            this.activePlayers.add(new Player(nicknames.get(i), i));
        PrivObjHandler.setPrivOC(this.numPlayers);
    }

    /**
     * Changes turn: turn goes from 0 to 3, either clockwise or anticlockwise
     *
     * @author Matteo
     */
    public void nextTurn(){
        canExtract = true;
        //TODO: call controller to active nextPlayer;
        if (clockwise){
            if (turn == numPlayers-1){
                clockwise = false;
            } else {
                turn++;
            }
        } else {
            if (turn == 0){
                clockwise = true;
                nextRound();
            } else {
                turn--;
            }
        }
        this.realTurn++;
    }

    /**
     * Changes round, putting the remaining dice in the round track and refilling the reserve
     *
     * @author Matteo
     */
    private void nextRound(){
        //set RoundTrack
        //set Reserve
        if (round>9){ System.out.println("WARNING nextRound called, round is already 10"); return; }

        if (!reserve.isEmpty()){
            for (Dice d:reserve)
                roundTrack.add(d);

            reserve.clear();
        } else if (round != -1) { System.out.println("WARNING reserve is empty"); return; }

        for (int i=0;i<numPlayers*2+1;i++){ reserve.add(pickDiceFromBag()); }

        round++;

        //At the beginning of each round turns restart from 1
        //It will be incremented to 1 at the end of the nextTurn method
        this.realTurn = 0;

        //Resetting turns of players
        for (Player p : activePlayers) { p.resetTurns(); }
    }

    /**
     * Picks a new dice from bag (tool card 11)
     *
     * @return the new extracted dice
     * @author Matteo
     */
    public Dice pickDiceFromBag(){
        boolean isAVB = false;
        Enum.Color color = Enum.Color.getRandomColor();
        if (redExt==18 && purpleExt==18 && bluExt==18 && greenExt==18 && yellowExt==18){System.out.println("Bag is empty");
        return null;}
        while(!isAVB){
            color = Enum.Color.getRandomColor();
            switch (color){
                case RED: { if (redExt < 18) { isAVB = true; redExt++;} break; }
                case PURPLE: { if (purpleExt < 18) { isAVB = true; purpleExt++;} break; }
                case BLUE: { if (bluExt < 18) { isAVB = true; bluExt++;} break; }
                case GREEN: { if (greenExt < 18) { isAVB = true; greenExt++;} break; }
                case YELLOW: { if (yellowExt < 18) { isAVB = true; yellowExt++;} break; }
                //default: throw new Exception();
            }
        }
        return new Dice(color);
    }

    /**
     * Picks a dice from reserve
     * NOTE: Call this method only after move has been confirmed
     *
     * @param dicePos: the dice position on the reserve
     * @return the extracted dice if the extraction is allowed, otherwise null
     * @author Matteo
     */
    public Dice pickDiceFromReserve(int dicePos){
        if (dicePos >= reserve.size()) { return null; }
        if (canExtract){
            Dice temp = reserve.get(dicePos);
            reserve.remove(dicePos);
            canExtract = false;
            return temp;
        } else {
            return null;
        }
    }

    /**
     * Picks a dice from round track
     * NOTE: Call this method only after move has been confirmed
     *
     * @param dicePos: the dice position on the round track
     * @return the extracted dice
     * @author Matteo
     */
    public Dice pickDiceFromRoundtrack(int dicePos){
        if (dicePos >= roundTrack.size()) { return null; }
        Dice temp = roundTrack.get(dicePos);
        roundTrack.remove(dicePos);
        return temp;
    }

    /**
     * Picks a dice from reserve and puts it in the bag (tool card 11)
     *
     * @param dicePos: the dice position on the reserve
     * @author Matteo
     */
    public void putDiceInBag(int dicePos){
        if(!canExtract){System.out.println("Tool card 11 was called without permission (dice already extracted)"); return;}
        Enum.Color color = reserve.get(dicePos).getColor();
        switch (color){
            //TODO: colorExt must never be <0
            case RED: { redExt--; break; }
            case PURPLE: { purpleExt--; break; }
            case BLUE: { bluExt--; break; }
            case GREEN: { greenExt--; break; }
            case YELLOW: { yellowExt--; break; }
            //default: throw new Exception();
        }
        reserve.remove(dicePos);
    }

    /**
     * Rerolls the reserve (tool card 7)
     *
     * @return true if the operation is allowed (in the second part of the round), otherwise false
     * @author Matteo
     */
    public  boolean rerollReserve(){
        if (clockwise) return false;

        for(Dice d:reserve)
            d.rollDice();

        return true;
    }

    /**
     * Gets the dice to check position restrictions [reserve]
     *
     * @param dicePos: the dice position on the reserve
     * @return the dice in the specified position if it exists, otherwise null
     * @author Matteo
     */
    public Dice checkDiceFromReserve(int dicePos){
        if(dicePos<reserve.size())
            return reserve.get(dicePos);
        return null;
    }

    /**
     * Gets the dice to check position restrictions [round track]
     *
     * @param dicePos: the dice position on the round track
     * @return the dice in the specified position if it exists, otherwise null
     * @author Matteo
     */
    public Dice checkDiceFromRoundtrack(int dicePos){
        if(dicePos<roundTrack.size())
            return roundTrack.get(dicePos);
        return null;
    }

    /**
     * Puts a dice in a particular spot [reserve]
     *
     * @param d: the dice to place
     * @author Matteo
     */
    public void putDiceInReserve(Dice d){
        reserve.add(d);
        canExtract = true;
    }

    /**
     * Puts a dice in a particular spot [round track]
     *
     * @param d: the dice to place
     * @author Matteo
     */
    public void putDiceInRoundtrack(Dice d){roundTrack.add(d);}

    /**
     * This method is called by controller when the player decides to use a tool card
     *
     * @param indexToolCard: the identifier of the tool card on the table
     * @param activePlayer: the player who decides to use the tool card
     * @return true if the tool card is correctly used, otherwise false
     */
    public boolean useToolCard(int indexToolCard, Player activePlayer) {
        return ToolHandler.useTool(indexToolCard, activePlayer, this);
    }

    /**
     * Gets the current turn (0 to numPlayers-1 in the first part of the round, numPlayers-1 to 0 in the second part)
     *
     * @return the current turn
     * @author Matteo
     */
    public int getTurn(){ return this.turn; }

    /**
     * Gets the current turn (1 to 2*numPlayers)
     *
     * @return the current turn
     * @author Matteo
     */
    public int getRealTurn(){ return this.realTurn; }

    /**
     * Gets the current round
     *
     * @return the current round
     * @author Matteo
     */
    public int getRound(){ return this.round; }

    /**
     * Gets the list of active players
     *
     * @return the list of active players
     * @author Matteo
     */
    public List<Player> getActivePlayers() { return this.activePlayers; }

    /**
     * Used to print the table and its contents
     *
     * @return the string that represents the table
     * @author Matteo
     */
    @Override
    public String toString(){
        String s = "\n";
        for(int i = 0; i<NUM_CARDS ; i++){
            s += PubObjHandler.getName(i) + "\n";
            s += PubObjHandler.getDescription(i) + "\n";
        }
        return "Table: \n" + "Dice in reserve: " + reserve.size() + "\nReserve: " + reserve.toString() + "\nRoundtrack: " +
                roundTrack.toString() + "\nTurn: " + realTurn + "\nRound: " + (round+1) + "\nClockwise: " + clockwise +
                "\nPublic Objective cards: " + s;
    }

}