
package com.learning.weatherapi.controller;

import com.learning.weatherapi.client.WeatherApiClient;
import com.learning.weatherapi.dto.external.ForecastResponse;
import com.learning.weatherapi.dto.external.GeocodingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherApiClient weatherApiClient;

    @Test
    void shouldReturnWeatherForValidCity() throws Exception {

        when(weatherApiClient.searchCity("Hyderabad"))
                .thenReturn(
                        new GeocodingResponse(
                                List.of(
                                        new GeocodingResponse.Location(
                                                "Hyderabad",
                                                17.3850,
                                                78.4867,
                                                "India"
                                        )
                                )
                        )
                );

        when(weatherApiClient.getCurrentWeather(
                17.3850, 78.4867
        )).thenReturn(
                new ForecastResponse(
                        new ForecastResponse.Current(
                                29.5, 65, 12.5, 2
                        )
                )
        );

        mockMvc.perform(get("/api/weather/Hyderabad"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.city").value("Hyderabad"))
                .andExpect(jsonPath("$.temperature").value(29.5))
                .andExpect(jsonPath("$.humidity").value(65))
                .andExpect(jsonPath("$.windSpeed").value(12.5))
                .andExpect(jsonPath("$.condition").value("Partly cloudy"));
    }

    @Test
    void shouldReturn404WhenCityNotFound() throws Exception {

        when(weatherApiClient.searchCity("Atlantis"))
                .thenReturn(new GeocodingResponse(List.of()));

        mockMvc.perform(get("/api/weather/Atlantis"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("City not found: Atlantis"));
    }

    @Test
    void shouldReturn400ForInvalidCity() throws Exception {

        mockMvc.perform(get("/api/weather/Hyderabad123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Bad Request"));
    }

    @Test
    void shouldReturn503WhenExternalApiFails() throws Exception {

        when(weatherApiClient.searchCity("Hyderabad"))
                .thenThrow(
                        new RestClientException("Connection failed")
                );

        mockMvc.perform(get("/api/weather/Hyderabad"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503));
    }
}
