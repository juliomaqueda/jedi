package com.jmaq.jedi.pipeline;

import java.util.HashSet;
import java.util.Set;

import com.jmaq.jedi.handler.EventHandler;
import com.sun.jdi.event.Event;

public class EventHandlerPipeline implements IEventPipeline {

	private Set<EventHandler> eventHandlers = new HashSet<>();

	public final void addHandler(final EventHandler handler) {
		eventHandlers.add(handler);
	}

	public void handle(final Event event) {
		for (final EventHandler eventHandler : eventHandlers) {
			if (eventHandler.canHandle(event)) {
				eventHandler.handle(event);
				break;
			}
		}
	}
}
