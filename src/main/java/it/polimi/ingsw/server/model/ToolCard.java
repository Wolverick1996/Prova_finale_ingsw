package it.polimi.ingsw.server.model;

import java.io.InputStream;
import java.util.Scanner;

/**
 * ToolCard represents the tool card: there are 12 in total and 3 of them are permanently on the table
 *
 * @author Riccardo
 */
public class ToolCard {
    private String name;
    private String description;
    private int tokens;
    private int cardID;

    /**
     * Constructor for the tool card
     *
     * @param cardID: the ID of the tool card to be created (1 to 12)
     * @throws IllegalArgumentException if ID passed as a parameter is not valid
     * @author Riccardo
     */
    public ToolCard(int cardID){
        if (cardID < 1 || cardID > ToolHandler.NUM_TOOLS)
            throw new IllegalArgumentException("Not valid ID: " + cardID);

        this.cardID = cardID;
        InputStream inputFile = PublicOC.class.getResourceAsStream("/cards/ToolCards.txt");
        Scanner scan = new Scanner(inputFile);

        for (int i=0; i<(cardID-1)*2; i++)
            scan.nextLine();

        this.name = scan.nextLine();
        this.description = scan.nextLine();
        this.tokens = 0;

        try {
            scan.close();
        } catch (Exception e1) {
            System.err.println("Error closing scan (ToolCard)");
        }
    }

    /**
     * Returns the tool card's name
     *
     * @return tool card's name
     * @author Riccardo
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the tool card's description
     *
     * @return tool card's description
     * @author Riccardo
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the number of tokens currently on the tool card
     *
     * @return the number of tokens currently on the tool card
     * @author Riccardo
     */
    public int getTokens() {
        return this.tokens;
    }

    /**
     * Defines a function to be override and executed in the tool effects array using lambda expressions
     *
     * @author Riccardo
     */
    interface UseEffect {

        /**
         * Function to be override and executed in the tool effects array using lambda expressions
         *
         * @param player: the player who wants to use the tool card
         * @param table: the instance of table (useful for certain tool cards)
         * @return true if the card is correctly used, otherwise false
         * @author Riccardo
         */
        boolean use(Player player, Table table);

    }

