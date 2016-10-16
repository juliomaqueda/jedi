package com.jmaq.jedi.vm;

import java.io.IOException;
import java.util.Map;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

public class VMConnection {

	private String hostname;

	private String port;

	private String timeout;

	public VirtualMachine connect()	throws IOException, IllegalConnectorArgumentsException {
		
		if (hostname == null | port == null)
			throw new IllegalConnectorArgumentsException("Hostaname and port must be setted", "sdsdf");
			
		final AttachingConnector connector = getConnector();

		try {
			final Map<String, Connector.Argument> args = connector.defaultArguments();

			final Connector.Argument hostArgument = args.get("hostname");
			final Connector.Argument portArgument = args.get("port");
			final Connector.Argument timeoutArgument = args.get("timeout");

			if (hostArgument == null || portArgument == null || timeoutArgument == null) {
				throw new IllegalStateException();
			}

			hostArgument.setValue(hostname);
			portArgument.setValue(String.valueOf(port));
			
			if (timeout != null)
				timeoutArgument.setValue(String.valueOf(timeout));

			return connector.attach(args);
		} catch (IllegalConnectorArgumentsException e) {
			throw new IllegalStateException(e);
		}
	}

	private AttachingConnector getConnector() {
		final VirtualMachineManager vmManager = Bootstrap.virtualMachineManager();

		for (final Connector connector : vmManager.attachingConnectors()) {
			System.out.println(connector.name());

			if ("com.sun.jdi.SocketAttach".equals(connector.name())) {
				return (AttachingConnector) connector;
			}
		}

		throw new IllegalStateException();
	}

	public VMConnection setHostname(final String hostname) {
		this.hostname = hostname;
		return this;
	}

	public VMConnection setPort(final Integer port) {
		if (port != null)
			this.port = String.valueOf(port);

		return this;
	}

	public VMConnection setTimeout(Long timeout) {
		if (timeout != null)
			this.timeout = String.valueOf(timeout);

		return this;
	}
}
