package it.polimi.ingsw.Model.PubOC;

import it.polimi.ingsw.Model.Box;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Scheme;

public class PublicOC7 implements ObjectiveCard {

    private String name;
    private String description;

    public PublicOC7 (){
        this.name = "Dark shades";
        this.description = "Sets of 5 & 6 everywhere";
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
        int fives = 0;
        int sixs = 0;

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++) {
                try {
                    if (grid[i][j].getDice().getValue() == 5)
                        fives++;
                    else if (grid[i][j].getDice().getValue() == 6)
                        sixs++;
                } catch (NullPointerException e) {}
            }
        }

        if (fives == 0 || sixs == 0)
            return points;
        else{
            for (int i = 0; i<fives && i<sixs;){
                points += 2;
                fives--;
                sixs--;
            }
        }
        return points;
    }

    @Override
    public String toString() {
        return this.name + " : " + this.description;
    }
}
