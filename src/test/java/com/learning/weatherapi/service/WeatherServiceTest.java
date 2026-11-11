
package com.learning.weatherapi.service;

import com.learning.weatherapi.client.WeatherApiClient;
import com.learning.weatherapi.dto.WeatherResponseDTO;
import com.learning.weatherapi.dto.external.ForecastResponse;
import com.learning.weatherapi.dto.external.GeocodingResponse;
import com.learning.weatherapi.exception.CityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherApiClient weatherApiClient;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void shouldReturnWeatherForValidCity() {

        // Arrange: Prepare mock geocoding response.
        GeocodingResponse geocodingResponse =
                new GeocodingResponse(
                        List.of(
                                new GeocodingResponse.Location(
                                        "Hyderabad",
                                        17.3850,
                                        78.4867,
                                        "India"
                                )
                        )
                );

        when(weatherApiClient.searchCity("Hyderabad"))
                .thenReturn(geocodingResponse);

        // Arrange: Prepare mock forecast response.
        ForecastResponse forecastResponse =
                new ForecastResponse(
                        new ForecastResponse.Current(
                                29.5,
                                65,
                                12.5,
                                2
                        )
                );

        when(weatherApiClient.getCurrentWeather(
                17.3850, 78.4867
        )).thenReturn(forecastResponse);

        // Act: Execute the business logic.
        WeatherResponseDTO response =
                weatherService.getWeather("Hyderabad");

        // Assert: Verify the expected result.
        assertNotNull(response);
        assertEquals("Hyderabad", response.getCity());
        assertEquals(29.5, response.getTemperature(), 0.001);
        assertEquals(65, response.getHumidity());
        assertEquals(12.5, response.getWindSpeed(), 0.001);
        assertEquals("Partly cloudy", response.getCondition());

        verify(weatherApiClient).searchCity("Hyderabad");
        verify(weatherApiClient).getCurrentWeather(
                17.3850, 78.4867
        );
    }

    @Test
    void shouldThrowExceptionWhenCityNotFound() {

        when(weatherApiClient.searchCity("Atlantis"))
                .thenReturn(new GeocodingResponse(List.of()));

        assertThrows(
                CityNotFoundException.class,
                () -> weatherService.getWeather("Atlantis")
        );

        verify(weatherApiClient).searchCity("Atlantis");

        verify(weatherApiClient, never())
                .getCurrentWeather(anyDouble(), anyDouble());
    }

    @Test
    void shouldThrowExceptionForInvalidCityName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> weatherService.getWeather("Hyderabad123")
        );

        verifyNoInteractions(weatherApiClient);
    }

    @Test
    void shouldThrowExceptionWhenExternalApiFails() {

        when(weatherApiClient.searchCity("Hyderabad"))
                .thenThrow(
                        new RestClientException("Connection failed")
                );

        assertThrows(
                RestClientException.class,
                () -> weatherService.getWeather("Hyderabad")
        );
    }
}
