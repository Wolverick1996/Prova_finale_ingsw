package it.polimi.ingsw.client.view;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw.client.view.GUIController.myFont;
import static it.polimi.ingsw.client.view.SchemesController.*;

public class GameController {

    private boolean taskIsRunning = false;
    private static final String COMMA = ",";
    private static final String PUBOC = "Public Objective Cards:";
    private static final String TAB = "\t";
    private static final String TOOL = "USING TOOL CARD...\n";
    private static final int MAX_HEIGHT_ROUNDTRACK = 27;
    private ArrayList<ImageView> draftIMG = new ArrayList<>();
    private ArrayList<ImageView> roundtrackIMG = new ArrayList<>();
    private ArrayList<ImageView> gridIMG = new ArrayList<>();
    private DropShadow ds = new DropShadow(70, Color.GOLD);
    private static boolean gridEffectOn = false;
    private static boolean reserveEffectOn = false;
    private static boolean roundtrackEffectOn = false;
    private static boolean toolEffectOn = false;
    private static boolean passEffectOn = false;
    private static boolean refreshEffectOn = false;

    @FXML
    private GridPane grid;
    @FXML
    private Button pass;
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
    private Button refresh;
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

    private synchronized void refreshEffects(){
        if (gridEffectOn)
            for (ImageView i:gridIMG)
                i.setEffect(ds);
        else
            for (ImageView i:gridIMG)
                i.setEffect(null);

        if (reserveEffectOn)
            for (ImageView i:draftIMG)
                i.setEffect(ds);
        else
            for (ImageView i:draftIMG)
                i.setEffect(null);

        if (roundtrackEffectOn)
            for (ImageView i:roundtrackIMG)
                i.setEffect(ds);
        else
            for (ImageView i:roundtrackIMG)
                i.setEffect(null);

        if (toolEffectOn) {
            tool1.setEffect(ds);
            tool2.setEffect(ds);
            tool3.setEffect(ds);
        } else {
            tool1.setEffect(null);
            tool2.setEffect(null);
            tool3.setEffect(null);
        }

        if (passEffectOn)
            pass.setEffect(ds);
        else
            pass.setEffect(null);

        if (refreshEffectOn)
            refresh.setEffect(ds);
        else
            refresh.setEffect(null);
    }

    private Task keepRefreshing = new Task<Void>() {
        @Override
        protected Void call() {
            taskIsRunning = true;
            while (!GUIupdater.getHasGameEnded()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                refreshEffects();
            }
            return null;
        }
    };

    ArrayList getRoundtrackIMG() { return roundtrackIMG; }

    static synchronized void highlightGrid(boolean on){
        gridEffectOn = on;
    }

    static synchronized void highlightDraft(boolean on){
        reserveEffectOn = on;
    }

    static synchronized void highlightRoundtrack(boolean on){
        roundtrackEffectOn = on;
    }

    static synchronized void highlightTool(boolean on){
        toolEffectOn = on;
    }

    static synchronized void highlightPass(boolean on){
        passEffectOn = on;
    }

    static synchronized void highlightRefresh(boolean on){
        refreshEffectOn = on;
    }

    static void choicePopup(String message, String option1, String option2, boolean isOne){
        Platform.runLater(() -> {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Make a decision!");
            Label label1 = new Label(TOOL);
            label1.setFont(myFont);
            Label label2 = new Label("You have to make a decision...\n");
            label2.setFont(myFont);
            Text text = new Text();
            text.setText(message);
            text.setFont(myFont);
            Button button1 = new Button(option1);
            button1.setFont(myFont);
            button1.setOnAction(e -> {
                if(isOne)
                    GUIupdater.setToSend("+1");
                else
                    GUIupdater.setToSend("y");
                popup.close();
            });
            Button button2 = new Button(option2);
            button2.setFont(myFont);
            button2.setOnAction(e -> {
                if (isOne)
                    GUIupdater.setToSend("-1");
                else
                    GUIupdater.setToSend("n");
                popup.close();
            });
            HBox choiceLine = new HBox();
            Region r1 = new Region();
            Region r2 = new Region();
            Region r3 = new Region();
            choiceLine.getChildren().addAll(r1, button1, r2, button2, r3);
            choiceLine.setHgrow(r1, Priority.ALWAYS);
            choiceLine.setHgrow(r2, Priority.ALWAYS);
            choiceLine.setHgrow(r3, Priority.ALWAYS);
            VBox layout = new VBox(40);
            layout.getChildren().addAll(label1, label2, text, choiceLine);
            layout.setAlignment(Pos.CENTER);
            Scene popupScene = new Scene(layout, 400, 350);
            popup.setScene(popupScene);
            popup.setResizable(false);
            popup.setIconified(false);
            popup.initStyle(StageStyle.UNDECORATED);
            popup.showAndWait();
        });
    }

