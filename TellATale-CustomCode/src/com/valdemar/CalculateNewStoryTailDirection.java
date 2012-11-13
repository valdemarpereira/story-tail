package com.valdemar;

import com.stackmob.core.ServiceNotActivatedException;
import com.stackmob.core.customcode.CustomCodeMethod;
import com.stackmob.core.rest.ProcessedAPIRequest;
import com.stackmob.core.rest.ResponseToProcess;
import com.stackmob.sdkapi.SDKServiceProvider;
import com.valdemar.wind.WeatherStrategy;
import com.valdemar.wind.WindParameter;
import com.valdemar.wind.YahooWeatherStrategy;

import java.net.HttpURLConnection;
import java.util.*;

public class CalculateNewStoryTailDirection implements CustomCodeMethod {

	/**
	 * This method contains the code that you want to execute.
	 * 
	 * @return the response
	 */
	@Override
	public ResponseToProcess execute(ProcessedAPIRequest request,
			SDKServiceProvider serviceProvider) {

		WeatherStrategy ws = null;
		WindParameter wParameter = new WindParameter();

		try {
			ws = new WeatherStrategy(new YahooWeatherStrategy(), serviceProvider.getHttpService());
			wParameter = ws.executeStrategy();
		} catch (ServiceNotActivatedException e) {
			
			wParameter.setCouldNotRetrieveValues();
			wParameter.setErrorMsg("ServiceNotActivatedException");
		}
		catch(Throwable tr)
		{
			wParameter.setCouldNotRetrieveValues();
			wParameter.setErrorMsg("Throwable " + tr.getMessage());
		}

		ResponseToProcess responseToProcess = new ResponseToProcess(
				HttpURLConnection.HTTP_OK, wParameter.toMap());

		return responseToProcess;
	}

	/**
	 * This method simply returns the name of your method that we'll expose over
	 * REST for this class. Although this name can be anything you want, we
	 * recommend replacing the camel case convention in your class name with
	 * underscores, as shown here.
	 * 
	 * @return the name of the method that should be exposed over REST
	 */
	@Override
	public String getMethodName() {
		return "CalculateNewStoryTailDirection";
	}

	/**
	 * This method returns the parameters that your method should expect in its
	 * query string. Here we are using no parameters, so we just return an empty
	 * list.
	 * 
	 * @return a list of the parameters to expect for this REST method
	 */
	@Override
	public List<String> getParams() {
		return Arrays.asList();
	}

}
