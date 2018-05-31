package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Scheme;
import it.polimi.ingsw.model.Table;
import javafx.beans.value.ObservableValue;

import java.util.*;

public class IOhandler implements Observer{

    //***************************//
    //        Attributes         //
    //***************************//

    private static List<Player> players = new ArrayList<>();
    private Observable ov = null;
    private static Table table = null;
    private static final String STATUS = "STATUS";

    public IOhandler(List<Player> users, Table board/*, Observable observed*/){
        this.players = users;
        this.table = board;
        /*this.ov = observed;*/
    }

    //***************************//
    //         Methods           //
    //***************************//

    public static void broadcast(Object message){
        System.out.println(message);
        if (message.equals(STATUS)){
            System.out.println("\n\n");
            System.out.println(table);
            for (Player p: players){
                System.out.println(p);
            }
            System.out.println("\n\n");
        }
    }

    public static String getStandardAction(){
        boolean send = false;
        String answer = null;
        Scanner s = new Scanner(System.in);

        while (!send){

            System.out.println("Insert action (d = place dice, q = pass turn)");
            answer = s.nextLine();

            if (answer.equals("d")){
                return "d";
            } else if (answer.equals("q")){
                return "q";
            } else {
                System.out.println("Invalid input");
            }
        }

        return null;
    }

    public static int getDice(){
        int answer;
        Scanner s = new Scanner(System.in);

        System.out.println("Insert the place of the dice in the reserve");
        answer = Integer.parseInt(s.nextLine());
       //TODO: CHECK THIS INPUT!
        return answer-1;
    }

    public static int getCoordinate(String coor){
        int answer;
        Scanner s = new Scanner(System.in);

        System.out.println("Insert coordinate " + coor + " of the dice");
        answer = Integer.parseInt(s.nextLine());
        //TODO: CHECK THE INPUT!
        return answer-1;

    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == ov)
        {
            System.out.println(ov);
        }
    }

    public int chooseScheme(int s1, int s2, int s3, int s4){

        int answer = -1;
        boolean isValid = false;
        Scanner s = new Scanner(System.in);

        Integer[] schemes = new Integer[4];
        schemes[0] = s1;
        schemes[1] = s2;
        schemes[2] = s3;
        schemes[3] = s4;

        for (int i = 0; i<4; i++){
            System.out.println("Scheme " + (i+1));
            System.out.println(Scheme.initialize(schemes[i]));
        }

        while(!isValid){
            System.out.println("Pick a scheme (write 1, 2, 3 or 4)");
            answer = Integer.parseInt(s.nextLine());

            if (answer == 1 || answer == 2 || answer == 3 || answer == 4){
                isValid = true;
            } else {
                System.out.println("Not a scheme!");
            }
        }
        return schemes[answer-1];
    }
}
