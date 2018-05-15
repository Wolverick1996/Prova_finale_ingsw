package it.polimi.ingsw.Model;

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

    //constructor, takes the ID and call a HandlerMethod in Controller
    public Scheme (int id){
        File inputFile = new File("Schemes/Schemes.txt");
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

    //Check what dice is in a box
    public Dice checkBox(int x, int y){
        Dice dice;

        dice = this.grid[x][y].getDice();

        return dice;
    }

    //Check if a dice is placeable in a box with all restrictions
    public boolean isPlaceableAllRestr(int x, int y, Dice dice) {
        boolean placeable = true;
        boolean freeGrid = isGridFree();
        boolean diceNear = false;
        int i;
        int j;

        if ((x < 0 || x > MAX_ROW) || (y < 0 || y > MAX_COL))
            return false;

        //If first die check if it respects first-dice-placement-rule
        if (freeGrid) {
            if (grid[x][y].isEmployable(dice) && ((x == 0) || (y == 0) || (x == MAX_ROW - 1) || (y == MAX_COL - 1)))
                return true;
            else
                return false;
        }

        //Check if die respects box restrictions
        if (!grid[x][y].isEmployable(dice))
            return false;

        //Algorithm to find if the die respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            if (i > 0 && i < MAX_ROW){
                for (j = y - 1; j <= y + 1; j++) {
                    if (!(i == x && j == y) && j > 0 && j < MAX_COL) {
                        if (grid[i][j].isFull()) {
                            diceNear = true;
                        }
                        if ((j == y || i == x) && grid[i][j].isFull()) {
                            if (grid[i][j].getDice().getColor() == dice.getColor() ||
                                    grid[i][j].getDice().getValue() == dice.getValue()) {
                                placeable = false;
                            }
                        }
                    }
                }
            }
        }
        if (placeable && diceNear) {
            return true;
        }
        return false;
    }

    //Check if a dice is placeable in a box ignoring color restrictions (TOOL3)
    public boolean isPlaceableNoCol(int x, int y, Dice dice){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        if ((x < 0 || x > MAX_ROW) || (y < 0 || y > MAX_COL))
            return false;

        //Check if die respects box restrictions
        if (!grid[x][y].isEmployableNoCol(dice))
            return false;

        //Algorithm to find if the die respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            if (i > 0 && i < MAX_ROW){
                for (j = y - 1; j <= y + 1; j++) {
                    if (!(i == x && j == y) && j > 0 && j < MAX_COL){
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
        if (placeable && diceNear) {
            return true;
        }
        return false;
    }

    //Chech if a dice is placeable in a box ignoring number restrictions (TOOL2)
    public boolean isPlaceableNoNum(int x, int y, Dice dice){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        if ((x < 0 || x > MAX_ROW) || (y < 0 || y > MAX_COL))
            return false;

        //Check if die respects box restrictions
        if (!grid[x][y].isEmployableNoNum(dice))
            return false;

        //Algorithm to find if the die respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            if (i > 0 && i < MAX_ROW){
                for (j = y - 1; j <= y + 1; j++) {
                    if (!(i == x && j == y) && j > 0 && j < MAX_COL) {
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
        if (placeable && diceNear) {
            return true;
        }
        return false;
    }

    //Check if a dice is placeable ignoring dice-near-rule (TOOL 9)
    public boolean isPlaceableNoDiceNear(int x, int y, Dice dice){
        boolean freeGrid = isGridFree();

        if ((x < 0 || x > MAX_ROW) || (y < 0 || y > MAX_COL))
            return false;

        //If first die check if it respects first-dice-placement-rule
        if (freeGrid) {
            if (grid[x][y].isEmployable(dice) && ((x == 0) || (y == 0) || (x == MAX_ROW - 1) || (y == MAX_COL - 1)))
                return true;
            else
                return false;
        }

        //Check if die respects box restrictions
        if (!grid[x][y].isEmployable(dice))
            return false;

        return true;
    }

    //Check if the grid is full
    public boolean isGridFree(){
        int i, j;
        for (i = 0; i < MAX_ROW; i++) {
            for (j = 0; j < MAX_COL; j++) {
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
        else{
            return grid[x][y].free();
        }
    }

    //Set dice
    public boolean placeDice(int x, int y, Dice dice){
        if(grid[x][y].setDice(dice))
            return true;
        else
            return false;
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
            for (int j = 0; j < MAX_COL; j++) {
                s = s+grid[i][j];

            }
            s = s+"\n";
        }
        return "Name: "+this.name+"\nDifficulty: "+this.difficulty+"\n"+s;
    }
}
