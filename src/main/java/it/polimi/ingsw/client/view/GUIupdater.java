package it.polimi.ingsw.client.view;

import java.util.ArrayList;
import java.util.List;

public class GUIupdater {

    private static GameController controller;

    /**
     * Gets the GameController for the current setup
     *
     * @return the controller
     * @author Matteo
     */
    public static synchronized GameController getController(){
        return controller;
    }

    /**
     * Sets the GameController for the current setup
     *
     * @param value : the controller
     * @author Matteo
     */
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
     * Gets a particular player from the data
     *
     * @param number: the id of the player
     * @return the String of the corresponding player
     * @author Matteo
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
     * Reads the active player that is currently playing
     *
     * @return the String of the player
     * @author Matteo
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
     * Gets the current player in the setup
     *
     * @return the String of the Own player
     * @author Matteo
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
     * Gets the scheme of the current player
     *
     * @return the String of the own scheme
     * @author Matteo
     */
    public static synchronized String getOwnScheme(){
        String me = getOwnPlayer();
        String[] split = me.split("\n");
        me = split[3] + "\n" + split[4] + "\n" + split[5] + "\n" + split[6] + "\n" + split[7] + "\n" + split[8] + "\n" + split[9];
        return me;
    }

    /**
     * Check if the GUI needs to refresh the Scene
     *
     * @return true if the scene has to be reloaded
     * @author Matteo
     */
    static synchronized boolean getNeedsToReload(){
        return needsToReload;
    }

    /**
     * Checks if the game has ended
     *
     * @return true if game has ended
     * @author Matteo
     */
    static synchronized boolean getHasGameEnded(){
        return hasGameEnded;
    }

    /**
     * Checks if the GUIupdater is up to date during the prep phase of the game
     *
     * @return true if GUIupdater has received the status
     * @author Matteo
     */
    public static synchronized boolean getHasGetStatus(){
        return hasGetStatus;
    }

    /**
     * Gets the context of the input request.
     *
     * @return the Type of the Input requested
     * @author Matteo
     */
    static synchronized TypeRequested getTypeRequested(){
        return requested;
    }

    /**
     * Gets the username of the current player
     *
     * @return the player's own username
     * @author Matteo
     */
    public static synchronized String getOwnUsername(){
        return ownUsername;
    }

    /**
     * Checks the answer to the "Do you want custom schemes?" question
     *
     * @return y if custom schemes are chosen
     * @author Matteo
     */
    static synchronized boolean getCustomSchemes(){
        return customSchemes;
    }

    /**
     * Gets the scheme that was chosen for the game
     *
     * @return the number of the Scheme that was chosen
     * @author Matteo
     */
    static synchronized int getSchemeChosen(){
        return schemeChosen;
    }

    /**
     * Checks if the Client is able to get to the Game Phase
     *
     * @return true if the Game is ready
     * @author Matteo
     */
    static synchronized boolean getCanGoToGame(){
        return canGoToGame;
    }

    /**
     * Gets the toSend variable This variable has medium priority, and is sended only if the Type requested
     * is not specified and no toSendForced is defined
     *
     * @return the String to be sent to the Server
     * @author Matteo
     */
    static synchronized String getToSend(){
        return toSend;
    }

    /**
     * Gets the String that contains the ranking of the game.
     *
     * @return the String of the ranking
     * @author Matteo
     */
    static synchronized String getFinalMessage(){
        return finalMessage;
    }

    /**
     * Gets the toSendForced variable. This variable has maximum priority and if defined, is sent to the server
     *
     * @return the Forced toSend String
     * @author Matteo
     */
    static synchronized String getToSendForced(){
        String out = toSendForced;
        toSendForced = null;
        return out;
    }

    /**
     * Gets the list of schemes that can be chosen in the prep phase, before the game
     *
     * @return : the String containing the 4 schemes
     * @author Matteo
     */
    static synchronized List getSchemesToChoose(){
        return schemesToChoose;
    }

    /**
     * Gets the last number of players registered during the lobby
     * NOTE: during debugging, if lobby delay is less than 5 seconds, this value is 1 for player1
     *
     * @return the number of players
     * @author Matteo
     */
    static synchronized int getNumPlayers(){
        return numPlayers;
    }

    /**
     * Returns the string of the table in the current setup
     *
     * @return the String of the table
     * @author Matteo
     */
    public static synchronized String getTable(){
        return table;
    }

    /**
     * Gets the Players of the current setup
     *
     * @return the list of players
     * @author Matteo
     */
    public static synchronized List getPlayers(){
        return players;
    }

    /**
     * gets the Private Objective of the current player
     *
     * @return the String of the PrivOC
     * @author Matteo
     */
    static synchronized String getPrivObj(){
        return privObj;
    }

    /**
     * Gets the Tools fr the current setup
     *
     * @return the string of the tools
     * @author Matteo
     */
    static synchronized String getTools(){
        return tools;
    }

    /**
     * Sets if the GUI needs to be updated
     *
     * @param value: true if GUI needs to refresh
     * @author Matteo
     */
    static synchronized void setNeedsToReload(boolean value){
        needsToReload = value;
    }

    /**
     * Sets the username of the active player that is currently playing
     *
     * @param value: the username of the active player
     * @author Matteo
     */
    static synchronized void setActivePlayer(String value){
        activePlayer = value;
    }

