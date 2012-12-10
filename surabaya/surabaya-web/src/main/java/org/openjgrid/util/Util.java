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
package org.openjgrid.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Akira Sonoda
 * 
 */
public final class Util {

	private static final Logger log = LoggerFactory.getLogger(Util.class);

	public static final void dumpHttpRequest(HttpServletRequest request) {

		log.debug("---- HTTP ServletRequest Dump ----");
		log.debug("Method     : {}", request.getMethod());
		log.debug("Request URI: {}", request.getRequestURI());
		log.debug("Protocol   : {}", request.getProtocol());
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			log.debug("Header: {} - {}", headerName, request.getHeader(headerName));
		}
		try {
			if (request.getContentType().equalsIgnoreCase("application/json")) {
				log.debug("Content : {}", requestContent2String(request));
			} else if (request.getContentType().equalsIgnoreCase("application/x-gzip")) {
				log.debug("Content : {}", requestContent2ByteArray(request));
			}
		} catch (Exception ex) {
			log.debug("Exception occurred during reading of the content");
			log.debug("Exception: {}", ex.getMessage());
		}
		log.debug("---- End HTTP ServletRequest Dump ----");

	}
	
	public static String requestContent2String(HttpServletRequest request) throws IOException {
		BufferedReader aReader = request.getReader();
		StringBuilder line = new StringBuilder();
		String aLine = null;
		while ((aLine = aReader.readLine()) != null) { 
			line.append(aLine);
		}
		return(line.toString());
		
	}
	
	public static byte[] requestContent2ByteArray(HttpServletRequest request) throws IOException {
		InputStream aStream = request.getInputStream();
		byte[] dataBuffer = new byte[request.getContentLength()];
		while (aStream.read(dataBuffer) != -1) { }
		return (dataBuffer);
		
	}
}
