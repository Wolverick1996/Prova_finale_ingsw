package it.polimi.ingsw.Model.PubOC;

import it.polimi.ingsw.Model.Box;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Scheme;

import java.util.ArrayList;

public class PublicOC8 implements ObjectiveCard {
    private String name;
    private String description;

    public PublicOC8 (){
        this.name = "Different shades";
        this.description = "Sets of dice each value everywhere";
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
        ArrayList<Integer> checkValue = new ArrayList<>();

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                if(checkValue.size() == 6){
                    points += 5;
                    checkValue.clear();
                }
                try{
                    if (checkValue.isEmpty() || !checkValue.contains(grid[i][j].getDice().getValue()))
                        checkValue.add(grid[i][j].getDice().getValue());
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
