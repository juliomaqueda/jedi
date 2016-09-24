package com.jmaq.jedi.handler;

import com.sun.jdi.event.Event;
import com.sun.jdi.request.EventRequest;

public abstract class EventHandler implements IEventHandler {

	protected EventRequest request;

	protected EventHandler nextHandler;

	public final void chainHandler(final EventHandler next) {
		if (nextHandler == null)
			nextHandler = next;
		else
			nextHandler.chainHandler(next);
	}

	public void handle(final Event event) {
		if (nextHandler != null)
			nextHandler.handle(event);
	}

	public final void enable() {
		request.enable();
	}

	public final void disable() {
		request.disable();
	}
}
