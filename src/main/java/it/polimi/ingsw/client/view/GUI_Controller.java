package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.network_client.ClientMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    private RadioButton socket, rmi;

    @Override
    public void initialize(URL location, ResourceBundle resources) { System.out.println("Switching between scenes..."); }

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
        if (rmi.isSelected()) return RMI;
        else return SOCKET;
    }

    @FXML
    private boolean trySetup(ActionEvent event){
        String name = setUsername(event);
        String ip = setIP(event);
        String connection = checkConnection(event);
        System.out.println( name + " is trying to connect using GUI... \n" + connection + " " + ip);
        ClientMain clientMain = ClientMain.instance(ip);
        try {
            if (connection.equals(RMI)){
                
            } else {

            }
        } catch (IOException e){
            return false;
        } catch (MalformedURLException m){
            return false;
        }

        return false;
    }

    @FXML
    private void loadThird(ActionEvent event) throws IOException {
        if (!trySetup(event)){
            loadSecond(event);
        }
        pane2 = FXMLLoader.load(getClass().getResource("/FXML/lobby.fxml"));
        pane1.getChildren().setAll(pane2);
    }

    @FXML
    private void loadSchemes(ActionEvent event) throws IOException {
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