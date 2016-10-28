package com.jmaq.jedi.event;

import java.util.List;

import com.sun.jdi.Location;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;

public final class MethodExitHandler extends EventHandler {

	private MethodExitHandler() {}

	public void handle(final Event event) {
		if (canHandle(event)) {
			final BreakpointEvent eventInfo = (BreakpointEvent) event;
			System.out.println(now() + " - EXIT METHOD: " + eventInfo.location().method());
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

		public MethodExitHandler build(final EventRequestManager erm) {
			MethodExitHandler handler = new MethodExitHandler();

			filteredClasses.forEach(klass -> {
				filterMethods(klass).forEach(method -> {
					try {
						List<Location> lineLocations = method.allLineLocations();

						final BreakpointRequest request = erm.createBreakpointRequest(lineLocations.get(lineLocations.size()-1));
						request.setSuspendPolicy(EventRequest.SUSPEND_NONE);
						
						handler.requests.add(request);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			});

			return handler;
		}

		public boolean validateBuilder() {
			return true;
		}
	}
}
