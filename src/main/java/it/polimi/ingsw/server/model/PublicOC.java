package it.polimi.ingsw.server.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * PublicOC represents the public objective card: there are 10 in total and 3 of them are permanently on the table
 *
 * @author Andrea
 */
public class PublicOC implements ObjectiveCard {

    //***************************//
    //        Attributes         //
    //***************************//

    private String name;
    private String description;
    private int cardID;

    /**
     * Constructor for the public objective card
     *
     * @param cardID: the ID of the public objective card to be created (1 to 10)
     * @throws IllegalArgumentException if ID passed as a parameter is not valid
     * @author Andrea
     */
    public PublicOC(int cardID){
        if (cardID < 1 || cardID > PubObjHandler.NUM_PUB_OC)
            throw new IllegalArgumentException("Not valid ID: " + cardID);

        this.cardID = cardID;
        InputStream inputFile = PublicOC.class.getResourceAsStream("/cards/PublicOC.txt");
        Scanner scan = new Scanner(inputFile);

        for (int i=0; i<(cardID-1)*2; i++)
            scan.nextLine();

        this.name = scan.nextLine();
        this.description = scan.nextLine();

        try {
            scan.close();
        } catch (Exception e1) {
            System.err.println("Error closing scan (PublicOC)");
        }
    }

    //***************************//
    //         Methods           //
    //***************************//

    /**
     * Returns the public objective card's name
     *
     * @return public objective card's name
     * @author Andrea
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Returns the public objective card's description
     *
     * @return public objective card's description
     * @author Andrea
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Receives a player as a parameter and calls cards in play methods to count his points related to public objectives
     *
     * @param player: player whose points the method must calculate
     * @return the amount of points earned from public objective cards in play
     * @author Andrea
     */
    @Override
    public int countPoints(Player player) {
        int points = 0;
        switch (this.cardID){
            case 1:
                points += publicOC1(player);
                break;
            case 2:
                points += publicOC2(player);
                break;
            case 3:
                points += publicOC3(player);
                break;
            case 4:
                points += publicOC4(player);
                break;
            case 5: case 6: case 7:
                points += publicOC567(player);
                break;
            case 8:
                points += publicOC8(player);
                break;
            case 9:
                points += publicOC9(player);
                break;
            case 10:
                points += publicOC10(player);
                break;
            default:
                points = -1;
                break;
        }
        return points;
    }

