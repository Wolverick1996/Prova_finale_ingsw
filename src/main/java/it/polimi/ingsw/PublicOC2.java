package it.polimi.ingsw;

import java.util.ArrayList;

public class PublicOC2 implements ObjectiveCard{

    private String name;
    private String description;

    public PublicOC2 (){
        this.name = "Different colors - columns";
        this.description = "Columns without repeated colors";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int countPoints(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        int points= 0;
        ArrayList<Enum.Color> checkColor = new ArrayList<>();

        for (int j = 0; j<Scheme.MAX_ROW; j++){
            for (int i = 0; i<Scheme.MAX_COL; i++){
                try{
                    if (checkColor.isEmpty() || !checkColor.contains(grid[i][j].getDice().getColor()))
                        checkColor.add(grid[i][j].getDice().getColor());
                }catch (NullPointerException e){}
            }
            if(checkColor.size() == 4)
                points += 5;
        }

        return points;
    }
}
