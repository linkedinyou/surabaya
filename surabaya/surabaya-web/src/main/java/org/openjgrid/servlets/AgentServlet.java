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

	@EJB
	private ConfigurationService configuration;
	
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
        	
        	if (request.getContentType().equalsIgnoreCase("application/json")) {
        		// TODO Decide and implement if we are going to keep the Agent Data.
        		StringEntity stringEntity = new StringEntity(Util.requestContent2String(request),request.getContentType());
        		httppost.setEntity(stringEntity);
        	} else if (request.getContentType().equalsIgnoreCase("application/x-gzip")) {
        		ByteArrayEntity byteArrayEntity = new ByteArrayEntity(Util.requestContent2ByteArray(request));
        		byteArrayEntity.setContentType(request.getContentType());
        		httppost.setEntity(byteArrayEntity);
        	} else {
                Util.dumpHttpRequest(request);        		
        	}
        	
        	HttpResponse httpResponse = httpclient.execute(httppost);
        	HttpEntity entity = httpResponse.getEntity();
        	if (entity != null) {
       	    	entity.writeTo(out);
       	        out.close();
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
