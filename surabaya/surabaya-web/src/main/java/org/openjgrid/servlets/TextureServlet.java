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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openjgrid.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Texture Servlet
 * 
 * This Servlet will handle getTexture requests from the viewer
 * 
 * Because of their static nature the received textures can esaily be cached 
 * and for performance purposes they will be stored in a local cache
 * 
 * Author: Akira Sonoda
 */
public class TextureServlet extends HttpServlet {
	private static final long serialVersionUID = -7144097163138378596L;
	private static final Logger log = LoggerFactory.getLogger(TextureServlet.class);
	private static final String DEFAULT_FORMAT = "x-j2c";
	
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			OutputStream out = response.getOutputStream();
			HttpClient httpclient = new DefaultHttpClient();

			Util.dumpHttpRequest(request);
			
			
		} catch (Exception ex) {
			log.debug("Exception {} occurred", ex.getClass().toString());
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
