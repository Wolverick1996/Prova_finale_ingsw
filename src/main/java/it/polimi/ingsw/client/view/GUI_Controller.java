package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.network_client.ClientMain;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.util.ResourceBundle;

public class GUI_Controller implements Initializable {

    final static String RMI = "rmi";
    final static String SOCKET = "socket";

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
    @FXML
    private TextField username, ip;
    @FXML
    private RadioButton socketButton, rmiButton;

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
    private String setUsername(ActionEvent event) {
        return username.getText();
    }

    @FXML
    private String setIP(ActionEvent event) {
        return ip.getText();
    }

    @FXML
    private String checkConnection(ActionEvent event) {
        if (rmiButton.isSelected()) return RMI;
        else return SOCKET;
    }

    @FXML
    private boolean trySetup(ActionEvent event){
        String name = setUsername(event);
        String address = setIP(event);
        String connection = checkConnection(event);
        if (name.equals("") || name.equals("*")){
            popup("Invalid name, your ID should be at least 1 character (not *)");
            return false;
        }
        System.out.println( name + " is trying to connect using GUI... \n" + connection + " " + address);
        ClientMain clientMain = ClientMain.instance(address);
        try {
            if (connection.equals(RMI)){
                String message = clientMain.startGUIRMI(name);
                if (message.equals("OK")){
                    System.out.println("Hello " + name + ", my world");
                    return true;
                }
                else
                    popup(message);
                return false;
            } else {
                String message = clientMain.startGUISocket(name);
                if (message.equals("OK")){
                    System.out.println("Hello " + name + ", my world");
                    return true;
                }
                else
                    popup(message);
                return false;
            }
        } catch (MalformedURLException m){
            popup("Sei un culetto MalformedURL");
            return false;
        } catch (NotBoundException n) {
            popup("Quell'errore che pensavamo non esistesse, NotBound");
            return false;
        } catch (IOException e){
            popup("Perdirindina, IO");
            return false;
        }
    }

    @FXML
    private void loadThird(ActionEvent event) throws IOException {
        if (trySetup(event)){
            pane2 = FXMLLoader.load(getClass().getResource("/FXML/lobby.fxml"));
            pane1.getChildren().setAll(pane2);
        }
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
        Parent blah = FXMLLoader.load(getClass().getResource("/FXML/chooseSchemes.fxml"));
        Scene scene = new Scene(blah);
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