package com.jmaq.jedi.handler;

import java.util.Collections;
import java.util.List;

import com.sun.jdi.event.Event;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodExitRequest;

public final class MethodExitHandler extends EventHandler {

	private MethodExitHandler() {}

	public void handle(final Event event) {
		if (canHandle(event)) {
			final MethodExitEvent req = (MethodExitEvent) event;
			System.out.println(now() + " - EXIT METHOD: " + req.method());
		}
	}


	public static final class Builder implements IEventHandlerBuilder{
		
		private String classFilter;

		private boolean enabled = true;

		private List<String> exclusions = Collections.emptyList();

		public Builder classFilter(final String classFilter) {
			this.classFilter = classFilter;
			return this;
		}

		public Builder enabled(final boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		public Builder exclusions(final List<String> exclusions) {
			this.exclusions = exclusions;
			return this;
		}

		public MethodExitHandler build(final EventRequestManager erm) {
			final MethodExitRequest mer = erm.createMethodExitRequest();

			if (classFilter != null)
				mer.addClassFilter(classFilter);

			for (final String exclusion : exclusions)
				mer.addClassExclusionFilter(exclusion);

			mer.setSuspendPolicy(EventRequest.SUSPEND_NONE);
			mer.setEnabled(enabled);

			final MethodExitHandler methodExitHandler = new MethodExitHandler();

			methodExitHandler.request = mer;
			
			return methodExitHandler;
		}
	}
}
