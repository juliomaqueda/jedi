package com.jmaq.jedi.handler;

import java.util.Collections;
import java.util.List;

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
			System.out.println(now() + " - ENTRY METHOD: " + req.method());
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

		public MethodEntryHandler build(final EventRequestManager erm) {
			final MethodEntryRequest mer = erm.createMethodEntryRequest();

			if (classFilter != null)
				mer.addClassFilter(classFilter);

			for (final String exclusion : exclusions)
				mer.addClassExclusionFilter(exclusion);

			mer.setSuspendPolicy(EventRequest.SUSPEND_NONE);
			mer.setEnabled(enabled);

			final MethodEntryHandler methodEntryHandler = new MethodEntryHandler();

			methodEntryHandler.request = mer;

			return methodEntryHandler;
		}
	}
}
