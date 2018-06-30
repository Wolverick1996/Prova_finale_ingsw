package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Scheme;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUI_Controller implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane pane1;
    @FXML
    private AnchorPane pane2;
    @FXML
    private AnchorPane pane3;
    @FXML
    private BorderPane pane4;
    @FXML
    private AnchorPane pane5;

    @Override
    public void initialize(URL location, ResourceBundle resources) { System.out.println("Switching between scenes..."); }

    private static void popup(String error) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Something happened...");
        Label label1 = new Label("WAIT!\n");
        Label label2 = new Label("The following error occured:\n");
        Text text = new Text();
        text.setText(error);
        Button button = new Button("Ok");
        button.setOnAction(e -> popup.close());
        VBox layout = new VBox(40);
        layout.getChildren().addAll(label1, label2, text, button);
        layout.setAlignment(Pos.CENTER);
        Scene popupScene = new Scene(layout, 400, 350);
        popup.setScene(popupScene);
        popup.setResizable(false);
        popup.setIconified(false);
        popup.showAndWait();
    }

    private static void waiting(int timer, int numP) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Waiting...");
        Label label1 = new Label("Waiting...\n");
        Label label2 = new Label("Players in the lobby:\t" + numP);
        VBox layout = new VBox(40);
        layout.getChildren().addAll(label1, label2);
        layout.setAlignment(Pos.CENTER);
        Scene popupScene = new Scene(layout, 250, 200);
        popup.setScene(popupScene);
        PauseTransition delay = new PauseTransition(Duration.seconds(timer));
        delay.setOnFinished(event -> popup.close());
        delay.play();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.showAndWait();
    }

    @FXML
    private void loadSecond(ActionEvent event) throws IOException {
        pane1 = FXMLLoader.load(getClass().getResource("/FXML/login.fxml"));
        //The following command is used to replace the first window content with the second
        rootPane.getChildren().setAll(pane1);
    }

    @FXML
    private void loadThird(ActionEvent event) throws IOException {
        //if (numP == 1) {
        pane2 = FXMLLoader.load(getClass().getResource("/FXML/lobby.fxml"));
        pane1.getChildren().setAll(pane2);
        //} else
        //loadSchemes(event);
    }

    @FXML
    private void loadSchemes(ActionEvent event) throws IOException {
        //waiting should get the timer from input and the number of players from the controller
        //waiting(15, 3);
        Scheme s1 = Scheme.initialize(1, false, 24);
        Scheme s2 = Scheme.initialize(2, false, 24);
        Scheme s3 = Scheme.initialize(3, false, 24);
        Scheme s4 = Scheme.initialize(4, false, 24);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/chooseSchemes.fxml"));
        Parent root = loader.load();
        SchemesController controller = loader.getController();
        controller.setSchemes(s1.toString(), s2.toString(), s3.toString(), s4.toString());

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
    }

    @FXML
    private void loadGame(ActionEvent event) throws IOException {
        Parent blah = FXMLLoader.load(getClass().getResource("/FXML/gameScreen.fxml"));
        Scene scene = new Scene(blah);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
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