    private UseEffect[] useEffects = new UseEffect[] {

            //TOOL 1
            ((Player player, Table table) -> {
                boolean canExtract = table.getCanExtract();
                table.setCanExtract(true);
                Dice dice = extractFromReserve(player, table);

                //Dice extraction failed
                if (dice == null) {
                    table.setCanExtract(canExtract);
                    return false; }

                //If inserted value is not allowed the method returns false
                if (!modifyDiceValue(this.cardID, player, dice)) {
                    table.putDiceInReserve(dice);
                    return false; }

                table.putDiceInReserve(dice);
                return true;
            }),

            //TOOL 2 & 3
            ((Player player, Table table) -> {
                int[] coordOLD = getCoordinates(player);

                //Dice extraction failed
                if (!extractDice(player, coordOLD))
                    return false;

                int[] coordNEW = getCoordinates(player);

                //If placement fails the dice is reinserted into the previous box
                if (!moveDice(this.cardID, player, coordNEW)) {
                    player.getOwnScheme().placeDice(coordOLD[0], coordOLD[1], player.getDiceInHand());
                    return false; }

                return true;
            }),

            //TOOL 4
            (Player player, Table table) -> !twoDiceMovement(this.cardID, player, null),

            //TOOL 5
            ((Player player, Table table) -> {
                boolean canExtract = table.getCanExtract();
                table.setCanExtract(true);
                Dice dice1 = extractFromReserve(player, table);

                //Dice extraction failed
                if (dice1 == null) {
                    table.setCanExtract(canExtract);
                    return false; }

                Dice dice2 = chooseFromRoundtrack(this.cardID, player, table);

                //Dice extraction failed: the first dice is reinserted in the reserve
                if (dice2 == null) {
                    table.putDiceInReserve(dice1);
                    return false; }

                table.putDiceInRoundtrack(dice1);
                table.putDiceInReserve(dice2);
                return true;
            }),

            //TOOL 6
            ((Player player, Table table) -> {
                boolean canExtract = table.getCanExtract();
                table.setCanExtract(true);
                Dice dice = extractFromReserve(player, table);

                //Dice extraction failed
                if (dice == null) {
                    table.setCanExtract(canExtract);
                    return false; }

                dice.rollDice();
                if (isPlaceable(dice, player) == null) {
                    System.out.println("You can't place the dice in your window pattern.\nThe dice will be reinserted in the reserve");
                    table.putDiceInReserve(dice);
                    table.setCanExtract(canExtract);
                } else {
                    System.out.println(isPlaceable(dice, player));

                    int[] coord = getCoordinates(player);

                    if (canExtract) {
                        //Placement of the extracted dice: if it fails the dice is putted in the reserve
                        //NOTE: if the placement fails the method should not return false because the dice roll affects the game
                        if (!player.getOwnScheme().placeDice(coord[0], coord[1], dice))
                            table.putDiceInReserve(dice);
                        else
                            table.setCanExtract(false);
                    }
                }

                return true;
            }),

            //TOOL 7
            ((Player player, Table table) -> {
                if (table.getRealTurn() < table.getActivePlayers().size())
                    return false;

                table.rerollReserve();
                return true;
            }),

            //TOOL 8
            ((Player player, Table table) -> {
                //NOTE: This tool card will affect the controller turn logic
                if (table.getRealTurn() > table.getActivePlayers().size())
                    return false;

                ToolHandler.tool8();
                return true;
            }),

            //TOOL 9
            ((Player player, Table table) -> {
                boolean canExtract = table.getCanExtract();
                //If player has already placed in this turn tool 9 can't be used
                if (!canExtract)
                    return false;

                Dice dice = extractFromReserve(player, table);

                //Dice extraction failed
                if (dice == null)
                    return false;

                int[] coord = getCoordinates(player);

                //Placement of the extracted dice: if it fails the dice is putted in the reserve
                if (!player.getOwnScheme().placeDice(coord[0], coord[1], dice)) {
                    System.out.println("Placement not allowed, " + dice + " is now in the reserve");
                    table.putDiceInReserve(dice);
                    return false;
                } else
                    table.setCanExtract(false);

                return true;
            }),

            //TOOL 10
            ((Player player, Table table) -> {
                boolean canExtract = table.getCanExtract();
                table.setCanExtract(true);
                Dice dice = extractFromReserve(player, table);

                //Dice extraction failed
                if (dice == null) {
                    table.setCanExtract(canExtract);
                    return false; }

                dice.turnDice();
                table.putDiceInReserve(dice);
                return true;
            }),

            //TOOL 11
            ((Player player, Table table) -> {
                boolean canExtract = table.getCanExtract();
                table.setCanExtract(true);
                putDiceInBag(player, table);
                Dice dice = table.pickDiceFromBag();
                System.out.println("Dice extracted: " + dice);

                //If inserted value is not allowed the method returns true because there was a change in the game
                if (!modifyDiceValue(this.cardID, player, dice)) {
                    System.out.println("Placement not allowed, " + dice + " is now in the reserve");
                    table.putDiceInReserve(dice);
                    table.setCanExtract(canExtract);
                    return true; }

                int[] coord = getCoordinates(player);

                if (canExtract) {
                    //Placement of the extracted dice: if it fails the dice is putted in the reserve
                    //NOTE: if the placement fails the method should not return false because there was a change in the game
                    if (!player.getOwnScheme().placeDice(coord[0], coord[1], dice)) {
                        System.out.println("Placement not allowed, " + dice + " is now in the reserve");
                        table.putDiceInReserve(dice);
                    } else
                        table.setCanExtract(false);
                }

                return true;
            }),

            //TOOL 12
            ((Player player, Table table) -> {
                Dice dice = chooseFromRoundtrack(this.cardID, player, table);

                //Dice extraction failed
                if (dice == null)
                    return false;

                return (!twoDiceMovement(this.cardID, player, dice.getColor()));
            })

    };

    /**
     * Receives a player and the table as parameters and calls UseEffects[] methods
     *
     * @param player: the player who wants to use the tool card
     * @param table: the instance of table (useful for certain tool cards)
     * @return true if the card is correctly used, otherwise false
     * @author Riccardo
     */
    public boolean toolEffect(Player player, Table table){
        //If player doesn't have enough tokens the tool card can't be used
        if ((player.getTokens() == 1 && this.tokens > 0) || player.getTokens() == 0){
            System.out.println("You don't have enough tokens!");
            return false; }

        int equals = this.cardID-1;

        //Tool cards 2 and 3 are grouped together
        if (this.cardID >= 3)
            equals--;

        boolean correctlyUsed = useEffects[equals].use(player, table);

        //Incrementing tool card's tokens and decrementing player's tokens
        if (correctlyUsed)
            incrementTokens(player);
        return correctlyUsed;
    }

