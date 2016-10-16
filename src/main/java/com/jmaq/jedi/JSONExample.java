package com.jmaq.jedi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.jmaq.jedi.event.EventsHandlerManager;
import com.jmaq.jedi.event.IEventHandlerBuilder;
import com.jmaq.jedi.json.JsonManager;
import com.jmaq.jedi.util.JsonUtil;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

public class JSONExample {

	public static void main(String[] args) throws JsonProcessingException, IOException, InterruptedException, IllegalConnectorArgumentsException {

		final String jsonValue = new JSONExample().getJsonContent();
		final JsonNode rootNode = JsonUtil.parse(jsonValue);  

		final JsonManager jsonManager = new JsonManager(rootNode);

		final EventsHandlerManager eventsManager = new EventsHandlerManager(jsonManager.getConnection());

		final Set<IEventHandlerBuilder> eventHandlers = jsonManager.getEventHandlers();
		eventHandlers.forEach(eventsManager::addHandler);

		eventsManager.manageEvents();
	}

	private String getJsonContent() {
		final ClassLoader classLoader = getClass().getClassLoader();
		final File file = new File(classLoader.getResource("example.json").getFile());

		try {
			return IOUtils.toString(new FileInputStream(file), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
