package it.polimi.ingsw.client.view;

import java.util.ArrayList;
import java.util.List;

public class GUIupdater {

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

    public static synchronized boolean getCustomSchemes() {
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

    public static synchronized void setCustomSchemes(boolean value) {
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
}
