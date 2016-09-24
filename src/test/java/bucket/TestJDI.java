package bucket;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.Field;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.event.AccessWatchpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.request.AccessWatchpointRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;

/**
 * @author usui
 *
 * ã?ã?®çæ?ã?ãã?ã³ã¡ã³ãã?®æ¿å¥ã?ãããã³ãã¬ã¼ããå¤æ´ã?ãã?ã?
 * ã¦ã£ã³ãã¦ > è¨­å® > Java > ã³ã¼ãçæ? > ã³ã¼ãã?¨ã³ã¡ã³ã
 */

/*
 * å®è¡ã³ãã³ã
 * % java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=4321
 * 
 * API reference
 * http://java.sun.com/j2se/1.4/ja/docs/ja/guide/jpda/jdi/
 * 
 */
public class TestJDI {
	private static String[] excludes = { "java.*", "javax.*", "sun.*", "com.sun.*" };

	public static void main(String[] args) throws Exception {
		//MyClass.myMthod()ã?®å¼ã?³åºã?ã?¨MyClass.myFieldã?¸ã?®ã¢ã¯ã»ã¹ãç¶ã?ã
		new Thread(){
			public void run() {
				MyClass mc = new MyClass();
				for (int i = 0; i < 100; i++) {
					System.out.println("run:" + mc.myField++);
					mc.myMethod();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
				System.exit(0);
			}
		}.start();
		Thread.sleep(2000);
		
		//æ¥ç¶ã?ãã?ãVMãå?å¾ã?ã
		AttachingConnector connector = (AttachingConnector) findConnector("com.sun.jdi.SocketAttach");
		Map arguments = connector.defaultArguments();
		((Connector.Argument) arguments.get("hostname")).setValue("localhost");
		((Connector.Argument) arguments.get("port")).setValue("4321");
		VirtualMachine vm = connector.attach(arguments);
		
		//ã¯ã©ã¹ã?®å?å®ç¾©
//		ReferenceType t = (ReferenceType) vm.classesByName(MyClass.class.getName()).get(0);
//		FileInputStream file = new FileInputStream("newMyClass/MyClass.class");
//		byte[] classData = new byte[file.available()];
//		file.read(classData);
//		file.close();
//		Map map = new HashMap();
//		map.put(t, classData);
//		vm.redefineClasses(map);
//		
//		System.out.println("MyClass is redefined");
//		Thread.sleep(2000);
		
		
		//ä»¥ä¸ãã£ã¼ã«ãã¢ã¯ã»ã¹ãã¡ã½ããå¼ã?³åºã?ã?®ç£è¦
		EventRequestManager mgr = vm.eventRequestManager();
		
		//ã¡ã½ããå¼ã?³åºã?(å®è¡å?)ã?®ãªã¯ã¨ã¹ããä½æ?ã?ããå®è¡å¾ã?¯ MethodExitRequest
		MethodEntryRequest request = mgr.createMethodEntryRequest();
//		for (int i = 0; i < excludes.length; ++i) {
//			request.addClassExclusionFilter(excludes[i]);
//		}
		request.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
		request.enable();
		
		//ãã£ã¼ã«ãã¢ã¯ã»ã¹(å?ç§)ã?®ãªã¯ã¨ã¹ããä½æ?ã?ããä»£å¥ã?®å ´å?ã?¯ ModificationWatchpointRequest
//		Field field = t.fieldByName("myField");
//		AccessWatchpointRequest awrequest = mgr.createAccessWatchpointRequest(field);
//		for (int i = 0; i < excludes.length; ++i) {
//			awrequest.addClassExclusionFilter(excludes[i]);
//		}
//		awrequest.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
//		awrequest.enable();
		
		
		//ãªã¯ã¨ã¹ã(ã¤ãã³ã)ãç£è¦ã?ã
		EventQueue queue = vm.eventQueue();
		while(true){
			EventSet events = queue.remove();//ãªã¯ã¨ã¹ããå¾ã?¤
			for (EventIterator it = events.eventIterator(); it.hasNext();) {
				Event evnt = it.nextEvent();
				if(evnt instanceof MethodEntryEvent){
					MethodEntryEvent req = (MethodEntryEvent) evnt;
					System.out.println("method entry request:"+req.method());
				}else 
				if(evnt instanceof AccessWatchpointEvent){
					AccessWatchpointEvent req = (AccessWatchpointEvent) evnt;
					System.out.println("field access request:"+req.field());
				}
			}
			//mgr.deleteEventRequest(request);
			events.resume();
		}
		
	}
	
	private static Connector findConnector(String connector) {
		List connectors = Bootstrap.virtualMachineManager().allConnectors();
		Iterator iter = connectors.iterator();
		while (iter.hasNext()) {
			Connector con = (Connector) iter.next();
			if (con.name().equals(connector)) {
				return con;
			}
		}
		throw new RuntimeException("No connector : " + connector);
	}
}

class MyClass {
	
	int myField;
	
	public void myMethod() {
		System.out.println("Executing mymethod...");
	}
}
