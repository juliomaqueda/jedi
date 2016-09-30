package com.jmaq.jedi.handler;

import com.sun.jdi.request.EventRequestManager;

public interface IEventHandlerBuilder {

	public EventHandler build(final EventRequestManager erm);
}