    static void chooseValuePopup(){
        Platform.runLater(() -> {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Choose a dice value!");
            Label label1 = new Label(TOOL);
            label1.setFont(myFont);
            Label label2 = new Label("You have to choose a value for the extracted dice...\n");
            label2.setFont(myFont);

            Button button1 = new Button("1");
            button1.setFont(myFont);
            button1.setOnAction(e -> {
                GUIupdater.setToSend("1");
                popup.close();
            });
            Button button2 = new Button("2");
            button2.setFont(myFont);
            button2.setOnAction(e -> {
                GUIupdater.setToSend("2");
                popup.close();
            });
            Button button3 = new Button("3");
            button3.setFont(myFont);
            button3.setOnAction(e -> {
                GUIupdater.setToSend("3");
                popup.close();
            });
            Button button4 = new Button("4");
            button4.setFont(myFont);
            button4.setOnAction(e -> {
                GUIupdater.setToSend("4");
                popup.close();
            });
            Button button5 = new Button("5");
            button5.setFont(myFont);
            button5.setOnAction(e -> {
                GUIupdater.setToSend("5");
                popup.close();
            });
            Button button6 = new Button("6");
            button6.setFont(myFont);
            button6.setOnAction(e -> {
                GUIupdater.setToSend("6");
                popup.close();
            });

            VBox layout = new VBox(10);
            layout.getChildren().addAll(label1, label2, button1, button2, button3, button4, button5, button6);
            layout.setAlignment(Pos.CENTER);
            Scene popupScene = new Scene(layout, 500, 350);
            popup.setScene(popupScene);
            popup.setResizable(false);
            popup.setIconified(false);
            popup.showAndWait();
        });
    }

    static void dicePopup(String message){
        Platform.runLater(() -> {
            String[] divide;
            String[] toBePrepared = new String[1];
            divide = message.split(DIVISOR);
            ArrayList<String> color = new ArrayList<>();
            ArrayList<String> value = new ArrayList<>();
            toBePrepared[0] = divide[1];
            prepareString(color, value, toBePrepared);
            ImageView img = new ImageView(new Image("/images/" + color.get(0) + value.get(0) + ".jpeg",
                    60, 60, false, false));

            Stage popup = new Stage(); //TODO: SOLVE INVOCATIONEXCEPTION
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Advertisement");
            Label label1 = new Label(TOOL);
            label1.setFont(myFont);
            Label label2 = new Label(divide[0]+":");
            label2.setFont(myFont);
            Button button = new Button("Ok");
            button.setFont(myFont);
            button.setOnAction(e -> popup.close());
            VBox layout = new VBox(40);
            layout.getChildren().addAll(label1, label2, img, button);
            layout.setAlignment(Pos.CENTER);
            Scene popupScene = new Scene(layout, 400, 350);
            popup.setScene(popupScene);
            popup.setResizable(false);
            popup.setIconified(false);
            popup.showAndWait();
        });
    }

