package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Enum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchemesController {

    private static final String COLORS = "\u001B\\[[;\\d]*m";
    private static final String NEWLINE = "\n";
    private static final String DIVISOR = ": ";
    private static final int MAX_ROW = 4;
    private static final int MAX_COL = 5;
    private ArrayList<ImageView> restrictions = new ArrayList<>();

    @FXML
    private Text name1;
    @FXML
    private Text name2;
    @FXML
    private Text name3;
    @FXML
    private Text name4;
    @FXML
    private Text difficulty1;
    @FXML
    private Text difficulty2;
    @FXML
    private Text difficulty3;
    @FXML
    private Text difficulty4;
    @FXML
    private GridPane grid1;
    @FXML
    private GridPane grid2;
    @FXML
    private GridPane grid3;
    @FXML
    private GridPane grid4;
    @FXML
    private RadioButton grid1Button;
    @FXML
    private RadioButton grid2Button;
    @FXML
    private RadioButton grid3Button;

    private void imageFactory(String restriction, int paneIndex, int row, int col){
        ArrayList<GridPane> panes = new ArrayList<>();
        panes.add(grid1);
        panes.add(grid2);
        panes.add(grid3);
        panes.add(grid4);

        switch (restriction) {
            case "R":
                panes.get(paneIndex).add(new ImageView(new Image("/images/color_red.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/color_red.jpeg",
                        60, 60, false, false)));
                break;
            case "G":
                panes.get(paneIndex).add(new ImageView(new Image("/images/color_green.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/color_green.jpeg",
                        60, 60, false, false)));
                break;
            case "Y":
                panes.get(paneIndex).add(new ImageView(new Image("/images/color_yellow.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/color_yellow.jpeg",
                        60, 60, false, false)));
                break;
            case "B":
                panes.get(paneIndex).add(new ImageView(new Image("/images/color_blue.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/color_blue.jpeg",
                        60, 60, false, false)));
                break;
            case "P":
                panes.get(paneIndex).add(new ImageView(new Image("/images/color_purple.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/color_purple.jpeg",
                        60, 60, false, false)));
                break;
            case "1":
                panes.get(paneIndex).add(new ImageView(new Image("/images/value_1.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/value_1.jpeg",
                        60, 60, false, false)));
                break;
            case "2":
                panes.get(paneIndex).add(new ImageView(new Image("/images/value_2.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/value_2.jpeg",
                        60, 60, false, false)));
                break;
            case "3":
                panes.get(paneIndex).add(new ImageView(new Image("/images/value_3.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/value_3.jpeg",
                        60, 60, false, false)));
                break;
            case "4":
                panes.get(paneIndex).add(new ImageView(new Image("/images/value_4.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/value_4.jpeg",
                        60, 60, false, false)));
                break;
            case "5":
                panes.get(paneIndex).add(new ImageView(new Image("/images/value_5.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/value_5.jpeg",
                        60, 60, false, false)));
                break;
            case "6":
                panes.get(paneIndex).add(new ImageView(new Image("/images/value_6.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/value_6.jpeg",
                        60, 60, false, false)));
                break;
            default:
                panes.get(paneIndex).add(new ImageView(new Image("/images/no_restr.jpeg",
                        60, 60, false, false)), col, row);
                restrictions.add(new ImageView(new Image("/images/no_restr.jpeg",
                        60, 60, false, false)));
                break;
        }
    }

    void setSchemes(String s1, String s2, String s3, String s4){
        ArrayList<String> schemes = new ArrayList<>();
        schemes.add(s1.replaceAll(COLORS, ""));
        schemes.add(s2.replaceAll(COLORS, ""));
        schemes.add(s3.replaceAll(COLORS, ""));
        schemes.add(s4.replaceAll(COLORS, ""));
        ArrayList<Text> names = new ArrayList<>();
        names.add(name1);
        names.add(name2);
        names.add(name3);
        names.add(name4);
        ArrayList<Text> difficulties = new ArrayList<>();
        difficulties.add(difficulty1);
        difficulties.add(difficulty2);
        difficulties.add(difficulty3);
        difficulties.add(difficulty4);

        String[] strings;
        for (int i = 0; i < 4; i++) {
            strings = schemes.get(i).split(NEWLINE);
            strings = strings[0].split(DIVISOR);
            names.get(i).setText(strings[1]);
            strings = schemes.get(i).split(NEWLINE);
            strings = strings[1].split(DIVISOR);
            difficulties.get(i).setText(strings[1]);
        }

        for (int pane_index = 0; pane_index < 4; pane_index++) {
            int count = 0;
            for (int i = 0; i < MAX_ROW; i++) {
                for (int j = 0; j < MAX_COL; j++) {
                    strings = schemes.get(pane_index).split("\\[");
                    strings = strings[count+1].split("]");
                    imageFactory(strings[0], pane_index, i, j);
                    count++;
                }
            }
        }

    }

    @FXML
    private void loadGame(ActionEvent event) throws IOException {
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

        /*
            //Block of code relative to the first version of reloadGame method
            //ownRestriction is an ImageView array used to pass images as parameters to GameController

            ArrayList<ImageView> ownRestrictions = new ArrayList<>();
            if (grid1Button.isSelected())
                for (int i = 0; i < MAX_COL*MAX_ROW; i++)
                    ownRestrictions.add(restrictions.get(i));
            else if (grid2Button.isSelected())
                for (int i = 0; i < MAX_COL*MAX_ROW; i++)
                    ownRestrictions.add(restrictions.get(20+i));
            else if (grid3Button.isSelected())
                for (int i = 0; i < MAX_COL*MAX_ROW; i++)
                    ownRestrictions.add(restrictions.get(40+i));
            else
                for (int i = 0; i < MAX_COL*MAX_ROW; i++)
                    ownRestrictions.add(restrictions.get(60+i));
        */

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

        /*
        for (int i=0; i<numP*2-1; i++)
            table.nextTurn();
        table.nextTurn();
        for (int i=0; i<numP*2-1; i++)
            table.nextTurn();
        table.nextTurn();
        for (int i=0; i<numP*2-1; i++)
            table.nextTurn();
        table.nextTurn();
        for (int i=0; i<numP*2-1; i++)
            table.nextTurn();
        table.nextTurn();
        for (int i=0; i<numP*2-1; i++)
            table.nextTurn();
        table.nextTurn();
        for (int i=0; i<numP*2-1; i++)
            table.nextTurn();
        table.nextTurn();
        for (int i=0; i<numP*2-1; i++)
            table.nextTurn();
        table.nextTurn();
        for (int i=0; i<numP*2-1; i++)
            table.nextTurn();
        table.nextTurn();
        for (int i=0; i<numP*2-1; i++)
            table.nextTurn();
        table.nextTurn();
        for (int i=0; i<numP*2-1; i++)
            table.nextTurn();
        table.nextTurn();
        */

        Scheme s1 = Scheme.initialize(1, false, 24);

        controller.reloadGame(numP, s1.toString(), privOC, table.toString(), tools, p1.toString(), p2.toString());

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
    }

}