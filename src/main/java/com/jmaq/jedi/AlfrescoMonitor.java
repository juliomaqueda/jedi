package com.jmaq.jedi;

import java.io.IOException;

import com.jmaq.jedi.event.EventsHandlerManager;
import com.jmaq.jedi.event.MethodEntryHandler;
import com.jmaq.jedi.event.MethodExitHandler;
import com.jmaq.jedi.vm.VMConnection;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

public class AlfrescoMonitor {

	public static void main(String[] args) throws IOException, InterruptedException, IllegalConnectorArgumentsException {

		VMConnection vmConnection = new VMConnection()
				.setHostname("localhost")
				.setPort(5000);

		final EventsHandlerManager eventsManager = new EventsHandlerManager(vmConnection);

		//MethodExitHandler
		eventsManager.addHandler(
				new MethodEntryHandler.Builder()
//				.classFilter(CLASS_FILTER)
				.classExclusion("java.*")
				.classExclusion("javax.*")
				.classExclusion("sun.*")
				.classExclusion("com.sun.*")
				.classExclusion("net.*")
				.classExclusion("org.*")
				.classExclusion("freemarker.*")
				.classExclusion("com.hazelcast.*")
				.classExclusion("com.google.*")
				.classExclusion("com.ixxus.queue.*")
				.classExclusion("com.ixxus.alfresco.shareconfig.*")
		);

		//MethodExitHandler
		eventsManager.addHandler(
				new MethodExitHandler.Builder()
//				.classFilter(CLASS_FILTER)
				.classExclusion("java.*")
				.classExclusion("javax.*")
				.classExclusion("sun.*")
				.classExclusion("com.sun.*")
				.classExclusion("net.*")
				.classExclusion("org.*")
				.classExclusion("freemarker.*")
				.classExclusion("com.hazelcast.*")
				.classExclusion("com.google.*")
				.classExclusion("com.ixxus.queue.*")
				.classExclusion("com.ixxus.alfresco.shareconfig.*")
		);

		eventsManager.manageEvents();
	}
}
