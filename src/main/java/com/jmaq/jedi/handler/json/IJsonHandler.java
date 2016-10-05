package com.jmaq.jedi.handler.json;

import com.fasterxml.jackson.databind.JsonNode;

public interface IJsonHandler {

	public void chainHandler(IJsonHandler next);

	public void handle(JsonNode rootNode);
}
