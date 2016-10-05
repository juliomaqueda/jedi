package com.jmaq.jedi.pipeline;

import com.fasterxml.jackson.databind.JsonNode;
import com.jmaq.jedi.handler.json.IJsonHandler;

public class JsonHandlerPipeline {

	private IJsonHandler firstHandler;

	public void addHandler(IJsonHandler handler) {
		if (firstHandler == null)
			firstHandler = handler;
		else
			firstHandler.chainHandler(handler);
	}

	public void handle(JsonNode rootNode) {
		if (firstHandler != null)
			firstHandler.handle(rootNode);
	}
}
