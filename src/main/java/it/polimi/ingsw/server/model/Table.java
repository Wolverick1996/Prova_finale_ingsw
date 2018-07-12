package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.IOhandler;

import java.io.InputStream;
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
    private int blueExt = 0;
    private int round = -1; //must match ROUND in controller
    private boolean clockwise = true;
    private int turn = 0;
    private int numPlayers; //must receive data
    private int realTurn = 1;
    private int numSchemes = 24;
    private boolean custom = false;

    /**
     * Constructor of the table which sets tool cards and public objective cards of the game and starts the first round
     *
     * @param numP: number of players
     * @author Matteo
     */
    public Table(int numP){
        ToolHandler.setTools();
        PubObjHandler.setPubOC();
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
        if (round>9){ System.err.println("WARNING nextRound called, round is already 10"); return; }

        if (!reserve.isEmpty()){
            roundTrack.addAll(reserve);

            reserve.clear();
        } else if (round != -1){ System.err.println("WARNING reserve is empty"); return; }

        for (int i=0;i<numPlayers*2+1;i++){ reserve.add(pickDiceFromBag()); }

        round++;

        //At the beginning of each round turns restart from 1
        //It will be incremented to 1 at the end of the nextTurn method
        this.realTurn = 0;
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
        if (redExt==18 && purpleExt==18 && blueExt ==18 && greenExt==18 && yellowExt==18){
            System.err.println("Bag is empty");
            return null; }
        while (!isAVB){
            color = Enum.Color.getRandomColor();
            switch (color){
                case RED: if (redExt < 18) { isAVB = true; redExt++;} break;
                case PURPLE: if (purpleExt < 18) { isAVB = true; purpleExt++;} break;
                case BLUE: if (blueExt < 18) { isAVB = true; blueExt++;} break;
                case GREEN: if (greenExt < 18) { isAVB = true; greenExt++;} break;
                case YELLOW: if (yellowExt < 18) { isAVB = true; yellowExt++;} break;
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
        if (dicePos >= reserve.size() || dicePos < 0) { return null; }
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
        if (dicePos >= roundTrack.size() || dicePos < 0) { return null; }
        Dice temp = roundTrack.get(dicePos);
        roundTrack.remove(dicePos);
        return temp;
    }

    /**
     * Picks a dice from reserve and puts it in the bag (tool card 11)
     *
     * @param dicePos: the dice position on the reserve
     * @return true if dice movement from reserve to bag was made correctly, otherwise false
     * @author Matteo
     */
    public boolean putDiceInBag(int dicePos){
        if (dicePos >= reserve.size() || dicePos < 0) { return false; }
        Enum.Color color = reserve.get(dicePos).getColor();
        switch (color){
            case RED: redExt--; break;
            case PURPLE: purpleExt--; break;
            case BLUE: blueExt--; break;
            case GREEN: greenExt--; break;
            case YELLOW: yellowExt--; break;
        }
        if (redExt < 0 || purpleExt < 0 || blueExt < 0 || greenExt < 0 || yellowExt < 0)
            System.err.println("colorExt < 0: something strange happened");
        reserve.remove(dicePos);
        return true;
    }

    /**
     * Rerolls the reserve (tool card 7)
     *
     * @return true if the operation is allowed (in the second part of the round), otherwise false
     * @author Matteo
     */
    public  boolean rerollReserve(){
        if (clockwise) return false;

        for (Dice d:reserve)
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
        if (dicePos >= 0 && dicePos < reserve.size())
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
        if (dicePos >= 0 && dicePos < roundTrack.size())
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
    public void putDiceInRoundtrack(Dice d){ roundTrack.add(d); }

    /**
     * This method is called by controller when the player decides to use a tool card
     *
     * @param indexToolCard: the identifier of the tool card on the table
     * @param activePlayer: the player who decides to use the tool card
     * @return true if the tool card is correctly used, otherwise false
     */
    public boolean useToolCard(int indexToolCard, Player activePlayer, IOhandler out){
        return ToolHandler.useTool(indexToolCard, activePlayer, this, out);
    }

    /**
     * Sets the canExtract flag to the boolean value passed as parameter
     *
     * @param bool: the boolean value that canExtract should assumes
     * @author Riccardo
     */
    public void setCanExtract(boolean bool){ this.canExtract = bool; }

    /**
     * Gets the current value of the canExtract flag
     *
     * @return the current value of canExtract
     * @author Riccardo
     */
    public boolean getCanExtract(){ return this.canExtract; }

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
    int getRealTurn(){ return this.realTurn; }

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
     * Used to print the reserve
     *
     * @return the string that represents the reserve
     * @author Riccardo
     */
    public String printReserve(){
        return Enum.Color.PURPLE.escape() + "Dice in reserve: " + Enum.Color.RESET + reserve.size() +
                Enum.Color.PURPLE.escape() + "\nReserve: " + Enum.Color.RESET + reserve.toString();
    }

    /**
     * Used to print the round track
     *
     * @return the string that represents the round track
     * @author Riccardo
     */
    public String printRoundtrack(){
        return Enum.Color.PURPLE.escape() + "Roundtrack: " + Enum.Color.RESET + roundTrack.toString();
    }

    /**
     * Sets the custom flag true in order to use custom window patterns (advanced functionality)
     * NOTE: The method checks just if number of lines is multiple of 6 or not, but there are no checks on the correctness of lines (and therefore of window patterns)
     *
     * @author Riccardo
     */
    public void setCustom(){
        int lines = 0;
        InputStream inputFile = Scheme.class.getResourceAsStream("/schemes/CustomSchemes.txt");
        Scanner scan = new Scanner(inputFile);

        while (scan.hasNextLine()){
            scan.nextLine();
            lines++; }

        try {
            scan.close();
        } catch (Exception e1) {
            System.err.println("Error closing scan (CustomSchemes)");
        }

        if (lines%6 != 0) {
            System.err.println("CustomSchemes.txt file is not correctly written!");
            return; }

        this.custom = true;
        this.numSchemes = this.numSchemes + (lines/6);
    }

    /**
     * Gets the current value of custom flag
     *
     * @return true if custom window pattern advanced functionality is on, otherwise false
     * @author Andrea
     */
    public boolean getCustom(){
        return this.custom;
    }

    /**
     * Gets the current value of numSchemes variable
     *
     * @return the current value of numSchemes
     * @author Riccardo
     */
    public int getNumSchemes(){
        return numSchemes;
    }

    /**
     * Used to print the table and its contents
     *
     * @return the string that represents the table
     * @author Matteo
     */
    @Override
    public String toString(){
        String s = "\n";
        for (int i = 0; i<NUM_CARDS ; i++){
            s += Enum.Color.RED.escape() + PubObjHandler.getName(i) + Enum.Color.RESET + "\n";
            s += PubObjHandler.getDescription(i) + "\n";
        }
        return "Table: \n" + printReserve() + "\n" + printRoundtrack() + Enum.Color.PURPLE.escape() + "\nTurn: " +
                Enum.Color.RESET + realTurn + Enum.Color.PURPLE.escape() + "\nRound: " + Enum.Color.RESET + (round+1) +
                Enum.Color.PURPLE.escape() + "\nPublic Objective Cards: " + Enum.Color.RESET + s;
    }

}