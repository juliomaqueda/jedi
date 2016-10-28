package com.jmaq.jedi.json;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.jmaq.jedi.event.IEventHandlerBuilder;
import com.jmaq.jedi.event.WatchPointHandler;

final class WatchPointParser extends JsonEventParser {

	private final static String MAIN_ELEMENT = "watchpoints";

	protected void generateEventHandler(final JsonNode rootNode, final Set<IEventHandlerBuilder> handlers) {
		if (rootNode.has(MAIN_ELEMENT)) {
			JsonNode entries = rootNode.get(MAIN_ELEMENT);

			if (entries.isArray()) {
				entries.forEach(entry -> {
					IJsonEventSkeleton skeleton = parseSkeleton(entry, WatchPointParser.Skeleton.class);
					handlers.add(skeleton.generateEventHandler());
				});
			}
		}

		super.generateEventHandler(rootNode, handlers);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Skeleton implements IJsonEventSkeleton{

		private String className;

		private int line;

		public IEventHandlerBuilder generateEventHandler() {
			return new WatchPointHandler.Builder()
					.className(className)
					.lineNumber(line);
		}

		public void setClassName(final String className) {
			this.className = className;
		}

		public void setLine(final int line) {
			this.line = line;
		}
	}
}
