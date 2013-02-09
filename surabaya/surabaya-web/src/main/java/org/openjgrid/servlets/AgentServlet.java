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
package org.openjgrid.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.openjgrid.agents.Agent;
import org.openjgrid.services.agent.AgentCaps;
import org.openjgrid.services.agent.AgentManagementService;
import org.openjgrid.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class AgentServlet.
 * 
 * Passes /agent/xxxx calls directly to the Simulator HTTP Server.
 * No further Processing is done, because the relevant processing has
 * to be done inside the Simulator
 * 
 * Author: Akira Sonoda
 */
@WebServlet(name = "AgentServlet", urlPatterns = {"/agent"})
public class AgentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(AgentServlet.class);

	@EJB(mappedName="java:module/AgentManagementService")
	private AgentManagementService agentManagementService;
	
	/**
	 * The main processing routine is called from doGet() and doPost()
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private void processRequest(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        Map<String, String> result = new HashMap<String, String>();
		ObjectMapper objectMapper = new ObjectMapper();
        
        assert(Util.dumpHttpRequest(request));
        
        try {
        	if (request.getContentType().equalsIgnoreCase("application/json")) {
        		String jsonString = Util.requestContent2String(request);
        		AgentCaps agentCaps = new AgentCaps();
        		agentCaps.capsMap = objectMapper.readValue(jsonString, agentCaps.capsMap.getClass());
        		Agent agent = new Agent(agentCaps.capsMap);
        		log.info("Agent-UUID: {}", agent.agent_id.toString());
        		log.info("Agent-HOST: {}", agent.host);
        		
            	if (agent != null) {
            		// if there is already an agent remove with the given id
            		// remove it first (Housekeeping)
            		if(agentManagementService.hasAgent(agent.agent_id)) {
            			agentManagementService.removeAgent(agent.agent_id);
            		}
            		agentManagementService.setAgent(agent.agent_id.toString(), agent);
                	result.put("result", "ok");
            	} else {
					log.error("Unable to create Agent: {}", jsonString);
	                result.put("result", "fail");										
            	}

        	} else {
        		log.error("Unexpected ContentType: {}", request.getContentType());
                Util.dumpHttpRequest(request);
                result.put("result", "fail");
                result.put("reason", "invalid ContentType : "+ request.getContentType());
        	}
        	
        	StringEntity resultStringEntity = new StringEntity(objectMapper.writeValueAsString(result));
        	resultStringEntity.writeTo(out);
        	out.flush();
        	out.close();
        	
        	
        } catch (Exception ex) {
        	log.debug("Exception occurred in AgentServlet.processRequest():",ex);
        }
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