    private static synchronized void prepareString(ArrayList<String> imageColor, ArrayList<String> imageValue, String[] divide){
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

    static synchronized void loadScheme(String wp, ArrayList<ImageView> toFill, GridPane gridObj){
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

        for (ImageView i: toFill)
            i.setOnMouseClicked(e -> {
                if(GUIupdater.getTypeRequested() == GUIupdater.TypeRequested.WINDOWPATTERN
                        && !GUIupdater.getNeedsToReload()){
                    int posx = toFill.indexOf(i);
                    if(posx<5)
                        posx = 1;
                    else if (posx<10)
                        posx = 2;
                    else if (posx<15)
                        posx = 3;
                    else
                        posx = 4;
                    GUIupdater.addToSendIntList(Integer.toString(posx));
                    GUIupdater.addToSendIntList(Integer.toString(toFill.indexOf(i)%MAX_COL + 1));
                }
            });
    }

    private synchronized void loadReserve(String draft){
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

        for (ImageView i:draftIMG)
            i.setOnMouseClicked(e -> {
                if (GUIupdater.getTypeRequested() == GUIupdater.TypeRequested.STANDARDREQUEST
                        && !GUIupdater.getNeedsToReload()) {
                    GUIupdater.addToSendIntList("d");
                    GUIupdater.addToSendIntList(Integer.toString(draftIMG.size() - draftIMG.indexOf(i)));
                } else if (GUIupdater.getTypeRequested() == GUIupdater.TypeRequested.RESERVE
                        && !GUIupdater.getNeedsToReload()) {
                    GUIupdater.addToSendIntList(Integer.toString(draftIMG.size() - draftIMG.indexOf(i)));
                }
            });
    }

    private synchronized void loadRoundtrack(String track){
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

        for (ImageView i:roundtrackIMG)
            i.setOnMouseClicked(e -> {
                if (GUIupdater.getTypeRequested() == GUIupdater.TypeRequested.ROUNDTRACK
                        && !GUIupdater.getNeedsToReload()) {
                    GUIupdater.addToSendIntList(Integer.toString(roundtrackIMG.size() - roundtrackIMG.indexOf(i)));
                }
            });
    }

    private synchronized void setButtonImage(String path, Button button){
        button.setStyle("-fx-background-image: url('" + path + "')");
    }

    private synchronized void loadObjectiveCards(String privateOC, String table){
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

    private synchronized void loadTools(String activeTools){
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

        tool1.setOnMouseClicked(e -> {
            refreshEffects();
            if (GUIupdater.getTypeRequested() == GUIupdater.TypeRequested.STANDARDREQUEST
                    && !GUIupdater.getNeedsToReload()) {
                GUIupdater.addToSendIntList("t");
                GUIupdater.addToSendIntList("1");
            }
        });
        tool2.setOnMouseClicked(e -> {
            refreshEffects();
            if (GUIupdater.getTypeRequested() == GUIupdater.TypeRequested.STANDARDREQUEST
                    && !GUIupdater.getNeedsToReload()) {
                GUIupdater.addToSendIntList("t");
                GUIupdater.addToSendIntList("2");
            }
        });
        tool3.setOnMouseClicked(e -> {
            if (GUIupdater.getTypeRequested() == GUIupdater.TypeRequested.STANDARDREQUEST
                    && !GUIupdater.getNeedsToReload()) {
                GUIupdater.addToSendIntList("t");
                GUIupdater.addToSendIntList("3");
            }
        });
    }

    @FXML
    synchronized void refreshScreen(){
            GUIupdater.setNeedsToReload(false);
            highlightRefresh(false);
            reloadGame(GUIupdater.getNumPlayers(), GUIupdater.getOwnScheme(),
                    GUIupdater.getPrivObj(), GUIupdater.getTable(), GUIupdater.getTools(),
                    GUIupdater.getOwnPlayer(), GUIupdater.getActivePlayer());
    }

    @FXML
    synchronized void reloadGame(int numP, String scheme, String privateOC, String table, String activeTools, String me, String activePlayer){
        for (ImageView i:gridIMG)
            i.setImage(null);
        gridIMG.clear();
        for (ImageView i:roundtrackIMG)
            i.setImage(null);
        roundtrackIMG.clear();
        for (ImageView i:draftIMG)
            i.setImage(null);
        draftIMG.clear();

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

        if(!taskIsRunning){
            Thread refresh = new Thread(keepRefreshing);
            refresh.setDaemon(true);
            refresh.start();
        }
    }

    @FXML
    private void loadPlayer1(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/player.fxml"));
        Parent root = loader.load();
        PlayerController controller = loader.getController();

        controller.loadGrid(GUIupdater.getPlayer(1), draftIMG);

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
    }

    @FXML
    private void loadPlayer2(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/player.fxml"));
        Parent root = loader.load();
        PlayerController controller = loader.getController();

        controller.loadGrid(GUIupdater.getPlayer(2), draftIMG);

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
    }

    @FXML
    private void loadPlayer3(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/player.fxml"));
        Parent root = loader.load();
        PlayerController controller = loader.getController();

        controller.loadGrid(GUIupdater.getPlayer(3), draftIMG);

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
    }

     private synchronized void endGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameEnd.fxml"));
        Parent root = loader.load();
        GUIController controller = loader.getController();

        controller.setPlayers(GUIupdater.getNumPlayers(), GUIupdater.getFinalMessage());

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(false);
        appStage.show();
    }

    @FXML
    private void passTurn(ActionEvent event) {
        if (GUIupdater.getFinalMessage() != null) {
            try {
                System.out.println(GUIupdater.getFinalMessage());
                endGame(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (GUIupdater.getTypeRequested() == GUIupdater.TypeRequested.STANDARDREQUEST
                && !GUIupdater.getNeedsToReload()) {
            GUIupdater.addToSendIntList("q");
        }
    }

}