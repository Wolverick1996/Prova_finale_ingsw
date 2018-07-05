package it.polimi.ingsw.client.view;

import java.util.*;

import static it.polimi.ingsw.client.view.IOHandlerClient.Interface.cli;

public class IOHandlerClient implements Observer {

    //THIS CLASS IS INTENDED FOR GUI/CLI MESSAGE SORTING

    private boolean debug = true;
    private String name;
    private Interface outputInt;
    private CLI commandLine;

    public IOHandlerClient (String username, Interface type) {
        this.name = username;
        GUIupdater.setOwnUsername(this.name);
        this.outputInt = type;
    }

    public void startInterface(){
        if (this.outputInt == cli)
            this.commandLine = new CLI();
    }

    public void send(String message){
        if(this.outputInt == cli){
            commandLine.output(message);
        } else {
            sendGUI(message);
        }
    }

    public String request(){
        if(this.outputInt == cli){
            return commandLine.input();
        } else {
            return requestGUI();
        }
    }

    private boolean chooseSchemes = false;
    private int lineReadNumber = 0;
    private boolean readStatus = false;
    private boolean readActivePlayer = false;

    private void sendGUI(String message){

        if (readActivePlayer){ readActivePlayer(message); return; }

        if (readStatus) { getStatus(message); return; }

        if (chooseSchemes) { chooseSchemes(message); }

        switch (message) {
            case "Do you want to use custom window patterns?" :
                if (GUIupdater.getCustomSchemes()) {
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
            case "Turn passed": case "Used tool 8, so is passing the turn..." :
                resetGUIupdater();
                readActivePlayer = true;
                break;
            case "Insert action (d = place dice, t = use tool, q = pass turn)" :
                GUIupdater.setTypeRequested(GUIupdater.TypeRequested.STANDARDREQUEST);
                break;
            default: break;
        }
    }

    private void chooseSchemes(String message){
        if (lineReadNumber%2 != 0) {
            GUIupdater.getSchemesToChoose().add(message);
        }
        lineReadNumber++;
        if (lineReadNumber == 8){
            chooseSchemes = false;
            lineReadNumber = 0;
        }
    }

    private void readActivePlayer(String message){
        GUIupdater.setActivePlayer(message.split(",")[0]);
        readActivePlayer = false;
    }

    private void getStatus(String message){
        if (message.equals("\n\n---------------------------------------------\n\n")) {
            GUIupdater.refresh();
            readStatus = false;
            lineReadNumber = 0;
        } else {
            switch (lineReadNumber) {
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
    }

    private void resetGUIupdater() {
        GUIupdater.setToSend(null);
        GUIupdater.setTypeRequested(null);
        GUIupdater.emptyToSendIntList();
    }

    private String requestGUI(){
        if (GUIupdater.getTypeRequested() == null) {
            String toSend = GUIupdater.getToSend();
            GUIupdater.setToSend(null);
            if (debug) System.out.println("I'm sending: " + toSend);
            return toSend;
        }
        while (GUIupdater.isToSendIntListEmpty()){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        String output = GUIupdater.getToSendList();
        if (debug) System.out.println("I'm sending: " + output);
        return output;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public enum Interface{
        cli,
        gui
    }
}
