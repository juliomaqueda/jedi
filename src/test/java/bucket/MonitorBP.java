package bucket;

import java.io.IOException;
import java.util.List;

import com.jmaq.jedi.vm.VMConnection;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequestManager;

public class MonitorBP {

	private static final String CLASS_FILTER = "test.Test";

	public static void main(String[] args) throws IOException, InterruptedException, IllegalConnectorArgumentsException {

		VMConnection vmConnection = new VMConnection()
				.setHostname("localhost")
				.setPort(5000);

		VirtualMachine vm = vmConnection.connect();

		EventRequestManager erm = vm.eventRequestManager();

		vm.classesByName(CLASS_FILTER).stream()
		.forEach(cl -> {
			for (Method method : cl.methods()) {
				if (method.name().equals("pintar")) {
					try {
						List<Location> methodLinesLocations = method.allLineLocations();

						BreakpointRequest bprEntry = erm.createBreakpointRequest(methodLinesLocations.get(0));
						bprEntry.enable();

						BreakpointRequest bprExit = erm.createBreakpointRequest(methodLinesLocations.get(methodLinesLocations.size()-1));
						bprExit.enable();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		vm.resume();

		EventQueue eventQueue = vm.eventQueue();

		while (true) {
			EventSet eventSet = eventQueue.remove();

			for (Event event : eventSet) {
				if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) {
					return;
				}
				else if (event instanceof BreakpointEvent) {
					BreakpointEvent bpe = (BreakpointEvent) event;
					Method method = bpe.location().method();

					if (method.location().lineNumber() == bpe.location().lineNumber())
						System.out.println("Entering method: " + method.name());
					else
						System.out.println("Exiting method: " + method.name());
				}
			}

			eventSet.resume();
		}
	}
}
