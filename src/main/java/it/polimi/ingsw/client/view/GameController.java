package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Scheme;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GameController {

    private static final String COLORS = "\u001B\\[[;\\d]*m";
    private static final String NEWLINE = "\n";
    private static final String DIVISOR = ": ";
    private static final String COMMA = ",";
    private static final String PUBOC = "Public Objective Cards:";
    private static final String TAB = "\t";
    private static final int MAX_ROW = 4;
    private static final int MAX_COL = 5;
    private static final int MAX_HEIGHT_ROUNDTRACK = 27;
    private ArrayList<ImageView> draftIMG = new ArrayList<>();
    private ArrayList<ImageView> roundtrackIMG = new ArrayList<>();
    private ArrayList<ImageView> gridIMG = new ArrayList<>();
    private DropShadow ds = new DropShadow(70, Color.GOLD);

    @FXML
    private BorderPane pane4;
    @FXML
    private AnchorPane pane5;
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
    @FXML
    private Text activeP;
    @FXML
    private Text tokensP;
    @FXML
    private Text tokensT1;
    @FXML
    private Text tokensT2;
    @FXML
    private Text tokensT3;
    @FXML
    private Text round;
    @FXML
    private Text turn;

    private static void prepareString(ArrayList<String> imageColor, ArrayList<String> imageValue, String[] divide){
        for (String s:divide) {
            if (s.contains("31m"))
                imageColor.add("red");
            else if (s.contains("32m"))
                imageColor.add("green");
            else if (s.contains("33m"))
                imageColor.add("yellow");
            else if (s.contains("34m"))
                imageColor.add("blue");
            else if (s.contains("35m"))
                imageColor.add("purple");
            else
                imageColor.add("none");
        }

        for (String s:divide) {
            s = s.replaceAll(COLORS, "");
            if (s.contains("1"))
                imageValue.add("1");
            else if (s.contains("2"))
                imageValue.add("2");
            else if (s.contains("3"))
                imageValue.add("3");
            else if (s.contains("4"))
                imageValue.add("4");
            else if (s.contains("5"))
                imageValue.add("5");
            else if (s.contains("6"))
                imageValue.add("6");
            else
                imageValue.add("none");
        }
    }

    static void loadScheme(String wp, ArrayList<ImageView> toFill, GridPane gridObj){
        ArrayList<String> imageColor = new ArrayList<>();
        ArrayList<String> imageValue = new ArrayList<>();
        ArrayList<String> divide3 = new ArrayList<>();
        String[] divide1;
        String[] divide2;

        divide1 = wp.split(NEWLINE);
        for (int i = 3; i < MAX_ROW+3; i++) {
            divide2 = divide1[i].split(TAB);
            for (int j = 1; j <= MAX_COL; j++)
                divide3.add(divide2[j]);
        }

        String[] boxes = new String[divide3.size()];
        divide3.toArray(boxes);
        prepareString(imageColor, imageValue, boxes);

        int scroll = 0;
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                if (!imageColor.get(scroll).equals("none") && !imageValue.get(scroll).equals("none"))
                    toFill.add(new ImageView(new Image("/images/" + imageColor.get(scroll) + imageValue.get(scroll) + ".jpeg",
                            60, 60, false, false)));
                else if (imageColor.get(scroll).equals("none") && !imageValue.get(scroll).equals("none"))
                    toFill.add(new ImageView(new Image("/images/value_" + imageValue.get(scroll) + ".jpeg",
                            60, 60, false, false)));
                else if (!imageColor.get(scroll).equals("none") && imageValue.get(scroll).equals("none"))
                    toFill.add(new ImageView(new Image("/images/color_" + imageColor.get(scroll) + ".jpeg",
                            60, 60, false, false)));
                else
                    toFill.add(new ImageView(new Image("/images/no_restr.jpeg",
                            60, 60, false, false)));
                gridObj.add(toFill.get(MAX_COL*i + j), j, i);
                scroll++;
            }
        }

        //TODO: Controller call to associate an action with the window pattern images
        for (ImageView i: toFill)
            i.setOnMouseClicked(e -> System.out.println("I'm a window pattern image!"));
    }

    private void loadReserve(String draft){
        ArrayList<String> imageColor = new ArrayList<>();
        ArrayList<String> imageValue = new ArrayList<>();
        String[] divide;

        divide = draft.split(NEWLINE);
        divide = divide[1].split(DIVISOR);
        divide = divide[1].split(COMMA);

        prepareString(imageColor, imageValue, divide);

        for (int i = imageColor.size(); i > 0; i--) {
            draftIMG.add(new ImageView(new Image("/images/" + imageColor.get(i-1) + imageValue.get(i-1) + ".jpeg",
            60, 60, false, false)));
            reserve.add((draftIMG.get(imageColor.size()-i)), i, 0);
        }

        //TODO: Controller call to associate an action with the reserve images
        for (ImageView i:draftIMG)
            i.setOnMouseClicked(e -> {
                i.setEffect(ds);
                System.out.println("I'm a dice in the reserve!");
                i.setEffect(null);
            });
    }

    private void loadRoundtrack(String track){
        ArrayList<String> imageColor = new ArrayList<>();
        ArrayList<String> imageValue = new ArrayList<>();
        String[] divide;

        divide = track.split(DIVISOR);

        //If roundtrack is empty the method returns to the caller
        if (divide[1].contains("[]"))
            return;

        divide = divide[1].split(COMMA);

        prepareString(imageColor, imageValue, divide);

        for (int i = imageColor.size(); i > 0; i--) {
            if (imageColor.size() > MAX_HEIGHT_ROUNDTRACK) {
                roundtrackIMG.add(new ImageView(new Image("/images/" + imageColor.get(i-1) + imageValue.get(i-1) + ".jpeg",
                        30, 30, false, false)));
                roundtrack.setPrefSize(30, 30);
                if (i <= (imageColor.size()/2)+1)
                    roundtrack.add((roundtrackIMG.get(imageColor.size()-i)), i, 0);
                else
                    roundtrack.add((roundtrackIMG.get(imageColor.size()-i)), i-(imageColor.size()/2)-1, 1);
            } else {
                roundtrackIMG.add(new ImageView(new Image("/images/" + imageColor.get(i-1) + imageValue.get(i-1) + ".jpeg",
                    60, 60, false, false)));
                roundtrack.setPrefSize(60, 60);
                roundtrack.add((roundtrackIMG.get(imageColor.size()-i)), i, 0);
            }
        }

        //TODO: Controller call to associate an action with the roundtrack images
        for (ImageView i:roundtrackIMG)
            i.setOnMouseClicked(e -> {
                i.setEffect(ds);
                System.out.println("I'm a dice in the roundtrack!");
                i.setEffect(null);
            });
    }

    private void setButtonImage(String path, Button button){
        button.setStyle("-fx-background-image: url('" + path + "')");
    }

    private void loadObjectiveCards(String privateOC, String table){
        String[] divide;
        String[] privOCs = {"Red", "Green", "Yellow", "Blue", "Purple"};
        String[] pubOCs = {"Row Color Variety", "Column Color Variety", "Row Shade Variety", "Column Shade Variety",
                "Light Shades", "Medium Shades", "Deep Shades", "Shade Variety", "Color diagonals", "Color Variety"};

        //Setting private objective card
        divide = privateOC.split(NEWLINE);
        for (String s:privOCs) {
            if (divide[1].contains(s)) {
                String path = "/images/priv_" + s.toLowerCase() + ".jpeg";
                setButtonImage(path, privOC);
            }
        }

        //Setting public objective cards
        divide = table.split(PUBOC);
        divide[1] = divide[1].replaceAll(COLORS, "");
        divide = divide[1].split(NEWLINE);
        for (int i = 0; i < pubOCs.length; i++) {
            if (divide[1].equals(pubOCs[i]) || divide[3].equals(pubOCs[i]) || divide[5].equals(pubOCs[i])) {
                String path = "/images/pub" + (i+1) + ".jpeg";
                if (divide[1].equals(pubOCs[i]))
                    setButtonImage(path, pubOC1);
                else if (divide[3].equals(pubOCs[i]))
                    setButtonImage(path, pubOC2);
                else if (divide[5].equals(pubOCs[i]))
                    setButtonImage(path, pubOC3);
            }
        }
    }

    private void loadTools(String activeTools){
        String[] divide;
        String[] tools = {"Grozing Pliers", "Eglomise Brush", "Copper Foil Burnisher", "Lathekin", "Lens Cutter", "Flux Brush",
                "Glazing Hammer", "Running Pliers", "Cork-backed Straightedge", "Grinding Stone", "Flux Remover", "Tap Wheel"};

        activeTools = activeTools.replaceAll(COLORS, "");
        divide = activeTools.split(NEWLINE);

        for (int i = 0; i < tools.length; i++) {
            if (divide[2].contains(tools[i]) || divide[5].contains(tools[i]) || divide[8].contains(tools[i])) {
                String path = "/images/tool" + (i+1) + ".jpg";
                if (divide[2].contains(tools[i])) {
                    setButtonImage(path, tool1);
                    tokensT1.setText(divide[4].split(DIVISOR)[1]);
                } else if (divide[5].contains(tools[i])) {
                    setButtonImage(path, tool2);
                    tokensT2.setText(divide[7].split(DIVISOR)[1]);
                } else if (divide[8].contains(tools[i])) {
                    setButtonImage(path, tool3);
                    tokensT3.setText(divide[10].split(DIVISOR)[1]);
                }
            }
        }

        //TODO: Controller call to associate an action with the tool cards buttons
        tool1.setOnMouseClicked(e -> System.out.println("I'm the tool card 1!"));
        tool2.setOnMouseClicked(e -> System.out.println("I'm the tool card 2!"));
        tool3.setOnMouseClicked(e -> System.out.println("I'm the tool card 3!"));
    }

    /*
        //Block of code relative to the first version of reloadGame method
        //ArrayList<ImageView> restrictions is passed as parameter from SchemesController

        int scroll = 0;
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                gridIMG.add(restrictions.get(scroll));
                grid.add(restrictions.get(scroll), j, i);
                scroll++;
            }
        }
    */

    @FXML
    void reloadGame(int numP, String scheme, String privateOC, String table, String activeTools, String me, String activePlayer){
        String[] divide;
        divide = table.split(NEWLINE);
        String draft = divide[1] + NEWLINE + divide[2];
        String track = divide[3];

        //Filling window pattern with restrictions images
        loadScheme(scheme, gridIMG, grid);

        //Removing player buttons
        if (numP == 3 || numP == 2)
            p3.setVisible(false);
        if (numP == 2)
            p2.setVisible(false);

        //Filling reserve
        loadReserve(draft);

        //Filling reserve
        loadRoundtrack(track);

        //Setting private and public objective cards
        loadObjectiveCards(privateOC, table);

        //Setting tool cards
        loadTools(activeTools);

        //Setting round and turn
        divide[4] = divide[4].replaceAll(COLORS, "");
        divide[5] = divide[5].replaceAll(COLORS, "");
        turn.setText(divide[4].split(DIVISOR)[1]);
        round.setText(divide[5].split(DIVISOR)[1]);

        //Setting active player
        activePlayer = activePlayer.replaceAll(COLORS, "");
        divide = activePlayer.split(NEWLINE);
        activeP.setText(divide[0]);

        //Setting my tokens
        me = me.replaceAll(COLORS, "");
        divide = me.split(NEWLINE);
        divide = divide[1].split(DIVISOR);
        tokensP.setText(divide[1]);
    }

    @FXML
    private void loadPlayer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/player.fxml"));
        Parent root = loader.load();
        PlayerController controller = loader.getController();

        //TODO: Controller call to request player toString and player's scheme toString

        Player p = new Player("ingconti", 0);
        p.chooseScheme(Scheme.initialize(1, false, 24));

        controller.loadGrid(p.toString(), draftIMG);

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
    }

    private void endGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameEnd.fxml"));
        Parent root = loader.load();
        GUIController controller = loader.getController();

        String rank = "Game ended!\n\ningconti: \t1000000\nn1zzo: \t100\nmichele-bertone: \t100\nvalerio-castelli: \t10";

        controller.setPlayers(4, rank);

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(false);
        appStage.show();
    }

    @FXML
    private void passTurn(ActionEvent event) throws IOException {
        //if (TURN IS THE LAST OF THE GAME)
        endGame(event);
        //else
        //TODO: Method to pass the turn
    }

}