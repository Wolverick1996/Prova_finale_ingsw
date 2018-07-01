package it.polimi.ingsw.client.view;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class SchemesController {

    private static final String COLORS = "\u001B\\[[;\\d]*m";
    private static final String NEWLINE = "\n";
    private static final String DIVISOR = ": ";
    private static final int MAX_ROW = 4;
    private static final int MAX_COL = 5;

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

    private void imageFactory(String restriction, int pane_index, int row, int col){
        ArrayList<GridPane> panes = new ArrayList<>();
        panes.add(grid1);
        panes.add(grid2);
        panes.add(grid3);
        panes.add(grid4);

        switch (restriction) {
            case "R":
                panes.get(pane_index).add(new ImageView(new Image("/images/color_red.jpeg",
                        60, 60, false, false)), col, row);
                break;
            case "G":
                panes.get(pane_index).add(new ImageView(new Image("/images/color_green.jpeg",
                        60, 60, false, false)), col, row);
                break;
            case "Y":
                panes.get(pane_index).add(new ImageView(new Image("/images/color_yellow.jpeg",
                        60, 60, false, false)), col, row);
                break;
            case "B":
                panes.get(pane_index).add(new ImageView(new Image("/images/color_blue.jpeg",
                        60, 60, false, false)), col, row);
                break;
            case "P":
                panes.get(pane_index).add(new ImageView(new Image("/images/color_purple.jpeg",
                        60, 60, false, false)), col, row);
                break;
            case "1":
                panes.get(pane_index).add(new ImageView(new Image("/images/value_1.jpeg",
                        60, 60, false, false)), col, row);
                break;
            case "2":
                panes.get(pane_index).add(new ImageView(new Image("/images/value_2.jpeg",
                        60, 60, false, false)), col, row);
                break;
            case "3":
                panes.get(pane_index).add(new ImageView(new Image("/images/value_3.jpeg",
                        60, 60, false, false)), col, row);
                break;
            case "4":
                panes.get(pane_index).add(new ImageView(new Image("/images/value_4.jpeg",
                        60, 60, false, false)), col, row);
                break;
            case "5":
                panes.get(pane_index).add(new ImageView(new Image("/images/value_5.jpeg",
                        60, 60, false, false)), col, row);
                break;
            case "6":
                panes.get(pane_index).add(new ImageView(new Image("/images/value_6.jpeg",
                        60, 60, false, false)), col, row);
                break;
            default:
                panes.get(pane_index).add(new ImageView(new Image("/images/no_restr.jpeg",
                        60, 60, false, false)), col, row);
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

}