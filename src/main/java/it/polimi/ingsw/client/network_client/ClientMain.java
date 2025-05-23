package it.polimi.ingsw.client.network_client;

import it.polimi.ingsw.client.view.GUIController;
import it.polimi.ingsw.client.view.GUIMain;
import it.polimi.ingsw.client.view.IOHandlerClient;
import it.polimi.ingsw.client.view.SocketMessengerClient;
import it.polimi.ingsw.server.network_server.ServerIntRMI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Entry point of the application: lets the program start (client-side)
 *
 * @author Andrea
 * @author Matteo
 */
public class ClientMain {

    private static ServerIntRMI serverRMI;
    private String ip;
    private static int port = 1337; //default
    private static final int MAX_PLAYERS = 4;
    private static boolean gameStarted = false;

    /**
     * Private constructor of the ClientMain class
     *
     * @param ip: IP of the server
     * @author Andrea
     */
    private ClientMain(String ip){
        this.ip = ip;
    }

    /**
     * Public constructor of the ClientMain class
     *
     * @param ip: IP of the server
     * @return the ClientMain object
     * @author Andrea
     */
    public static ClientMain instance(String ip){
        return new ClientMain(ip);
    }

    /**
     * Launches the application
     *
     * @param args: list of strings received from the user during the JAR execution
     * @author Andrea
     */
    public static void main(String[] args){
        String checkUI;
        try {
            checkUI = args[0];
        } catch (ArrayIndexOutOfBoundsException n){
            checkUI = "gui"; // default
        }
        try {
            port = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException n){
            port = 1337; // default
        }
        if (checkUI.equals("gui")){
            GUIMain.main(args);
            System.out.println("bye bye :)");
            System.exit(0);
        }
        //CLI only
        setUpConnection();
    }

    /**
     * Sets up the connection, can calls startClientRMI or startClientSocket
     *
     * @author Andrea
     */
    private static void setUpConnection(){
        System.out.println("Which kind of connection do you want to use? [RMI or socket]");
        Scanner scanner = new Scanner(System.in);
        String string;
        string = scanner.nextLine();
        string = string.toLowerCase();
        if (string.equals("rmi") || string.equals("socket")){
            boolean ipOK = false;
            while (!ipOK){
                if (gameStarted)
                    return;
                ClientMain clientMain = new ClientMain(askIP());
                if (string.equals("rmi")){
                    try {
                        clientMain.startClientRMI();
                        ipOK = true;
                    } catch (MalformedURLException | RemoteException e){
                        System.out.println("IP not correct (think about reloading the app with a different port)");
                    }
                } else {
                    try {
                        clientMain.startClientSocket();
                        ipOK = true;
                    } catch (IOException e) {
                        if (!gameStarted){
                            System.out.println("IP not correct (think about reloading the app with a different port)");
                        }
                    } catch (NoSuchElementException e) {
                        System.err.println("Nothing to read " + e.getMessage());
                    }
                }
            }
        } else {
            System.out.println("Invalid name. Type rmi or socket");
            setUpConnection();
        }
    }

    /**
     * Launches the GUI application (RMI case)
     *
     * @param text: player's username
     * @return the string representing the login status
     * @throws MalformedURLException if IP inserted is wrong
     * @throws RemoteException if client has connection issues
     * @throws NotBoundException if the stub is offline
     * @author Matteo
     */
    public String startGUIRMI(String text) throws MalformedURLException, RemoteException, NotBoundException {
        ServerIntRMI server;
        server = (ServerIntRMI) Naming.lookup("//" + this.ip + "/MyServer");
        serverRMI = server;
        ClientImplementationRMI client = new ClientImplementationRMI(text, IOHandlerClient.Interface.GUI);

        ClientIntRMI remoteRef = (ClientIntRMI) UnicastRemoteObject.exportObject(client, 0);

        if (server.login(remoteRef))
            return "OK";
        else {
            if (server.getConnected().size() >= MAX_PLAYERS)
                return "Lobby is full!";
            else
                return "ID already taken!";
        }
    }

