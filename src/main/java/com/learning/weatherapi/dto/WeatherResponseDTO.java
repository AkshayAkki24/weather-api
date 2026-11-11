package com.learning.weatherapi.dto;

public class WeatherResponseDTO {

    private String city;
    private double temperature;
    private int humidity;
    private double windSpeed;
    private String condition;

    public WeatherResponseDTO(String city, double temperature, int humidity, double windSpeed, String condition) {
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.condition = condition;
    }

    public String getCity() {
        return city;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getCondition() {
        return condition;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
