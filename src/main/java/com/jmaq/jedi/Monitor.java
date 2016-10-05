package com.jmaq.jedi;

import java.io.IOException;

import com.jmaq.jedi.handler.event.BreakPointHandler;
import com.jmaq.jedi.handler.event.MethodEntryHandler;
import com.jmaq.jedi.handler.event.MethodExitHandler;
import com.jmaq.jedi.pipeline.EventHandlerPipeline;
import com.jmaq.jedi.vm.VMAcquirer;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;

public class Monitor {

	private static final String CLASS_FILTER = "test.Test";

	public static void main(String[] args) throws IOException, InterruptedException {

		VirtualMachine vm = new VMAcquirer().connect(5000);
		
		EventHandlerPipeline eventsPipeline = new EventHandlerPipeline();

		//MethodEntryHandler
		eventsPipeline.addHandler(
				new MethodEntryHandler.Builder()
				.classFilter(CLASS_FILTER)
				.build(vm.eventRequestManager())
		);

		//MethodExitHandler
		eventsPipeline.addHandler(
				new MethodExitHandler.Builder()
				.classFilter(CLASS_FILTER)
				.build(vm.eventRequestManager())
		);

		//BreakPointHandler
		eventsPipeline.addHandler(
				new BreakPointHandler.Builder()
				.classFilter(CLASS_FILTER)
				.lineNumber(32)
				.build(vm.eventRequestManager())
		);

		eventsPipeline.enableAll();

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
