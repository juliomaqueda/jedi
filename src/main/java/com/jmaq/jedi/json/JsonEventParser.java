package com.jmaq.jedi.json;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.jmaq.jedi.event.IEventHandlerBuilder;

public class JsonEventParser extends JsonParser{

	private JsonEventParser nextParser;

	final void chainParser(final JsonEventParser next) {
		if (nextParser == null)
			nextParser = next;
		else
			nextParser.chainParser(next);
	}

	protected void generateEventHandler(final JsonNode rootNode, final Set<IEventHandlerBuilder> handlers) {
		if (nextParser != null)
			nextParser.generateEventHandler(rootNode, handlers);
	}
}
