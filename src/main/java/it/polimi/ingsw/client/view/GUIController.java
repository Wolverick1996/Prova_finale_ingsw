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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.SchemesController.NEWLINE;

public class GUIController implements Initializable {

    private static final int STARTED = 999;
    static final int INFINITE = 5;
    private static final String RMI = "rmi";
    private static final String SOCKET = "socket";
    private static final String RANK_DIVISOR = ": \t";
    private static SocketMessengerClient messenger = GUIupdater.messenger;
    private int lobbyDelay = GUIupdater.lobbyDelay;
    private int numPlayersAtBeginning = GUIupdater.numPlayersAtBeginning;
    private int numPlayers = GUIupdater.numPlayers;
    private boolean isRMI = GUIupdater.isRMI;
    private boolean customSchemes = GUIupdater.getCustomSchemes();
    private static Stage activePopup;
    static Font myFont = new Font("Century Gothic", 18);

    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane pane1;
    @FXML
    private AnchorPane pane2;
    @FXML
    private TextField username;
    @FXML
    private TextField ip;
    @FXML
    private RadioButton rmiButton;
    @FXML
    private RadioButton customSchemesButtonNo;
    @FXML
    private TextField delayTextField;
    @FXML
    private GridPane player2;
    @FXML
    private GridPane player3;
    @FXML
    private GridPane player4;
    @FXML
    private Text player1Name;
    @FXML
    private Text player1Score;
    @FXML
    private Text player2Name;
    @FXML
    private Text player2Score;
    @FXML
    private Text player3Name;
    @FXML
    private Text player3Score;
    @FXML
    private Text player4Name;
    @FXML
    private Text player4Score;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        System.out.println("Switching between scenes...");
    }

    private static void popup(String error){
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Something happened...");
        Label label1 = new Label("WAIT!\n");
        label1.setFont(myFont);
        Label label2 = new Label("The following error occured:\n");
        label2.setFont(myFont);
        Text text = new Text();
        text.setText(error);
        text.setFont(myFont);
        Button button = new Button("Ok");
        button.setFont(myFont);
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

    static void waiting(int timer, int numP, boolean schemes){
        if (activePopup != null)
            activePopup.close();

        Stage popup = new Stage();
        activePopup = popup;
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Waiting...");
        Label label1 = new Label("Waiting...\n");
        label1.setFont(myFont);
        String message;
        if (schemes)
            message = "Somebody is picking a scheme";
        else
            message = "Players in the lobby:\t" + numP;
        Label label2 = new Label(message);
        label2.setFont(myFont);
        VBox layout = new VBox(40);
        layout.getChildren().addAll(label1, label2);
        layout.setAlignment(Pos.CENTER);
        Scene popupScene = new Scene(layout, 350, 300);
        popup.setScene(popupScene);
        PauseTransition delay = new PauseTransition(Duration.seconds(timer));
        delay.setOnFinished(event -> {
            activePopup = null;
            popup.close();
        });
        delay.play();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.showAndWait();
    }

    @FXML
    private void loadSecond() throws IOException {
        pane1 = FXMLLoader.load(getClass().getResource("/FXML/login.fxml"));
        //The following command is used to replace the first window content with the second
        rootPane.getChildren().setAll(pane1);
    }

    @FXML
    private String setUsername(){
        return username.getText();
    }

    @FXML
    private String setIP(){
        return ip.getText();
    }

    @FXML
    private String checkConnection(){
        if (rmiButton.isSelected()) return RMI;
        else return SOCKET;
    }

    //***************************//
    //       LOGIN PHASE         //
    //***************************//

    @FXML
    private boolean trySetup(){
        String name = setUsername();
        String address = setIP();
        String connection = checkConnection();
        if (name.equals("") || name.equals("*")) {
            popup("Invalid name, your ID should be at least 1 character (not *)");
            return false;
        }
        System.out.println(name + " is trying to connect using GUI... \n" + connection + " " + address);
        ClientMain clientMain = ClientMain.instance(address);
        try {
            if (connection.equals(RMI)) {
                String message = clientMain.startGUIRMI(name);
                if (message.equals("OK")) {
                    setNumPlayersAtBeginning(ClientMain.getPlayers());
                    isRMI = true;
                    GUIupdater.isRMI = isRMI;
                    return true;
                } else
                    popup(message);
                return false;
            } else {
                String message = clientMain.startGUISocket(name, this);
                if (message.equals("OK")) {
                    new Thread(messenger).start();
                    System.out.println("Hello " + name + ". I'm your GUI :)");
                    return true;
                } else
                    popup(message);
                return false;
            }
        } catch (MalformedURLException m) {
            popup("Malformed URL");
            return false;
        } catch (NotBoundException n) {
            popup("Not Bound!");
            return false;
        } catch (IOException e) {
            popup("IOException");
            return false;
        }
    }

    public void setNumPlayersAtBeginning(int num){
        numPlayersAtBeginning = num;
        GUIupdater.numPlayersAtBeginning = numPlayersAtBeginning;
        numPlayers = numPlayersAtBeginning;
        GUIupdater.numPlayers = numPlayers;
    }


    private void waitForGameStart(){
        if (isRMI) {
            try {
                waiting(INFINITE, numPlayers, false);
                int n = ClientMain.waitForGameStart();
                if (n == STARTED) {
                    if (activePopup!=null)
                    activePopup.close();
                } else {
                    GUIupdater.numPlayers = numPlayers;
                    numPlayers = n;
                    waitForGameStart();
                }
            } catch (RemoteException e) {
                System.out.println("server is down :(");
            }
        } else {
            int k = 0;
            try {
                k = messenger.waitForGameStart();
                System.out.println("I read " + k);
            } catch (IOException e) {
                System.out.println("SOMETHING IS WRONG WITH THE SERVER, IOEXC ON CHECKNEWPLAYERS");
            }
            if (k == STARTED) {
                if (activePopup!=null)
                activePopup.close();
            } else {
                GUIupdater.numPlayers = numPlayers;
                numPlayers = k;
                waiting(INFINITE, numPlayers, false);
                waitForGameStart();
            }
        }
    }

    @FXML
    private void loadThird(ActionEvent event) throws IOException {
        if (trySetup()) {
            if (numPlayersAtBeginning == 1) {
                pane2 = FXMLLoader.load(getClass().getResource("/FXML/lobby.fxml"));
                pane1.getChildren().setAll(pane2);
            } else {
                loadSchemes(event);
            }
        }
    }

    //***************************//
    //       SETUP PHASE         //
    //***************************//

    @FXML
    private void setParameters(){
        if (!customSchemesButtonNo.isSelected()) {
            customSchemes = true;
            GUIupdater.setCustomSchemes(customSchemes);
        }
        try {
            lobbyDelay = Integer.parseInt(delayTextField.getText());
            GUIupdater.lobbyDelay = lobbyDelay;
        } catch (NumberFormatException e) {
            System.out.println("Invalid delay, was set to 20");
        }
        setDelay(lobbyDelay);
    }

    private void setDelay(int delay){
        if (isRMI) {
            try {
                ClientMain.setGUIlobbyDelay(delay);
            } catch (RemoteException e) {
                System.out.println("FATAL ERROR ON SERVER :(");
            }
        } else {
            messenger.GUIsetTimer(delay);
        }
    }

    @FXML
    private void loadSchemes(ActionEvent event) throws IOException {
        if (numPlayersAtBeginning == 1){
            setParameters();
            //TODO: CHECK FOR LATE "NEXT"
        }

        waitForGameStart();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/chooseSchemes.fxml"));
        Parent root = loader.load();
        SchemesController controller = loader.getController();

        waitForSchemes();

        controller.setSchemes((String)GUIupdater.getSchemesToChoose().get(0),
                (String)GUIupdater.getSchemesToChoose().get(1), (String)GUIupdater.getSchemesToChoose().get(2),
                (String)GUIupdater.getSchemesToChoose().get(3));

        Scene scene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.setResizable(true);
        appStage.setFullScreen(true);
        appStage.show();
    }

    private void waitForSchemes(){
        while (GUIupdater.getSchemesToChoose().size() != 4)
            waiting(INFINITE, 0, true);
    }

    public static void setMessenger(SocketMessengerClient sm){
        messenger = sm;
    }

    void setPlayers(int numP, String rank){
        if (numP <= 3)
            player4.setVisible(false);
        if (numP <= 2)
            player3.setVisible(false);
        if (numP == 1)
            player2.setVisible(false);

        String[] divide;
        String[] splitter;
        divide = rank.split(NEWLINE);
        splitter = divide[2].split(RANK_DIVISOR);
        player1Name.setText(splitter[0]);
        player1Score.setText(splitter[1]);
        if (numP >= 2) {
            splitter = divide[3].split(RANK_DIVISOR);
            player2Name.setText(splitter[0]);
            player2Score.setText(splitter[1]);
        }
        if (numP >= 3) {
            splitter = divide[4].split(RANK_DIVISOR);
            player3Name.setText(splitter[0]);
            player3Score.setText(splitter[1]);
        }
        if (numP >= 4) {
            splitter = divide[5].split(RANK_DIVISOR);
            player4Name.setText(splitter[0]);
            player4Score.setText(splitter[1]);
        }
    }

}