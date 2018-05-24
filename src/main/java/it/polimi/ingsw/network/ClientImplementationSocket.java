package it.polimi.ingsw.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ClientImplementationSocket {

    private Socket socket;

    ClientImplementationSocket (Socket socket){
        this.socket = socket;
    }

    void login (){
        boolean success = false;
        Scanner scanner = new Scanner(System.in);
        String string;
        try {
            Scanner in = new Scanner(this.socket.getInputStream());
            PrintWriter out = new PrintWriter(this.socket.getOutputStream());
            do {
                System.out.println("Login:\t\t(to refresh the page type '*')");
                string = scanner.nextLine();
                if (!string.equals("*")){
                    out.println(string);
                    out.flush();
                    if (in.nextLine().equals("true")){
                        System.out.println("Welcome " +string+ ".\nYou have connected successfully.");
                        success = true;
                    }
                    else{
                        System.out.println("Login failed! Retry with another username.");
                    }
                }
            }while (!success);
            in.close();
            out.close();
            scanner.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
