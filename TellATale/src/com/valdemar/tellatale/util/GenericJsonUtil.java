package com.valdemar.tellatale.util;


import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;



public enum GenericJsonUtil
{
	INSTANCE;

	private GenericJsonUtil() {
//		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}


	protected ObjectMapper objectMapper = new ObjectMapper();
	protected JsonFactory jsonFactory = new MappingJsonFactory();

	public <T> T fromJsonString(String json, Class<T> type) throws JsonParseException, IOException	{
		T resulObject;

		JsonParser jp = null;
		jp = jsonFactory.createJsonParser(json);

		resulObject = objectMapper.readValue(jp, type);
		return resulObject;
	}

	public JsonFactory getJsonFactory() {
		return jsonFactory;
	}
	
	
	public <T> String toJson(T t) throws JsonGenerationException, JsonMappingException, IOException {

		String json = objectMapper.writeValueAsString(t);
		return json;
	}
	

}