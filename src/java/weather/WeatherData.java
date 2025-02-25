package weather;
import com.google.gson.annotations.SerializedName;
import java.util.List;

//https://jsonformatter.org/json-to-java
public class WeatherData {
    public double latitude;
    public double longitude;
    public double generationtime_ms;
    public int utc_offset_seconds;
    public String timezone;
    public String timezone_abbreviation;
    public double elevation;
    public DailyUnits daily_units;
    public Daily daily;

    public static class DailyUnits {
        public String time;
        @SerializedName("temperature_2m_max")
        public String temperature2mMax;
        @SerializedName("temperature_2m_min")
        public String temperature2mMin;
        public String sunrise;
        public String sunset;
        @SerializedName("uv_index_max")
        public String uvIndexMax;
        @SerializedName("precipitation_hours")
        public String precipitationHours;
    }

    public static class Daily {
        public List<String> time;
        @SerializedName("temperature_2m_max")
        public List<Double> temperature2mMax;
        @SerializedName("temperature_2m_min")
        public List<Double> temperature2mMin;
        public List<String> sunrise;
        public List<String> sunset;
        @SerializedName("uv_index_max")
        public List<Double> uvIndexMax;
        @SerializedName("precipitation_hours")
        public List<Double> precipitationHours;
    }
}
