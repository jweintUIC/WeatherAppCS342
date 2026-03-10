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
    //this is up here so it can be accessed outside the class
    public static String cityName = "";
    public static String timeZone = "";

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
            ObjectMapper om = new ObjectMapper();
            JsonNode root = om.readTree(response.body());
            String region = root.get("properties").get("gridId").asText();
            int gridX = root.get("properties").get("gridX").asInt();
            int gridY = root.get("properties").get("gridY").asInt();
            System.out.println(region);
            System.out.println(gridX);
            System.out.println(gridY);

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

            ObjectMapper om = new ObjectMapper();
            JsonNode root = om.readTree(geoResponse.body());
            JsonNode results = root.get("results");

            JsonNode city = null;

            //This is to make sure it is a US city as our weatherAPI only collects from the US
            for (JsonNode r : results) {
                if (r.get("country_code").asText().equals("US")) {
                    city = r;
                    break;
                }
            }

            if (city == null) {
                System.err.println("Failed to parse JSon");
                return null;
            }
            //Gets the offical city name so format is the same
            //for example corrects inputted "NYC" to "New York, New York"
            cityName= city.get("name").asText() + ", " + city.get("admin1").asText() + " ";
            timeZone = city.get("timezone").asText();
            double lat = city.get("latitude").asDouble();
            double lon = city.get("longitude").asDouble();

            return getPointForecast(lat, lon);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}