package it.polimi.ingsw.client.network_client;

import it.polimi.ingsw.client.view.IOHandlerClient;

import java.util.Observable;

/**
 * Implementation of the ClientIntRMI interface
 *
 * @author Andrea
 */
public class ClientImplementationRMI extends Observable implements ClientIntRMI {

    private String name;
    private IOHandlerClient handler;

    /**
     * Constructor of the ClientImplementationRMI object
     *
     * @param n: player's username
     * @param handlerType: choice between CLI or GUI
     * @author Andrea
     */
    ClientImplementationRMI(String n, IOHandlerClient.Interface handlerType){
        name = n;
        this.handler = new IOHandlerClient(n, handlerType);
    }

    /**
     * Shows to the client a specified message
     *
     * @param message: the string to be showed
     * @author Andrea
     */
    public void notify(String message){
        try {
            this.handler.send(message);
        } catch (NullPointerException e) {
            //The CLI or the GUI is not active yet
            System.out.println(message);
        }
    }

    /**
     * Gets the player's username
     *
     * @return the player's username
     * @author Andrea
     */
    public String getName(){
        return this.name;
    }

    /**
     * Throws an exception if client has connection issues (the exception will be caught from the server)
     *
     * @author Andrea
     */
    public void confirmConnection(){
        //used to ping and check connection
    }

    /**
     * Calls the IOhandler getInput method
     *
     * @return the input inserted by the player
     * @author Andrea
     */
    public String getInput(){
        return this.handler.request();
    }

    /**
     * Calls the IOhandler method to start the CLI
     *
     * @author Andrea
     */
    public void startInterface(){
        this.handler.startInterface();
    }

}