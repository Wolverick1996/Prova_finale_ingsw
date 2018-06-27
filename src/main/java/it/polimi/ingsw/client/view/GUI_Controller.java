package it.polimi.ingsw.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) { System.out.println("Switching between scenes..."); }

    @FXML
    private void loadSecond(ActionEvent event) throws IOException {
        pane1 = FXMLLoader.load(getClass().getResource("/FXML/login.fxml"));
        //The following command is used to replace the first window content with the second
        rootPane.getChildren().setAll(pane1);
    }

    @FXML
    private void loadThird(ActionEvent event) throws IOException {
        pane2 = FXMLLoader.load(getClass().getResource("/FXML/lobby.fxml"));
        pane1.getChildren().setAll(pane2);
    }

}