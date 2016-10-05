package com.jmaq.jedi.handler.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonHandler implements IJsonHandler {

	private IJsonHandler nextHandler;

	private ObjectMapper mapper = new ObjectMapper();

	public final void chainHandler(final IJsonHandler next) {
		if (nextHandler == null)
			nextHandler = next;
		else
			nextHandler.chainHandler(next);
	}

	public void handle(final JsonNode rootNode) {
		if (nextHandler != null)
			nextHandler.handle(rootNode);
	}

	protected <T> T parseSkeleton(final JsonNode json, Class<T> klass) {
		T skeleton = null;

		try {
			skeleton = mapper.readValue(json.toString(), klass);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return skeleton;
	}
}
