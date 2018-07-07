package it.polimi.ingsw.client.view;

import java.util.ArrayList;
import java.util.List;

public class GUIupdater {

    private static GameController controller;
    public static synchronized GameController getController() { return controller; }
    public static synchronized void setController(GameController value) { controller = value; }

    private static boolean hasGameEnded = false;
    private static boolean hasGetStatus = false;

    private static String ownUsername;
    private static String activePlayer;
    static SocketMessengerClient messenger;
    static int lobbyDelay = 20;
    static int numPlayersAtBeginning;
    static int numPlayers;
    static boolean isRMI = false;

    private static boolean customSchemes = false;
    private static int schemeChosen = -1;
    private static boolean canGoToGame = false;

    private static TypeRequested requested;

    private static String toSend;
    private static List<String> toSendList = new ArrayList<>();
    private static List<String> schemesToChoose = new ArrayList<>();

    //Attributes of the game
    private static String table;
    private static String privObj;
    private static String tools;
    private static List<String> players = new ArrayList<>();

    public static synchronized String getActivePlayer() {
        for (String p : players) {
            String temp = p;
            if (p.split("\n")[0].equals(activePlayer)){
                return temp;
            }
        }
        return players.get(0);
    }

    public static synchronized String getOwnPlayer() {
        for (String p : players) {
            String temp = p;
            if (p.split("\n")[0].equals(ownUsername)){
                return temp;
            }
        }
        return players.get(0);
    }

    public static synchronized String getOwnScheme() {
        String me = getOwnPlayer();
        String[] split = me.split("\n");
        me = split[3] + "\n" + split[4] + "\n" + split[5] + "\n" + split[6] + "\n" + split[7] + "\n" +
                split[8] + "\n" + split[9];
        return me;
    }

    public static synchronized boolean getHasGameEnded() { return hasGameEnded; }

    public static synchronized boolean getHasGetStatus() { return hasGetStatus; }

    public static synchronized TypeRequested getTypeRequested(){ return requested; }

    public static synchronized String getOwnUsername(){
        return ownUsername;
    }

    public static synchronized boolean getCustomSchemes(){
        return customSchemes;
    }

    public static synchronized int getSchemeChosen(){
        return schemeChosen;
    }

    public static synchronized boolean getCanGoToGame(){
        return canGoToGame;
    }

    public static synchronized String getToSend(){
        return toSend;
    }

    public static synchronized List getSchemesToChoose(){
        return schemesToChoose;
    }

    public static synchronized int getNumPlayers(){
        return numPlayers;
    }

    public static synchronized String getTable(){
        return table;
    }

    public static synchronized List getPlayers(){
        return players;
    }

    public static synchronized String getPrivObj(){
        return privObj;
    }

    public static synchronized String getTools(){
        return tools;
    }

    public static synchronized void setHasGameEnded(boolean value) { hasGameEnded = value; }

    public static synchronized void setActivePlayer(String value) { activePlayer = value; }

    public static synchronized void setTypeRequested(TypeRequested value){
        if (value == null){
            notifyGUI(false);
            requested = value;
        } else {
            requested = value;
            notifyGUI(true);
        }
    }

    public static synchronized void setOwnUsername(String value){
        ownUsername = value;
    }

    public static synchronized void setCustomSchemes(boolean value){
        customSchemes = value;
    }

    public static synchronized void setSchemeChosen(int value){
        schemeChosen = value;
    }

    public static synchronized void setCanGoToGame(boolean value){
        canGoToGame = value;
    }

    public static synchronized void setToSend(String value){
        if (value == null)
            toSend = null;
        else
            toSend = value;
    }

    public static synchronized void setTable(String value){
        table = value;
    }

    public static synchronized void setTools(String value){
        tools = value;
    }

    public static synchronized void setPrivObj(String value){
        privObj = value;
    }

    public static synchronized void addPlayer(String value){
        players.add(value);
    }

    public static synchronized void clearPlayers() { players.clear(); }

    public static synchronized void addToSendIntList(String value) { toSendList.add(value); }

    public static synchronized String getToSendList() {
        String res = toSendList.get(0);
        toSendList.remove(0);
        return res;
    }

    public static synchronized void setHasGetStatus(boolean value) { hasGetStatus = value; }

    public static synchronized void emptyToSendIntList() {
        toSendList.clear();
    }

    public static synchronized boolean isToSendIntListEmpty() {
        return toSendList.isEmpty();
    }

    enum TypeRequested {
        ROUNDTRACK,
        RESERVE,
        WINDOWPATTERN,
        STANDARDREQUEST;
    }

    public static synchronized void refresh() {
        GameController.setNeedsToReload(true);
    }

    private static synchronized void notifyGUI(boolean turnOn){
        if (requested == null) return;
        switch (requested) {
            case STANDARDREQUEST:
                GameController.highlightTool(turnOn);
                GameController.highlightDraft(turnOn);
                GameController.highlightPass(turnOn);
                break;
            case RESERVE:
                GameController.highlightDraft(turnOn);
                break;
            case ROUNDTRACK:
                GameController.highlightRoundtrack(turnOn);
                break;
            case WINDOWPATTERN:
                GameController.highlightGrid(turnOn);
                break;
            default:
        }
    }

}