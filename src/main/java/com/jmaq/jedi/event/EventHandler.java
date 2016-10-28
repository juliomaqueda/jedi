package com.jmaq.jedi.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.sun.jdi.event.Event;
import com.sun.jdi.request.EventRequest;

public abstract class EventHandler implements IEventHandler {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSSS");

	protected Set<EventRequest> requests = new HashSet<>();

	public final void enable() {
		requests.forEach(EventRequest::enable);
	}

	public final void disable() {
		requests.forEach(EventRequest::disable);
	}

	public final boolean canHandle(final Event event) {
		return requests.contains(event.request()); 
	}

	protected final String now() {
		return dateFormat.format(new Date());
	}
}