    /**
     * Receives a player as a parameter and calculate the amount of points earned from the public objective card 1
     *
     * @param player: player whose points the method must calculate
     * @throws NullPointerException if there are problems with scrolling the grid
     * @return the amount of points earned from the public objective card 1
     * @author Andrea
     */
    private int publicOC1(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        int points = 0;
        ArrayList<Enum.Color> checkColor = new ArrayList<>();

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                try {
                    if (checkColor.isEmpty() || !checkColor.contains(grid[i][j].getDice().getColor()))
                        checkColor.add(grid[i][j].getDice().getColor());
                } catch (NullPointerException e) {
                    // grid[x][y] doesn't contains any dice so this position has to be ignored
                }
            }
            if(checkColor.size() == 5)
                points += 6;
            checkColor.clear();
        }
        return points;
    }

    /**
     * Receives a player as a parameter and calculate the amount of points earned from the public objective card 2
     *
     * @param player: player whose points the method must calculate
     * @throws NullPointerException if there are problems with scrolling the grid
     * @return the amount of points earned from the public objective card 2
     * @author Andrea
     */
    private int publicOC2(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        int points= 0;
        ArrayList<Enum.Color> checkColor = new ArrayList<>();

        for (int j = 0; j<Scheme.MAX_COL; j++){
            for (int i = 0; i<Scheme.MAX_ROW; i++){
                try {
                    if (checkColor.isEmpty() || !checkColor.contains(grid[i][j].getDice().getColor()))
                        checkColor.add(grid[i][j].getDice().getColor());
                } catch (NullPointerException e) {
                    // grid[x][y] doesn't contains any dice so this position has to be ignored
                }
            }
            if(checkColor.size() == 4)
                points += 5;
            checkColor.clear();
        }
        return points;
    }

    /**
     * Receives a player as a parameter and calculate the amount of points earned from the public objective card 3
     *
     * @param player: player whose points the method must calculate
     * @throws NullPointerException if there are problems with scrolling the grid
     * @return the amount of points earned from the public objective card 3
     * @author Andrea
     */
    private int publicOC3(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        int points= 0;
        ArrayList<Integer> checkValue = new ArrayList<>();

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                try {
                    if (checkValue.isEmpty() || !checkValue.contains(grid[i][j].getDice().getValue()))
                        checkValue.add(grid[i][j].getDice().getValue());
                } catch (NullPointerException e) {
                    // grid[x][y] doesn't contains any dice so this position has to be ignored
                }
            }
            if(checkValue.size() == 5)
                points += 5;
            checkValue.clear();
        }
        return points;
    }

    /**
     * Receives a player as a parameter and calculate the amount of points earned from the public objective card 4
     *
     * @param player: player whose points the method must calculate
     * @throws NullPointerException if there are problems with scrolling the grid
     * @return the amount of points earned from the public objective card 4
     * @author Andrea
     */
    private int publicOC4(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        int points= 0;
        ArrayList<Integer> checkValue = new ArrayList<>();

        for (int j = 0; j<Scheme.MAX_COL; j++){
            for (int i = 0; i<Scheme.MAX_ROW; i++){
                try {
                    if (checkValue.isEmpty() || !checkValue.contains(grid[i][j].getDice().getValue()))
                        checkValue.add(grid[i][j].getDice().getValue());
                } catch (NullPointerException e) {
                    // grid[x][y] doesn't contains any dice so this position has to be ignored
                }
            }
            if(checkValue.size() == 4)
                points += 4;
            checkValue.clear();
        }
        return points;
    }

    /**
     * Receives a player as a parameter and calculate the amount of points earned from the public objective cards 5, 6 or 7
     *
     * @param player: player whose points the method must calculate
     * @throws NullPointerException if there are problems with scrolling the grid
     * @return the amount of points earned from the public objective card 5, 6 or 7
     * @author Andrea
     */
    private int publicOC567(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        int points = 0;
        int firstValue;
        int secondValue;
        int firsts = 0;
        int seconds = 0;

        if (this.cardID == 5){
            firstValue = 1;
            secondValue = 2;
        } else if (this.cardID == 6){
            firstValue = 3;
            secondValue = 4;
        } else {
            firstValue = 5;
            secondValue = 6;
        }

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++) {
                try {
                    if (grid[i][j].getDice().getValue() == firstValue)
                        firsts++;
                    else if (grid[i][j].getDice().getValue() == secondValue)
                        seconds++;
                } catch (NullPointerException e) {
                    // grid[x][y] doesn't contains any dice so this position has to be ignored
                }
            }
        }

        if (firsts == 0 || seconds == 0)
            return points;
        else {
            for (int i = 0; i<firsts && i<seconds;){
                points += 2;
                firsts--;
                seconds--;
            }
        }
        return points;
    }

    /**
     * Receives a player as a parameter and calculate the amount of points earned from the public objective card 8
     *
     * @param player: player whose points the method must calculate
     * @throws NullPointerException if there are problems with scrolling the grid
     * @return the amount of points earned from the public objective card 8
     * @author Andrea
     */
    private int publicOC8(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        boolean[][] flag = new boolean[Scheme.MAX_ROW][Scheme.MAX_COL];
        int points = 0;
        int i = 0;
        int j = 0;
        ArrayList<Integer> checkValue = new ArrayList<>();

        while (i < Scheme.MAX_ROW || j < Scheme.MAX_COL) {
            for (i = 0; i < Scheme.MAX_ROW  && checkValue.size() != 6; i++) {
                for (j = 0; j < Scheme.MAX_COL && checkValue.size() != 6; j++) {
                    try {
                        if (checkValue.isEmpty() ||
                                (!checkValue.contains(grid[i][j].getDice().getValue()) && !flag[i][j])) {
                            checkValue.add(grid[i][j].getDice().getValue());
                            flag[i][j] = true;
                        }
                    } catch (NullPointerException e) {
                        // grid[x][y] doesn't contains any dice so this position has to be ignored
                    }

                    if (checkValue.size() == 6)
                        points += 5;
                }
            }
            checkValue.clear();
        }
        return points;
    }

    /**
     * Receives a player as a parameter and calculate the amount of points earned from the public objective card 9
     *
     * @param player: player whose points the method must calculate
     * @throws NullPointerException if there are problems with scrolling the grid
     * @return the amount of points earned from the public objective card 9
     * @author Andrea
     */
    private int publicOC9(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        int points = 0;
        ArrayList<Dice> diceCounted = new ArrayList<>();

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                try {
                    if ((i+1 < Scheme.MAX_ROW) && (j+1 < Scheme.MAX_COL)) {
                        if (grid[i][j].getDice().getColor() == grid[i + 1][j + 1].getDice().getColor()) {
                            if (!diceCounted.contains(grid[i][j].getDice())) {
                                diceCounted.add(grid[i][j].getDice());
                                points++;
                            }
                            if (!diceCounted.contains(grid[i + 1][j + 1].getDice())) {
                                diceCounted.add(grid[i + 1][j + 1].getDice());
                                points++;
                            }
                            if ((i+2 < Scheme.MAX_ROW) && (j+2 < Scheme.MAX_COL)) {
                                if (grid[i][j].getDice().getColor() == grid[i + 2][j + 2].getDice().getColor()
                                        && !diceCounted.contains(grid[i + 2][j + 2].getDice())) {
                                    diceCounted.add(grid[i + 2][j + 2].getDice());
                                    points++;
                                    if ((i+3 < Scheme.MAX_ROW) && (j+3 < Scheme.MAX_COL)) {
                                        if (grid[i][j].getDice().getColor() == grid[i + 3][j + 3].getDice().getColor()
                                                && !diceCounted.contains(grid[i + 3][j + 3].getDice())) {
                                            diceCounted.add(grid[i + 3][j + 3].getDice());
                                            points++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    // grid[x][y] doesn't contains any dice so this position has to be ignored
                }
                try {
                    if ((i+1 < Scheme.MAX_ROW) && (j-1 >= 0)) {
                        if (grid[i][j].getDice().getColor() == grid[i + 1][j - 1].getDice().getColor()) {
                            if (!diceCounted.contains(grid[i][j].getDice())) {
                                diceCounted.add(grid[i][j].getDice());
                                points++;
                            }
                            if (!diceCounted.contains(grid[i + 1][j - 1].getDice())) {
                                diceCounted.add(grid[i + 1][j - 1].getDice());
                                points++;
                            }
                            if ((i+2 < Scheme.MAX_ROW) && (j-2 >= 0)) {
                                if (grid[i][j].getDice().getColor() == grid[i + 2][j - 2].getDice().getColor()) {
                                    if (!diceCounted.contains(grid[i + 2][j - 2].getDice())) {
                                        diceCounted.add(grid[i + 2][j - 2].getDice());
                                        points++;
                                    }
                                    if ((i+3 < Scheme.MAX_ROW) && (j-3 >= 0)) {
                                        if (grid[i][j].getDice().getColor() == grid[i + 3][j - 3].getDice().getColor()
                                                && !diceCounted.contains(grid[i + 3][j - 3].getDice())) {
                                            diceCounted.add(grid[i + 3][j - 3].getDice());
                                            points++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    // grid[x][y] doesn't contains any dice so this position has to be ignored
                }
            }
        }
        return points;
    }

    /**
     * Receives a player as a parameter and calculate the amount of points earned from the public objective card 10
     *
     * @param player: player whose points the method must calculate
     * @throws NullPointerException if there are problems with scrolling the grid
     * @return the amount of points earned from the public objective card 10
     * @author Andrea
     */
    private int publicOC10(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        boolean[][] flag = new boolean[Scheme.MAX_ROW][Scheme.MAX_COL];
        int points = 0;
        int i = 0;
        int j = 0;
        ArrayList<Enum.Color> checkColor = new ArrayList<>();

        while (i < Scheme.MAX_ROW || j < Scheme.MAX_COL) {
            for (i = 0; i < Scheme.MAX_ROW  && checkColor.size() != 5; i++) {
                for (j = 0; j < Scheme.MAX_COL && checkColor.size() != 5; j++) {
                    try {
                        if (checkColor.isEmpty() ||
                                (!checkColor.contains(grid[i][j].getDice().getColor()) && !flag[i][j])) {
                            checkColor.add(grid[i][j].getDice().getColor());
                            flag[i][j] = true;
                        }
                    } catch (NullPointerException e) {
                        // grid[x][y] doesn't contains any dice so this position has to be ignored
                    }

                    if (checkColor.size() == 5)
                        points += 4;
                }
            }
            checkColor.clear();
        }
        return points;
    }

    /**
     * Used to print a public objective card
     *
     * @return the string that represents the public objective card
     * @author Andrea
     */
    @Override
    public String toString() { return this.name + " : " + this.description; }

}