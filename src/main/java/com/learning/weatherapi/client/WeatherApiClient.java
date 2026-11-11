
package com.learning.weatherapi.client;

import com.learning.weatherapi.dto.external.ForecastResponse;
import com.learning.weatherapi.dto.external.GeocodingResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WeatherApiClient {

    private final RestClient geocodingClient;
    private final RestClient forecastClient;

    public WeatherApiClient(RestClient.Builder builder) {

        this.geocodingClient = builder.clone()
                .baseUrl("https://geocoding-api.open-meteo.com")
                .build();

        this.forecastClient = builder.clone()
                .baseUrl("https://api.open-meteo.com")
                .build();
    }

    public GeocodingResponse searchCity(String city) {

        return geocodingClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search")
                        .queryParam("name", city)
                        .queryParam("count", 1)
                        .queryParam("language", "en")
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(GeocodingResponse.class);
    }

    public ForecastResponse getCurrentWeather(
            double latitude,
            double longitude) {

        return forecastClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam(
                                "current",
                                "temperature_2m,relative_humidity_2m,"
                                        + "wind_speed_10m,weather_code"
                        )
                        .queryParam("timezone", "auto")
                        .build())
                .retrieve()
                .body(ForecastResponse.class);
    }
}
