package it.polimi.ingsw.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GameController {

    private static final String COLORS = "\u001B\\[[;\\d]*m";
    private static final String NEWLINE = "\n";
    private static final String DIVISOR = ": ";
    private static final String COMMA = ",";
    private static final String PUBOC = "Public Objective Cards:";
    private static final int MAX_ROW = 4;
    private static final int MAX_COL = 5;

    @FXML
    private GridPane grid;
    @FXML
    private Button p1;
    @FXML
    private Button p2;
    @FXML
    private Button p3;
    @FXML
    private GridPane reserve;
    @FXML
    private GridPane roundtrack;
    @FXML
    private Button privOC;
    @FXML
    private Button pubOC1;
    @FXML
    private Button pubOC2;
    @FXML
    private Button pubOC3;
    @FXML
    private Button tool1;
    @FXML
    private Button tool2;
    @FXML
    private Button tool3;

    private void loadReserve(String draft){
        ArrayList<String> imageColor = new ArrayList<>();
        ArrayList<String> imageValue = new ArrayList<>();

        String[] divide;
        divide = draft.split(NEWLINE);
        divide = divide[1].split(DIVISOR);
        divide = divide[1].split(COMMA);
        for (String s:divide) {
            if (s.contains("31m"))
                imageColor.add("red");
            if (s.contains("32m"))
                imageColor.add("green");
            if (s.contains("33m"))
                imageColor.add("yellow");
            if (s.contains("34m"))
                imageColor.add("blue");
            if (s.contains("35m"))
                imageColor.add("purple");
        }

        for (String s:divide) {
            s = s.replaceAll(COLORS, "");
            if (s.contains("1"))
                imageValue.add("1");
            if (s.contains("2"))
                imageValue.add("2");
            if (s.contains("3"))
                imageValue.add("3");
            if (s.contains("4"))
                imageValue.add("4");
            if (s.contains("5"))
                imageValue.add("5");
            if (s.contains("6"))
                imageValue.add("6");
        }

        for (int i = imageColor.size(); i > 0; i--)
            reserve.add(new ImageView(new Image("/images/"+imageColor.get(i-1)+imageValue.get(i-1)+".jpeg",
                    60, 60, false, false)), i, 0);
    }

    private void setButtonImage(String path, Button button){
        button.setStyle("-fx-background-image: url('" + path + "')");
    }

    private void loadObjectiveCards(String privateOC, String table){
        //Setting private objective card
        String[] divide;
        divide = privateOC.split(NEWLINE);
        String[] privOCs = {"Red", "Green", "Yellow", "Blue", "Purple"};
        for (String s:privOCs) {
            if (divide[1].contains(s)) {
                String path = "/images/priv_" + s.toLowerCase() + ".jpeg";
                setButtonImage(path, privOC);
            }
        }

        //Setting public objective cards
        String[] pubOCs = {"Different colors - rows", "Different colors - columns", "Different shades - rows", "Different shades - columns",
                "Light shades", "Medium shades", "Dark shades", "Different shades", "Colored diagonals", "Variety of color"};
        divide = table.split(PUBOC);
        divide = divide[1].split(NEWLINE);
        for (int i = 0; i < pubOCs.length; i++) {
            if (divide[1].contains(pubOCs[i]) || divide[3].contains(pubOCs[i]) || divide[5].contains(pubOCs[i])) {
                String path = "/images/pub" + (i+1) + ".jpeg";
                if (divide[1].contains(pubOCs[i]))
                    setButtonImage(path, pubOC1);
                else if (divide[3].contains(pubOCs[i]))
                    setButtonImage(path, pubOC2);
                else if (divide[5].contains(pubOCs[i]))
                    setButtonImage(path, pubOC3);
            }
        }
    }

    private void loadTools(String activeTools){
        String[] divide;
        String[] tools = {"Grozing Pliers", "Eglomise Brush", "Copper Foil Burnisher", "Lathekin", "Lens Cutter", "Flux Brush",
                "Glazing Hammer", "Running Pliers", "Cork-backed Straightedge", "Grinding Stone", "Flux Remover", "Tap Wheel"};
        divide = activeTools.split(NEWLINE);
        for (int i = 0; i < tools.length; i++) {
            if (divide[2].contains(tools[i]) || divide[5].contains(tools[i]) || divide[8].contains(tools[i])) {
                String path = "/images/tool" + (i+1) + ".jpg";
                if (divide[2].contains(tools[i]))
                    setButtonImage(path, tool1);
                else if (divide[5].contains(tools[i]))
                    setButtonImage(path, tool2);
                else if (divide[8].contains(tools[i]))
                    setButtonImage(path, tool3);
            }
        }
    }

    void loadGame(ArrayList<ImageView> restrictions, int numP, String draft, String privateOC, String table, String activeTools){
        //Filing window pattern with restrictions images
        int scroll = 0;
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                grid.add(restrictions.get(scroll), j, i);
                scroll++;
            }
        }

        //Removing player buttons
        if (numP == 3 || numP == 2)
            p3.setVisible(false);
        if (numP == 2)
            p2.setVisible(false);

        //Filling reserve
        loadReserve(draft);

        //Setting private and public objective cards
        loadObjectiveCards(privateOC, table);

        //Setting tool cards
        loadTools(activeTools);
    }

    @FXML
    private void loadPlayer(ActionEvent event) throws IOException {
        Parent blah = FXMLLoader.load(getClass().getResource("/FXML/player.fxml"));
        Scene scene = new Scene(blah);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
    }

}