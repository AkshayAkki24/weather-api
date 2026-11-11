
package com.learning.weatherapi.controller;

import com.learning.weatherapi.dto.WeatherResponseDTO;
import com.learning.weatherapi.dto.WeatherV2ResponseDTO;
import com.learning.weatherapi.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/weather")
@Tag(
        name = "Weather API V2",
        description = "Enhanced weather API version 2"
)
public class WeatherV2Controller {

    private final WeatherService weatherService;

    public WeatherV2Controller(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    @Operation(
            summary = "Get weather using API version 2",
            description = "Returns weather information with units "
                    + "and provider details."
    )
    public WeatherV2ResponseDTO getWeather(@PathVariable String city) {

        WeatherResponseDTO weather =
                weatherService.getWeather(city);

        return new WeatherV2ResponseDTO(
                weather.getCity(),
                weather.getTemperature(),
                "°C",
                weather.getHumidity(),
                weather.getWindSpeed(),
                "km/h",
                weather.getCondition(),
                "Open-Meteo"
        );
    }
}
