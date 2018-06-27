package it.polimi.ingsw.client.view;

import java.util.*;

import static it.polimi.ingsw.client.view.IOHandlerClient.Interface.cli;

public class IOHandlerClient implements Observer {

    //THIS CLASS IS INTENDED FOR GUI/CLI MESSAGE SORTING

    private String name;
    private Interface outputInt;
    private CLI commandLine;
    //GUI graphicInt;

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
        } /* else {
            sendGUI(message);
        }*/
    }

    public String request(){
        if(this.outputInt == cli){
            return commandLine.input();
        } /* else {
            requestGUI(message);
        }*/
        return "-1";
    }

    private void sendGUI(String message){
        //TODO
    }

    private String requestGUI(){
        //TODO
        return "not implemented";
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public enum Interface{
        cli,
        gui
    }
}
