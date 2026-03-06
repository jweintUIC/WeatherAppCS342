import weather.Period;
import weather.WeatherAPI;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyWeatherAPI extends WeatherAPI {

    public static ArrayList<Period> getPointForecast(double lat, double lon) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.gov/points/" + lat + "," + lon))
                .build();
        HttpResponse<String> response = null;
        try {

            response =
                    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode props = root.get("properties");
            String region = props.get("gridId").asText();
            int gridX = props.get("gridX").asInt();
            int gridY = props.get("gridY").asInt();

            return WeatherAPI.getForecast(region, gridX, gridY);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}