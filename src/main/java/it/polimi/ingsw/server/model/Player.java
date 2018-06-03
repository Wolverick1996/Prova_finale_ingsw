package it.polimi.ingsw.server.model;

/**
 * Player class contains methods related to player's actions in the game
 *
 * @author Andrea
 */
public class Player extends Enum {

    //***************************//
    //        Attributes         //
    //***************************//

    private String nickname;
    private int points;
    private int tokens;
    private Dice diceInHand = null;
    private Scheme ownScheme;
    private int IDplayer;
    private int turns;

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
        this.turns = 0;
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
        }
        else
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
    public int getIDplayer() {
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
    public void decrementTokens(int used){
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
     * Update the turns variable when the player end a turn in a round
     * Turns can go from 0 to 2 in a round, then it will be reset
     *
     * @author Riccardo
     */
    public void addTurn() { this.turns++; }

    /**
     * Reset the turns variable when the round ends
     *
     * @author Riccardo
     */
    public void resetTurns() { this.turns = 0; }

    /**
     * Used to print a player
     *
     * @return the string that represents the player
     * @author Andrea
     */
    @Override
    public String toString(){
        return this.nickname+
                "\n Tokens available: "+this.tokens+"\nActive scheme:\n"+this.ownScheme;
    }

}