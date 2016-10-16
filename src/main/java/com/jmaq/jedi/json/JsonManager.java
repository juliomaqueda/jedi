package com.jmaq.jedi.json;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.jmaq.jedi.event.IEventHandlerBuilder;
import com.jmaq.jedi.vm.VMConnection;

public class JsonManager {

	private JsonNode jsonRootNode;

	private ConnectionParser connectionHandler;
	private JsonEventParsersPipeline eventHandlersPipeline;

	public JsonManager(final JsonNode jsonNode) {
		jsonRootNode = jsonNode;
		connectionHandler = new ConnectionParser();
		eventHandlersPipeline = new JsonEventParsersPipeline();
	}

	public VMConnection getConnection() {
		return connectionHandler.generateConnection(jsonRootNode);
	}

	public void registerJsonEventHandler(JsonEventParser handler) {
		eventHandlersPipeline.registerParser(handler);
	}

	public Set<IEventHandlerBuilder> getEventHandlers() {
		return eventHandlersPipeline.generateEventHandlers(jsonRootNode);
	}
}
