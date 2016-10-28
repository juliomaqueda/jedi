package com.jmaq.jedi.event;

import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;

public final class MethodEntryHandler extends EventHandler {

	private MethodEntryHandler() {}

	public void handle(final Event event) {
		if (canHandle(event)) {
			final BreakpointEvent eventInfo = (BreakpointEvent) event;
			System.out.println(now() + " - ENTRY METHOD: " + eventInfo.location().method());
		}
	}


	public static final class Builder extends EventHandlerBuilder{

		public Builder classFilter(final String className) {
			addClassFilter(className);
			return this;
		}

		public Builder classExclusion(final String className) {
			addClassExclusionFilter(className);
			return this;
		}

		public Builder methodFilter(final String methodName) {
			addMethodFilter(methodName);
			return this;
		}

		public MethodEntryHandler build(final EventRequestManager erm) {
			MethodEntryHandler handler = new MethodEntryHandler();

			filteredClasses.forEach(klass -> {
				filterMethods(klass).forEach(method -> {
					final BreakpointRequest request = erm.createBreakpointRequest(method.location());
					request.setSuspendPolicy(EventRequest.SUSPEND_NONE);

					handler.requests.add(request);
				});
			});

			return handler;
		}

		public boolean validateBuilder() {
			return true;
		}
	}
}
