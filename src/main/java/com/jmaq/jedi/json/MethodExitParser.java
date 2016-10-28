package com.jmaq.jedi.json;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.jmaq.jedi.event.IEventHandlerBuilder;
import com.jmaq.jedi.event.MethodExitHandler;

final class MethodExitParser extends JsonEventParser {

	private final static String MAIN_ELEMENT = "methodExit";

	protected void generateEventHandler(final JsonNode rootNode, final Set<IEventHandlerBuilder> handlers) {
		if (rootNode.has(MAIN_ELEMENT)) {
			JsonNode entries = rootNode.get(MAIN_ELEMENT);

			if (entries.isArray()) {
				entries.forEach(entry -> {
					IJsonEventSkeleton skeleton = parseSkeleton(entry, MethodExitParser.Skeleton.class);
					handlers.add(skeleton.generateEventHandler());
				});
			}
		}

		super.generateEventHandler(rootNode, handlers);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Skeleton implements IJsonEventSkeleton{

		private Set<String> classInclusions;

		private Set<String> classExclusions;

		private Set<String> methodNames;

		public IEventHandlerBuilder generateEventHandler() {
			MethodExitHandler.Builder builder = new MethodExitHandler.Builder();
			
			classInclusions.forEach(builder::classFilter);
			classExclusions.forEach(builder::classExclusion);
			methodNames.forEach(builder::methodFilter);
			
			return builder;
		}

		public void setClassInclusions(Set<String> classInclusions) {
			this.classInclusions = classInclusions;
		}

		public void setClassExclusions(Set<String> classExclusions) {
			this.classExclusions = classExclusions;
		}

		public void setMethodNames(Set<String> methodNames) {
			this.methodNames = methodNames;
		}
	}
}
