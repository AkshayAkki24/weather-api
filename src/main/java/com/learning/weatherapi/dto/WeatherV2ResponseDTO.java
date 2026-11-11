
package com.learning.weatherapi.dto;

public record WeatherV2ResponseDTO(
        String city,
        double temperature,
        String temperatureUnit,
        int humidity,
        double windSpeed,
        String windSpeedUnit,
        String condition,
        String provider
) {
}