    /**
     * Lets the player choose (correct) coordinates
     *
     * @param player: the player who has to choose coordinates
     * @return an integer array containing coordinates
     * @author Riccardo
     */
    private int[] getCoordinates(Player player){
        int[] coordinates = new int[2];
        int x;
        int y;

        do {
            x = ToolHandler.getCoordinates("x", player);
            y = ToolHandler.getCoordinates("y", player);
        } while (!(0 <= x && x <= Scheme.MAX_ROW-1) || !(0 <= y && y <= Scheme.MAX_COL-1));
        coordinates[0] = x;
        coordinates[1] = y;

        return coordinates;
    }

    /**
     * The method allows player to move two dice of his window pattern (TOOLS 4, 12)
     * If at least one placement fails, the method returns false and the grid returns to the starting situation
     * If the tool card used is the 12, dice chosen must have the same color of the one chosen from round track
     *
     * @param index: tool card's ID
     * @param player: the player who wants to use the tool card
     * @param color: the color of dice chosen from round track (null for the tool card 4)
     * @return true if dice are correctly moved, otherwise false
     * @author Riccardo
     */
    private boolean twoDiceMovement(int index, Player player, Enum.Color color){
        Dice dice;

        int[] coord1OLD;
        int[] coord1NEW;
        int[] coord2OLD;
        int[] coord2NEW;

        coord1OLD = getCoordinates(player);

        //First dice extraction failed
        if (!extractDice(player, coord1OLD))
            return false;

        //Dice should have the same color of the one chosen from round track
        //Loop of extractions until the color will coincide
        //If extraction fails the method returns false
        if (index == 12){
            while (player.getDiceInHand().getColor() != color) {
                System.out.println("You have to choose a dice with the same color of the one chosen from round track");

                if (!extractDice(player, coord1OLD))
                    return false;
            }
        }

        coord1NEW = getCoordinates(player);

        //If first placement fails the dice is reinserted into the previous box and the method returns false
        if (!moveDice(this.cardID, player, coord1NEW)){
            player.getOwnScheme().placeDice(coord1OLD[0], coord1OLD[1], player.getDiceInHand());
            return false; }

        //TODO: Let the player choose if move another dice or not (tool 12); return true if he wants to stop

        coord2OLD = getCoordinates(player);

        //Second dice extraction failed: the first dice placed is removed and reinserted into the previous box
        if (!extractDice(player, coord2OLD)){
            dice = player.getOwnScheme().removeDice(coord1NEW[0], coord1NEW[1]);
            player.getOwnScheme().placeDice(coord1OLD[0], coord1OLD[1], dice);
            return false; }

        //Dice should have the same color of the one chosen from round track
        //Loop of extractions until the color will coincide
        //If extraction fails the first dice placed is removed and reinserted into the previous box and the method returns false
        if (index == 12){
            while (player.getDiceInHand().getColor() != color) {
                System.out.println("You have to choose a dice with the same color of the one chosen from round track");

                if (!extractDice(player, coord2OLD)){
                    dice = player.getOwnScheme().removeDice(coord1NEW[0], coord1NEW[1]);
                    player.getOwnScheme().placeDice(coord1OLD[0], coord1OLD[1], dice);
                    return false; }
            }
        }

        coord2NEW = getCoordinates(player);

        //If second placement fails the dice is reinserted into the previous box and the method returns false
        //The first dice placed is also removed and reinserted into the previous box
        if (!moveDice(this.cardID, player, coord2NEW)){
            player.getOwnScheme().placeDice(coord2OLD[0], coord2OLD[1], player.getDiceInHand());
            dice = player.getOwnScheme().removeDice(coord1NEW[0], coord1NEW[1]);
            player.getOwnScheme().placeDice(coord1OLD[0], coord1OLD[1], dice);
            return false; }

        return true;
    }

    /**
     * Lets the player choose a dice of the round track
     * The dice chosen is extracted from the round track (TOOL 5) or simply returned without being extracted (TOOL 12)
     *
     * @param index: tool card's ID
     * @param player: the player who wants to use the tool card
     * @param table: the instance of table
     * @return the dice chosen (null if it not exists)
     * @author Riccardo
     */
    private Dice chooseFromRoundtrack(int index, Player player, Table table){
        int dicePos = ToolHandler.getFromRoundtrack(player);

        if (index == 5)
            return table.pickDiceFromRoundtrack(dicePos);
        else    //index == 12
            return table.checkDiceFromRoundtrack(dicePos);
    }

