package com.jmaq.jedi.pipeline;

import com.jmaq.jedi.handler.EventHandler;
import com.sun.jdi.event.Event;

public interface IEventPipeline {

	public void addHandler(EventHandler handler);

	public void handle(Event event);
}
