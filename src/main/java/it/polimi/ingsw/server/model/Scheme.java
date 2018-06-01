package it.polimi.ingsw.server.model;

import java.util.*;
import java.io.*;

/**
 * Scheme represents the 5x4 window pattern of each player
 *
 * @author Andrea
 * @author Riccardo
 */
public class Scheme {

    //***************************//
    //        Attributes         //
    //***************************//

    private String name;
    private int difficulty;
    static final int MAX_COL = 5;
    static final int MAX_ROW = 4;

    private Box[][] grid = new Box[MAX_ROW][MAX_COL];

    /**
     * Constructor for the window pattern which reads from file the specific grid to create
     *
     * @param id: identification number of the window pattern to be created (id range 1-24)
     * @author Riccardo
     */
    protected Scheme (int id){
        File inputFile = new File("src/main/resources/schemes/Schemes.txt");
        Scanner scan = null;
        String s;
        char [][] c = new char[MAX_ROW][MAX_COL];

        try {
            scan = new Scanner(inputFile);

            for (int i=0; i<(id-1)*6; i++)
                scan.nextLine();

            this.name = scan.nextLine();
            this.difficulty = Integer.parseInt(scan.nextLine());

            for (int i=0; i<MAX_ROW; i++) {
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
        } catch (IOException e){
            System.err.println("Error");
        }
        finally {
            if (scan != null) {
                try {
                    scan.close();
                } catch (Exception e1) {
                    System.err.println("Error");
                }
            }

        }

    }

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Calls the constructor and gives it the ID to create a window pattern if it's a correct one
     *
     * @param id: identification number of the window pattern to be created
     * @return the specific window pattern required if it exists, otherwise null
     * @author Riccardo
     */
    public static Scheme initialize(int id){
        if (id < 1 || id >24){
            System.err.println("ID not valid!");
            return null;
        } else
            return new Scheme(id);
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
     * Checks if restrictions inherent to the first dice placement are correctly respected
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param dice: the dice to be placed in the box
     * @return true if the placement is allowed, otherwise false
     * @author Andrea
     */
    private boolean firstDice(int x, int y, Dice dice){
        if (grid[x][y].isEmployableNoNum(dice) && grid[x][y].isEmployableNoCol(dice)
                && ((x == 0) || (y == 0) || (x == MAX_ROW - 1) || (y == MAX_COL - 1)))
            return true;
        else {
            if (!((x == 0) || (y == 0) || (x == MAX_ROW - 1) || (y == MAX_COL - 1)))
                System.err.println("You can't place the first dice out of border");
            return false;
        }
    }

    /**
     * Checks if value restrictions inherent a dice placement are correctly respected
     * Color restrictions are ignored (useful for tool card 3)
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param dice: the dice to be placed in the box
     * @return true if the placement is allowed, otherwise false
     * @author Andrea
     */
    private boolean checkValueRestr(int x, int y, Dice dice){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        if ((x < 0 || x >= MAX_ROW) || (y < 0 || y >= MAX_COL)){
            System.err.println("This box doesn't exists");
            return false; }

        //If first dice check if it respects first-dice-placement-rule
        if (isGridEmpty())
            return firstDice(x, y, dice);

        //Algorithm to find if the dice respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            if (i >= 0 && i < MAX_ROW){
                for (j = y - 1; j <= y + 1; j++) {
                    if (!(i == x && j == y) && j >= 0 && j < MAX_COL){
                        if (grid[i][j].isFull()) {
                            diceNear = true;
                        }
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
            System.err.println("You can't place the dice in a box not adjacent to another full one");
            return false;
        } else if (placeable && diceNear) {
            //Check if dice respects box restrictions
            if (!grid[x][y].isEmployableNoCol(dice))
                return false;
            else
                return true;
        }

        System.err.println("There is another dice near with the same value!");
        return false;
    }

    /**
     * Checks if color restrictions inherent a dice placement are correctly respected
     * Value restrictions are ignored (useful for tool card 2)
     *
     * @param x: identifier of the row number
     * @param y: identifier of the column number
     * @param dice: the dice to be placed in the box
     * @return true if the placement is allowed, otherwise false
     * @author Andrea
     */
    private boolean checkColorRestr(int x, int y, Dice dice){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        if ((x < 0 || x >= MAX_ROW) || (y < 0 || y >= MAX_COL)){
            System.err.println("This box doesn't exists");
            return false; }

        //If first dice check if it respects first-dice-placement-rule
        if (isGridEmpty())
            return firstDice(x, y, dice);

        //Algorithm to find if the dice respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            if (i >= 0 && i < MAX_ROW){
                for (j = y - 1; j <= y + 1; j++) {
                    if (!(i == x && j == y) && j >= 0 && j < MAX_COL) {
                        if (grid[i][j].isFull()) {
                                diceNear = true;
                            }
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
            System.err.println("You can't place the dice in a box not adjacent to another full one");
            return false;
        } else if (placeable && diceNear) {
            //Check if dice respects box restrictions
            if (!grid[x][y].isEmployableNoNum(dice))
                return false;
            else
                return true;
        }

        System.err.println("There is another dice near with the same color!");
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
            System.err.println("This box doesn't exists");
            return false; }

        //If first dice check if it respects first-dice-placement-rule
        if (isGridEmpty())
            return firstDice(x, y, dice);

        //Algorithm to find if the dice respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            if (i >= 0 && i < MAX_ROW){
                for (j = y - 1; j <= y + 1; j++) {
                    if (!(i == x && j == y) && j >= 0 && j < MAX_COL) {
                        if (grid[i][j].isFull()) {
                            diceNear = true;
                        }
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
            System.err.println
                    ("You can't place the dice in a box adjacent to another full one using this tool card");
            return false;
        } else if (placeable && !diceNear) {
            //Check if dice respects box restrictions
            if (!grid[x][y].isEmployableNoNum(dice) || !grid[x][y].isEmployableNoCol(dice))
                return false;
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
            if (!(checkValueRestr(x, y, dice)))
                return false;
        } else if (id == 3) {
            if (!(checkColorRestr(x, y, dice)))
                return false;
        } else if (id == 9) {
            if (!(isPlaceableNoDiceNear(x, y, dice)))
                return false;
        }

        return grid[x][y].setDice(dice);
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
        if (!checkValueRestr(x, y, dice) || !checkColorRestr(x, y, dice))
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
    Box[][] getGrid() {
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
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++)
                s = s+grid[i][j];

            s = s+"\n";
        }
        return "Name: "+this.name+"\nDifficulty: "+this.difficulty+"\n"+s;
    }

}