    /**
     * Lets the player choose a dice of the reserve to be extracted (TOOLS 1, 5, 6, 10)
     *
     * @param player: the player who wants to use the tool card
     * @param table: the instance of table
     * @return the dice chosen (null if it not exists)
     * @author Riccardo
     */
    private Dice extractFromReserve(Player player, Table table){
        int dicePos = ToolHandler.getFromReserve(player);

        return table.pickDiceFromReserve(dicePos);
    }

    /**
     * Extracts a dice from the player's window pattern (TOOLS 2, 3, 9, 12) and puts it in player's hand
     *
     * @param player: the player who wants to use the tool card
     * @param coordinates: coordinates of the dice to extract
     * @author Riccardo
     */
    private boolean extractDice(Player player, int[] coordinates){
        return player.extractDice(coordinates[0], coordinates[1]);
    }

    /**
     * The method modifies the value of a dice adding/subtracting 1 (TOOL 1) or letting the user choose a value (TOOL 11)
     *
     * @param index: tool card's ID
     * @param player: the player who wants to use the tool card
     * @param dice: the dice whose value must be changed
     * @return true if the operation is allowed, otherwise false
     * @author Riccardo
     */
    private boolean modifyDiceValue(int index, Player player, Dice dice){
        //NOTE: for the tool card 1 it MUST be +/-1, for the tool card 11 it MUST be a dice value (1 to 6)
        Boolean restricted = false;
        if (this.cardID == 1)
            restricted = true;

        int value = ToolHandler.getDiceValue(restricted, player);

        //Return false if the value in incompatible with the tool card
        if (index == 1 && (value != +1 && value != -1))
            return false;
        if (index == 11 && !(1 <= value && value <= 6))
            return false;

        if (index == 1) {
            if ((dice.getValue() == 1 && value == -1) || (dice.getValue() == 6 && value == +1)) {
                System.out.println("Operation not allowed!");
                return false;
            } else
                dice.assignValue(dice.getValue() + value);
        } else   //index == 11
            dice.assignValue(value);

        return true;
    }

    /**
     * The method allows the player to place a dice already extracted (TOOLS 2, 3, 4, 9, 12)
     *
     * @param index: tool card's ID
     * @param player: the player who wants to use the tool card
     * @param coordinates: coordinates of the dice to place
     * @return Scheme placing methods results (true if the dice is correctly placed, otherwise false)
     * @author Riccardo
     */
    private boolean moveDice(int index, Player player, int[] coordinates){
        if (index == 4 || index == 12)
            return player.getOwnScheme().placeDice(coordinates[0], coordinates[1], player.getDiceInHand());

        //index == 2, 3, 9
        return player.getOwnScheme().placeFromTool(coordinates[0], coordinates[1], index, player.getDiceInHand());
    }

    /**
     * Checks if a dice is placeable in some boxes of the player's grid and returns the list of them (TOOL 6)
     *
     * @param dice: the dice to be placed in the box
     * @param player: the player who wants to use the tool card
     * @return the string containing the list of available positions
     * @author Riccardo
     */
    private String isPlaceable(Dice dice, Player player) {
        String s = "You can place the dice in the following boxes: ";
        int flag = 0;

        for (int i = 0; i < Scheme.MAX_ROW; i++) {
            for (int j = 0; j < Scheme.MAX_COL; j++) {
                if (player.getOwnScheme().isPlaceable(i, j, dice)) {
                    s = s + "[" + i+1 + ", " + j+1 + "]\t";
                    flag = 1;
                }
            }
        }

        if (flag == 0)
            return null;
        else
            return s;
    }

    /**
     * Lets the player choose a dice of the reserve that will be reinserted into the bag (TOOL 11)
     *
     * @param player: the player who wants to use the tool card
     * @param table: the instance of table
     * @author Riccardo
     */
    private void putDiceInBag(Player player, Table table){
        int dicePos = ToolHandler.getFromReserve(player);

        table.putDiceInBag(dicePos);
    }

    /**
     * Increments tool card's tokens and calls the method to decrement player's tokens
     *
     * @param player: the player who used the tool card
     * @author Riccardo
     */
    private void incrementTokens(Player player){
        player.decrementTokens(this.tokens);

        if (this.tokens == 0)
            this.tokens++;
        else
            this.tokens += 2;
    }

    /**
     * Used to print a tool card
     *
     * @return the string that represents the tool card
     * @author Riccardo
     */
    @Override
    public String toString() { return this.name + " : " + this.description + "\nTokens currently on the card : " + this.tokens; }

}