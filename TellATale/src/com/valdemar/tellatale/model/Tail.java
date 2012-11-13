package com.valdemar.tellatale.model;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.stackmob.sdk.model.StackMobModel;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class Tail  extends StackMobModel
{

	public static final String OBJECT_NAME = "tail";
	public static final String FIELD_TALE = "tale";

	private String city;
	private String createdby;
	private String tail_id;
	private String country;
	private Long lastmoddate;
	private GPSCoord coord;
	private String story;
	private Long createddate;
	private String tale;

	public Tail()
	{
		super(Tail.class);
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getCreatedby()
	{
		return createdby;
	}

	public void setCreatedby(String createdby)
	{
		this.createdby = createdby;
	}

	public String getTail_id()
	{
		return tail_id;
	}

	public void setTail_id(String tail_id)
	{
		this.tail_id = tail_id;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public Long getLastmoddate()
	{
		return lastmoddate;
	}

	public void setLastmoddate(Long lastmoddate)
	{
		this.lastmoddate = lastmoddate;
	}

	public GPSCoord getCoord()
	{
		return coord;
	}

	public void setCoord(GPSCoord coord)
	{
		this.coord = coord;
	}

	public String getStory()
	{
		return story;
	}

	public void setStory(String story)
	{
		this.story = story;
	}

	public Long getCreateddate()
	{
		return createddate;
	}

	public void setCreateddate(Long createddate)
	{
		this.createddate = createddate;
	}

	public String getTale()
	{
		return tale;
	}

	public void setTale(String tale)
	{
		this.tale = tale;
	}

	public String getFormatedDate()
	{
		if (createddate == null)
			return "";

		String date = DateFormat.getDateTimeInstance().format(new Date(createddate));
		return date;
	}

	private String fullPlaceBaseOnGPS = null;
	
	public String getFullPlaceBaseOnGPS(Context context)
	{
		if(fullPlaceBaseOnGPS != null)
			return fullPlaceBaseOnGPS;
		
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		try
		{
			//TODO: Put this in a utiliy class

			List<Address> addresses = geocoder.getFromLocation(coord.getLat(), coord.getLon(), 1);
			if(addresses.size() == 0)
				fullPlaceBaseOnGPS = getCityAndPlace();
			
			fullPlaceBaseOnGPS = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
		}
		catch (IOException e)
		{
			fullPlaceBaseOnGPS = getCityAndPlace();
		}
		
		return fullPlaceBaseOnGPS;
	}

	public String getCityAndPlace()
	{
		return getCity() + ", " + getCountry();
	}

}
