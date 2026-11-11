
package com.learning.weatherapi.dto.external;

public record ForecastResponse(
        Current current
) {
    public record Current(
            double temperature_2m,
            int relative_humidity_2m,
            double wind_speed_10m,
            int weather_code
    ) {
    }
}
