package com.jmaq.jedi.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.jmaq.jedi.vm.VMConnection;

final class ConnectionParser extends JsonParser{

	private static final String CONNECTION_NODE = "connection";

	VMConnection generateConnection(final JsonNode jsonContent) {
		final JsonNode connectionNode = jsonContent.get(CONNECTION_NODE);

		if (connectionNode == null)
			throw new IllegalArgumentException("Object 'connection' not found");

		final Skeleton connectionData = parseSkeleton(connectionNode, ConnectionParser.Skeleton.class);

		if (connectionData != null && connectionData.validate()) {
			return new VMConnection()
					.setHostname(connectionData.hostname)
					.setPort(connectionData.port)
					.setTimeout(connectionData.timeout);
		}

 		throw new IllegalStateException("Connection parameters are not valid");
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Skeleton implements IJsonSkeleton{

		private String hostname;

		private Integer port;

		private Long timeout;

		@Override
		public boolean validate() {
			return hostname != null;
		}

		public void setHostname(String hostname) {
			this.hostname = hostname;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public void setTimeout(Long timeout) {
			this.timeout = timeout;
		}
	}
}
