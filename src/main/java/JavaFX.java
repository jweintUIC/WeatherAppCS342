import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import weather.Period;
import weather.WeatherAPI;

import java.util.ArrayList;

public class JavaFX extends Application {
	TextField  searchField;
	BorderPane s1bp;
	VBox infoBox, sideBar, fullVbox;
	HBox s1hb, topBar;
	Label title, city, rainChance, temperature,weather;
	Button homeBtn, forecastBtn;
	public static void main(String[] args) {
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("I'm a professional Weather App!");
		ArrayList<Period> forecast = WeatherAPI.getForecast("LOT",77,70);
		ArrayList<Period> points = MyWeatherAPI.getPointForecast("Chicago");

		if (forecast == null){
			throw new RuntimeException("Forecast did not load");
		}
		//Scene 1 Labels
		temperature = new Label();
		weather = new Label();
		city = new Label();
		title = new Label();
		rainChance = new Label();

		//Scene 1 Label Set Text
		temperature.setText("Today's weather is: "+String.valueOf(points.get(0).temperature));
        weather.setText(points.get(0).shortForecast);
		city.setText(MyWeatherAPI.cityName);
		title.setText("Weather App");
		rainChance.setText("The chance of rain is " +String.valueOf(points.get(0).probabilityOfPrecipitation.value)+"%");

		//Scene 1 Text Field
		searchField = new TextField();
		searchField.setPromptText("Search for a city...");
		searchField.setMinWidth(500);
		searchField.setOnAction(e -> {
			ArrayList<Period> newCity = MyWeatherAPI.getPointForecast(searchField.getText());
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
			temperature.setText("Today's weather is: " + String.valueOf(newCity.get(0).temperature));
			weather.setText(newCity.get(0).shortForecast);
			rainChance.setText("The chance of rain is " + String.valueOf(newCity.get(0).probabilityOfPrecipitation.value) + "%");
			}
		});

		//Scene 1 Buttons
		homeBtn = new Button("Home");
		forecastBtn = new Button("Forecast");

		//VBox, HBox, BorderPanes
		topBar = new HBox(20,title, searchField);
		topBar.setPadding(new Insets(10));
		sideBar = new VBox(5,homeBtn,forecastBtn);
		sideBar.setPadding(new Insets(10));
		infoBox = new VBox(20,city, temperature, weather,rainChance);
		s1hb = new HBox(200,sideBar, infoBox);
		s1bp = new BorderPane();
		fullVbox = new VBox (20, topBar,s1hb);
		s1bp.setCenter(fullVbox);

		//SetScene
		Scene scene = new Scene(s1bp, 700,700);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
