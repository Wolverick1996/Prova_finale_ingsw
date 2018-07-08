package it.polimi.ingsw.client.view;

import static it.polimi.ingsw.client.view.IOHandlerClient.Interface.CLI;

/**
 * Class intended for GUI/CLI message sorting
 *
 * @author Matteo
 */
public class IOHandlerClient {

    //THIS CLASS IS INTENDED FOR GUI/CLI MESSAGE SORTING

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
        if (this.outputInt == CLI)
            this.commandLine = new CLI();
    }

    /**
     * Sends a message to the user interface
     *
     * @param message: the string to be passed to CLI/GUI
     * @author Matteo
     */
    public void send(String message){
        if (this.outputInt == CLI){
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
        if (this.outputInt == CLI){
            return commandLine.input();
        } else {
            return requestGUI();
        }
    }

    private boolean chooseSchemes = false;
    private boolean privOC = false;
    private int lineReadNumber = 0;
    private boolean readStatus = false;
    private boolean readActivePlayer = false;
    private String activePlayer;
    private static final String FAIL = "999";
    private boolean tool8 = false;

    /**
     * Gets the message sent by Client and decodes it for the GUI.
     * Each string correspond to a different mechanism that triggers different events
     *
     * @param message : the message sent from the client
     * @author Matteo
     */
    private void sendGUI(String message){
        if (debug) System.out.println(message);
        String playerdidnotdoitright = "Player " + activePlayer + " didn't do it right, try again\n";
        String exceptioncaughtnotright = "EXCEPTION CAUGHT! Player " + activePlayer + " didn't do it right, try again\n";

        if (message.contains(" is using Tool 8")){
            tool8 = true;
        }

        if (checkIfGameEnded(message)){
            readActivePlayer = false;
            GUIupdater.setFinalMessage(message);
            return;
        }

        if (checkIfTool6or11(message)){
            return;
        }

        if (message.equals(playerdidnotdoitright) || message.equals(exceptioncaughtnotright)) {
            resetGUIupdater();
            //Undo the move, go back to standardchoice
            GUIupdater.setToSendForced("0");
        }

        if (readActivePlayer){ readActivePlayer(message); return; }

        if (readStatus){ getStatus(message); return; }

        if (chooseSchemes){ chooseSchemes(message); }

        if (privOC) {
            setPrivObj(message);
            return;
        }

        switch (message){
            case "Do you want to use custom window patterns?" :
                if (GUIupdater.getCustomSchemes()){
                    GUIupdater.setToSend("y");
                } else {
                    GUIupdater.setToSend("n");
                }
                break;
            case "Custom window patterns enabled!":
            case "Old school, only standard window patterns!":
                privOC = true;
                break;
            case "CHOOSE A SCHEME :" :
                chooseSchemes = true;
                break;
            case "Pick a scheme (write 1, 2, 3 or 4)" :
                while (GUIupdater.getSchemeChosen() == -1){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                        System.exit(777);
                    }
                }
                GUIupdater.setToSend(Integer.toString(GUIupdater.getSchemeChosen()));
                break;
            case "Here is the status: " :
                readStatus = true;
                break;
            case "Used tool 8, so is passing the turn..." :
            case "Turn passed":
                GUIupdater.setDraftDisabled(false);
                if (!tool8)
                    GUIupdater.setToolDisabled(false);
                else
                    tool8 = false;
                resetGUIupdater();
                readActivePlayer = true;
                break;
            case "Insert action (d = place dice, t = use tool, q = pass turn)" :
                resetGUIupdater();
                boolean check;
                setTypeRequested(GUIupdater.TypeRequested.STANDARDREQUEST);
                break;
            case "Someone is trying to use a tool card TWICE... YOU CAAAAAAAAAAAAAN'T" :
            case "Nope, nothing done :(" :
            case "Something went wrong... :(" :
            case "Someone is trying to place a dice TWICE... YOU CAAAAAAAAAAAAAN'T" :
            case "Nope, nothing done" :
                resetGUIupdater();
                break;
            case "Dice correctly placed!" :
                GUIupdater.setDraftDisabled(true);
                break;
            case "HOORAY!" :
                GUIupdater.setToolDisabled(true);
                break;
            case "Choose a dice from round track [from 1 to N]":
                resetGUIupdater();
                if (GUIupdater.getController().getRoundtrackIMG().isEmpty()) {
                    GUIupdater.setToSend(FAIL);
                    break;
                }
                setTypeRequested(GUIupdater.TypeRequested.ROUNDTRACK);
                break;
            case "Insert the place of the dice in the reserve":
                resetGUIupdater();
                setTypeRequested(GUIupdater.TypeRequested.RESERVE);
                break;
            case "Insert the coordinates of the dice to be placed, one at a time (x, y)":
            case "Insert the OLD coordinates of the dice to be moved, one at a time (x, y)" :
            case "Insert the NEW coordinates of the dice to be moved, one at a time (x, y)" :
            case "Insert the OLD coordinates of the FIRST dice to be moved, one at a time (x, y)" :
            case "Insert the NEW coordinates of the FIRST dice to be moved, one at a time (x, y)" :
            case "Insert the OLD coordinates of the SECOND dice to be moved, one at a time (x, y)" :
            case "Insert the NEW coordinates of the SECOND dice to be moved, one at a time (x, y)" :
                resetGUIupdater();
                setTypeRequested(GUIupdater.TypeRequested.WINDOWPATTERN);
                break;
            case "Increment or decrement the value typing '+1' or '-1'" :
                resetGUIupdater();
                GameController.choicePopup("Do you want to increment or decrement the value?",
                        "+1", "-1", true);
                break;
            case "Do you want to move another dice?" :
                resetGUIupdater();
                GameController.choicePopup("Do you want to move another dice?",
                        "yes", "no", false);
                break;
            default: break;
        }
    }

    /**
     * Reads and sets the privOC
     *
     * @param message the message from the Server
     * @author Matteo
     */
    private void setPrivObj(String message){
        if (!message.contains("has to choose a scheme")){
            privOC = false;
            GUIupdater.setPrivObj(message);
        }
    }

    /**
     * Triggers the Game End
     *
     * @param message : the message containing the ranking
     * @return the right message to be read
     * @author Matteo
     */
    private boolean checkIfGameEnded(String message) {
        return (message.split("!")[0].equals("Game ended"));
    }

    /**
     * Sets the type of input requested on the GUIupdater
     *
     * @param requested : the type of input requested to the user
     * @author Matteo
     */
    private void setTypeRequested(GUIupdater.TypeRequested requested) {
        GUIupdater.setTypeRequested(requested);
    }

    /**
     * Triggers the algorithm to read the Schemes to be chosen
     *
     * @param message : the message from the server
     * @author Matteo
     */
    private void chooseSchemes(String message){
        if (lineReadNumber%2 != 0)
            GUIupdater.getSchemesToChoose().add(message);

        lineReadNumber++;
        if (lineReadNumber == 8){
            chooseSchemes = false;
            lineReadNumber = 0;
        }
    }

    /**
     * Checks from the message the right tool that was used and needs further examination
     *
     * @param message : the message from the server
     * @return true if tool was used
     * @author Matteo
     */
    private boolean checkIfTool6or11(String message){
        String temp = message;
        if (temp.split(":")[0].equals("Dice rolled") || temp.split((":"))[0].equals("Dice extracted from the bag")){
            resetGUIupdater();
            GameController.dicePopup(message);
            return true;
        } else
            return false;
    }

    /**
     * Reads the active player and sets it
     *
     * @param message : the message from the server
     * @author Matteo
     */
    private void readActivePlayer(String message){
        activePlayer = message.split(",")[0];
        GUIupdater.setActivePlayer(activePlayer);
        readActivePlayer = false;
    }

    /**
     * Gets the STATUS from the Server and updates all the data for GUI refreshing
     *
     * @param message : the status
     * @author Matteo
     */
    private void getStatus(String message){
        if (message.equals("\n\n---------------------------------------------\n\n")) {
            GUIupdater.setCanGoToGame(true);
            setTypeRequested(GUIupdater.TypeRequested.REFRESH);
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
                    GUIupdater.clearPlayers();
                    break;
                default:
                    GUIupdater.addPlayer(message);
            }
            lineReadNumber++;
        }
    }

    /**
     * Clears all data in the GUIupdater
     *
     * @author Matteo
     */
    private void resetGUIupdater(){
        GUIupdater.setToSend(null);
        setTypeRequested(null);
        GUIupdater.emptyToSendIntList();
    }

    /**
     * Request an input from the gui. toSendForced has maximum priority.
     *
     * @return the input of the player
     * @author Matteo
     */
    private String requestGUI(){
        if (debug) System.out.println("The context of the input request is " + GUIupdater.getTypeRequested());
        String forced = GUIupdater.getToSendForced();
        if (forced != null)
            return forced;
        if (GUIupdater.getTypeRequested() == null) {
            String toSend = GUIupdater.getToSend();
            if (toSend == null) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                return requestGUI();
            }
            GUIupdater.setToSend(null);
            if (debug) System.out.println("I'm sending: " + toSend);
            return toSend;
        }
        while (GUIupdater.isToSendIntListEmpty()){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(e.getMessage());
            }
        }
        String output = null;
        do {
            output = GUIupdater.getToSendList();
        } while (output == null);
        if (debug) System.out.println("I'm sending from list: " + output);
        return output;
    }

    /**
     * The interface of the current user
     *
     * @author Matteo
     */
    public enum Interface {
        CLI,
        GUI
    }

}