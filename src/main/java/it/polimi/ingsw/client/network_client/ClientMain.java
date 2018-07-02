package it.polimi.ingsw.client.network_client;

import com.sun.org.apache.xpath.internal.operations.Bool;
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

public class ClientMain {

    private String ip;
    private static final int PORT = 1337;
    private static final int MAX_PLAYERS = 4;

    private ClientMain(String ip){
        this.ip = ip;
    }

    public static ClientMain instance(String ip){
        return new ClientMain(ip);
    }

    public static void main(String[] args) {
        String check_UI = "CLI";

        if (check_UI.equals("GUI")){
            GUIMain.main(args);
            System.out.println("bye bye :)");
            System.exit(0);
        }
        //ONLY CLI
        System.out.println("Which kind of connection do you want to use? [RMI or socket]");
        Scanner scanner = new Scanner(System.in);
        String string;
        Boolean check = false;
        while (!check) {
            string = scanner.nextLine();
            string = string.toLowerCase();
            if (string.equals("rmi") || string.equals("socket")) {
                check = true;
                boolean ipOK = false;
                while(!ipOK){
                    ClientMain clientMain = new ClientMain(askIP());
                    if (string.equals("rmi")) {
                        try {
                            clientMain.startClientRMI();
                            ipOK = true;
                        } catch (MalformedURLException | RemoteException e){
                            System.out.println("IP not correct");
                        }
                    } else {
                        try {
                            clientMain.startClientSocket();
                            ipOK = true;
                        } catch (IOException e) {
                            System.out.println("IP not correct");
                        } catch (NoSuchElementException e) {
                            System.err.println("Nothing to read " + e.getMessage());
                        }
                    }
                }
                break;
            } else {
                System.out.println("Invalid name. Type rmi or socket");
            }
        }
    }

    public String startGUIRMI(String text) throws MalformedURLException, RemoteException, NotBoundException{
        ServerIntRMI server;
        server = (ServerIntRMI) Naming.lookup("//" + this.ip + "/MyServer");

        ClientImplementationRMI client = new ClientImplementationRMI(text);

        ClientIntRMI remoteRef = (ClientIntRMI) UnicastRemoteObject.exportObject(client, 0);

        if (server.login(remoteRef)) {
            return "OK";
        }
        else {
            if (server.getConnected().size() >= MAX_PLAYERS)
                return "Lobby is full!";
            else
                return "ID already taken!";
        }
    }

    private void startClientRMI() throws MalformedURLException, RemoteException{
        ServerIntRMI server;
        try {

            boolean loginSuccess = false;
            boolean idTaken = false;
            boolean fullLobby = false;
            server = (ServerIntRMI) Naming.lookup("//" + this.ip + "/MyServer");
            do {
                if (fullLobby) {
                    System.out.println("Retry later...");
                    fullLobby = false;
                }

                if (idTaken) {
                    System.out.println("Login failed, this userID is already used");
                    idTaken = false;
                }

                Scanner scanner = new Scanner(System.in);
                System.out.println("Connection established");
                System.out.println("[Players in the lobby: " + server.playersInLobby() + "]\n" +
                        "Login:\t\t(to refresh the page type '*')");
                String text = scanner.nextLine();

                if (text.equals("")){
                    System.out.println("Invalid name, your ID should be an alphanumeric of at least 1 character");
                    text = "*";
                }
                if (!text.equals("*")) {
                    ClientImplementationRMI client = new ClientImplementationRMI(text);

                    ClientIntRMI remoteRef = (ClientIntRMI) UnicastRemoteObject.exportObject(client, 0);

                    if (server.login(remoteRef))
                        loginSuccess = true;
                    else {
                        if (server.getConnected().size() >= MAX_PLAYERS)
                            fullLobby = true;
                        else
                            idTaken = true;
                    }
                }
            } while (!loginSuccess);

            Scanner scanner = new Scanner(System.in);
            if (server.playersInLobby() == 1){
                System.out.println("You are the first player of the lobby!");
                int delay = 0;
                while(delay < 15 || delay > 60){
                    System.out.println("Please set a timer (min 15s, max 60s)");
                    try {
                        delay = Integer.parseInt(scanner.nextLine());
                    }catch (NumberFormatException e){
                        delay = -1;
                    }
                }
                server.setDelay(delay);
            }
            boolean active = true;
            System.out.println("Waiting for other players...\n");

            int num = 0;
            while (active) {

                server.confirmConnections();
                if (num != server.playersInLobby() && !server.hasStarted()){
                    System.out.println("[Players in the lobby: " + server.playersInLobby() + "]");
                    num = server.playersInLobby();
                } else if (server.hasStarted()){
                    break;
                }

            }

        } catch (NotBoundException e) {
            System.err.println("This reference is not connected!");
        } catch (NoSuchElementException e) {
            System.err.println("NOTHING TO READ " +e.getMessage());
        }
    }

