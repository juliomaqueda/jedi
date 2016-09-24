package com.jmaq.jedi.handler;

import com.sun.jdi.event.Event;

public interface IEventHandler {

	public void chainHandler(EventHandler nextHandler);

	public void handle(Event event);
}
