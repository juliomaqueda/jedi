package com.jmaq.jedi;

import java.io.IOException;
import java.util.Arrays;

import com.jmaq.jedi.event.EventsHandlerManager;
import com.jmaq.jedi.event.MethodEntryHandler;
import com.jmaq.jedi.event.MethodExitHandler;
import com.jmaq.jedi.vm.VMConnection;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

public class AlfrescoMonitor {

	private static final String[] excludes = {
			"java.*", "javax.*",
			"sun.*", "com.sun.*", "net.*", "org.*", "freemarker.*",
			"com.hazelcast.*", "com.westernacher.*", "com.google.*",
			"com.ixxus.queue.*", "com.ixxus.alfresco.shareconfig.*"
	};

	public static void main(String[] args) throws IOException, InterruptedException, IllegalConnectorArgumentsException {

		VMConnection vmConnection = new VMConnection()
				.setHostname("localhost")
				.setPort(5000);

		final EventsHandlerManager eventsManager = new EventsHandlerManager(vmConnection);

		//MethodEntryHandler
		eventsManager.addHandler(
				new MethodEntryHandler.Builder()
//				.classFilter(CLASS_FILTER)
				.exclusions(Arrays.asList(excludes))
//				.enabled(false)
		);

		//MethodExitHandler
		eventsManager.addHandler(
				new MethodExitHandler.Builder()
//				.classFilter(CLASS_FILTER)
				.exclusions(Arrays.asList(excludes))
//				.enabled(false)
		);

		eventsManager.manageEvents();
	}
}
