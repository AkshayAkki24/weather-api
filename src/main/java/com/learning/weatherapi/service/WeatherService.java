
package com.learning.weatherapi.service;

import com.learning.weatherapi.client.WeatherApiClient;
import com.learning.weatherapi.dto.WeatherResponseDTO;
import com.learning.weatherapi.dto.external.ForecastResponse;
import com.learning.weatherapi.dto.external.GeocodingResponse;
import com.learning.weatherapi.exception.CityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private static final Logger log =
            LoggerFactory.getLogger(WeatherService.class);

    private final WeatherApiClient weatherApiClient;

    public WeatherService(WeatherApiClient weatherApiClient) {
        this.weatherApiClient = weatherApiClient;
    }

    public WeatherResponseDTO getWeather(String city) {
        log.info("Fetching weather for city: {}", city);
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException(
                    "City name cannot be empty"
            );
        }

        String cityName = city.trim();

        if (!cityName.matches("[a-zA-Z][a-zA-Z\\s'-]*")) {
            throw new IllegalArgumentException(
                    "City name contains invalid characters"
            );
        }

        // Step 1: Find the city's latitude and longitude.
        GeocodingResponse geocodingResponse =
                weatherApiClient.searchCity(cityName);

        if (geocodingResponse == null
                || geocodingResponse.results() == null
                || geocodingResponse.results().isEmpty()) {

            throw new CityNotFoundException(
                    "City not found: " + cityName
            );
        }
        log.info("Found city: {}", cityName);

        GeocodingResponse.Location location =
                geocodingResponse.results().getFirst();

        // Step 2: Fetch current weather using coordinates.
        ForecastResponse forecastResponse =
                weatherApiClient.getCurrentWeather(
                        location.latitude(),
                        location.longitude()
                );

        if (forecastResponse == null
                || forecastResponse.current() == null) {
            throw new IllegalStateException(
                    "Weather information is currently unavailable"
            );
        }
        log.info("Fetched weather for city: {}", cityName);

        ForecastResponse.Current current =
                forecastResponse.current();

        // Step 3: Convert external API response to our DTO.
        return new WeatherResponseDTO(
                location.name(),
                current.temperature_2m(),
                current.relative_humidity_2m(),
                current.wind_speed_10m(),
                getWeatherCondition(current.weather_code())
        );
    }

    private String getWeatherCondition(int weatherCode) {

        return switch (weatherCode) {
            case 0 -> "Clear sky";
            case 1 -> "Mainly clear";
            case 2 -> "Partly cloudy";
            case 3 -> "Overcast";
            case 45, 48 -> "Fog";
            case 51, 53, 55 -> "Drizzle";
            case 56, 57 -> "Freezing drizzle";
            case 61, 63, 65 -> "Rain";
            case 66, 67 -> "Freezing rain";
            case 71, 73, 75, 77 -> "Snow";
            case 80, 81, 82 -> "Rain showers";
            case 85, 86 -> "Snow showers";
            case 95, 96, 99 -> "Thunderstorm";
            default -> "Unknown";
        };
    }
}
