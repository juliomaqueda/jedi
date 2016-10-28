package com.jmaq.jedi.event;

import com.sun.jdi.ReferenceType;
import com.sun.jdi.request.EventRequestManager;

public interface IEventHandlerBuilder {

	public void applyFilters(final ReferenceType klass);

	public IEventHandler build(final EventRequestManager erm);

	public boolean requireFullScanClasses();

	public boolean validateBuilder();
}
