
package com.learning.weatherapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI weatherApiDocumentation() {

        return new OpenAPI()
                .info(new Info()
                        .title("Weather API")
                        .description(
                                "REST API for retrieving current weather "
                                        + "information for cities."
                        )
                        .version("1.0.0"));
    }
}
