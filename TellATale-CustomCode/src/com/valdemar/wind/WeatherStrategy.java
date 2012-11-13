package com.valdemar.wind;

import com.stackmob.sdkapi.http.HttpService;

public class WeatherStrategy {

	private IWeatherWindStrategy strategy;
	private HttpService httpService;
	 
    // Constructor
    public WeatherStrategy(IWeatherWindStrategy strategy, HttpService httpService) {
        this.strategy = strategy;
        this.httpService = httpService;
    }
 
    public WindParameter executeStrategy() {
        return strategy.getWindParameter(httpService);
    }
    
}
