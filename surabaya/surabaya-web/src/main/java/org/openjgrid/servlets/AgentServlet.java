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
import java.util.UUID;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openjgrid.agents.Agent;
import org.openjgrid.agents.AgentManagementService;
import org.openjgrid.services.configuration.ConfigurationService;
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
@WebServlet(name = "AgentServlet", urlPatterns = {"/agent/*"})
public class AgentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(AgentServlet.class);

	@EJB(mappedName="java:module/ConfigurationService")
	private ConfigurationService configuration;

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
	private void processRequest(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        
        try {
        	HttpClient httpclient = new DefaultHttpClient();
        	HttpPost httppost = new HttpPost("http://localhost:"+
        			configuration.getProperty("OpenSim", "sim_http_port") + 
        			request.getRequestURI());
        	Agent agent = null;      	        	        	
        	if (request.getContentType().equalsIgnoreCase("application/json")) {
        		String jsonString = Util.requestContent2String(request);
        		agent = new Agent(jsonString);
        		log.debug("Agent-Name: {} {} ", agent.getFirst_name(), agent.getLast_name() );
        		log.debug("Agent-UUID: {}", agent.getAgent_id().toString());
        		log.debug("Agent-CAPS: {}", agent.getCaps_path());
        		StringEntity stringEntity = new StringEntity(jsonString,request.getCharacterEncoding());
        		stringEntity.setContentType(request.getContentType());
        		httppost.setEntity(stringEntity);
        	} else if (request.getContentType().equalsIgnoreCase("application/x-gzip")) {
        		ByteArrayEntity byteArrayEntity = new ByteArrayEntity(Util.requestContent2ByteArray(request));
        		httppost.setEntity(byteArrayEntity);
        	} else {
                Util.dumpHttpRequest(request);        		
        	}
        	
    		httppost.setHeader("expect", "100-continue");
    		httppost.setHeader("connection", "close");
        	
        	HttpResponse httpResponse = httpclient.execute(httppost);
        	HttpEntity entity = httpResponse.getEntity();
        	log.debug("HttpResponse-ContentType: {}",entity.getContentType());
        	if (entity != null) {
       	    	entity.writeTo(out);
       	        out.close();
        	}
        	if(agent != null) {
        		agent.setFetchinventory2_caps(UUID.randomUUID());
        		agent.setFetchinventorydescendants2_caps(UUID.randomUUID());
        		agent.setGetmesh_caps(UUID.randomUUID());
        		agent.setGettexture_caps(UUID.randomUUID());
        		agentManagementService.setAgent(agent.getAgent_id().toString(), agent);
        		agentManagementService.setAgent(agent.getFetchinventory2_caps().toString(),agent);
        		agentManagementService.setAgent(agent.getFetchinventorydescendants2_caps().toString(), agent);
        		agentManagementService.setAgent(agent.getGetmesh_caps().toString(), agent);
        		agentManagementService.setAgent(agent.getGettexture_caps().toString(), agent);
        		agentManagementService.setAgent(agent.getCaps_path(), agent);
        	}
        	
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
