package com.jmaq.jedi.json;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.jmaq.jedi.event.IEventHandlerBuilder;

final class JsonEventParsersPipeline {
	
	private static final String EVENTS_NODE = "events";

	private JsonEventParser firstParser;

	JsonEventParsersPipeline() {
		registerParser(new BreakPointParser());
		registerParser(new WatchPointParser());
		registerParser(new MethodEntryParser());
		registerParser(new MethodExitParser());
	}

	void registerParser(JsonEventParser parser) {
		if (firstParser == null)
			firstParser = parser;
		else
			firstParser.chainParser(parser);
	}

	Set<IEventHandlerBuilder> generateEventHandlers(JsonNode jsonContent) {
		final Set<IEventHandlerBuilder> handlers = new HashSet<>();

		final JsonNode eventsNode = jsonContent.get(EVENTS_NODE);

		if (eventsNode != null) {
			if (firstParser != null)
				firstParser.generateEventHandler(eventsNode, handlers);
		}

		return handlers;
	}
}