    public String startGUISocket(String name) throws IOException{
        Socket socket = null;
        String feedback = "";
        try {
            socket = new Socket(ip, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connection established");
            ClientImplementationSocket clientImplementationSocket = new ClientImplementationSocket(socket);
            feedback = clientImplementationSocket.loginGUI(name);
            if (Integer.parseInt(in.readLine()) == 1) {
                out.println("20");
                out.flush();
            }
        }catch (NoSuchElementException e){
            System.err.println("NOTHING TO READ "+e.getMessage());
        } finally {
            SocketMessengerClient s = new SocketMessengerClient(this.ip, PORT, socket, name, IOHandlerClient.Interface.gui);
            GUIController.setMessenger(s);
        }
        return feedback;
    }

    class InputDelay implements Runnable {

        PrintWriter out;

        InputDelay (PrintWriter out) {
            this.out = out;
        }
        @Override
        public void run() {
            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                int delay = 0;
                String delayString = null;
                do {
                    System.out.println("Please set a timer (min 15s, max 60s)");
                    try {
                        // wait until we have data to complete a readLine()
                        while (!in.ready()) {
                            Thread.sleep(200);
                        }
                        delayString = in.readLine();
                        delay = Integer.parseInt(delayString);
                    } catch (InterruptedException e) {
                        System.out.println("Never mind... you were too slow");
                        return;
                    } catch (NumberFormatException n) {
                        delay = -1;
                    }
                } while ("".equals(delay) || delay < 15 || delay > 60);
                this.out.println(delayString);
                this.out.flush();
            } catch (IOException e){
                System.out.println("IOEXCEPTION IN INPUTDELAY");
            }
        }
    }

    private void startClientSocket() throws IOException{
        Socket socket = null;
        int activePlayers;
        boolean success;
        String name = "";
        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            socket = new Socket(ip, PORT);
            Scanner scanner = new Scanner(System.in);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connection established");
            ClientImplementationSocket clientImplementationSocket = new ClientImplementationSocket(socket);
            do {
                name = clientImplementationSocket.login();
                activePlayers = Integer.parseInt(in.readLine());
                success = true;
                if (activePlayers == 1){
                    System.out.println("You are the first player of the lobby!");
                    InputDelay doWait = new InputDelay(out);
                    executorService.submit(doWait);
                } else {
                    System.out.println("Waiting for other players...\n");
                }
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
                            SocketMessengerClient messenger = new SocketMessengerClient(this.ip, PORT, socket, name,
                                    IOHandlerClient.Interface.cli);
                            messenger.close();
                        }
                        System.out.println("[Players in the lobby: " + i + "]");
                        num = i;
                    }
                    //TODO: SOLVE THIS WHILE(TRUE)
                    //MOVE ALL THE METHODS TO CLIENT IMPLEMENTATION SOCKET, THE TO MESSENGER
                }
            }while (!success);

            //DOES NOTHING
            while (!success){
                Scanner s = new Scanner(System.in);
                String t = s.nextLine();
                System.err.println(t);
            }

            scanner.close();
            in.close();
            out.close();
        }catch (NoSuchElementException e){
            System.err.println("NOTHING TO READ "+e.getMessage());
        }
        finally {
            if (socket != null)
                socket.close();
        }
    }


    private static String askIP(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("IP address of the Server: ");
        return scanner.nextLine();
    }
}