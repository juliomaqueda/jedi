package com.jmaq.jedi.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	public static JsonNode parse(final String jsonValue) {
		JsonFactory factory = new JsonFactory();

		ObjectMapper mapper = new ObjectMapper(factory);
		JsonNode rootNode = null;

		try {
			rootNode = mapper.readTree(jsonValue);
		} catch (JsonProcessingException e) {
			System.out.println("There was an error while processing the json content");
		} catch (IOException e) {
			System.out.println("There was an error while reading the json string");
		}

		return rootNode;
	}
}
