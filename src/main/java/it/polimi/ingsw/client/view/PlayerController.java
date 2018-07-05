package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Enum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerController {

    private static final String COLORS = "\u001B\\[[;\\d]*m";
    private static final String NEWLINE = "\n";
    private static final String DIVISOR = ": ";
    private ArrayList<ImageView> playerIMG = new ArrayList<>();

    @FXML
    private GridPane playerGrid;
    @FXML
    private GridPane reserve;
    @FXML
    private Text playerName;
    @FXML
    private Text playerTokens;
    @FXML
    private Text schemeName;
    @FXML
    private Text schemeDifficulty;

    @FXML
    private void loadGameFromPlayer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameScreen.fxml"));
        Parent root = loader.load();
        GameController controller = loader.getController();

        //TODO: Notify to the controller the window pattern chosen: loops are replaced by controller calls
        //TODO: CONTROLLER REQUEST to get the following
        // 1. (int) Number of players;
        // 2. (String) Window pattern chosen;
        // 3. (String) Private Objective Card;
        // 4. (String) Table
        // 5. (String) Tool Cards;
        // 6. (String) My player string
        // 7. (String) Active player

        int numP = (int)(Math.random()*3 + 2);
        Table table = new Table(numP);
        Player p1 = new Player("ingconti", 0);
        Player p2 = new Player("n1zzo", 0);
        List<String> nicknames = Arrays.asList("ingconti", "n1zzo", "michele-bertoni", "valerio-castelli");
        table.setPlayers(nicknames);

        String privOC = "This is your Private Objective Card:\n" +
                PrivObjHandler.getColor(p1).escape() + PrivObjHandler.getName(p1) + "\n" + Enum.Color.RESET +
                PrivObjHandler.getDescription(p1) + "\n";

        String tools = "\nTool Cards on table:\n";
        for (int i = 0; i < 3; i++)
            tools = tools + Enum.Color.YELLOW.escape() + ToolHandler.getName(i) + "\n" + Enum.Color.RESET + ToolHandler.getDescription(i) +
                    Enum.Color.YELLOW.escape() + "\n Tokens on: " + Enum.Color.RESET + ToolHandler.getTokens(i) + "\n";

        Scheme s1 = Scheme.initialize(1, false, 24);

        controller.reloadGame(numP, s1.toString(), privOC, table.toString(), tools, p1.toString(), p2.toString());

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
    }

    void loadGrid(String player, ArrayList<ImageView> draftIMG){
        String[] divide;

        divide = player.split("Active window pattern:\n");
        String scheme = divide[1];
        String schemeNoCol = scheme.replaceAll(COLORS, "");
        divide = schemeNoCol.split(NEWLINE);
        divide = divide[0].split(DIVISOR);
        schemeName.setText(divide[1]);
        divide = schemeNoCol.split(NEWLINE);
        divide = divide[1].split(DIVISOR);
        schemeDifficulty.setText(divide[1]);

        GameController.loadScheme(scheme, playerIMG, playerGrid);

        player = player.replaceAll(COLORS, "");
        divide = player.split(NEWLINE);
        playerName.setText(divide[0]);
        divide = divide[1].split(DIVISOR);
        playerTokens.setText(divide[1]);

        for (int i = 1; i <= draftIMG.size(); i++)
            reserve.add((draftIMG.get(draftIMG.size()-i)), i, 0);
    }

}