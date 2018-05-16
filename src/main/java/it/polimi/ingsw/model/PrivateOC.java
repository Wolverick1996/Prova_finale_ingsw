package it.polimi.ingsw.model;


public class PrivateOC implements ObjectiveCard {
    private String name;
    private String description;
    private Enum.Color color;

    public PrivateOC (Enum.Color color){
        this.color = color;

        switch (color){
            case BLUE:
                this.name = "Blue shades";
                this.description = "Sum the values of all blue dice";
                break;
            case GREEN:
                this.name = "Green shades";
                this.description = "Sum the values of all green dice";
                break;
            case RED:
                this.name = "Red shades";
                this.description = "Sum the values of all red dice";
                break;
            case PURPLE:
                this.name = "Purple shades";
                this.description = "Sum the values of all purple dice";
                break;
            case YELLOW:
                this.name = "Yellow shades";
                this.description = "Sum the values of all yellow dice";
                break;
            default:
                this.name = "ERROR";
                this.description = "ERROR";
                break;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public int countPoints(Player player) {
        Box[][] grid = player.getOwnScheme().getGrid();
        int points = 0;

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                try{
                    if(grid[i][j].getDice().getColor() == this.color)
                        points += grid[i][j].getDice().getValue();
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
