package it.polimi.ingsw.server.model;

import java.util.Observable;
import java.util.Observer;

/**
 * Player class contains methods related to player's actions in the game
 *
 * @author Andrea
 */
public class Player extends Observable {

    //***************************//
    //        Attributes         //
    //***************************//

    private String nickname;
    private int points;
    private int tokens;
    private Dice diceInHand = null;
    private Scheme ownScheme;
    private boolean tool8 = false;
    private int IDplayer;
    private boolean disconnected = false;

    /**
     * Constructor for the player which receives from controller nickname and ID
     *
     * @param nick: player's nickname (inserted by the user during the connection phase)
     * @param ID: player's identification number (range: 0 to 3 in a game with 4 players)
     * @author Andrea
     */
    public Player(String nick, int ID){
        this.nickname = nick;
        this.points = 0;
        this.IDplayer = ID;
    }

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Assigns a window pattern to the player and gives him the right number of tokens
     *
     * @param scheme: the window pattern chosen
     * @author Andrea
     */
    public void chooseScheme(Scheme scheme){
        this.ownScheme = scheme;
        this.tokens = scheme.getDifficulty();
    }

    /**
     * Picks a dice from the scheme and puts it in player's hand
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @return true if the box exists and is effectively full, otherwise false
     * @author Andrea
     */
    public boolean extractDice(int x, int y){
        if (ownScheme.checkBox(x, y) != null){
            this.diceInHand = ownScheme.removeDice(x, y);
            return true;
        } else
            return false;
    }

    /**
     * Places a dice in a specific box of the own window pattern
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param table: the game table with which the player interacts
     * @param indexDice: the dice to be placed in the box
     * @return window pattern placeDice method result (true if the dice is correctly placed, false if the box is already full)
     * @author Andrea
     */
    public boolean placeDice (int x, int y, Table table, int indexDice){
        Dice tempDice;
        if (table ==  null)
            return false;

        tempDice = table.pickDiceFromReserve(indexDice);
        if (tempDice == null)
            return false;

        return ownScheme.placeDice(x, y, tempDice);
    }

    /**
     * Counts player's points at the end of the game, when player decides to leave or whenever else the method is called
     *
     * @author Andrea
     */
    public void countPoints(){
        this.points = 0;
        int freeBoxes = 0;
        Box[][] tempGrid = this.ownScheme.getGrid();

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                if (!tempGrid[i][j].isFull())
                    freeBoxes++;
            }
        }

        this.points += PrivObjHandler.countPoints(this);
        for (int i = 0; i<3; i++)
            this.points += PubObjHandler.countPoints(this, i);

        this.points += this.tokens;
        this.points -= freeBoxes;
    }

    /**
     * Returns points done with private objective cards
     *
     * @return points done with private objective cards
     * @author Andrea
     */
    public int pointsInPrivObj (){
        return PrivObjHandler.countPoints(this);
    }

    /**
     * Gets the own window pattern made up of name, degree of difficulty and current grid situation
     *
     * @return the window pattern at the time the method is called
     * @author Andrea
     */
    public Scheme getOwnScheme() {
        return this.ownScheme;
    }

    /**
     * Gets the own ID player
     *
     * @return own ID player
     * @author Andrea
     */
    int getIDplayer() {
        return this.IDplayer;
    }

    /**
     * Gets the own Username
     *
     * @return own username
     * @author Matteo
     */
    public String getUsername() {
        return this.nickname;
    }

    /**
     * Returns the amount of current points
     *
     * @return the amount of own points
     * @author Andrea
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Returns the amount of current tokens
     *
     * @return the amount of own tokens
     * @author Andrea
     */
    public int getTokens() {
        return this.tokens;
    }

    /**
     * Decrements the number of player tokens when the player uses a tool card
     *
     * @param used: an integer that represents the number of tokens already on the tool card used
     * @author Riccardo
     */
    void decrementTokens(int used){
        if (used == 0)
            this.tokens--;
        else
            this.tokens -= 2;
    }

    /**
     * Returns the dice currently in hand (useful for tool cards)
     *
     * @return dice in hand (null if there is not)
     * @author Andrea
     */
    public Dice getDiceInHand() {
        return this.diceInHand;
    }

    /**
     * Sets attribute disconnected
     *
     * @param disconnected: change status of attribute disconnected
     * @author Andrea
     */
    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    /**
     * Gets the current status of attribute disconnected
     *
     * @return the status of attribute disconnected
     * @author Andrea
     */
    public boolean isDisconnected() {
        return disconnected;
    }

    /**
     * Sets the usage of tool card 8
     *
     * @param bool: flag representing the usage of tool card 8: if it's true tool 8 will be used, otherwise not
     * @author Matteo
     */
    public void setTool8(boolean bool){
        this.tool8 = bool;
    }

    /**
     * Gets the boolean flag representing the usage of tool card 8
     *
     * @return true if tool card 8 is used, otherwise false
     * @author Matteo
     */
    public boolean getTool8() {
        return this.tool8;
    }

    /**
     * Used to print a player
     *
     * @return the string that represents the player
     * @author Andrea
     */
    @Override
    public String toString(){
        return this.nickname+ "\n Tokens available: "+this.tokens+Enum.Color.BLUE.escape()+"\nActive window pattern:\n"+this.ownScheme;
    }

    /**
     * Adds an observer to the current observable object
     *
     * @param o: object that implements observer
     * @author Matteo
     */
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        this.ownScheme.addObserver(o);
    }

    /**
     * Calls update method on observers
     * Overrides notifyObservers method but if argument is a string bypasses setChanged algorithm
     *
     * @param arg: argument passed to the update method
     * @author Matteo
     */
    @Override
    public void notifyObservers(Object arg) {
        if (arg.getClass().equals(String.class)){
            setChanged();
        }
        super.notifyObservers(arg);
    }

}