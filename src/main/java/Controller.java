
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller implements  Initializable{

    //		temperature = new Label();
//		weather = new Label();
//		city = new Label();
//		title = new Label();
//		rainChance = new Label();
//		loading = new Label();
//		loading.setVisible(false);
    @FXML
    private Label weather;

    @FXML
    private Label temperature;

    @FXML
    private Label city;

    @FXML
    private Label title;

    @FXML
    private Label rainChance;

    @FXML
    private Label loading;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
}
