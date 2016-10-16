package com.jmaq.jedi.event;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.jdi.event.Event;
import com.sun.jdi.request.EventRequest;

public abstract class EventHandler implements IEventHandler {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSSS");

	protected EventRequest request;

	public boolean canHandle(final Event event) {
		return event.request() == request;
	}

	public final void enable() {
		request.enable();
	}

	public final void disable() {
		request.disable();
	}

	protected final String now() {
		return dateFormat.format(new Date());
	}
}
