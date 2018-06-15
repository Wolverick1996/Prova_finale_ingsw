package it.polimi.ingsw.client.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLI {

    private final String SAGRADA = "                    ____                            _       \n" +
            "                   / ___|  __ _  __ _ _ __ __ _  __| | __ _ \n" +
            "                   \\___ \\ / _` |/ _` | '__/ _` |/ _` |/ _` |\n" +
            "                    ___) | (_| | (_| | | | (_| | (_| | (_| |\n" +
            "                   |____/ \\__,_|\\__, |_|  \\__,_|\\__,_|\\__,_|\n" +
            "                                |___/                       \n";

    private static final InputStreamReader stream = new InputStreamReader(System.in);
    private static final BufferedReader reader = new BufferedReader(stream);
    private static final int MAX = 50000;
    private char[] dump = new char[MAX];

    public CLI(){
        output(SAGRADA);
    }

    public String input(){

        //TODO:RESOLVE FALSE INPUT
        String input = "???";
        boolean exc;

        try {
            System.in.read(new byte[System.in.available()]);
        } catch (IOException e) {
            System.err.println("Error in Sysin flush");
            e.printStackTrace();
        }

        do{
            exc = false;
            try {
                input = reader.readLine();
                output(input);
            } catch (IOException e) {
                exc = true;
            }
        } while (!isValid(input) || exc);

        return input;
    }

    private boolean isValid(String s){
        //INPUT IS VALID ONLY IF:
        //123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz
        boolean check = true;
        int unicode;

        if (s.isEmpty()){
            check = false;
            return check;
        }

        for(int i=0; i<s.length(); i++) {
            unicode = s.codePointAt(i);
            if (unicode<0x30 || (unicode>0x39 && unicode<0x41) || (unicode>0x5a &&unicode<0x61) || unicode > 0x7a){
                check = false;
            }
        }

        if (!check) output("INPUT CONTAINS ILLEGAL CHARACTERS");
        return check;
    }

    public void output(String message){

        System.out.println(message);

    }

}
