package com.jmaq.jedi.handler;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;

public final class MethodEntryHandler extends EventHandler {

	private MethodEntryHandler() {}

	public void handle(final Event event) {
		if (canHandle(event)) {
			final MethodEntryEvent req = (MethodEntryEvent) event;
			System.out.println(new Date() + " - entry request: " + req.method());
		}
		else
			super.handle(event);
	}

	private boolean canHandle(final Event event) {
		return event instanceof MethodEntryEvent;
	}


	public static final class Builder {

		private String classFilter;

		private List<ThreadReference> threadFilters = Collections.emptyList();

		public Builder classFilter(final String classFilter) {
			this.classFilter = classFilter;
			return this;
		}

		public Builder threadFilters(final List<ThreadReference> threadFilters) {
			this.threadFilters = threadFilters;
			return this;
		}

		public MethodEntryHandler build(final EventRequestManager erm) {
			final MethodEntryRequest mer = erm.createMethodEntryRequest();

			for (final ThreadReference threadFilter : threadFilters)
				mer.addThreadFilter(threadFilter);

			mer.addClassFilter(classFilter);
			mer.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
			mer.enable();

			final MethodEntryHandler methodEntryHandler = new MethodEntryHandler();

			methodEntryHandler.request = mer;

			return methodEntryHandler;
		}
	}
}
