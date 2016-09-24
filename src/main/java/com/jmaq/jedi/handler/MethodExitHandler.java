package com.jmaq.jedi.handler;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodExitRequest;

public final class MethodExitHandler extends EventHandler {

	private MethodExitHandler() {}

	public void handle(final Event event) {
		if (canHandle(event)) {
			MethodExitEvent req = (MethodExitEvent) event;
			System.out.println(new Date() + " - exit request: " + req.method());
		}
		else
			super.handle(event);
	}

	private boolean canHandle(final Event event) {
		return event instanceof MethodExitEvent;
	}


	public static final class Builder {
		
		private String classFilter;
		
		private List<ThreadReference> threadFilters = Collections.emptyList();
		
		public Builder classFilter(final String classFilter) {
			this.classFilter = classFilter;
			return this;
		}
		
		public Builder threadFilter(final List<ThreadReference> threadFilters) {
			this.threadFilters = threadFilters;
			return this;
		}

		public MethodExitHandler build(final EventRequestManager erm) {
			final MethodExitRequest mer = erm.createMethodExitRequest();

			for (final ThreadReference threadFilter : threadFilters)
				mer.addThreadFilter(threadFilter);

			mer.addClassFilter(classFilter);
			mer.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
			mer.enable();

			final MethodExitHandler methodExitHandler = new MethodExitHandler();

			methodExitHandler.request = mer;
			
			return methodExitHandler;
		}
	}
}