    /**
     * Launches the GUI application (socket case)
     *
     * @param name: player's username
     * @param controller: GUIcontroller object (allows communications with GUI)
     * @return the string representing the login status
     * @throws IOException if client has connection issues
     * @author Matteo
     */
    public String startGUISocket(String name, GUIController controller) throws IOException {
        Socket socket = null;
        String feedback = "";
        try {
            socket = new Socket(ip, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connection established");
            ClientImplementationSocket clientImplementationSocket = new ClientImplementationSocket(socket);
            feedback = clientImplementationSocket.loginGUI(name);
            controller.setNumPlayersAtBeginning(Integer.parseInt(in.readLine())); //this sends the number of players to the server
        } catch (NoSuchElementException e) {
            System.err.println("NOTHING TO READ "+e.getMessage());
        } finally {
            SocketMessengerClient s = new SocketMessengerClient(socket, name, IOHandlerClient.Interface.GUI);
            GUIController.setMessenger(s);
        }
        return feedback;
    }

    /**
     * Returns the number of active players
     *
     * @return the number of active players
     * @throws RemoteException if client has connection issues
     * @author Matteo
     */
    public static int getPlayers() throws RemoteException {
        return serverRMI.playersInLobby();
    }

    /**
     * Asks the IP of the server to the user (CLI only)
     *
     * @return IP inserted
     * @author Andrea
     */
    private static String askIP(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("IP address of the Server: ");
        return scanner.nextLine();
    }

    /**
     * Sets the lobby delay with GUI interface
     *
     * @param delay: number of seconds
     * @throws RemoteException if client has connection issues
     * @author Matteo
     */
    public static void setGUIlobbyDelay(int delay) throws RemoteException {
        if (delay>=15 && delay<=60)
            serverRMI.setDelay(delay);
    }

    /**
     * Returns the number of players in the lobby if it changes, 999 if game started
     *
     * @return number of players in the lobby (999 if game already started)
     * @throws RemoteException if client has connection issues
     * @author Matteo
     */
    public static int waitForGameStart() throws RemoteException {
        int check;
        boolean hasStarted;
        serverRMI.confirmConnections();
        check = serverRMI.playersInLobby();
        hasStarted = serverRMI.hasStarted();
        if (hasStarted) return 999;
        else return check;
    }

    /**
     * Waiting room before the game start (RMI, client-side)
     *
     * @throws MalformedURLException if IP inserted is wrong
     * @throws RemoteException if client has connection issues
     * @author Andrea
     */
    private void startClientRMI() throws MalformedURLException, RemoteException {
        ServerIntRMI server;
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            boolean loginSuccess = false;
            boolean idTaken = false;
            boolean fullLobby = false;
            server = (ServerIntRMI) Naming.lookup("//" + this.ip + "/MyServer");
            do {
                if (fullLobby){
                    System.out.println("Retry later...");
                    fullLobby = false;
                }

                if (idTaken){
                    System.out.println("Login failed, this userID is already used");
                    idTaken = false;
                }

                Scanner scanner = new Scanner(System.in);
                System.out.println("Connection established");
                System.out.println("[Players in the lobby: " + server.playersInLobby() + "]\n" + "Login:\t\t(to refresh the page type '*')");
                String text = scanner.nextLine();

                if (text.equals("")){
                    System.out.println("Invalid name, your ID should be an alphanumeric of at least 1 character");
                    text = "*";
                }
                if (!text.equals("*")){
                    ClientImplementationRMI client = new ClientImplementationRMI(text, IOHandlerClient.Interface.CLI);

                    ClientIntRMI remoteRef = (ClientIntRMI) UnicastRemoteObject.exportObject(client, 0);

                    if (server.login(remoteRef)){
                        loginSuccess = true;
                        if (server.hasStarted()){
                            gameStarted = true;
                            return;
                        }
                    } else {
                        if (server.getConnected().size() >= MAX_PLAYERS)
                            fullLobby = true;
                        else if (server.hasStarted()){
                            fullLobby = false;
                            idTaken = false;
                        } else
                            idTaken = true;
                    }
                }
            } while (!loginSuccess);

            if (server.playersInLobby() == 1){
                System.out.println("You are the first player of the lobby!");
                InputDelay doWait = new InputDelay(new PrintWriter(System.out), true);
                executorService.submit(doWait);
                server.setDelay(doWait.getResult());
            }
            boolean active = true;
            System.out.println("Waiting for other players...\n");

            int num = 0;
            while (active){
                if (num != server.playersInLobby() && !server.hasStarted()){
                    server.confirmConnections();
                    System.out.println("[Players in the lobby: " + server.playersInLobby() + "]");
                    num = server.playersInLobby();
                } else if (server.hasStarted()){
                    gameStarted = true;
                    executorService.shutdownNow();
                    active = false;
                }
            }
        } catch (NotBoundException e) {
            System.err.println("This reference is not connected!");
        } catch (NoSuchElementException e) {
            System.err.println("NOTHING TO READ " +e.getMessage());
        }
    }

