package it.polimi.ingsw.model;

public class Player {

    //***************************//
    //        Attributes         //
    //***************************//

    private String nickname;
    private int points;
    private int tokens;
    private Dice diceInHand = null;
    private Scheme ownScheme;
    private int IDplayer;

    //constructor
    public Player(String nick, int ID){
        this.nickname = nick;
        this.points = 0;
        this.IDplayer = ID;
    }

    //***************************//
    //         Methods           //
    //***************************//


    //Assign Scheme to a player
    public void chooseScheme(Scheme scheme){
        this.ownScheme = scheme;
        this.tokens = scheme.getDifficulty();
    }

    // Pick a dice from scheme
    public boolean extractDice(int x, int y){
        if(ownScheme.checkBox(x, y) != null){
            this.diceInHand = ownScheme.removeDice(x, y);
            return true;
        }
        else
            return false;
    }

    //Is a dice picked from reserve placeable in the scheme?
    public boolean isPlaceableDiceFromReserve(int x, int y, Table table, int indexDice){
        Dice tempDice;
        if (table ==  null)
            return false;
        tempDice = table.checkDiceFromReserve(indexDice);
        if(ownScheme.isPlaceableAllRestr(x, y, tempDice))
            return true;
        else
            return false;
    }

    //Place a dice (!!CALL IT AFTER CHECKING THE PLACEMENT CONDITIONS!!)
    public void placeDice (int x, int y, Table table, int indexDice){
        Dice tempDice;

        tempDice = table.pickDiceFromReserve(indexDice);
        ownScheme.placeDice(x, y, tempDice);
    }

    //Use a tool card
    public boolean useToolCard(int indexToolCard){
        return false;
    }


    //Count the points, at game end or game left.
    public void countPoints(){
        int freeBoxes = 0;
        Box[][] tempGrid = this.ownScheme.getGrid();

        for (int i = 0; i<Scheme.MAX_ROW; i++){
            for (int j = 0; j<Scheme.MAX_COL; j++){
                if (!tempGrid[i][j].isFull())
                    freeBoxes++;
            }
        }

        this.points += PrivObjHandler.countPoints(this);
        for (int i = 0; i<3; i++){
            this.points += PubObjHandler.countPoints(this, i);
        }
        this.points += this.tokens;
        this.points -= freeBoxes;
    }

    public Scheme getOwnScheme() {
        return this.ownScheme;
    }

    public int getIDplayer() {
        return this.IDplayer;
    }

    public int getPoints() {
        return this.points;
    }

    public Dice getDiceInHand() {
        return this.diceInHand;
    }

    @Override
    public String toString(){
        return this.nickname+" : "+this.IDplayer+"\nActive scheme:\n"+this.ownScheme;
    }
}
