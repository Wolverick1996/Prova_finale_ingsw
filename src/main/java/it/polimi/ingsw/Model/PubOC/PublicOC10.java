package it.polimi.ingsw.Model.PubOC;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enum;

import java.util.ArrayList;

public class PublicOC10 implements ObjectiveCard {
    private String name;
    private String description;

    public PublicOC10 (){
        this.name = "Variety of color";
        this.description = "Sets of dice each color everywhere";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int countPoints(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        int points = 0;
        ArrayList<Enum.Color> checkColor = new ArrayList<>();

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                if(checkColor.size() == 5){
                    points += 4;
                    checkColor.clear();
                }
                try{
                    if (checkColor.isEmpty() || !checkColor.contains(grid[i][j].getDice().getColor()))
                        checkColor.add(grid[i][j].getDice().getColor());
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
