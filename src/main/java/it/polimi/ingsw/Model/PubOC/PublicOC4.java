package it.polimi.ingsw.Model.PubOC;

import it.polimi.ingsw.Model.Box;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Scheme;

import java.util.ArrayList;

public class PublicOC4 implements ObjectiveCard {
    private String name;
    private String description;

    public PublicOC4 (){
        this.name = "Different shades - columns";
        this.description = "Columns without repeated shades";
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
        int points= 0;
        ArrayList<Integer> checkValue = new ArrayList<>();

        for (int j = 0; j<Scheme.MAX_ROW; j++){
            for (int i = 0; i<Scheme.MAX_COL; i++){
                try{
                    if (checkValue.isEmpty() || !checkValue.contains(grid[i][j].getDice().getValue()))
                        checkValue.add(grid[i][j].getDice().getValue());
                }catch (NullPointerException e){}
            }
            if(checkValue.size() == 4)
                points += 4;
        }

        return points;
    }

    @Override
    public String toString() {
        return this.name + " : " + this.description;
    }
}
