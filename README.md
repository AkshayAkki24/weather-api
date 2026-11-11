# Weather API

A RESTful Weather API built using Java 25 and Spring Boot 4. It retrieves real-time weather information for cities using the Open-Meteo external API.

## Features

- Retrieve current weather by city name.
- Integrate with Open-Meteo Geocoding and Forecast APIs.
- REST API versioning (Legacy, V1, V2).
- Input validation.
- Centralized exception handling.
- Structured error responses.
- Logging using SLF4J.
- External API timeout configuration.
- Unit tests using JUnit 5 and Mockito.
- Integration tests using Spring Boot testing.
- API documentation using Swagger/OpenAPI.
- Docker containerization.

## Technology Stack

| Technology | Version / Usage |
|---|---|
| Java | 25 |
| Spring Boot | 4.1.2 |
| Maven | Build management |
| Spring RestClient | External API communication |
| JUnit 5 | Unit testing |
| Mockito | Mocking |
| Springdoc OpenAPI | API documentation |
| Docker | Containerization |
| Open-Meteo | Weather data provider |

## API Endpoints

Base URL: `http://localhost:8024`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/weather/{city}` | Legacy weather endpoint |
| GET | `/api/v1/weather/{city}` | Weather API V1 |
| GET | `/api/v2/weather/{city}` | Weather API V2 |

### Example Request

```http
GET http://localhost:8024/api/v2/weather/Hyderabad
```

### Example Response

```json
{
  "city": "Hyderabad",
  "temperature": 30.5,
  "temperatureUnit": "°C",
  "humidity": 65,
  "windSpeed": 12.4,
  "windSpeedUnit": "km/h",
  "condition": "Partly cloudy",
  "provider": "Open-Meteo"
}
```

*Example response values are illustrative.*

## Swagger Documentation

After starting the application, access:

- Swagger UI: `http://localhost:8024/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8024/v3/api-docs`

## Run Locally

Clone the repository and navigate to the project directory.

Build:

```bash
./mvnw clean package
```

Run:

```bash
./mvnw spring-boot:run
```

The application starts on port `8024`.

## Run Tests

```bash
./mvnw test
```

## Docker

Build Docker image:

```bash
docker build -t weather-api:1.0 .
```

Run container:

```bash
docker run -d \
  --name weather-api-container \
  -p 8024:8024 \
  weather-api:1.0
```

Check running containers:

```bash
docker ps
```

View logs:

```bash
docker logs weather-api-container
```

Stop container:

```bash
docker stop weather-api-container
```

## External API

This project uses Open-Meteo:

- Geocoding: `https://geocoding-api.open-meteo.com`
- Forecast: `https://api.open-meteo.com`

No API key is required for the endpoints used in this project.

## Project Structure

```text
weather-api/
├── src/
│   ├── main/
│   │   ├── java/com/learning/weatherapi/
│   │   │   ├── client/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   └── service/
│   │   └── resources/
│   └── test/
├── Dockerfile
├── .dockerignore
├── pom.xml
├── mvnw
└── README.md
```

## Future Improvements

- Add weather forecast for multiple days.
- Add caching using Redis.
- Add API rate limiting.
- Add health checks and monitoring.
- Deploy the application to a cloud platform.