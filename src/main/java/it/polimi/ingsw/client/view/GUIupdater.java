package it.polimi.ingsw.client.view;

import java.util.ArrayList;
import java.util.List;

public class GUIupdater {

    private static GameController controller;

    public static synchronized GameController getController(){
        return controller;
    }

    public static synchronized void setController(GameController value){
        controller = value;
    }

    private static boolean hasGameEnded = false;
    private static boolean hasGetStatus = false;
    private static boolean needsToReload = false;

    private static String ownUsername;
    private static String activePlayer;
    static SocketMessengerClient messenger;
    static int lobbyDelay = 20000;
    static int numPlayersAtBeginning;
    static int numPlayers;
    static boolean isRMI = false;

    private static boolean customSchemes = false;
    private static int schemeChosen = -1;
    private static boolean canGoToGame = false;

    private static TypeRequested requested;

    private static String toSend;
    private static String toSendForced;
    private static List<String> toSendList = new ArrayList<>();
    private static List<String> schemesToChoose = new ArrayList<>();

    //Attributes of the game
    private static String table;
    private static String privObj;
    private static String tools;
    private static List<String> players = new ArrayList<>();

    private static String finalMessage;

    /**
     *
     *
     * @param number:
     * @return
     */
    static synchronized String getPlayer(int number){
        int count = 1;
        for (String p: players) {
            if (p.equals(getOwnPlayer()))
                continue;
            else {
                if (count == number)
                    return p;
                count++;
            }
        }
        return getOwnPlayer();
    }

    /**
     *
     *
     * @return
     */
    static synchronized String getActivePlayer(){
        for (String p : players) {
            String temp = p;
            if (p.split("\n")[0].equals(activePlayer))
                return temp;
        }
        return players.get(0);
    }

    /**
     *
     *
     * @return
     */
    static synchronized String getOwnPlayer(){
        for (String p : players) {
            String temp = p;
            if (p.split("\n")[0].equals(ownUsername))
                return temp;
        }
        return players.get(0);
    }

    /**
     *
     *
     * @return
     */
    public static synchronized String getOwnScheme(){
        String me = getOwnPlayer();
        String[] split = me.split("\n");
        me = split[3] + "\n" + split[4] + "\n" + split[5] + "\n" + split[6] + "\n" + split[7] + "\n" + split[8] + "\n" + split[9];
        return me;
    }

    /**
     *
     *
     * @return
     */
    static synchronized boolean getNeedsToReload(){
        return needsToReload;
    }

    /**
     *
     *
     * @return
     */
    static synchronized boolean getHasGameEnded(){
        return hasGameEnded;
    }

    /**
     *
     *
     * @return
     */
    public static synchronized boolean getHasGetStatus(){
        return hasGetStatus;
    }

    /**
     *
     *
     * @return
     */
    static synchronized TypeRequested getTypeRequested(){
        return requested;
    }

    /**
     *
     *
     * @return
     */
    public static synchronized String getOwnUsername(){
        return ownUsername;
    }

    /**
     *
     *
     * @return
     */
    static synchronized boolean getCustomSchemes(){
        return customSchemes;
    }

    /**
     *
     *
     * @return
     */
    static synchronized int getSchemeChosen(){
        return schemeChosen;
    }

    /**
     *
     *
     * @return
     */
    static synchronized boolean getCanGoToGame(){
        return canGoToGame;
    }

    /**
     *
     *
     * @return
     */
    static synchronized String getToSend(){
        return toSend;
    }

    /**
     *
     *
     * @return
     */
    static synchronized String getFinalMessage(){
        return finalMessage;
    }

    /**
     *
     *
     * @return
     */
    static synchronized String getToSendForced(){
        String out = toSendForced;
        toSendForced = null;
        return out;
    }

    /**
     *
     *
     * @return
     */
    static synchronized List getSchemesToChoose(){
        return schemesToChoose;
    }

    /**
     *
     *
     * @return
     */
    static synchronized int getNumPlayers(){
        return numPlayers;
    }

    /**
     *
     *
     * @return
     */
    public static synchronized String getTable(){
        return table;
    }

    /**
     *
     *
     * @return
     */
    public static synchronized List getPlayers(){
        return players;
    }

    /**
     *
     *
     * @return
     */
    static synchronized String getPrivObj(){
        return privObj;
    }

    /**
     *
     *
     * @return
     */
    static synchronized String getTools(){
        return tools;
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setNeedsToReload(boolean value){
        needsToReload = value;
    }

    /**
     *
     *
     * @param value:
     */
    public static synchronized void setHasGameEnded(boolean value){
        hasGameEnded = value;
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setActivePlayer(String value){
        activePlayer = value;
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setFinalMessage(String value){
        finalMessage = value;
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setTypeRequested(TypeRequested value){
        if (value == TypeRequested.REFRESH) {
            setNeedsToReload(true);
            GameController.highlightRefresh(true);
            return;
        }
        if (value == null) {
            notifyGUI(false);
            requested = null;
        } else {
            requested = value;
            notifyGUI(true);
        }
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setOwnUsername(String value){
        ownUsername = value;
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setCustomSchemes(boolean value){
        customSchemes = value;
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setSchemeChosen(int value){
        schemeChosen = value;
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setCanGoToGame(boolean value){
        canGoToGame = value;
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setToSend(String value){
        toSend = value;
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setToSendForced(String value){
        toSendForced = value;
    }

    /**
     *
     *
     * @param value:
     */
    public static synchronized void setTable(String value){
        table = value;
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setTools(String value){
        tools = value;
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void setPrivObj(String value){
        privObj = value;
    }


    /**
     *
     *
     * @param value:
     */
    static synchronized void addPlayer(String value){
        players.add(value);
    }

    /**
     *
     *
     */
    static synchronized void clearPlayers(){
        players.clear();
    }

    /**
     *
     *
     * @param value:
     */
    static synchronized void addToSendIntList(String value){
        toSendList.add(value);
    }

    /**
     *
     *
     * @return
     */
    static synchronized String getToSendList(){
        String res = toSendList.get(0);
        toSendList.remove(0);
        return res;
    }

    /**
     *
     *
     * @param value:
     */
    public static synchronized void setHasGetStatus(boolean value){
        hasGetStatus = value;
    }

    /**
     *
     *
     */
    static synchronized void emptyToSendIntList(){
        toSendList.clear();
    }

    /**
     *
     *
     * @return
     */
    static synchronized boolean isToSendIntListEmpty(){
        return toSendList.isEmpty();
    }

    /**
     *
     *
     */
    enum TypeRequested {
        REFRESH,
        ROUNDTRACK,
        RESERVE,
        WINDOWPATTERN,
        STANDARDREQUEST;
    }

    /**
     *
     *
     * @param turnOn:
     */
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
            case REFRESH:
                GameController.highlightRefresh(turnOn);
                break;
            default:
        }
    }

}