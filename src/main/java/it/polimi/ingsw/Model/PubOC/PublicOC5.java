package it.polimi.ingsw.Model.PubOC;

import it.polimi.ingsw.Model.Box;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Scheme;

public class PublicOC5 implements ObjectiveCard {
    private String name;
    private String description;

    public PublicOC5 (){
        this.name = "Light shades";
        this.description = "Sets of 1 & 2 everywhere";
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
        int ones = 0;
        int twos = 0;

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++) {
                try {
                    if (grid[i][j].getDice().getValue() == 1)
                        ones++;
                    else if (grid[i][j].getDice().getValue() == 2)
                        twos++;
                } catch (NullPointerException e) {}
            }
        }

        if (ones == 0 || twos == 0)
            return points;
        else{
            for (int i = 0; i<ones && i<twos;){
                points += 2;
                ones--;
                twos--;
            }
        }
        return points;
    }

    @Override
    public String toString() {
        return this.name + " : " + this.description;
    }
}
