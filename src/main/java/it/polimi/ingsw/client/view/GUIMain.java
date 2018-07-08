package it.polimi.ingsw.client.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class which launches the GUI application
 *
 * @author Riccardo
 */
public class GUIMain extends Application {

    /**
     * Launches the GUI application
     *
     * @param args: list of strings received from the user during the JAR execution
     * @author Riccardo
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Overrides start method of interface Application, loads the first stage of the application
     *
     * @param primaryStage: main stage of the application
     * @throws Exception: generic exception thrown if something uncaught could trigger the application
     * @author Riccardo
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/firstWindow.fxml"));
        primaryStage.setTitle("Sagrada");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}