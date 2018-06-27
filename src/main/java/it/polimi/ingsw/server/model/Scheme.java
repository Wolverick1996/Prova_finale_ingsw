package it.polimi.ingsw.server.model;

import java.util.*;
import java.io.*;

/**
 * Scheme represents the 5x4 window pattern of each player
 *
 * @author Andrea
 * @author Riccardo
 */
public class Scheme extends Observable {

    //***************************//
    //        Attributes         //
    //***************************//

    private String name;
    private int difficulty;
    static final int MAX_COL = 5;
    static final int MAX_ROW = 4;
    private static final int STD_NUM_SCHEMES = 24;
    private Box[][] grid = new Box[MAX_ROW][MAX_COL];

    /**
     * Constructor for the window pattern which reads from file the specific grid to create
     *
     * @param id: identification number of the window pattern to be created (id range 1-24)
     * @param custom: flag useful for custom window pattern advanced functionality (true if player wants to use custom window pattern, otherwise false)
     * @author Riccardo
     */
    protected Scheme (int id, boolean custom){
        if (id > STD_NUM_SCHEMES && !custom)
            System.err.println("Not valid ID: " + id);

        InputStream inputFile;
        if (id <= STD_NUM_SCHEMES)
            inputFile = Scheme.class.getResourceAsStream("/schemes/Schemes.txt");
        else
            inputFile = Scheme.class.getResourceAsStream("/schemes/CustomSchemes.txt");

        String s;
        char [][] c = new char[MAX_ROW][MAX_COL];

        Scanner scan = new Scanner(inputFile);

        if (id <= STD_NUM_SCHEMES) {
            for (int i = 0; i < (id - 1) * 6; i++)
                scan.nextLine();
        } else {
            for (int i = 0; i < (id - 25) * 6; i++)
                scan.nextLine();
        }

        this.name = scan.nextLine();
        this.difficulty = Integer.parseInt(scan.nextLine());

        for (int i=0; i<MAX_ROW; i++){
            s = scan.nextLine();
            c[i] = s.toCharArray();
        }

        for (int i=0; i<MAX_ROW; i++){
            for (int j=0; j<MAX_COL; j++){
                switch(c[i][j]){
                    case 'r': grid[i][j] = new Box(Enum.Color.RED); break;
                    case 'g': grid[i][j] = new Box(Enum.Color.GREEN); break;
                    case 'y': grid[i][j] = new Box(Enum.Color.YELLOW); break;
                    case 'b': grid[i][j] = new Box(Enum.Color.BLUE); break;
                    case 'p': grid[i][j] = new Box(Enum.Color.PURPLE); break;
                    case '1': case '2': case '3': case '4': case '5': case '6':
                        grid[i][j] = new Box(Character.getNumericValue(c[i][j])); break;
                    default: grid[i][j] = new Box(); break;
                }
            }
        }
        try {
            scan.close();
        } catch (Exception e1) {
            System.err.println("Error closing scan (Scheme)");
        }
    }

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Calls the constructor and gives it the ID to create a window pattern if it's a correct one
     *
     * @param id: identification number of the window pattern to be created
     * @param custom: flag useful for custom window pattern advanced functionality (true if player wants to use custom window pattern, otherwise false)
     * @param numSchemes: number of window pattern to be used (>24 just if player decides to use custom window patterns)
     * @return the specific window pattern required if it exists, otherwise null
     * @author Riccardo
     */
    public static Scheme initialize(int id, boolean custom, int numSchemes){
        if (id < 1 || id > numSchemes) {
            System.err.println("Not valid ID: " + id);
            return null;
        } else
            return new Scheme(id, custom);
    }

    /**
     * Checks which dice is in a specific box
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @return the dice in the specified box (null if the box is empty)
     * @author Andrea
     */
    public Dice checkBox(int x, int y){ return this.grid[x][y].getDice(); }

    /**
     * Checks if color restrictions inherent to the first dice placement are correctly respected
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param dice: the dice to be placed in the box
     * @param print: specifies if the method should print errors to the player or not
     * @return true if the placement is allowed, otherwise false
     * @author Riccardo
     */
    private boolean firstDiceNoNum(int x, int y, Dice dice, boolean print){
        if (grid[x][y].isEmployableNoNum(dice, print)
                && ((x == 0) || (y == 0) || (x == MAX_ROW - 1) || (y == MAX_COL - 1)))
            return true;
        else {
            if (!((x == 0) || (y == 0) || (x == MAX_ROW - 1) || (y == MAX_COL - 1)) && print)
                notifyObservers("You can't place the first dice out of border");
            return false;
        }
    }

