package com.jmaq.jedi.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonParser {

	private ObjectMapper mapper = new ObjectMapper();

	protected final <T> T parseSkeleton(final JsonNode json, Class<T> klass) {
		T skeleton = null;

		try {
			skeleton = mapper.readValue(json.toString(), klass);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return skeleton;
	}
}
