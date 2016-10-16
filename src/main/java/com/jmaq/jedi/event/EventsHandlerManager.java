package com.jmaq.jedi.event;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.jmaq.jedi.vm.VMConnection;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.EventRequestManager;

public final class EventsHandlerManager {

	private VirtualMachine virtualMachine;
	private EventRequestManager eventRequestManager;

	private Set<IEventHandler> eventHandlers = new HashSet<>();

	public EventsHandlerManager(final VMConnection vmConnection) throws IOException, IllegalConnectorArgumentsException {		
		virtualMachine = vmConnection.connect();
		eventRequestManager = virtualMachine.eventRequestManager();
	}

	public void addHandler(final IEventHandlerBuilder handlerBuilder) {
		eventHandlers.add(handlerBuilder.build(eventRequestManager));
	}

	private void handle(final Event event) {
		for (final IEventHandler eventHandler : eventHandlers) {
			if (eventHandler.canHandle(event)) {
				eventHandler.handle(event);
				break;
			}
		}
	}

	private void enableAll() {
		eventHandlers.forEach(IEventHandler::enable);
	}

	public void manageEvents() throws InterruptedException {
		enableAll();

		virtualMachine.resume();

		final EventQueue eventQueue = virtualMachine.eventQueue();

		while (true) {
			final EventSet eventSet = eventQueue.remove();

			for (final Event event : eventSet) {
				if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) {
					return;
				}
				else {
					handle(event);
				}
			}

			eventSet.resume();
		}
	}
}
