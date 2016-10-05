package com.jmaq.jedi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.jmaq.jedi.handler.json.BreakPointHandler;
import com.jmaq.jedi.pipeline.JsonHandlerPipeline;
import com.jmaq.jedi.util.JsonUtil;

public class JSONExample {

	public static void main(String[] args) throws JsonProcessingException, IOException {

		final String jsonValue = new JSONExample().getJsonContent();

		JsonNode rootNode = JsonUtil.parse(jsonValue);  

		JsonHandlerPipeline jsonHandlerPipeline = new JsonHandlerPipeline();

		jsonHandlerPipeline.addHandler(new BreakPointHandler());

		jsonHandlerPipeline.handle(rootNode);
	}

	private String getJsonContent() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("example.json").getFile());

		try {
			return IOUtils.toString(new FileInputStream(file), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
