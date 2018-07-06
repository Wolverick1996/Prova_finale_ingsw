package it.polimi.ingsw.client.view;

import java.util.*;

import static it.polimi.ingsw.client.view.IOHandlerClient.Interface.cli;

/**
 * Class intended for GUI/CLI message sorting
 *
 * @author Matteo
 */
public class IOHandlerClient implements Observer {

    private boolean debug = false;
    private String name;
    private Interface outputInt;
    private CLI commandLine;

    /**
     * Constructor of the IOHandlerClient class
     *
     * @param username: player's username
     * @param type: type of the user interface (CLI/GUI)
     * @author Matteo
     */
    public IOHandlerClient (String username, Interface type){
        this.name = username;
        GUIupdater.setOwnUsername(this.name);
        this.outputInt = type;
    }

    /**
     * Calls the CLI constructor to let it start
     *
     * @author Matteo
     */
    public void startInterface(){
        if (this.outputInt == cli)
            this.commandLine = new CLI();
    }

    /**
     * Sends a message to the user interface
     *
     * @param message: the string to be passed to CLI/GUI
     * @author Matteo
     */
    public void send(String message){
        if (this.outputInt == cli){
            commandLine.output(message);
        } else {
            sendGUI(message);
        }
    }

    /**
     * Requests an input from the user interface
     *
     * @return the input requested
     * @author Matteo
     */
    public String request(){
        if (this.outputInt == cli){
            return commandLine.input();
        } else {
            return requestGUI();
        }
    }

    private boolean chooseSchemes = false;
    private int lineReadNumber = 0;
    private boolean readStatus = false;

    private void sendGUI(String message){
        if (debug) System.out.println(message);

        if (readStatus){
            if (message.equals("\n\n---------------------------------------------\n\n")){
                readStatus = false;
                lineReadNumber = 0;
            } else {
                switch (lineReadNumber){
                    case 0:
                        GUIupdater.setTable(message);
                        break;
                    case 1:
                        GUIupdater.setTools(message);
                        break;
                    case 2:
                        GUIupdater.setPrivObj(message);
                        break;
                    default:
                        GUIupdater.addPlayer(message);
                }
                lineReadNumber++;
            }
            return;
        }

        if (chooseSchemes){
            if (lineReadNumber%2 != 0)
                GUIupdater.getSchemesToChoose().add(message);

            lineReadNumber++;
            if (lineReadNumber == 8){
                chooseSchemes = false;
                lineReadNumber = 0;
            }
        }

        switch (message){
            case "Do you want to use custom window patterns?" :
                if (GUIupdater.getCustomSchemes()){
                    GUIupdater.setToSend("y");
                } else {
                    GUIupdater.setToSend("n");
                }
                break;
            case "CHOOSE A SCHEME :" :
                chooseSchemes = true;
                break;
            case "Pick a scheme (write 1, 2, 3 or 4)" :
                while (GUIupdater.getSchemeChosen() == -1){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.exit(777);
                    }
                }
                GUIupdater.setToSend(Integer.toString(GUIupdater.getSchemeChosen()));
                break;
            case "Game is starting!\n" :
                GUIupdater.setCanGoToGame(true);
                break;
            case "Here is the status: " :
                readStatus = true;
                break;
            default: break;
        }
    }

    private String requestGUI(){
        String toSend = GUIupdater.getToSend();
        GUIupdater.setToSend(null);
        return toSend;
    }

    @Override
    public void update(Observable o, Object arg){

    }

    public enum Interface {
        cli,
        gui
    }

}