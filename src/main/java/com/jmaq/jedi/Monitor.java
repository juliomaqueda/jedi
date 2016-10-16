package com.jmaq.jedi;

import java.io.IOException;

import com.jmaq.jedi.event.BreakPointHandler;
import com.jmaq.jedi.event.EventsHandlerManager;
import com.jmaq.jedi.event.MethodEntryHandler;
import com.jmaq.jedi.event.MethodExitHandler;
import com.jmaq.jedi.vm.VMConnection;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

public class Monitor {

	private static final String CLASS_FILTER = "test.Test";

	public static void main(String[] args) throws IOException, InterruptedException, IllegalConnectorArgumentsException {

		VMConnection vmConnection = new VMConnection()
				.setHostname("localhost")
				.setPort(5000);

		final EventsHandlerManager eventsManager = new EventsHandlerManager(vmConnection);

		//MethodEntryHandler
		eventsManager.addHandler(
				new MethodEntryHandler.Builder()
				.classFilter(CLASS_FILTER)
		);

		//MethodExitHandler
		eventsManager.addHandler(
				new MethodExitHandler.Builder()
				.classFilter(CLASS_FILTER)
		);

		//BreakPointHandler
		eventsManager.addHandler(
				new BreakPointHandler.Builder()
				.className(CLASS_FILTER)
				.lineNumber(32)
		);

		eventsManager.manageEvents();
	}
}
