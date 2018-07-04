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

    private void sendGUI(String message){
        if (debug) System.out.println(message);

        if (chooseSchemes && lineReadNumber<8) {
            if (lineReadNumber%2 != 0) {
                GUIupdater.getSchemesToChoose().add(message);
            }
            lineReadNumber++;
            if (lineReadNumber == 8){
                chooseSchemes = false;
            }
        }

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
            default: break;
        }
    }

    private String requestGUI(){
        String toSend = GUIupdater.getToSend();
        GUIupdater.setToSend(null);
        return toSend;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public enum Interface{
        cli,
        gui
    }
}
