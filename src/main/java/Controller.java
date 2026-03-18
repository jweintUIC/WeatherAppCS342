import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import HourlyWeather.HourlyPeriod;
import javafx.application.Platform;
import weather.Period;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class Controller implements  Initializable{

    @FXML //Short forecast Scene 1
    private Label weather;

    @FXML //Temperature Scene 1
    private Label temperature;

    @FXML //City Scene 1
    private Label city;

    @FXML //Rain chance Scene 1
    private Label rainChance;

    @FXML //Search bar used in both scenes to input new city
    private TextField cityInput;

    @FXML //Time at top of Scene 1
    private Label time;

    @FXML //Future Date Scene 2
    private Label FTRD1,FTRD2,FTRD3,FTRD4,FTRD5,FTRD6;

    @FXML //Future Morning Temp Scene 2
    private Label FTRMT1,FTRMT2,FTRMT3,FTRMT4,FTRMT5,FTRMT6;

    @FXML //Future Nightime Temp Scene 2
    private Label FTRNT1,FTRNT2,FTRNT3,FTRNT4,FTRNT5,FTRNT6;

    @FXML //Future Wind Speed Scene 2
    private Label FTRWS1,FTRWS2,FTRWS3,FTRWS4,FTRWS5,FTRWS6;

    @FXML //Future Rain Chance Scene 2
    private Label FTRRC1,FTRRC2,FTRRC3,FTRRC4,FTRRC5,FTRRC6;

    @FXML //In Scene 2 shows city
    private Label FtrForecastCity;

    @FXML //Hourly Times in Scene 1
    private Label dailyTimeOne, dailyTimeTwo, dailyTimeThree, dailyTimeFour, dailyTimeFive, dailyTimeSix;

    @FXML //Hourly Temperatures in Scene 1
    private Label dailyTemperatureOne, dailyTemperatureTwo, dailyTemperatureThree, dailyTemperatureFour, dailyTemperatureFive, dailyTemperatureSix;

    @FXML
    private Label loadingLabel;

    @FXML
    private Rectangle loadingRectangle;

    @FXML
    private Label loadingLabelFuture;

    @FXML
    private Rectangle loadingRectangleFuture;

    @FXML //Weather Icon in Scene 1
    private ImageView S1WeatherImage;

    //Used to show current city. Defaults to Chicago
    public static String currentCityString = "Chicago";

    //Function made to set Scene 1 Values
    public void setValuesTodayForecast() {
        ArrayList<HourlyPeriod> pointsHourly = MyWeatherAPI.getPointForecastHourly(currentCityString);
        ZoneId timeZone = ZoneId.of(MyWeatherAPI.timeZone);
        time.setText(LocalTime.now(timeZone).format(DateTimeFormatter.ofPattern("h:mma")));
        temperature.setText(pointsHourly.get(0).temperature +"F");
        city.setText(MyWeatherAPI.cityName);
        weather.setText(pointsHourly.get(0).shortForecast);
        rainChance.setText("The chance of rain is " + String.valueOf(pointsHourly.get(0).probabilityOfPrecipitation.value) + "%");
        setHourlyLabels(pointsHourly);
        if (pointsHourly.get(0).shortForecast.contains("Sunny")) {
            S1WeatherImage.setImage(new Image(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/sun-light.png")));
        }
        else if (pointsHourly.get(0).shortForecast.contains("Cloudy")) {
            S1WeatherImage.setImage(new Image(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/cloud.png")));
        }
        else if (pointsHourly.get(0).shortForecast.contains("Rain")) {
            S1WeatherImage.setImage(new Image(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/rain.png")));
        }
        else if (pointsHourly.get(0).shortForecast.contains("Snow")) {
            S1WeatherImage.setImage(new Image(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/snow.png")));
        }
        else if (pointsHourly.get(0).shortForecast.contains("Thunderstorm")) {
            S1WeatherImage.setImage(new Image(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/thunderstorm.png")));
        }
        else {
            S1WeatherImage.setImage(new Image(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/cloud-sunny.png")));
        }
    }

    //Function made to set Scene 2 Values
    public void setTextFutureForecast() {
        ArrayList<Period> newCity = MyWeatherAPI.getPointForecast(currentCityString);
        int startPoint;
        if (newCity.get(0).isDaytime == false) {
            startPoint = 1;
        } else {
            startPoint = 2;
        }
        Label[] dateLabels =    {FTRD1,  FTRD2,  FTRD3,  FTRD4,  FTRD5,  FTRD6};
        Label[] morningLabels = {FTRMT1, FTRMT2, FTRMT3, FTRMT4, FTRMT5, FTRMT6};
        Label[] nightLabels =   {FTRNT1, FTRNT2, FTRNT3, FTRNT4, FTRNT5, FTRNT6};
        Label[] windLabels =    {FTRWS1, FTRWS2, FTRWS3, FTRWS4, FTRWS5, FTRWS6};
        Label[] chanceLabels =  {FTRRC1, FTRRC2, FTRRC3, FTRRC4, FTRRC5, FTRRC6};

        FtrForecastCity.setText("6 Day Future Forecast for "+ MyWeatherAPI.cityName);
        ZoneId zone = ZoneId.of(MyWeatherAPI.timeZone);
        for (int i = 0; i < 6; i++) {
            dateLabels[i].setText(LocalDate.now(zone).plusDays(i+1).format(DateTimeFormatter.ofPattern("MMMM d")));
            morningLabels[i].setText(newCity.get(startPoint + i * 2).temperature + "F");
            nightLabels[i].setText(newCity.get(startPoint + i * 2 + 1).temperature + "F");
            windLabels[i].setText(newCity.get(startPoint +i*2).windSpeed+" "+newCity.get(startPoint +i*2).windDirection);
            chanceLabels[i].setText(newCity.get(startPoint + i * 2).probabilityOfPrecipitation.value + "%");
        }
    }

    //Function made to change visibilities upon nullCity in Scene 1. Saves a lot of lines of code
    public void setVisibleUponSearch(boolean setVisi) {
        Label[] visiLabels = {temperature, weather, rainChance,
                dailyTimeOne, dailyTimeTwo, dailyTimeThree,
                dailyTimeFour, dailyTimeFive, dailyTimeSix,
                dailyTemperatureOne, dailyTemperatureTwo, dailyTemperatureThree,
                dailyTemperatureFour, dailyTemperatureFive, dailyTemperatureSix, time};
        for (Label l : visiLabels) {
            l.setVisible(setVisi);
        }
    }

    //Function made to change visibilities upon nullCity in Scene 2
    private void setFutureVisibility(boolean setVisi) {
        Label[] visiLabelsFuture = {FTRD1,  FTRD2,  FTRD3,  FTRD4,  FTRD5,  FTRD6,
                FTRMT1, FTRMT2, FTRMT3, FTRMT4, FTRMT5, FTRMT6,
                FTRNT1, FTRNT2, FTRNT3, FTRNT4, FTRNT5, FTRNT6,
                FTRWS1, FTRWS2, FTRWS3, FTRWS4, FTRWS5, FTRWS6,
                FTRRC1, FTRRC2, FTRRC3, FTRRC4, FTRRC5, FTRRC6};
        for (Label l : visiLabelsFuture) {
            l.setVisible(setVisi);
        }
    }

    //Used to format hour in Time Labels. Function made for readability and preventing long if statements
    private String formatTimeLabelText(int pointsHour) {
        if (pointsHour == 0)  {
            return "12AM";
        }
        else if (pointsHour == 12) {
            return "12PM";
        }
        else if (pointsHour > 12)  {
            return (pointsHour - 12) + "PM";
        }
        else {
            return pointsHour + "AM";
        }
    }

    //Function made to set the hourly labels in Scene 1. Saves code as opposed to calling long for loop multiple times
    private void setHourlyLabels(ArrayList<HourlyPeriod> pointsHourly) {
        Label[] timeLabels = {dailyTimeOne, dailyTimeTwo, dailyTimeThree,
                dailyTimeFour, dailyTimeFive, dailyTimeSix};
        Label[] tempLabels = {dailyTemperatureOne, dailyTemperatureTwo, dailyTemperatureThree,
                dailyTemperatureFour, dailyTemperatureFive, dailyTemperatureSix};

        ZoneId timeZone;
        if (MyWeatherAPI.timeZone != null) {
            timeZone = ZoneId.of(MyWeatherAPI.timeZone);
        } else {
            timeZone = ZoneId.systemDefault();
        }

        int alignedIndex = 0;

        for (int i = 0; i < 168; i++) {
            if (pointsHourly.get(i).startTime.toInstant().atZone(ZoneId.of(MyWeatherAPI.timeZone)).getHour() ==
                LocalTime.now(timeZone).getHour()) {
                System.out.println("Points hour:" + pointsHourly.get(i).startTime.toInstant().atZone(ZoneId.of(MyWeatherAPI.timeZone)).getHour());
                System.out.println("Current hour:" + LocalTime.now(timeZone).getHour());
                alignedIndex = i;
                break;
            }
        }

        int loopFinal = alignedIndex + 15;
        int timeLabelIndex = 0;
        int tempLabelIndex = 0;

        for (int i = alignedIndex; i <= loopFinal; i+=3) {
            int pointsHour = pointsHourly.get(i).startTime.toInstant().atZone(ZoneId.of(MyWeatherAPI.timeZone)).getHour();
            timeLabels[timeLabelIndex].setText(formatTimeLabelText(pointsHour));
            tempLabels[tempLabelIndex].setText(pointsHourly.get(i).temperature + "F");
            timeLabelIndex++;
            tempLabelIndex++;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)  {
        if (city != null) { //Checks if in Scene 1
            setValuesTodayForecast();
        }
        if (FTRD1!= null) { //Checks if in Scene 2
            setTextFutureForecast();
        }
    }

    //Function made to switch to Scene 2
    public void switchSceneFuture(ActionEvent e) throws IOException {
        loadingRectangle.toFront();
        loadingLabel.toFront();
        System.out.println("Start");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/FutureForecast.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Controller controller = loader.getController();
                    cityInput.getScene().setRoot(root);
                    controller.setTextFutureForecast();
                });
            }
        };
        timer.schedule(task, 500);
    }

    //Function made to switch back to Scene 1
    public void switchSceneToday(ActionEvent e) throws IOException  {
        loadingRectangleFuture.toFront();
        loadingLabelFuture.toFront();
        System.out.println("Start");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/TodayForecast.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Controller controller = loader.getController();
                    cityInput.getScene().setRoot(root);
                    controller.setValuesTodayForecast();
                });
            }
        };
        timer.schedule(task, 500);

    }

    //Function made to handle Scene 1 Searchbar
    public void searchMethod(ActionEvent e) throws IOException {
            String cityString = cityInput.getText();
			ArrayList<Period> newCity = MyWeatherAPI.getPointForecast(cityString);
			if (newCity==null) {
				city.setText("City not found");
                setVisibleUponSearch(false);
			} else {
                currentCityString = cityString;
                setVisibleUponSearch(true);
                setValuesTodayForecast();
            }
    }

    //Function made to handle Scene 2 Searchbar
    public void searchMethodFuture(ActionEvent e) throws IOException {
        String cityString = cityInput.getText();
        ArrayList<Period> newCity = MyWeatherAPI.getPointForecast(cityString);
        if (newCity == null) {
            FtrForecastCity.setText("City not found");
            setFutureVisibility(false);
            return;
        }
        currentCityString = cityString;
        setFutureVisibility(true);
        if (FTRD1!= null) { //checks if in Scene 2
            setTextFutureForecast();
        }
    }
}
