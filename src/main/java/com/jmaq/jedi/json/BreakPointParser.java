package com.jmaq.jedi.json;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.jmaq.jedi.event.BreakPointHandler;
import com.jmaq.jedi.event.IEventHandlerBuilder;

final class BreakPointParser extends JsonEventParser {

	private final static String MAIN_ELEMENT = "breakpoints";

	protected void generateEventHandler(final JsonNode rootNode, final Set<IEventHandlerBuilder> handlers) {
		if (rootNode.has(MAIN_ELEMENT)) {
			JsonNode breakpoints = rootNode.get(MAIN_ELEMENT);

			if (breakpoints.isArray()) {
				breakpoints.forEach(b -> {
					Skeleton breakPoint = parseSkeleton(b, BreakPointParser.Skeleton.class);

					if (breakPoint.validate())
						handlers.add(breakPoint.generateEventHandler());
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
			return new BreakPointHandler.Builder()
					.className(className)
					.lineNumber(line);
		}

		public boolean validate() {
			return className != null;
		}

		public void setClassName(final String className) {
			this.className = className;
		}

		public void setLine(final int line) {
			this.line = line;
		}
	}
}