    /**
     * Sets the messege with the ranking
     *
     * @param value: the String of the ranking
     * @authot Matteo
     */
    static synchronized void setFinalMessage(String value){
        finalMessage = value;
    }

    /**
     * Sets the Type of input requested, to show the user with effect what to do and to prevent invalid inputs
     *
     * @param value: the Type that is requested
     * @author matteo
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
     * Sets the username of the current player
     *
     * @param value: the username
     * @author matteo
     */
    static synchronized void setOwnUsername(String value){
        ownUsername = value;
    }

    /**
     * Sets the boolean of the "Custom scheme" setup choice
     *
     * @param value: true if Custom schemes were chosen
     * @author Matteo
     */
    static synchronized void setCustomSchemes(boolean value){
        customSchemes = value;
    }

    /**
     *  Sets the scheme that was chosen during the prep phase
     *
     * @param value: the int of the scheme chosen
     * @author Matteo
     */
    static synchronized void setSchemeChosen(int value){
        schemeChosen = value;
    }

    /**
     * Set if the Client can proceed to the game
     *
     * @param value: true if he can go
     * @author Matteo
     */
    static synchronized void setCanGoToGame(boolean value){
        canGoToGame = value;
    }

    /**
     * Sets the toSend variable (check the get method for further information)
     *
     * @param value: the String to be sent
     */
    static synchronized void setToSend(String value){
        toSend = value;
    }

    /**
     * Sets the toSendForced variable (check the get method for further information)
     *
     * @param value: the String to be Forcedly sent
     * @author Matteo
     */
    static synchronized void setToSendForced(String value){
        toSendForced = value;
    }

    /**
     * Sets the table for the current setup
     *
     * @param value: the string of the table
     * @author Matteo
     */
    public static synchronized void setTable(String value){
        table = value;
    }

    /**
     * Sets the tools for the current setup
     *
     * @param value: the String of the tools
     * @author Matteo
     */
    static synchronized void setTools(String value){
        tools = value;
    }

    /**
     * Sets the PrivOC for the current setup
     *
     * @param value: String of the privOC
     * @author Matteo
     */
    static synchronized void setPrivObj(String value){
        privObj = value;
    }


    /**
     * Add a player to the current setup
     *
     * @param value: the String of the player
     * @author Matteo
     */
    static synchronized void addPlayer(String value){
        players.add(value);
    }

    /**
     * Clear all the players from the current setup
     *
     * @author Matteo
     */
    static synchronized void clearPlayers(){
        players.clear();
    }

    /**
     * Adds a String to the list of consecutive outputs from the Client to the Server
     *
     * @param value: the message to add
     * @author Matteo
     */
    static synchronized void addToSendIntList(String value){
        toSendList.add(value);
    }

    /**
     * gets the list of consecutive outputs from the Client to the Server
     *
     * @return the whole list of messages to be sent
     * @author Matteo
     */
    static synchronized String getToSendList(){
        String res = toSendList.get(0);
        toSendList.remove(0);
        return res;
    }

    /**
     * Sets if the GUIupdater has got the STATUS of the game
     *
     * @param value: true if Status was sent
     * @author Matteo
     */
    public static synchronized void setHasGetStatus(boolean value){
        hasGetStatus = value;
    }

    /**
     * Empty the list of consecutive ouputs to be sent to the server
     *
     * @author Matteo
     */
    static synchronized void emptyToSendIntList(){
        toSendList.clear();
    }

    /**
     * Checks if the List of messages to be sent to server is empty
     *
     * @return true if the list is empty
     * @author Matteo
     */
    static synchronized boolean isToSendIntListEmpty(){
        return toSendList.isEmpty();
    }

    /**
     * Enum that contains all of the different kind of Requests required from the server
     *
     * @author Matteo
     */
    enum TypeRequested {
        REFRESH,
        ROUNDTRACK,
        RESERVE,
        WINDOWPATTERN,
        STANDARDREQUEST;
    }

    private static boolean toolDisabled = false;
    private static boolean draftDisabled = false;

    /**
     * Sets the tool's effect "disabled" on the GUI
     *
     * @param value : true if the tools needs to be disabled
     * @author Matteo
     */
    static synchronized void setToolDisabled(boolean value) { toolDisabled = value; }
    /**
     * Sets the Draft's effect "disabled" on the GUI
     *
     * @param value : true if the draft needs to be disabled
     * @author Matteo
     */
    static synchronized void setDraftDisabled(boolean value) { draftDisabled = value; }
    /**
     * Gets the "disabled" flag of the Tools on the GUI
     *
     * @return true if the tools needs to be disabled
     * @author Matteo
     */
    private static synchronized boolean getToolDisabled() { return toolDisabled; }
    /**
     * Gets the "disabled" flag of the Draft on the GUI
     *
     * @return true if the draft needs to be disabled
     * @author Matteo
     */
    private static synchronized boolean getDraftDisabled() { return draftDisabled; }

    /**
     * Sends the controller the right flags in order to trigger the right effects
     *
     * @param turnOn: true if elements need to be turned on
     * @author Matteo
     */
    private static synchronized void notifyGUI(boolean turnOn){
        if (requested == null) return;
        switch (requested) {
            case STANDARDREQUEST:
                if (!getToolDisabled())
                    GameController.highlightTool(turnOn);
                if (!getDraftDisabled())
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