package com.valdemar.wind;

import com.stackmob.sdkapi.http.HttpService;

public interface IWeatherWindStrategy {
	
	public WindParameter getWindParameter(HttpService httpService);

}
