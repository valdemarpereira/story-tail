package com.valdemar.tellatale.model;

import android.location.Location;

public class GPSCoord {
	private Double lon;
	private Double lat;

	public GPSCoord() {
	}

	public GPSCoord(Location location) {
		lon = location.getLongitude();
		lat = location.getLatitude();
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

}
