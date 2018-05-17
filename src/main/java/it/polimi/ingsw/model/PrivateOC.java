package it.polimi.ingsw.model;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class PrivateOC implements ObjectiveCard {
    private String name;
    private String description;
    private Enum.Color color;

    public PrivateOC(Enum.Color color){
        this.color = color;
        int index = color.ordinal();

        File inputFile = new File("src/main/resources/cards/PrivateOC.txt");
        Scanner scan = null;

        try {
            scan = new Scanner(inputFile);

            for (int i = 0; i<index*2; i++)
                scan.nextLine();

            this.name = scan.nextLine();
            this.description = scan.nextLine();

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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public int countPoints(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        int points = 0;

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                try{
                    if(grid[i][j].getDice().getColor() == this.color)
                        points += grid[i][j].getDice().getValue();
                }catch (NullPointerException e){}
            }
        }

        return points;
    }

    @Override
    public String toString() {
        return this.name + " : " + this.description;
    }
}
