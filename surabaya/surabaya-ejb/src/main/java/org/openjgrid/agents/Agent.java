/**
 *  Surabaya - a replacement http server for the OpenSimulator
 *  Copyright (C) 2012 Akira Sonoda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openjgrid.agents;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple POJO that holds the information of a connected Agent.
 * Not sure if it is ever needed
 * 
 * @author Akira Sonoda 
 */
public final class Agent {

	private static final Logger log = LoggerFactory.getLogger(Agent.class);

	private UUID agent_id = null;
	private String caps_path = null;
	private String circuit_code = null;
	private String first_name = null;
	private String last_name = null;
	private UUID secure_session_id = null;
	private UUID session_id = null;
	private String start_pos = null;
	private String client_ip = null;
	private String viewer = null;
	private String channel = null;
	private String mac = null;
	private String id0 = null;

	private Map<String, String> service_urls = null;
	private String destination_x = null;
	private String destination_y = null;
	private String destination_name = null;
	private UUID destination_uuid = null;
	private String teleport_flags = null;

	/**
	 * Constructs an Agent from a given JSON String
	 * 
	 * @param jsonString
	 */
	public Agent(String jsonString) {
		try {
			JsonFactory jsonFactory = new JsonFactory();
			JsonParser jp = jsonFactory.createJsonParser(jsonString);
			jp.nextToken();
			String fieldname;
			
	        while (jp.nextToken() != JsonToken.END_OBJECT) {
	            fieldname = jp.getCurrentName();
	            jp.nextToken();
	            
	            if ("agent_id".equals(fieldname)) {
	                this.agent_id = UUID.fromString(jp.getText());
	            } else if ("caps_path".equals(fieldname)) {
	                this.caps_path = jp.getText();
	            } else if ("circuit_code".equals(fieldname)) {
	                this.circuit_code = jp.getText();
	            } else if ("first_name".equals(fieldname)) {
	                this.first_name = jp.getText();
	            } else if ("last_name".equals(fieldname)) {
	                this.last_name = jp.getText();
	            } else if ("secure_session_id".equals(fieldname)) {
	                this.secure_session_id = UUID.fromString(jp.getText());
	            } else if ("session_id".equals(fieldname)) {
	                this.session_id = UUID.fromString(jp.getText());
	            } else if ("start_pos".equals(fieldname)) {
	                this.start_pos = jp.getText();
	            } else if ("client_ip".equals(fieldname)) {
	                this.client_ip = jp.getText();
	            } else if ("viewer".equals(fieldname)) {
	                this.viewer = jp.getText();
	            } else if ("channel".equals(fieldname)) {
	                this.channel = jp.getText();
	            } else if ("mac".equals(fieldname)) {
	                this.mac = jp.getText();
	            } else if ("id0".equals(fieldname)) {
	                this.id0 = jp.getText();
	            } else if ("destination_x".equals(fieldname)) {
	                this.destination_x = jp.getText();
	            } else if ("destination_y".equals(fieldname)) {
	                this.destination_y = jp.getText();
	            } else if ("destination_name".equals(fieldname)) {
	                this.destination_name = jp.getText();
	            } else if ("destination_uuid".equals(fieldname)) {
	                this.destination_uuid = UUID.fromString(jp.getText());
	            } else if ("teleport_flags".equals(fieldname)) {
	                this.teleport_flags = jp.getText();
	            } else if ("service_urls".equals(fieldname)) {
	            	jp.nextToken();
	            	this.service_urls = new HashMap<String, String>();
	            	while(jp.nextToken() != JsonToken.END_ARRAY) {
	            		String key = jp.getText();
	            		jp.nextToken();
	            		String value = jp.getText();
	            		this.service_urls.put(key, value);
	            	}
	            } 
	        }
		} catch (Exception ex) {
			log.debug("Exception during Agent creation from JSON String: {}", ex.getCause());
		}
	}

	/**
	 * @return the agent_id
	 */
	public UUID getAgent_id() {
		return agent_id;
	}

	/**
	 * @return the caps_path
	 */
	public String getCaps_path() {
		return caps_path;
	}

	/**
	 * @return the circuit_code
	 */
	public String getCircuit_code() {
		return circuit_code;
	}

	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return first_name;
	}

	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}

	/**
	 * @return the secure_session_id
	 */
	public UUID getSecure_session_id() {
		return secure_session_id;
	}

	/**
	 * @return the session_id
	 */
	public UUID getSession_id() {
		return session_id;
	}

	/**
	 * @return the start_pos
	 */
	public String getStart_pos() {
		return start_pos;
	}

	/**
	 * @return the client_ip
	 */
	public String getClient_ip() {
		return client_ip;
	}

	/**
	 * @return the viewer
	 */
	public String getViewer() {
		return viewer;
	}

	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * @return the id0
	 */
	public String getId0() {
		return id0;
	}

	/**
	 * @return the service_urls
	 */
	public Map<String, String> getService_urls() {
		return service_urls;
	}

	/**
	 * @return the destination_x
	 */
	public String getDestination_x() {
		return destination_x;
	}

	/**
	 * @return the destination_y
	 */
	public String getDestination_y() {
		return destination_y;
	}

	/**
	 * @return the destination_name
	 */
	public String getDestination_name() {
		return destination_name;
	}

	/**
	 * @return the destination_uuid
	 */
	public UUID getDestination_uuid() {
		return destination_uuid;
	}

	/**
	 * @return the teleport_flags
	 */
	public String getTeleport_flags() {
		return teleport_flags;
	}

}
