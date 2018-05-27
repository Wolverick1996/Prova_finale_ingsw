package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class IOhandler {

    //***************************//
    //        Attributes         //
    //***************************//

    List<Player> players = new ArrayList<>();

    public IOhandler(List<Player> users){
        players = users;
    }

    //***************************//
    //         Methods           //
    //***************************//

    public static void broadcast(Object message){
        System.out.println(message);
    }

}
