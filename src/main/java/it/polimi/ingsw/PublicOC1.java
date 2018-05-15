package it.polimi.ingsw;

import java.util.ArrayList;

public class PublicOC1 implements ObjectiveCard {

    private String name;
    private String description;

    public PublicOC1 (){
        this.name = "Different colors - rows";
        this.description = "Rows without repeated colors";
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
                try{
                    if (checkColor.isEmpty() || !checkColor.contains(grid[i][j].getDice().getColor()))
                        checkColor.add(grid[i][j].getDice().getColor());
                }catch (NullPointerException e){}
            }
            if(checkColor.size() == 5)
                points += 6;
        }
        return points;
    }
}
