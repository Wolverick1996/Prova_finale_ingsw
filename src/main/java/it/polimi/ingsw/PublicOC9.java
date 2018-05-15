package it.polimi.ingsw;

import java.util.ArrayList;

public class PublicOC9 implements ObjectiveCard {
    private String name;
    private String description;

    public PublicOC9 (){
        this.name = "Colored diagonals";
        this.description = "Number of same-colored dice diagonally adjacent";
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
        ArrayList<Dice> diceCounted = new ArrayList<>();

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                try {
                    if (grid[i][j].getDice().getColor() == grid[i+1][j+1].getDice().getColor()
                            && !diceCounted.contains(grid[i][j].getDice()) && !diceCounted.contains(grid[i+1][j+1].getDice())){
                        diceCounted.add(grid[i][j].getDice());
                        diceCounted.add(grid[i+1][j+1].getDice());
                        points += 2;
                        if (grid[i][j].getDice().getColor() == grid[i+2][j+2].getDice().getColor()
                                && !diceCounted.contains(grid[i+2][j+2].getDice())){
                            diceCounted.add(grid[i+2][j+2].getDice());
                            points++;
                            if (grid[i][j].getDice().getColor() == grid[i+3][j+3].getDice().getColor()
                                    && !diceCounted.contains(grid[i+3][j+3].getDice())){
                                diceCounted.add(grid[i+3][j+3].getDice());
                                points++;
                            }
                        }
                    }
                }catch (NullPointerException e){}
                try {
                    if (grid[i][j].getDice().getColor() == grid[i+1][j-1].getDice().getColor()){
                        if (!diceCounted.contains(grid[i+1][j-1].getDice())){
                            diceCounted.add(grid[i+1][j-1].getDice());
                            points++;
                        }
                        if (grid[i][j].getDice().getColor() == grid[i+2][j-2].getDice().getColor()){
                            if (!diceCounted.contains(grid[i+2][j-2].getDice())){
                                diceCounted.add(grid[i+2][j-2].getDice());
                                points++;
                            }
                            if (grid[i][j].getDice().getColor() == grid[i+3][j-3].getDice().getColor()
                                    && !diceCounted.contains(grid[i+3][j-3].getDice())){
                                diceCounted.add(grid[i+3][j-3].getDice());
                                points++;
                            }
                        }
                    }
                }catch (NullPointerException e){}
            }
        }
        return points;
    }
}
