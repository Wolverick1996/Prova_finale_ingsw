package it.polimi.ingsw.server.network_server;

import it.polimi.ingsw.server.controller.Lobby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerImplementationSocket implements Runnable {
    private Socket socket;
    private Lobby lobby;
    private String playerConnected;

    ServerImplementationSocket(Socket socket, Lobby lobby) {
        this.socket = socket;
        this.lobby = lobby;
    }

    class WaitForDelay implements Runnable {

        WaitForDelay() {
//TRY AND USE EXECUTORSERVICE HERE
        }
        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!in.ready()){
                    Thread.sleep(200);
                }
                lobby.setDelay(Integer.parseInt(in.readLine()));
                System.out.println("GOT IT!");
            } catch (IOException e) {
                System.err.println("Connection lost from a client");
                if (playerConnected != null){
                    System.out.println("[Socket Server]\t" +playerConnected+ "  disconnected....");
                    lobby.removePlayer(playerConnected);
                    playerConnected = null;
                }
            } catch (InterruptedException i) {
                System.out.println("WAITFORDELAY WAS INTERRUPTED");
            }
        }
    }

    public void run() {
        try {
            //BufferedReader in;
            PrintWriter out;
            boolean gameCanStart;
            do{
                login();
                //in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                out = new PrintWriter(this.socket.getOutputStream());
                out.println(this.lobby.getPlayers().size());
                out.flush();
                Thread waitForInput = new Thread(new WaitForDelay());
                if (this.lobby.getPlayers().size() == 1){
                    waitForInput.start();
                }
                gameCanStart = true;
                while (gameCanStart && !this.lobby.hasStarted()){
                    out.println(this.lobby.getPlayers().size());
                    out.flush();
                    //Here old and deprecated code is commented for future investigation
                    /*string = in.readLine();
                    if (string.equals("e")){
                        logout();
                        gameCanStart = false;
                    }else{
                        out.println(this.lobby.getPlayers().size());
                        out.flush();
                    }*/
                }
                if (!waitForInput.isInterrupted())
                    waitForInput.interrupt();
                out.println(999);
                out.flush();
                System.out.println("HERE I CLOSE THE SOCKET IMPLEMENTATION THREAD :\t" + Thread.currentThread().getName());
            }while (!gameCanStart);

            //DOES NOTHING
            while (!gameCanStart){
                Scanner s = new Scanner(System.in);
                String t = s.nextLine();
            }
            //in.close();
            //out.close();
            //this.socket.close();
        }catch (IOException e){
            System.err.println("Connection lost from a client");
            if (this.playerConnected != null){
                System.out.println("[Socket Server]\t" +this.playerConnected+ "  disconnected....");
                this.lobby.removePlayer(this.playerConnected);
                this.playerConnected = null;
            }
        }
    }

    private synchronized void login() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        PrintWriter out = new PrintWriter(this.socket.getOutputStream());
        boolean loginSuccess = false;

        while (!loginSuccess) {
            out.println(this.lobby.getPlayers().size());
            out.flush();
            String string = in.readLine();
            if (!string.equals("*")){
                if (string.equals("")){
                    out.println("invalid");
                    out.flush();
                }
                else if (this.lobby.addPlayer(string)){
                    this.lobby.addSocket(this.socket);
                    loginSuccess = true;
                    System.out.println("[Socket Server]\t" +string+ "  got connected....");
                    this.playerConnected = string;
                    out.println("true");
                    out.flush();
                }else {
                    boolean sameUsername = false;
                    for (String s : this.lobby.getPlayers())
                        if (s.equals(string))
                            sameUsername = true;
                    if (this.lobby.hasStarted()){
                        out.println("started");
                        out.flush();
                    } else if (sameUsername){
                        out.println("same");
                        out.flush();
                    } else {
                        out.println("max");
                        out.flush();
                    }
                }
            }
        }
    }

    private synchronized void logout() throws IOException{
        PrintWriter out = new PrintWriter(this.socket.getOutputStream());
        if(this.lobby.removePlayer(this.playerConnected)){
            out.println("ok");
            out.flush();
            System.out.println("[Socket Server]\t" +this.playerConnected+ "  disconnected....");
        }
        else{
            out.println("ko");
            out.flush();
        }
    }
}
