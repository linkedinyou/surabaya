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
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openjgrid.datatypes.Constants;
import org.openjgrid.services.asset.AssetService;
import org.openjgrid.services.inventory.InventoryService;
import org.openjgrid.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Texture Servlet
 * 
 * This Servlet will handle getTexture requests from the viewer
 * 
 * Because of their static nature the received textures can esaily be cached and
 * for performance purposes they will be stored in a local cache
 * 
 * Author: Akira Sonoda
 */
@WebServlet(name = "TextureServlet", urlPatterns = { "/CAPS/GTEX/*" })
public class TextureServlet extends HttpServlet {
	private static final long serialVersionUID = -7144097163138378596L;
	private static final Logger log = LoggerFactory.getLogger(TextureServlet.class);
	private static final String DEFAULT_FORMAT = "x-j2c";

	@EJB
	private AssetService assetService;

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			HttpClient httpclient = new DefaultHttpClient();

			Util.dumpHttpRequest(request);

			String uri = request.getRequestURI();
			String capsPath = null;
			// the last 4 char of the CAPS Path are omitted otherwise a match to
			// the CAPS Path
			// given with the agent would not fit
			Pattern p = Pattern.compile("^/CAPS/GTEX/(.*)....//");
			Matcher m = p.matcher(uri);
			if (m.find()) {
				capsPath = m.group(1);
			}
			log.debug("CAPS Path: {}", capsPath);
			// Agent agent = agentManagementService.getAgent(capsPath);
			// Now let's find out, what Caps actually is requested and do the
			// corresponding processing
			// if (capsPath.equals(agent.getFetchinventory2_caps().toString()))
			// {
			if (capsPath.equalsIgnoreCase("9e097a00-4f2d-11e2-bcfd-0800200c9a66")) {
				response.setContentType(request.getContentType());
				getTexture(request, response, httpclient);
				// StringEntity entity = new StringEntity(reply);
				// entity.writeTo(out);
				// out.close();
			} else {
				log.error("Unknow Request received");
			}
			log.debug("end of processRequest");
		} catch (Exception ex) {
			log.debug("Exception {} occurred", ex.getClass().toString());
		}
	}

	/**
	 * @param request
	 * @param httpclient
	 * @throws IOException
	 */
	private void getTexture(HttpServletRequest request, HttpServletResponse response, HttpClient httpclient) throws IOException {
		StringBuilder responseS = new StringBuilder();
		log.debug("getTexture() called");
		Map<String, String[]> parameterMap = request.getParameterMap();

		String[] textureIds = null;
		String[] formats = null;
		String textureId = null;
		String formatString = null;
		if (parameterMap.containsKey("texture_id")) {
			textureIds = parameterMap.get("texture_id");
			textureId = textureIds[0];
		} else {
			log.error("getTexture() no texture_id received");
		}
		if (parameterMap.containsKey("format")) {
			// TODO implement handling of the "format" Parameter
			formats = parameterMap.get("format");
			formatString = formats[0];
			log.error("getTexture() format String received ... handling is currently not implemented ");
		}

		UUID textureID = Constants.UUID_ZERO;
		if (!Util.isNullOrEmpty(textureId) && Util.parseUUID(textureId)) {
			textureID = UUID.fromString(textureId);

			String[] formatArray;
			if (Util.isNullOrEmpty(formatString)) {
				// TODO Handling format alternatives not implemented
				// formats =
				// WebUtil.GetPreferredImageTypes(httpRequest.Headers.Get("Accept"));
				// if (formats.Length == 0)
				formatArray = new String[] { DEFAULT_FORMAT }; // default
			} else {
				formatArray = new String[] { formatString.toLowerCase() };
			}

			for (int i = 0; i < formatArray.length; i++) {
				if (fetchTexture(request, response, textureID, formatString)) {
					break;
				}
			}
		} else {
			log.error("Failed to parse texture ID"); 
		}

	}

	private boolean fetchTexture(HttpServletRequest httpRequest, HttpServletResponse httpResponse, UUID textureID, String format) {
		boolean result = false;
		log.debug("fetchTexture() called");

		return (result);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
