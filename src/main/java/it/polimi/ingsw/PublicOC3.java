package it.polimi.ingsw;

import java.util.ArrayList;

public class PublicOC3 implements ObjectiveCard {

    private String name;
    private String description;

    public PublicOC3 (){
        this.name = "Different shades - rows";
        this.description = "Rows without repeated shades";
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

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                try{
                    if (checkValue.isEmpty() || !checkValue.contains(grid[i][j].getDice().getValue()))
                        checkValue.add(grid[i][j].getDice().getValue());
                }catch (NullPointerException e){}
            }
            if(checkValue.size() == 5)
                points += 5;
        }

        return points;
    }
}
