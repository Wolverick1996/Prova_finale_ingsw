package it.polimi.ingsw.client.view;

import java.util.ArrayList;
import java.util.List;

public class GUIupdater {

    private static String ownUsername;
    static SocketMessengerClient messenger;
    static int lobbyDelay = 20;
    static int numPlayersAtBeginning;
    static int numPlayers;
    static boolean isRMI = false;

    private static boolean customSchemes = false;
    private static int schemeChosen = -1;
    private static boolean canGoToGame = false;

    private static String toSend;
    private static List<String> schemesToChoose = new ArrayList<>();

    //Attributes of the game
    private static String table;
    private static String privObj;
    private static String tools;
    private static List<String> players = new ArrayList<>();

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

}