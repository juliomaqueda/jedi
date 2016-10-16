package com.jmaq.jedi.event;

import com.sun.jdi.event.Event;

public interface IEventHandler {

	public boolean canHandle(Event event);

	public void handle(Event event);

	public void enable();

	public void disable();
}
