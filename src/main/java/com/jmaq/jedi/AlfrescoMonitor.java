package com.jmaq.jedi;

import java.io.IOException;
import java.util.Arrays;

import com.jmaq.jedi.handler.MethodEntryHandler;
import com.jmaq.jedi.handler.MethodExitHandler;
import com.jmaq.jedi.pipeline.EventHandlerPipeline;
import com.jmaq.jedi.vm.VMAcquirer;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;

public class AlfrescoMonitor {

	private static final String[] excludes = {
			"java.*", "javax.*",
			"sun.*", "com.sun.*", "net.*", "org.*", "freemarker.*",
			"com.hazelcast.*", "com.westernacher.*", "com.google.*",
			"com.ixxus.queue.*", "com.ixxus.alfresco.shareconfig.*"
	};

	public static void main(String[] args) throws IOException, InterruptedException {

		VirtualMachine vm = new VMAcquirer().connect(8000);
		
		EventHandlerPipeline eventsPipeline = new EventHandlerPipeline();

		//MethodEntryHandler
		eventsPipeline.addHandler(
				new MethodEntryHandler.Builder()
//				.classFilter(CLASS_FILTER)
				.exclusions(Arrays.asList(excludes))
//				.enabled(false)
				.build(vm.eventRequestManager())
		);

		//MethodExitHandler
		eventsPipeline.addHandler(
				new MethodExitHandler.Builder()
//				.classFilter(CLASS_FILTER)
				.exclusions(Arrays.asList(excludes))
//				.enabled(false)
				.build(vm.eventRequestManager())
		);

		vm.resume();

		EventQueue eventQueue = vm.eventQueue();

		while (true) {
			EventSet eventSet = eventQueue.remove();

			for (Event event : eventSet) {
				if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) {
					return;
				}
				else {
					eventsPipeline.handle(event);
				}
			}

			eventSet.resume();
		}
	}
}
