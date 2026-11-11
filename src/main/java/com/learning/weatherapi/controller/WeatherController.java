package com.learning.weatherapi.controller;

import com.learning.weatherapi.dto.WeatherResponseDTO;
import com.learning.weatherapi.service.WeatherService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api/v1/weather")
@Tag(
        name = "Weather API",
        description = "Operations for retrieving weather information"
)
@Validated
public class WeatherController {

    private static final Logger log =
            LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Operation(
            summary = "Get current weather by city",
            description = "Retrieves current temperature, humidity, "
                    + "wind speed and weather condition for the given city."
    )
    @GetMapping("/{city}")
    public WeatherResponseDTO getWeather(
            @PathVariable
            @NotBlank(message = "City name cannot be empty")
            @Pattern(
                    regexp = "^[a-zA-Z][a-zA-Z\\s'-]*$",
                    message = "City name contains invalid characters"
            )
            String city) {

        log.info("Received weather request for city: {}", city);

        WeatherResponseDTO response = weatherService.getWeather(city);

        log.info("Successfully processed weather request for city: {}", city);

        return response;
    }
}
