package com.jmaq.jedi.json;

import com.jmaq.jedi.event.IEventHandlerBuilder;

interface IJsonEventSkeleton extends IJsonSkeleton{

	IEventHandlerBuilder generateEventHandler();
}
