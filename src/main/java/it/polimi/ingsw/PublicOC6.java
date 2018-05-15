package it.polimi.ingsw;

public class PublicOC6 implements ObjectiveCard {
    private String name;
    private String description;

    public PublicOC6 (){
        this.name = "Medium shades";
        this.description = "Sets of 3 & 4 everywhere";
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
        int threes = 0;
        int fours= 0;

        for (int i = 0; i<Scheme.MAX_ROWS; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++) {
                try {
                    if (grid[i][j].getDice().getValue() == 3)
                        threes++;
                    else if (grid[i][j].getDice().getValue() == 4)
                        fours++;
                } catch (NullPointerException e) {}
            }
        }

        if (threes == 0 || fours == 0)
            return points;
        else{
            for (int i = 0; i<threes && i<fours;){
                points += 2;
                threes--;
                fours--;
            }
        }
        return points;
    }
}
