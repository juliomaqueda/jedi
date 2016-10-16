package com.jmaq.jedi.event;

import com.sun.jdi.request.EventRequestManager;

public interface IEventHandlerBuilder {

	public IEventHandler build(final EventRequestManager erm);
}
