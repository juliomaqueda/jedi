package com.jmaq.jedi.handler.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

public final class BreakPointHandler extends JsonHandler {

	private final static String MAIN_ELEMENT = "breakpoints";

	public void handle(JsonNode rootNode) {

		if (rootNode.has(MAIN_ELEMENT)) {
			JsonNode breakpoints = rootNode.get(MAIN_ELEMENT);

			if (breakpoints.isArray()) {
				breakpoints.forEach(b -> {
					Skeleton breakPoint = parseSkeleton(b, BreakPointHandler.Skeleton.class);
					System.out.println(breakPoint.className);
				});
			}
		}

		super.handle(rootNode);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Skeleton {

		private String className;

		public void setClassName(String className) {
			this.className = className;
		}
	}
}
