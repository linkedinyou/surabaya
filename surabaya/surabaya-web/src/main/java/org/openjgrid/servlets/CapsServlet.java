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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openjgrid.agents.Agent;
import org.openjgrid.agents.AgentManagementService;
import org.openjgrid.services.configuration.ConfigurationService;
import org.openjgrid.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CAPS Servlet
 * 
 * This Servlet will handle various caps like:
 * 
 * - seed
 * - FetchInventory2
 * - FetchInventoryDescendents2
 * - getMesh
 * - getTexture
 * 
 * In order to provide some security all CAPS URL contain a random UUID for each
 * Capability. The UUID of an incoming request will be checked against the UUID 
 * stored in the logged in Agent which itself is managed in the AgentManagementService
 * 
 * If the given UUID fits the corresponding CAPS funcion is called. This security however
 * is only secure as long as the Transport is encrypted
 * 
 * TODO implement encrypted Transport
 * 
 * Author: Akira Sonoda
 */
@WebServlet(name = "CapsServlet", urlPatterns = {"/CAPS/*"})
public class CapsServlet extends HttpServlet {
	private static final long serialVersionUID = -8627204223385024589L;
	private static final Logger log = LoggerFactory.getLogger(CapsServlet.class);

	@EJB(mappedName="java:module/ConfigurationService")
	private ConfigurationService configuration;
	
	@EJB(mappedName="java:module/AgentManagementService")
	AgentManagementService agentManagementService;
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
		
		try {
	        OutputStream out = response.getOutputStream();
        	HttpClient httpclient = new DefaultHttpClient();

	        Util.dumpHttpRequest(request);
	        
	        String uri = request.getRequestURI();
	        String capsPath = null;
	        // the last 4 char of the CAPS Path are omitted otherwise a match to the CAPS Path
	        // given with the agent would not fit
	        Pattern p = Pattern.compile("^/CAPS/(.*)..../");
	        Matcher m = p.matcher(uri);
	        if(m.find()) {
	        	capsPath = m.group(1);
	        }
	        log.debug("CAPS Path: {}", capsPath);
	        Agent agent = agentManagementService.getAgent(capsPath);
    		// Now let's find out, what Caps actually is requested and do the corresponding processing
    		if(capsPath.equals(agent.getCaps_path())) {
    	        response.setContentType("application/xml;charset=UTF-8");
    			StringEntity reply = getSeed(request, httpclient, agent);
    			reply.writeTo(out);
    			out.close();
    		}
    		if(capsPath.equals(agent.getFetchinventory2_caps().toString())) {
    			String reply = fetchInventory2(request, httpclient); 
    		}
    		if(capsPath.equals(agent.getFetchinventorydescendents2_caps().toString())) {
    			String reply = fetchInventoryDescentdents2(request, httpclient);
    		}
    		if(capsPath.equals(agent.getGetmesh_caps().toString())) {
    			String reply = getMesh(request, httpclient);
    		}
    		if(capsPath.equals(agent.getGettexture_caps().toString())) {
    			String reply = getTexture(request, httpclient);
    		}
    		
	        log.debug("end of processRequest"); 
		} catch (Exception ex) {
			log.debug("Exception {} occurred", ex.getClass().toString());
		}
	}
	
	private StringEntity getSeed(HttpServletRequest request, HttpClient httpclient, Agent agent) throws IOException {
		log.debug("getSeed() called");
    	HttpPost httppost = new HttpPost("http://localhost:"+
    			configuration.getProperty("OpenSim", "sim_http_port") + 
    			request.getRequestURI());
		String jsonString = Util.requestContent2String(request);
		StringEntity stringEntity = new StringEntity(jsonString,request.getCharacterEncoding());
		stringEntity.setContentType(request.getContentType());
		httppost.setEntity(stringEntity);
		httppost.setHeader("expect", "100-continue");
		httppost.setHeader("connection", "close");
    	
    	HttpResponse httpResponse = httpclient.execute(httppost);
    	HttpEntity entity = httpResponse.getEntity();

    	long contentLength = entity.getContentLength();
    	Header contentType = entity.getContentType();
    	String content = IOUtils.toString(entity.getContent(), "UTF-8");
    	log.debug("ContentLength: {}", contentLength);
    	log.debug("ContentType: {}", contentType);
    	log.debug("Content: {}", content);
    	String capsFromSim = null;
    	Pattern p = Pattern.compile("^<llsd><map>(.*)</map></llsd>");
        Matcher m = p.matcher(content);
        if(m.find()) {
        	capsFromSim = m.group(1);
        }
        log.debug("CAPS from OpenSim: {}", capsFromSim);
        StringBuilder sb = new StringBuilder("<llsd><map>");
        sb.append(capsFromSim);
        sb.append("<key>FetchInventoryDescendents2</key><string>http://");
        sb.append(configuration.getProperty("Surabaya", "hostname"));
        sb.append(":");
        sb.append(configuration.getProperty("Surabaya", "http_port"));
        sb.append("/CAPS/").append(agent.getFetchinventorydescendents2_caps()).append("0000/</string>");
		// TODO add the CAPS URLs for FetchInventory2 served by this Server, to the List.
		// TODO add the CAPS URLs for GetTexture served by this Server, to the List.
		// TODO add the CAPS URLs for GetMesh served by this Server, to the List.
        sb.append("</map></llsd>");
        log.debug("CAPS after Injection {}", sb.toString());
    	StringEntity result = new StringEntity(sb.toString());    	
		return(result);
	}

	private String fetchInventory2(HttpServletRequest request, HttpClient httpclient) throws IOException {
		// TODO call fetchInvntory2 
		log.debug("fetchInventory2() called");
		String requestString = Util.requestContent2String(request);
		log.debug("Content: {}", requestString );
		return(null);
	}
	
	private String fetchInventoryDescentdents2(HttpServletRequest request, HttpClient httpclient) throws IOException {
		// TODO call fetchInvntoryDescendants2
		log.debug("fetchInventoryDescentdents2() called");
		String requestString = Util.requestContent2String(request);
		log.debug("Content: {}", requestString );

		return(null);
	}
	
	private String getMesh(HttpServletRequest request, HttpClient httpclient) {
		// TODO call getMesh
		log.debug("getMesh() called");
		return(null);
	}

	private String getTexture(HttpServletRequest request, HttpClient httpclient) {
		// TODO call getTexture 
		log.debug("getTexture() called");
		return(null);		
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
