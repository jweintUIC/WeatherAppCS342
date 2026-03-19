import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import HourlyWeather.HourlyPeriod;
import javafx.application.Platform;
import javafx.scene.control.Button;
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
    private Label dailyPercipitationOne, dailyPercipitationTwo, dailyPercipitationThree, dailyPercipitationFour, dailyPercipitationFive, dailyPercipitationSix;

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

    @FXML //Percip chance icons for hourly
    private ImageView HR1Icon, HR2Icon, HR3Icon, HR4Icon, HR5Icon, HR6Icon;

    @FXML
    private ImageView FuturePrecipIconOne, FuturePrecipIconTwo, FuturePrecipIconThree, FuturePrecipIconFour, FuturePrecipIconFive, FuturePrecipIconSix;

    @FXML
    private ImageView FutureDayTempOne, FutureDayTempTwo, FutureDayTempThree, FutureDayTempFour, FutureDayTempFive, FutureDayTempSix;

    @FXML
    private ImageView FutureNightTempOne, FutureNightTempTwo, FutureNightTempThree, FutureNightTempFour, FutureNightTempFive, FutureNightTempSix;

    @FXML
    private ImageView FutureWindOne, FutureWindTwo, FutureWindThree, FutureWindFour, FutureWindFive, FutureWindSix;

    @FXML
    private ImageView searchIcon;

    @FXML
    private Button switchSceneButton;

    @FXML
    private ImageView arrow;

    @FXML
    private Label buttonText;

    //Used to show current city. Defaults to Chicago
    public static String currentCityString = "Chicago";

    //Function made to change the weather icon in Scene 1
    private void setWeatherImage(String shortForecast) {
        if (shortForecast.contains("Sunny")) {
            S1WeatherImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/sun-light.png"))));
        } else if (shortForecast.contains("Cloudy")) {
            S1WeatherImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/cloud.png"))));
        } else if (shortForecast.contains("Rain")) {
            S1WeatherImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/rain.png"))));
        } else if (shortForecast.contains("Snow")) {
            S1WeatherImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/snow.png"))));
        } else if (shortForecast.contains("Thunderstorm")) {
            S1WeatherImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/thunderstorm.png"))));
        } else {
            S1WeatherImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/cloud-sunny.png"))));
        }
    }

    //Function made to set Scene 1 Values
    public void setValuesTodayForecast() {
        if (MyWeatherAPI.lastForecastHourly == null) {
            MyWeatherAPI.lastForecastHourly = MyWeatherAPI.getPointForecastHourly(currentCityString);
        }
        ArrayList<HourlyPeriod> pointsHourly = MyWeatherAPI.lastForecastHourly; //avoids extra API call when switching scenes
        ZoneId timeZone = ZoneId.of(MyWeatherAPI.timeZone);
        time.setText(LocalTime.now(timeZone).format(DateTimeFormatter.ofPattern("h:mm a")));
        temperature.setText(pointsHourly.get(0).temperature +"F");
        city.setText(MyWeatherAPI.cityName);
        weather.setText(pointsHourly.get(0).shortForecast);
        rainChance.setText("Chance of precipitation: " + String.valueOf(pointsHourly.get(0).probabilityOfPrecipitation.value) + "%");
        setWeatherImage(pointsHourly.get(0).shortForecast);
        setHourlyLabels(pointsHourly);
    }

    //Function made to set Scene 2 Values
    public void setTextFutureForecast() {
        if (MyWeatherAPI.lastForecast == null) {
            MyWeatherAPI.lastForecast = MyWeatherAPI.getPointForecast(currentCityString);
        }
        ArrayList<Period> newCity = MyWeatherAPI.lastForecast; //avoids extra API call when switching scenes
        int startPoint;
        if (!newCity.get(0).isDaytime) {
            startPoint = 1;
        } else {
            startPoint = 2;
        }
        Label[] dateLabels =    {FTRD1,  FTRD2,  FTRD3,  FTRD4,  FTRD5,  FTRD6};
        Label[] morningLabels = {FTRMT1, FTRMT2, FTRMT3, FTRMT4, FTRMT5, FTRMT6};
        Label[] nightLabels =   {FTRNT1, FTRNT2, FTRNT3, FTRNT4, FTRNT5, FTRNT6};
        Label[] windLabels =    {FTRWS1, FTRWS2, FTRWS3, FTRWS4, FTRWS5, FTRWS6};
        Label[] chanceLabels =  {FTRRC1, FTRRC2, FTRRC3, FTRRC4, FTRRC5, FTRRC6};
        ImageView[] precipIcons = {FuturePrecipIconOne, FuturePrecipIconTwo, FuturePrecipIconThree,
                FuturePrecipIconFour, FuturePrecipIconFive, FuturePrecipIconSix};
        ImageView[] dayIcons = {FutureDayTempOne, FutureDayTempTwo, FutureDayTempThree, FutureDayTempFour, FutureDayTempFive, FutureDayTempSix};

        FtrForecastCity.setText("6 Day Forecast for "+ MyWeatherAPI.cityName);
        ZoneId zone = ZoneId.of(MyWeatherAPI.timeZone);
        for (int i = 0; i < 6; i++) {
            dateLabels[i].setText(LocalDate.now(zone).plusDays(i+1).format(DateTimeFormatter.ofPattern("MMMM d")));
            morningLabels[i].setText(newCity.get(startPoint + i * 2).temperature + "F");
            nightLabels[i].setText(newCity.get(startPoint + i * 2 + 1).temperature + "F");
            windLabels[i].setText(newCity.get(startPoint +i*2).windSpeed+" "+newCity.get(startPoint +i*2).windDirection);
            chanceLabels[i].setText(newCity.get(startPoint + i * 2).probabilityOfPrecipitation.value + "%");
            if (newCity.get(i).shortForecast.contains("Snow") || newCity.get(i).temperature<=32) {
                precipIcons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/snow-flake.png"))));
            }
            else {
                precipIcons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/droplet.png"))));
            }
            if (newCity.get(i).shortForecast.contains("Sunny")) {
                dayIcons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/sun-light.png"))));
            } else if (newCity.get(i).shortForecast.contains("Cloudy")) {
                dayIcons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/cloud.png"))));
            } else if (newCity.get(i).shortForecast.contains("Rain")) {
                dayIcons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/rain.png"))));
            } else if (newCity.get(i).shortForecast.contains("Snow")) {
                dayIcons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/snow.png"))));
            } else if (newCity.get(i).shortForecast.contains("Thunderstorm")) {
                dayIcons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/thunderstorm.png"))));
            } else {
                dayIcons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/cloud-sunny.png"))));
            }
        }
    }

    //Function made to change visibilities upon nullCity in Scene 1. Saves a lot of lines of code
    public void setVisibleUponSearch(boolean setVisi) {
        Label[] visiLabels = {temperature, weather, rainChance,
                dailyTimeOne, dailyTimeTwo, dailyTimeThree,
                dailyTimeFour, dailyTimeFive, dailyTimeSix,
                dailyTemperatureOne, dailyTemperatureTwo, dailyTemperatureThree,
                dailyTemperatureFour, dailyTemperatureFive, dailyTemperatureSix, time,
                dailyPercipitationOne, dailyPercipitationTwo, dailyPercipitationThree,
                dailyPercipitationFour, dailyPercipitationFive, dailyPercipitationSix};

        ImageView[] visiImageView = {S1WeatherImage, HR1Icon, HR2Icon, HR3Icon, HR4Icon, HR5Icon, HR6Icon};

        for (Label l : visiLabels) {
            l.setVisible(setVisi);
        }

        for (ImageView I : visiImageView) {
            I.setVisible(setVisi);
        }
    }
    //Disables visibility during loading for visual clarity and feedback
    public void toggleLoadingVisibilityToday() {
        Label[] visiLabels = {temperature, weather, rainChance,
                dailyTimeOne, dailyTimeTwo, dailyTimeThree,
                dailyTimeFour, dailyTimeFive, dailyTimeSix,
                dailyTemperatureOne, dailyTemperatureTwo, dailyTemperatureThree,
                dailyTemperatureFour, dailyTemperatureFive, dailyTemperatureSix, time,
                dailyPercipitationOne, dailyPercipitationTwo, dailyPercipitationThree,
                dailyPercipitationFour, dailyPercipitationFive, dailyPercipitationSix,
                city, buttonText};

        ImageView[] visiImageView = {S1WeatherImage, HR1Icon, HR2Icon, HR3Icon, HR4Icon, HR5Icon, HR6Icon, searchIcon, arrow};

        for (Label l : visiLabels) {
            l.setVisible(false);
        }

        for (ImageView I : visiImageView) {
            I.setVisible(false);
        }

        switchSceneButton.setVisible(false);
        cityInput.setVisible(false);
    }

    //Function made to change visibilities upon nullCity in Scene 2
    private void setFutureVisibility(boolean setVisi) {
        Label[] visiLabelsFuture = {FTRD1,  FTRD2,  FTRD3,  FTRD4,  FTRD5,  FTRD6,
                FTRMT1, FTRMT2, FTRMT3, FTRMT4, FTRMT5, FTRMT6,
                FTRNT1, FTRNT2, FTRNT3, FTRNT4, FTRNT5, FTRNT6,
                FTRWS1, FTRWS2, FTRWS3, FTRWS4, FTRWS5, FTRWS6,
                FTRRC1, FTRRC2, FTRRC3, FTRRC4, FTRRC5, FTRRC6};

        ImageView[] visiImageView = {FuturePrecipIconOne, FuturePrecipIconTwo, FuturePrecipIconThree,
                FuturePrecipIconFour, FuturePrecipIconFive, FuturePrecipIconSix,
                FutureDayTempOne, FutureDayTempTwo, FutureDayTempThree,
                FutureDayTempFour, FutureDayTempFive, FutureDayTempSix,
                FutureNightTempOne, FutureNightTempTwo, FutureNightTempThree,
                FutureNightTempFour, FutureNightTempFive, FutureNightTempSix,
                FutureWindOne, FutureWindTwo, FutureWindThree,
                FutureWindFour, FutureWindFive, FutureWindSix,
                arrow};

        for (Label l : visiLabelsFuture) {
            l.setVisible(setVisi);
        }

        for (ImageView I : visiImageView) {
            I.setVisible(setVisi);
        }
    }

    //Disables visibility during loading for visual clarity and feedback
    private void toggleLoadingVisibilityFuture() {
        Label[] visiLabelsFuture = {FTRD1,  FTRD2,  FTRD3,  FTRD4,  FTRD5,  FTRD6,
                FTRMT1, FTRMT2, FTRMT3, FTRMT4, FTRMT5, FTRMT6,
                FTRNT1, FTRNT2, FTRNT3, FTRNT4, FTRNT5, FTRNT6,
                FTRWS1, FTRWS2, FTRWS3, FTRWS4, FTRWS5, FTRWS6,
                FTRRC1, FTRRC2, FTRRC3, FTRRC4, FTRRC5, FTRRC6,
                FtrForecastCity, buttonText};

        ImageView[] visiImageView = {FuturePrecipIconOne, FuturePrecipIconTwo, FuturePrecipIconThree,
                FuturePrecipIconFour, FuturePrecipIconFive, FuturePrecipIconSix,
                FutureDayTempOne, FutureDayTempTwo, FutureDayTempThree,
                FutureDayTempFour, FutureDayTempFive, FutureDayTempSix,
                FutureNightTempOne, FutureNightTempTwo, FutureNightTempThree,
                FutureNightTempFour, FutureNightTempFive, FutureNightTempSix,
                FutureWindOne, FutureWindTwo, FutureWindThree,
                FutureWindFour, FutureWindFive, FutureWindSix,
                searchIcon, arrow};

        for (Label l : visiLabelsFuture) {
            l.setVisible(false);
        }

        for (ImageView I : visiImageView) {
            I.setVisible(false);
        }

        switchSceneButton.setVisible(false);
        cityInput.setVisible(false);
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
        Label[] percipLabels = {dailyPercipitationOne, dailyPercipitationTwo, dailyPercipitationThree,
                dailyPercipitationFour, dailyPercipitationFive, dailyPercipitationSix};
        ImageView[] percipIcons = {HR1Icon,HR2Icon,HR3Icon,HR4Icon,HR5Icon,HR6Icon};

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
                alignedIndex = i;
                break;
            }
        }

        int loopFinal = alignedIndex + 15;
        int timeLabelIndex = 0;
        int tempLabelIndex = 0;
        int percipLabelIndex = 0;

        for (int i = alignedIndex; i <= loopFinal; i+=3) {
            int pointsHour = pointsHourly.get(i).startTime.toInstant().atZone(ZoneId.of(MyWeatherAPI.timeZone)).getHour();
            timeLabels[timeLabelIndex].setText(formatTimeLabelText(pointsHour));
            tempLabels[tempLabelIndex].setText(pointsHourly.get(i).temperature + "F");
            percipLabels[percipLabelIndex].setText(pointsHourly.get(i).probabilityOfPrecipitation.value + "%");
            if (pointsHourly.get(i).shortForecast.contains("Snow") || pointsHourly.get(i).temperature<=32) {
                percipIcons[percipLabelIndex].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/WeatherIcons/snow-flake.png"))));
            }
            else {
                percipIcons[percipLabelIndex].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FXML/Images/droplet.png"))));

            }
            timeLabelIndex++;
            tempLabelIndex++;
            percipLabelIndex++;
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
        toggleLoadingVisibilityToday();
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
        toggleLoadingVisibilityFuture();
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
                MyWeatherAPI.lastForecast = null; //this is done so search makes a new API call
                MyWeatherAPI.lastForecastHourly = null;
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
        MyWeatherAPI.lastForecast = null; //this is done so search makes a new API call
        MyWeatherAPI.lastForecastHourly = null;
        setFutureVisibility(true);
        if (FTRD1!= null) { //checks if in Scene 2
            setTextFutureForecast();
        }
    }
}
