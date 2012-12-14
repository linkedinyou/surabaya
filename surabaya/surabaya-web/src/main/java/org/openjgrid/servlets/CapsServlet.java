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

import org.openjgrid.agents.Agent;
import org.openjgrid.agents.AgentManagementService;
import org.openjgrid.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class CapsServlet
 * 
 * Author: Akira Sonoda
 */
@WebServlet(name = "CapsServlet", urlPatterns = {"/CAPS/*"})
public class CapsServlet extends HttpServlet {
	private static final long serialVersionUID = -8627204223385024589L;
	private static final Logger log = LoggerFactory.getLogger(CapsServlet.class);

	@EJB(mappedName="java:module/AgentManagementService")
	AgentManagementService agentManagementService;
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
		
		try {
	        response.setContentType("text/xml;charset=UTF-8");
	        OutputStream out = response.getOutputStream();

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
    			String reply = getSEED(request);
    		}
    		if(capsPath.equals(agent.getFetchinventory2_caps().toString())) {
    			String reply = fetchInventory2(request); 
    		}
    		if(capsPath.equals(agent.getFetchinventorydescendants2_caps().toString())) {
    			String reply = fetchInventoryDescentdants2(request);
    		}
    		if(capsPath.equals(agent.getGetmesh_caps().toString())) {
    			String reply = getMesh(request);
    		}
    		if(capsPath.equals(agent.getGettexture_caps().toString())) {
    			String reply = getTexture(request);
    		}
    		
	        log.debug("end of processRequest"); 
		} catch (Exception ex) {
			log.debug("Exception {} occurred", ex.getClass().toString());
		}
	}
	
	private String getSEED(HttpServletRequest request) {
		// TODO call seedCAPS
		return(null);
	}

	private String fetchInventory2(HttpServletRequest request) {
		// TODO call fetchInvntory2 
		return(null);
	}
	
	private String fetchInventoryDescentdants2(HttpServletRequest request) {
		// TODO call fetchInvntoryDescendants2
		return(null);
	}
	
	private String getMesh(HttpServletRequest request) {
		// TODO call getMesh
		return(null);
	}

	private String getTexture(HttpServletRequest request) {
		// TODO call getTexture 
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
