package com.jmaq.jedi.pipeline;

import com.jmaq.jedi.handler.EventHandler;
import com.sun.jdi.event.Event;

public class EventHandlerPipeline implements IEventPipeline {

	private EventHandler firstHandler;

	public final void addHandler(final EventHandler handler) {
		if (firstHandler == null)
			firstHandler = handler;
		else
			firstHandler.chainHandler(handler);
	}

	public void handle(final Event event) {
		if (firstHandler != null)
			firstHandler.handle(event);
	}
}
