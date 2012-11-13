package com.valdemar.wind;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.stackmob.core.rest.ResponseToProcess;
import com.stackmob.sdkapi.http.HttpService;
import com.stackmob.sdkapi.http.exceptions.AccessDeniedException;
import com.stackmob.sdkapi.http.exceptions.TimeoutException;
import com.stackmob.sdkapi.http.request.GetRequest;
import com.stackmob.sdkapi.http.response.HttpResponse;

public class YahooWeatherStrategy implements IWeatherWindStrategy {

	@Override
	public WindParameter getWindParameter(HttpService httpService) {

		WindParameter wParameter = new WindParameter();

		// create the HTTP request
		String url = "http://weather.yahooapis.com/forecastjson?w=2502265";
		GetRequest req;

		try {
			req = new GetRequest(url);

			HttpResponse resp = httpService.get(req);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("response_code", resp.getCode());
			map.put("url", url);

			String body = resp.getBody();

			Gson gson = new Gson();

			YahooWeather ya = gson.fromJson(body, YahooWeather.class);   
			
			wParameter.setWindDirection(ya.getWind().getWindDirection());
			wParameter.setWindSpeed(ya.getWind().getSpeed());

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AccessDeniedException e) {
			wParameter.setCouldNotRetrieveValues();
			wParameter.setErrorMsg("AccessDeniedException");

		} catch (TimeoutException e) {
			wParameter.setCouldNotRetrieveValues();
			wParameter.setErrorMsg("TimeoutException");
		} 

		return wParameter;

	}
	
	class YahooWeather{
		
		Wind wind;

		public Wind getWind() {
			return wind;
		}

		public void setWind(Wind wind) {
			this.wind = wind;
		}
	}
	
	class Wind{
		
		private int speed;
		private String direction;

		public int getSpeed() {
			return speed;
		}
		public int getWindDirection() {
			// TODO Auto-generated method stub
			return 200;
		}
		public void setSpeed(int speed) {
			this.speed = speed;
		}
		public String getDirection() {
			return direction;
		}
		public void setDirection(String direction) {
			this.direction = direction;
		}
	}

}
