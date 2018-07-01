package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Table;
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
        ArrayList<ImageView> ownRestrictions = new ArrayList<>();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameScreen.fxml"));
        Parent root = loader.load();
        GameController controller = loader.getController();
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

        //CONTROLLER CALL to communicate the window pattern chosen
        //CONTROLLER REQUEST to get numPlayers, reserve, tool cards, public objective cards and private objective card
        int numP = (int)(Math.random()*3 + 2);
        Table table = new Table(numP);

        controller.loadGame(ownRestrictions, numP, table.printReserve());

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
    }

}