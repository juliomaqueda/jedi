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

public final class BreakPointHandler extends EventHandler {

	private BreakPointHandler() {}

	public void handle(final Event event) {
		if (canHandle(event)) {
			final BreakpointEvent req = (BreakpointEvent) event;
			System.out.println(now() + " - ENTRY breakpoint: " + req.location().method() + " at line " + req.location().lineNumber());
		}
	}


	public static final class Builder implements IEventHandlerBuilder{

		private String className;

		private int lineNumber;

		private boolean enabled = false;

		public Builder className(final String classFilter) {
			this.className = classFilter;
			return this;
		}

		public Builder lineNumber(final int lineNumber) {
			this.lineNumber = lineNumber;
			return this;
		}

		public Builder enabled(final boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		public BreakPointHandler build(final EventRequestManager erm) {

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

			final BreakpointRequest bpr = erm.createBreakpointRequest(classLocation);
			bpr.setSuspendPolicy(EventRequest.SUSPEND_NONE);
			bpr.setEnabled(enabled);

			final BreakPointHandler breakPointHandler = new BreakPointHandler();

			breakPointHandler.request = bpr;

			return breakPointHandler;
		}
	}
}
