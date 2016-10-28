package com.jmaq.jedi.event;

import java.util.Collections;
import java.util.List;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;

public final class WatchPointHandler extends EventHandler {

	private WatchPointHandler() {}

	public void handle(final Event event) {
		if (canHandle(event)) {
			final BreakpointEvent req = (BreakpointEvent) event;
			System.out.println(now() + " - WATCHPOINT: " + req.location().method() + " at line " + req.location().lineNumber());
		}
	}

	public static final class Builder extends EventHandlerBuilder {

		private String className;

		private Integer lineNumber;

		public Builder className(final String className) {
			this.className = className;
			return this;
		}

		public Builder lineNumber(final int lineNumber) {
			this.lineNumber = lineNumber;
			return this;
		}

		public boolean requireFullScanClasses() {
			return false;
		}

		public WatchPointHandler build(final EventRequestManager erm) {
			final VirtualMachine vm = erm.virtualMachine();

			final List<ReferenceType> filteredClasses = vm.classesByName(className);

			if (filteredClasses.size() != 1) {
				return null;
			}

			final ReferenceType filteredClass = filteredClasses.get(0);

			List<Location> classLines = Collections.emptyList();

			try {
				classLines = filteredClass.locationsOfLine(lineNumber);
			} catch (AbsentInformationException e) {
				e.printStackTrace();
			}

			if (classLines.size() != 1) {
				return null;
			}

			final Location classLocation = classLines.get(0);

			final BreakpointRequest request = erm.createBreakpointRequest(classLocation);
			request.setSuspendPolicy(EventRequest.SUSPEND_NONE);

			WatchPointHandler handler = new WatchPointHandler();
			handler.requests.add(request);

			return handler;
		}

		public boolean validateBuilder() {
			return className != null && lineNumber != null;
		}
	}
}
