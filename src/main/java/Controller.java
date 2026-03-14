
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import java.util.concurrent.Future;


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
    @FXML
    private Label FTRD1,FTRD2,FTRD3,FTRD4,FTRD5,FTRD6;
    @FXML
    private Label FTRMT1,FTRMT2,FTRMT3,FTRMT4,FTRMT5,FTRMT6;
    @FXML
    private Label FTRNT1,FTRNT2,FTRNT3,FTRNT4,FTRNT5,FTRNT6;
    @FXML
    private Label FTRWS1,FTRWS2,FTRWS3,FTRWS4,FTRWS5,FTRWS6;
    @FXML
    private Label FTRRC1,FTRRC2,FTRRC3,FTRRC4,FTRRC5,FTRRC6;
    @FXML
    private Label FtrForecastCity;



    @Override
    public void initialize(URL location, ResourceBundle resources)  {
        ArrayList<Period> points = MyWeatherAPI.lastForecast;
        if (points == null) {
            points = MyWeatherAPI.getPointForecast("Chicago");
            MyWeatherAPI.lastForecast = points;
        }
        if (city != null) {
            time.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("h:mma")));
            weather.setText(points.get(0).shortForecast);
            city.setText(MyWeatherAPI.cityName);
            rainChance.setText("The chance of rain is " + String.valueOf(points.get(0).probabilityOfPrecipitation.value) + "%");
            temperature.setText(String.valueOf(points.get(0).temperature) + "F");
            if (points.get(1).isDaytime) {
                tmrWeather.setText("Tomorrow's Weather in " + city.getText() + "Will Be: ");

            } else {
                tmrWeather.setText("Tonight's Weather in " + city.getText() + "Will Be: ");
            }
            tmrWeatherForecast.setText(points.get(1).temperature + "F with " + points.get(1).shortForecast);
        }
        if (FTRD1!= null) {
            FtrForecastCity.setText("6 Day Future Forecast for "+ MyWeatherAPI.cityName);
            int start;
            if (points.get(0).isDaytime == false) {
                start = 1;
            } else {
                start = 2;
            }
            Label[] dateLabels =    {FTRD1,  FTRD2,  FTRD3,  FTRD4,  FTRD5,  FTRD6};
            Label[] morningLabels = {FTRMT1, FTRMT2, FTRMT3, FTRMT4, FTRMT5, FTRMT6};
            Label[] nightLabels =   {FTRNT1, FTRNT2, FTRNT3, FTRNT4, FTRNT5, FTRNT6};
            Label[] windLabels =    {FTRWS1, FTRWS2, FTRWS3, FTRWS4, FTRWS5, FTRWS6};
            Label[] chanceLabels =  {FTRRC1, FTRRC2, FTRRC3, FTRRC4, FTRRC5, FTRRC6};

            for (int i = 0; i < 6; i++) {
                dateLabels[i].setText(LocalDate.now().plusDays(i + 1).format(DateTimeFormatter.ofPattern("MMMM d")));
                morningLabels[i].setText(points.get(start + i * 2).temperature + "F");
                nightLabels[i].setText(points.get(start + i * 2 + 1).temperature + "F");
                windLabels[i].setText(points.get(start + i * 2).windSpeed + " " + points.get(start + i * 2).windDirection);
                chanceLabels[i].setText(points.get(start + i * 2).probabilityOfPrecipitation.value + "%");
            }
        }

    }
    public void switchSeneFuture(ActionEvent e) throws IOException  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/FutureForecast.fxml"));
        Parent root = loader.load();
        cityInput.getScene().setRoot(root);
    }
    public void switchSeneToday(ActionEvent e) throws IOException  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/TodayForecast.fxml"));
        Parent root = loader.load();
        cityInput.getScene().setRoot(root);
    }
    public void searchMethod(ActionEvent e) throws IOException {
            String cityString = cityInput.getText();
			ArrayList<Period> newCity = MyWeatherAPI.getPointForecast(cityString);
			if (newCity == null) {
				city.setText("City not found");
				temperature.setVisible(false);
				weather.setVisible(false);
				rainChance.setVisible(false);
                tmrWeather.setVisible(false);
                tmrWeatherForecast.setVisible(false);
			} else {
                city.setText(MyWeatherAPI.cityName);
                temperature.setVisible(true);
                weather.setVisible(true);
                rainChance.setVisible(true);
                tmrWeather.setVisible(true);
                tmrWeatherForecast.setVisible(true);
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
    public void searchMethodFuture(ActionEvent e) throws IOException {
        String cityString = cityInput.getText();
        ArrayList<Period> newCity = MyWeatherAPI.getPointForecast(cityString);
        if (FTRD1!= null) {
            FtrForecastCity.setText("6 Day Future Forecast for "+ MyWeatherAPI.cityName);
            FTRD1.setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("MMMM d")));
            FTRD2.setText(LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("MMMM d")));
            FTRD3.setText(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("MMMM d")));
            FTRD4.setText(LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("MMMM d")));
            FTRD5.setText(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("MMMM d")));
            FTRD6.setText(LocalDate.now().plusDays(6).format(DateTimeFormatter.ofPattern("MMMM d")));
            int start;
            if (newCity.get(0).isDaytime == false) {
                start = 1;
            } else {
                start = 2;
            }
            Label[] dateLabels =    {FTRD1,  FTRD2,  FTRD3,  FTRD4,  FTRD5,  FTRD6};
            Label[] morningLabels = {FTRMT1, FTRMT2, FTRMT3, FTRMT4, FTRMT5, FTRMT6};
            Label[] nightLabels =   {FTRNT1, FTRNT2, FTRNT3, FTRNT4, FTRNT5, FTRNT6};
            Label[] windLabels =    {FTRWS1, FTRWS2, FTRWS3, FTRWS4, FTRWS5, FTRWS6};
            Label[] chanceLabels =  {FTRRC1, FTRRC2, FTRRC3, FTRRC4, FTRRC5, FTRRC6};
            ZoneId zone = ZoneId.of(MyWeatherAPI.timeZone);
            for (int i = 0; i < 6; i++) {
                dateLabels[i].setText(LocalDate.now(zone).plusDays(i+1).format(DateTimeFormatter.ofPattern("MMMM d")));
                morningLabels[i].setText(newCity.get(start + i * 2).temperature + "F");
                nightLabels[i].setText(newCity.get(start + i * 2 + 1).temperature + "F");
                windLabels[i].setText(newCity.get(start+i*2).windSpeed+" "+newCity.get(start+i*2).windDirection);
                chanceLabels[i].setText(newCity.get(start + i * 2).probabilityOfPrecipitation.value + "%");
            }
        }
    }
}
