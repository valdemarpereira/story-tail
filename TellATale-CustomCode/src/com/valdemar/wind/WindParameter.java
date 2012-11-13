package com.valdemar.wind;

import java.util.HashMap;
import java.util.Map;

public class WindParameter {

	private int windSpeed;
	private int windDirection;
	private String errorMsg;
	
	public int getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}
	public int getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(int windDirection) {
		this.windDirection = windDirection;
	}
	
	public void setCouldNotRetrieveValues() {
		windSpeed = -1;
		windDirection = -1;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Map<String, ?> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("WindSpeed", Integer.toString(windSpeed));
		map.put("WindDirection", Integer.toString(windDirection));
		map.put("errorMsg", errorMsg);
		
		return map;
	}
}
