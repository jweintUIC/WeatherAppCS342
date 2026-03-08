import weather.Period;
import weather.WeatherAPI;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.net.URLEncoder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyWeatherAPI extends WeatherAPI {

    public static String cityName = "";

    public static ArrayList<Period> getPointForecast(double lat, double lon) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.gov/points/" + lat + "," + lon))
                .build();
        HttpResponse<String> response = null;
        try {

            response = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
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
    public static ArrayList<Period> getPointForecast(String locationName) {
        try {
            HttpRequest geoRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://geocoding-api.open-meteo.com/v1/search?name=" +
                            URLEncoder.encode(locationName, "UTF-8") + "&count=10"))
                    .build();

            HttpResponse<String> geoResponse = HttpClient.newHttpClient()
                    .send(geoRequest, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(geoResponse.body());
            JsonNode results = root.get("results");

            JsonNode result = null;

            for (JsonNode r : results) {
                if (r.get("country_code").asText().equals("US")) {
                    result = r;
                    break;
                }
            }

            if (result == null) {
                System.err.println("Failed to parse JSon");
                return null;
            }
            cityName= result.get("name").asText() + ", " + result.get("admin1").asText() + " ";
            double lat = result.get("latitude").asDouble();
            double lon = result.get("longitude").asDouble();

            return getPointForecast(lat, lon);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}