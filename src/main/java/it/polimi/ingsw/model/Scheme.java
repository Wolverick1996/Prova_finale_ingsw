package it.polimi.ingsw.model;

import java.util.*;
import java.io.*;

public class Scheme {

    //***************************//
    //        Attributes         //
    //***************************//

    private String name;
    private int difficulty;
    public static final int MAX_COL = 5;
    public static final int MAX_ROW = 4;

    private Box[][] grid = new Box[MAX_ROW][MAX_COL];

    //constructor, takes the ID and call a HandlerMethod in controller
    protected Scheme (int id){
        File inputFile = new File("src/main/resources/schemes/Schemes.txt");
        Scanner scan = null;
        String s;
        char [][] c = new char[4][5];

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
            System.out.println("Error");
        }
        finally {
            if (scan != null) {
                try {
                    scan.close();
                } catch (Exception e1) {
                    System.out.println("Error");
                }
            }

        }

    }

    //***************************//
    //         Methods           //
    //***************************//

    //Call the constructor if ID is correct (static ???)
    public static Scheme initialize(int id){
        if (id < 1 || id >24){
            System.out.println("ID not valid!");
            return null;
        } else
            return new Scheme(id);
    }

    //Check what dice is in a box
    public Dice checkBox(int x, int y){ return this.grid[x][y].getDice(); }

    //Placement of the first dice
    private boolean firstDice(int x, int y, Dice dice){
        if (grid[x][y].isEmployableNoNum(dice) && grid[x][y].isEmployableNoCol(dice)
                && ((x == 0) || (y == 0) || (x == MAX_ROW - 1) || (y == MAX_COL - 1)))
            return true;
        else {
            if (!((x == 0) || (y == 0) || (x == MAX_ROW - 1) || (y == MAX_COL - 1)))
                System.out.println("You can't place the first dice out of border");
            return false;
        }
    }

    //Check if a dice is placeable in a box ignoring color restrictions (TOOL3)
    protected boolean checkValueRestr(int x, int y, Dice dice){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        if ((x < 0 || x >= MAX_ROW) || (y < 0 || y >= MAX_COL)){
            System.out.println("This box doesn't exists");
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
            System.out.println("You can't place the dice in a box not adjacent to another full one");
            return false;
        } else if (placeable && diceNear) {
            //Check if dice respects box restrictions
            if (!grid[x][y].isEmployableNoCol(dice))
                return false;
            else
                return true;
        }

        System.out.println("There is another dice near with the same value!");
        return false;
    }

    //Check if a dice is placeable in a box ignoring value restrictions (TOOL2)
    protected boolean checkColorRestr(int x, int y, Dice dice){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        if ((x < 0 || x >= MAX_ROW) || (y < 0 || y >= MAX_COL)){
            System.out.println("This box doesn't exists");
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
            System.out.println("You can't place the dice in a box not adjacent to another full one");
            return false;
        } else if (placeable && diceNear) {
            //Check if dice respects box restrictions
            if (!grid[x][y].isEmployableNoNum(dice))
                return false;
            else
                return true;
        }

        System.out.println("There is another dice near with the same color!");
        return false;
    }

    //Check if a dice is placeable ignoring dice-near-rule (TOOL 9)
    protected boolean isPlaceableNoDiceNear(int x, int y, Dice dice){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        if ((x < 0 || x >= MAX_ROW) || (y < 0 || y >= MAX_COL)) {
            System.out.println("This box doesn't exists");
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
            System.out.println
                    ("You can't place the dice in a box adjacent to another full one using this tool card");
            return false;
        } else if (placeable && !diceNear) {
            //Check if dice respects box restrictions
            if (!grid[x][y].isEmployableNoNum(dice) || !grid[x][y].isEmployableNoCol(dice))
                return false;
        }

        return true;
    }

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

    //Set dice
    public boolean placeDice(int x, int y, Dice dice){
        if (!checkValueRestr(x, y, dice) || !checkColorRestr(x, y, dice))
            return false;

        return grid[x][y].setDice(dice);
    }

    //Check if the grid is empty
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

    //Remove dice from the grid
    public Dice removeDice(int x, int y){
        if(!grid[x][y].isFull())
            return null;
        else
            return grid[x][y].free();
    }

    //Get name of the scheme
    public String getName(){
        return this.name;
    }

    //Get the difficulty
    public int getDifficulty(){
        return this.difficulty;
    }

    //Get grid
    public Box[][] getGrid() {
        return grid;
    }

    @Override
    public String toString(){
        String s = "";
        for (int i=0; i<MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++)
                s = s+grid[i][j];

            s = s+"\n";
        }
        return "Name: "+this.name+"\nDifficulty: "+this.difficulty+"\n"+s;
    }
}