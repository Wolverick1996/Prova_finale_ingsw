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

        controller.reloadGame(GUIupdater.getNumPlayers(), GUIupdater.getOwnScheme(),
                GUIupdater.getPrivObj(), GUIupdater.getTable(), GUIupdater.getTools(),
                GUIupdater.getOwnPlayer(), GUIupdater.getActivePlayer());

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
    }

    void loadGrid(String player, ArrayList<ImageView> draftIMG){
        for (ImageView i : draftIMG)
            i.setEffect(null);

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