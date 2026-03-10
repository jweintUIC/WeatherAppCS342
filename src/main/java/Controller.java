
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import weather.Period;
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
import weather.WeatherAPI;
import java.util.ArrayList;


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

    @FXML
    private TextField cityInput;

    @FXML
    private Label time;

    @FXML
    private Label tmrWeather;

    @FXML
    private Label tmrWeatherForecast;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        time.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("h:mma")));
        ArrayList<Period> points = MyWeatherAPI.getPointForecast("Chicago");
        weather.setText(points.get(0).shortForecast);
        city.setText(MyWeatherAPI.cityName);
        rainChance.setText("The chance of rain is " +String.valueOf(points.get(0).probabilityOfPrecipitation.value)+"%");
        temperature.setText(String.valueOf(points.get(0).temperature)+"F");
        if (points.get(1).isDaytime) {
            tmrWeather.setText("Tomorrow's Weather in "+city.getText()+"Will Be: ");

        }
        else {
            tmrWeather.setText("Tonight's Weather in "+city.getText()+"Will Be: ");
        }
        tmrWeatherForecast.setText(points.get(1).temperature+"F with "+points.get(1).shortForecast);


    }
    public void searchMethod(ActionEvent e) {
            String cityString = cityInput.getText();
			ArrayList<Period> newCity = MyWeatherAPI.getPointForecast(cityString);
			if (newCity == null) {
				city.setText("City not found");
				temperature.setVisible(false);
				weather.setVisible(false);
				rainChance.setVisible(false);
			} else {
                city.setText(MyWeatherAPI.cityName);
                temperature.setVisible(true);
                weather.setVisible(true);
                rainChance.setVisible(true);
                temperature.setText(String.valueOf(newCity.get(0).temperature)+"F");
                weather.setText(newCity.get(0).shortForecast);
                rainChance.setText("The chance of rain is " + String.valueOf(newCity.get(0).probabilityOfPrecipitation.value) + "%");
                ZoneId zone = ZoneId.of(MyWeatherAPI.timeZone);
                LocalTime now = LocalTime.now(zone);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
                time.setText(now.format(formatter));
                if (newCity.get(1).isDaytime) {
                    tmrWeather.setText("Tomorrow's Weather in "+city.getText()+"Will Be: ");

                }
                else {
                    tmrWeather.setText("Tonight's Weather in "+city.getText()+"Will Be: ");
                }
                tmrWeatherForecast.setText(newCity.get(1).temperature+"F with "+newCity.get(1).shortForecast);
            }
    }
}
