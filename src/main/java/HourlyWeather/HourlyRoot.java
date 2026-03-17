package HourlyWeather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import weather.Geometry;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HourlyRoot {
    public String type;
    public Geometry geometry;
    public HourlyProperties properties;
}