    /**
     * Checks if value restrictions inherent to the first dice placement are correctly respected
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param dice: the dice to be placed in the box
     * @param print: specifies if the method should print errors to the player or not
     * @return true if the placement is allowed, otherwise false
     * @author Riccardo
     */
    private boolean firstDiceNoCol(int x, int y, Dice dice, boolean print){
        if (grid[x][y].isEmployableNoCol(dice, print)
                && ((x == 0) || (y == 0) || (x == MAX_ROW - 1) || (y == MAX_COL - 1)))
            return true;
        else {
            if (!((x == 0) || (y == 0) || (x == MAX_ROW - 1) || (y == MAX_COL - 1)) && print)
                notifyObservers("You can't place the first dice out of border");
            return false;
        }
    }

    /**
     * Checks if restrictions inherent to the first dice placement are correctly respected
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param dice: the dice to be placed in the box
     * @param print: specifies if the methods called should print errors to the player or not
     * @return true if the placement is allowed, otherwise false
     * @author Andrea
     */
    private boolean firstDice(int x, int y, Dice dice, boolean print){
        return (firstDiceNoCol(x, y, dice, print) && firstDiceNoNum(x, y, dice, print));
    }

    /**
     * Checks if value restrictions inherent a dice placement are correctly respected
     * Color restrictions are ignored (useful for tool card 3)
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param dice: the dice to be placed in the box
     * @param print: specifies if the method and methods called should print errors to the player or not
     * @return true if the placement is allowed, otherwise false
     * @author Andrea
     */
    private boolean checkValueRestr(int x, int y, Dice dice, boolean print){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        if ((x < 0 || x >= MAX_ROW) || (y < 0 || y >= MAX_COL)) {
            notifyObservers("This box doesn't exists");
            return false; }

        //If first dice check if it respects first-dice-placement-rule
        if (isGridEmpty())
            return firstDiceNoCol(x, y, dice, print);

        //Algorithm to find if the dice respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            if (i >= 0 && i < MAX_ROW) {
                for (j = y - 1; j <= y + 1; j++) {
                    if (!(i == x && j == y) && j >= 0 && j < MAX_COL) {
                        if (grid[i][j].isFull())
                            diceNear = true;
                        if ((j == y || i == x) && grid[i][j].isFull()) {
                            if (grid[i][j].getDice().getValue() == dice.getValue()) {
                                placeable = false;
                            }
                        }
                    }
                }
            }
        }

        if (!diceNear) {
            if (print)
                notifyObservers("You can't place the dice in a box not adjacent to another full one");
            return false;
        } else if (placeable && diceNear) {
            //Check if dice respects box restrictions
            return grid[x][y].isEmployableNoCol(dice, print);
        }

        if (print)
            notifyObservers("There is another dice near with the same value!");
        return false;
    }

    /**
     * Checks if color restrictions inherent a dice placement are correctly respected
     * Value restrictions are ignored (useful for tool card 2)
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param dice: the dice to be placed in the box
     * @param print: specifies if the method and methods called should print errors to the player or not
     * @return true if the placement is allowed, otherwise false
     * @author Andrea
     */
    private boolean checkColorRestr(int x, int y, Dice dice, boolean print){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        if ((x < 0 || x >= MAX_ROW) || (y < 0 || y >= MAX_COL)){
            notifyObservers("This box doesn't exists");
            return false; }

        //If first dice check if it respects first-dice-placement-rule
        if (isGridEmpty())
            return firstDiceNoNum(x, y, dice, print);

        //Algorithm to find if the dice respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            if (i >= 0 && i < MAX_ROW){
                for (j = y - 1; j <= y + 1; j++) {
                    if (!(i == x && j == y) && j >= 0 && j < MAX_COL) {
                        if (grid[i][j].isFull())
                            diceNear = true;
                        if ((j == y || i == x) && grid[i][j].isFull()) {
                            if (grid[i][j].getDice().getColor() == dice.getColor()) {
                                placeable = false;
                            }
                        }
                    }
                }
            }
        }

        if (!diceNear) {
            if (print)
                notifyObservers("You can't place the dice in a box not adjacent to another full one");
            return false;
        } else if (placeable && diceNear) {
            //Check if dice respects box restrictions
            return grid[x][y].isEmployableNoNum(dice, print);
        }

        if (print)
            notifyObservers("There is another dice near with the same color!");
        return false;
    }

    /**
     * Checks if color and value restrictions inherent a dice placement are correctly respected ignoring the dice-near rule
     * Dice-near rule ignoring is useful for tool card 9
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param dice: the dice to be placed in the box
     * @return true if the placement is allowed, otherwise false
     * @author Andrea
     */
    private boolean isPlaceableNoDiceNear(int x, int y, Dice dice){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        if ((x < 0 || x >= MAX_ROW) || (y < 0 || y >= MAX_COL)) {
            notifyObservers("This box doesn't exists");
            return false; }

        //If first dice check if it respects first-dice-placement-rule
        if (isGridEmpty())
            return firstDice(x, y, dice, true);

        //Algorithm to find if the dice respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            if (i >= 0 && i < MAX_ROW){
                for (j = y - 1; j <= y + 1; j++) {
                    if (!(i == x && j == y) && j >= 0 && j < MAX_COL) {
                        if (grid[i][j].isFull())
                            diceNear = true;
                        if ((j == y || i == x) && grid[i][j].isFull()) {
                            if ((grid[i][j].getDice().getValue() == dice.getValue()) ||
                                    grid[i][j].getDice().getColor() == dice.getColor()) {
                                placeable = false;
                            }
                        }
                    }
                }
            }
        }

        if (diceNear) {
            notifyObservers("You can't place the dice in a box adjacent to another full one using this tool card");
            return false;
        } else if (placeable && !diceNear) {
            //Check if dice respects box restrictions
            return !(!grid[x][y].isEmployableNoNum(dice, true) || !grid[x][y].isEmployableNoCol(dice, true));
        }

        return true;
    }

    /**
     * Placement of a dice in a specific box using tool cards (and ignoring specific rules according to the effect of the card)
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param id: identifier of the tool card used
     * @param dice: the dice to be placed in the box
     * @return Box setDice method result (true if the dice is correctly placed, false if the box is already full)
     * @author Riccardo
     */
    public boolean placeFromTool(int x, int y, int id, Dice dice){

        if (id == 2) {
            if (!(checkValueRestr(x, y, dice, true)))
                return false;
        } else if (id == 3) {
            if (!(checkColorRestr(x, y, dice, true)))
                return false;
        } else if (id == 9) {
            if (!(isPlaceableNoDiceNear(x, y, dice)))
                return false;
        }

        return grid[x][y].setDice(dice);
    }

    /**
     * Simple method that calls methods to check restrictions and returns true if the dice is placeable
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param dice: the dice to be placed in the box
     * @param print: specifies if the methods called should print errors to the player or not
     * @return false if the dice is not placeable cause of value or color restrictions, otherwise true
     * @author Riccardo
     */
    boolean isPlaceable(int x, int y, Dice dice, boolean print){
        return !(!checkValueRestr(x, y, dice, print) || !checkColorRestr(x, y, dice, print));
    }

    /**
     * Standard placement of a dice in a specific box (normal move of the game, without using tool cards)
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param dice: the dice to be placed in the box
     * @return Box setDice method result (true if the dice is correctly placed, false if the box is already full)
     * @author Andrea
     */
    public boolean placeDice(int x, int y, Dice dice){
        if (!isPlaceable(x, y, dice, true))
            return false;

        return grid[x][y].setDice(dice);
    }

    /**
     * Checks if the grid of the window pattern is empty
     *
     * @return true if the grid is empty, otherwise false
     * @author Andrea
     */
    public boolean isGridEmpty(){
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                if (this.grid[i][j].isFull()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Removes a dice from a specific box of the grid
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @return Box free method result (the dice in the box before the calling of the method, otherwise null)
     * @author Andrea
     */
    public Dice removeDice(int x, int y){
        return grid[x][y].free();
    }

    /**
     * Gets the name of the window pattern
     *
     * @return the name of the window pattern
     * @author Andrea
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the degree of difficulty of the window pattern
     *
     * @return the degree of difficulty of the window pattern
     * @author Andrea
     */
    public int getDifficulty(){
        return this.difficulty;
    }

    /**
     * Gets the actual situation of the grid of the window pattern
     *
     * @return the grid at the time the method is called
     * @author Andrea
     */
    Box[][] getGrid(){
        return grid;
    }

    /**
     * Used to print a window pattern
     *
     * @return the string that represents the window pattern
     * @author Riccardo
     */
    @Override
    public String toString(){
        String s = "";
        for (int i = 0; i <= MAX_ROW; i++) {
            for (int j = 0; j <= MAX_COL; j++){
                if (i == 0 && j == 0)
                    s = s + "\t";
                else if (i == 0)
                    s = s + " "+j+"\t";
                else if (j == 0)
                    s = s + " "+i+"\t";
                else
                    s = s+grid[i-1][j-1];
            }

            s = s+"\n";
        }
        return Enum.Color.BLUE.escape() + "Name: " + Enum.Color.RESET + this.name + Enum.Color.BLUE.escape() +
                "\nDifficulty: " + Enum.Color.RESET + this.difficulty + "\n" + s;
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
        for (Box[] B: grid){
            for (Box b: B){
                b.addObserver(o);
            }
        }
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