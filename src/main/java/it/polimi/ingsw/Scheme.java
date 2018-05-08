package it.polimi.ingsw;

import java.lang.*;

public class Scheme {

    //***************************//
    //        Attributes         //
    //***************************//

    private String name;
    private int difficulty;
    private static final int MAX_COL = 5;
    private static final int MAX_ROW = 4;

    private Box[][] grid;

    //constructor, takes the ID and call a HandlerMethod in Controller
    public Scheme(int id){

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

    //Place a dice in a box with all restrictions
    public boolean placeDiceWithAllRestr(Dice dice, int x, int y) {
        boolean placeable = true;
        boolean freeGrid = true;
        boolean diceNear = false;
        int i;
        int j;

        //First dice?
        for (i = 0; i < MAX_ROW; i++) {
            for (j = 0; j < MAX_COL; j++) {
                if (!grid[i][j].free()) {
                    freeGrid = false;
                }
            }
        }
        //If first die check if it respects first-dice-placement-rule
        if (freeGrid) {
            if (grid[x][y].isEmployable(dice) && ((x == 0) || (y == 0) || (x == MAX_ROW - 1) || (y == MAX_COL - 1))) {
                if (grid[x][y].setDice(dice))
                    return true;
            } else
                return false;
        }

        //Check if die respects box restrictions
        if (!grid[x][y].isEmployable(dice))
            return false;

        //Algorithm to find if the die respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            for (j = y - 1; j <= y + 1; j++) {
                if (!(i == x && j == y)) {
                    if ((i >= 0 && j >= 0) || (i < MAX_ROW && j >= 0) ||
                            (i >= 0 && j < MAX_COL) || (i < MAX_ROW && j < MAX_COL)) {
                        if (!grid[i][j].free()) {
                            diceNear = true;
                        }
                        if (j == y || i == x) {
                            if (grid[i][j].getDice().getColor() == dice.getColor() ||
                                    grid[i][j].getDice().getValue() == dice.getValue()) {
                                placeable = false;
                            }
                        }
                    }
                }
            }
        }
        if (placeable && diceNear && grid[x][y].setDice(dice)) {
            return true;
        }
        return false;
    }

    //Place a dice in a box ignoring color restrictions
    public boolean placeDiceNoCol(Dice dice, int x, int y){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        //Check if die respects box restrictions
        if (!grid[x][y].isEmployableNoCol(dice))
            return false;

        //Algorithm to find if the die respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            for (j = y - 1; j <= y + 1; j++) {
                if (!(i == x && j == y)){
                    if ((i >= 0 && j >= 0) || (i < MAX_ROW && j >= 0) ||
                            (i >= 0 && j < MAX_COL) || (i < MAX_ROW && j < MAX_COL)) {
                        if (!grid[i][j].free()) {
                            diceNear = true;
                        }
                        if (j == y || i == x) {
                            if (grid[i][j].getDice().getValue() == dice.getValue()) {
                                placeable = false;
                            }
                        }
                    }
                }
            }
        }
        if (placeable && diceNear && grid[x][y].setDice(dice)) {
            return true;
        }
        return false;
    }

    //
    public boolean placeDiceNoNum(int x, int y, Dice dice){
        boolean placeable = true;
        boolean diceNear = false;
        int i;
        int j;

        //Check if die respects box restrictions
        if (!grid[x][y].isEmployableNoNum(dice))
            return false;

        //Algorithm to find if the die respects placement restrictions
        for (i = x - 1; i <= x + 1 && placeable; i++) {
            for (j = y - 1; j <= y + 1; j++) {
                if (!(i == x && j == y)) {
                    if ((i >= 0 && j >= 0) || (i < MAX_ROW && j >= 0) ||
                            (i >= 0 && j < MAX_COL) || (i < MAX_ROW && j < MAX_COL)) {
                        if (!grid[i][j].free()) {
                            diceNear = true;
                        }
                        if (j == y || i == x) {
                            if (grid[i][j].getDice().getColor() == dice.getColor()) {
                                placeable = false;
                            }
                        }
                    }
                }
            }
        }
        if (placeable && diceNear) {
            if (grid[x][y].setDice(dice))
                return true;
        }
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

    @Override
    public String toString(){
        return "Scheme";
    }
}
