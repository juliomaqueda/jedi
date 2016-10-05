package com.jmaq.jedi.pipeline;

import java.util.HashSet;
import java.util.Set;

import com.jmaq.jedi.handler.event.EventHandler;
import com.jmaq.jedi.handler.event.IEventHandler;
import com.sun.jdi.event.Event;

public class EventHandlerPipeline {

	private Set<IEventHandler> eventHandlers = new HashSet<>();

	public final void addHandler(final EventHandler handler) {
		eventHandlers.add(handler);
	}

	public void handle(final Event event) {
		for (final IEventHandler eventHandler : eventHandlers) {
			if (eventHandler.canHandle(event)) {
				eventHandler.handle(event);
				break;
			}
		}
	}

	public void enableAll() {
		eventHandlers.stream().forEach(IEventHandler::enable);
	}

	public void disableAll() {
		eventHandlers.stream().forEach(IEventHandler::disable);
	}
}
