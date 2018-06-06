package it.polimi.ingsw.server.model;

import java.io.InputStream;
import java.util.Scanner;

/**
 * PrivateOC represents the private objective card of each player, which describes his goal (invisible to other people)
 * There are 5 in total
 *
 * @author Andrea
 */
public class PrivateOC implements ObjectiveCard {
    private String name;
    private String description;
    private Enum.Color color;

    /**
     * Constructor for the private objective card
     *
     * @param color: the color that will become the one of interest to the player, going to form his private objective card
     * @author Andrea
     */
    public PrivateOC(Enum.Color color){
        this.color = color;
        int index = color.ordinal();
        InputStream inputFile = PrivateOC.class.getResourceAsStream("/cards/PrivateOC.txt");
        Scanner scan = new Scanner(inputFile);

        for (int i = 0; i<index*2; i++)
            scan.nextLine();

        this.name = scan.nextLine();
        this.description = scan.nextLine();

        try {
            scan.close();
        } catch (Exception e1) {
            System.err.println("Error closing scan (privateOC)");
        }
    }

    /**
     * Returns the private objective card's name
     *
     * @return private objective card's name
     * @author Andrea
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the private objective card's description
     *
     * @return private objective card's description
     * @author Andrea
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Counts the number of dice of the color specified by the card, calculating points related to the private objective
     *
     * @param player: private objective card holder
     * @throws NullPointerException if there are problems with scrolling the grid
     * @return the amount of points earned from the card
     * @author Andrea
     */
    public int countPoints(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        int points = 0;

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                try {
                    if(grid[i][j].getDice().getColor() == this.color)
                        points += grid[i][j].getDice().getValue();
                } catch (NullPointerException e){}
            }
        }

        return points;
    }

    /**
     * Returns the private objective card's color
     *
     * @return private objective card's color
     * @author Andrea
     */
    public Enum.Color getColor() {
        return color;
    }

    /**
     * Used to print a private objective card
     *
     * @return the string that represents the private objective card
     * @author Andrea
     */
    @Override
    public String toString() {
        return this.name + " : " + this.description;
    }

}