    /**
     * Waiting room before the game start (socket, client-side)
     *
     * @throws IOException if client has connection issues
     * @author Andrea
     */
    private void startClientSocket() throws IOException {
        Socket socket = null;
        int activePlayers;
        boolean success;
        String name;
        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            socket = new Socket(ip, port);
            Scanner scanner = new Scanner(System.in);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connection established");
            ClientImplementationSocket clientImplementationSocket = new ClientImplementationSocket(socket);
            do {
                name = clientImplementationSocket.login();
                if (clientImplementationSocket.isGameStarted()){
                    gameStarted = true;
                    SocketMessengerClient messenger = new SocketMessengerClient(socket, name, IOHandlerClient.Interface.CLI, true);
                    messenger.close();
                }
                activePlayers = Integer.parseInt(in.readLine());
                success = true;
                if (activePlayers == 1){
                    System.out.println("You are the first player of the lobby!");
                    InputDelay doWait = new InputDelay(out, false);
                    executorService.submit(doWait);
                } else
                    System.out.println("Waiting for other players...\n");

                int num = 0;
                int i;
                while (success){
                    //HERE WE SHOULD WAIT FOR THE "GAME STARTED" SIGNAL
                    try {
                        i = Integer.parseInt(in.readLine());
                    } catch (NumberFormatException n) {
                        i = num;
                    }
                    if (num != i){
                        if (i == 999){
                            executorService.shutdownNow();
                            gameStarted = true;
                            success = false;
                            SocketMessengerClient messenger = new SocketMessengerClient(socket, name, IOHandlerClient.Interface.CLI);
                            messenger.close();
                        }
                        if (!gameStarted){
                            System.out.println("[Players in the lobby: " + i + "]");
                            num = i;
                        }
                    }
                }
                if (gameStarted)
                    success = true;
            } while (!success);

            scanner.close();
            in.close();
            out.close();
        } catch (NoSuchElementException e) {
            System.err.println("NOTHING TO READ "+e.getMessage());
        } finally {
            if (socket != null)
                socket.close();
        }
    }

    /**
     * Subclass of ClientMain used to instantiate an interruptible thread used to ask an input
     *
     * @author Matteo
     */
    class InputDelay implements Runnable {

        PrintWriter out;
        boolean isRmi;
        boolean finished;
        int result = 20;

        /**
         * Constructor of the InputDelay class
         *
         * @param out: handler to write towards the server
         * @param isRmi: flag representing the type of connection used (true if RMI, false if socket)
         * @author Matteo
         */
        InputDelay (PrintWriter out, boolean isRmi){
            this.out = out;
            this.isRmi = isRmi;
            this.finished = false;
        }

        /**
         * Override of the run method of the Runnable interface
         *
         * @author Matteo
         */
        @Override
        public void run(){
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                int delay = 0;
                String delayString = null;
                do {
                    System.out.println("Please set a timer (min 15s, max 60s)");
                    try {
                        //wait until we have data to complete a readLine()
                        while (!in.ready())
                            Thread.sleep(200);

                        delayString = in.readLine();
                        delay = Integer.parseInt(delayString);
                    } catch (InterruptedException e) {
                        System.out.println("Never mind... you were too slow");
                        this.finished = false;
                        Thread.currentThread().interrupt();
                        return;
                    } catch (NumberFormatException n) {
                        delay = -1;
                    }
                } while ("".equals(delayString) || delay < 15 || delay > 60);

                if (isRmi){
                    this.result = delay;
                    this.finished = true;
                } else {
                    this.out.println(delayString);
                    this.out.flush();
                    System.out.println("Delay set!");
                }
            } catch (IOException e) {
                System.out.println("IOEXCEPTION IN INPUTDELAY");
            }
        }

        /**
         * Gets the int representing the delay inserted by the user
         *
         * @return the int representing the delay inserted (number of seconds)
         * @author Andrea
         */
        int getResult(){
            while (!finished){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("GETRESULT INTERRUPTED");
                    Thread.currentThread().interrupt();
                }
            }
            return this.result;
        }

    }

}