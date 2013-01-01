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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Akira Sonoda
 * 
 */
public final class Util {

	private static final Logger log = LoggerFactory.getLogger(Util.class);
		
    /// <summary>
    /// Parse a range header.
    /// </summary>
    /// <remarks>
    /// As per http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html,
    /// this obeys range headers with two values (e.g. 533-4165) and no second value (e.g. 533-).
    /// Where there is no value, -1 is returned.
    /// FIXME: Need to cover the case where only a second value is specified (e.g. -4165), probably by returning -1
    /// for start.</remarks>
    /// <returns></returns>
    /// <param name='header'></param>
    /// <param name='start'>Start of the range.  Undefined if this was not a number.</param>
    /// <param name='end'>End of the range.  Will be -1 if no end specified.  Undefined if there was a raw string but this was not a number.</param>
    public static IntRange tryParseRange(String header) {
    	IntRange range = new IntRange();

        if (header.startsWith("bytes=")) {
            String[] rangeValues = header.substring(6).split("-");

            if (rangeValues.length == 2) {
            	if(!StringUtils.isNumeric(rangeValues[0])) {
            		range.isValid = false;
            		return(range);
            	} else {
            		range.start = Integer.parseInt(rangeValues[0]);
            	}

                String rawEnd = rangeValues[1];

                if (rawEnd.isEmpty()) {
                    range.end = -1;
                    range.isValid = true;
                    return(range);
                } else if (StringUtils.isNumeric(rawEnd)) {
                	range.end = Integer.parseInt(rawEnd);
                	range.isValid = true;
                    return(range);
                }
            }
        }

        range.start = 0;
        range.end = 0;
        range.isValid = false;
        return (range);
    }

	
	public static final boolean dumpParameterMap(Map<String, String[]> map) {
		if (map != null) {
			log.debug("---- HTTP ParameterMap Dump ----");
			Iterator<Entry<String, String[]>> mapIter = map.entrySet().iterator();
			while (mapIter.hasNext()) {
				Entry<String, String[]> entry = (Entry<String, String[]>) mapIter.next();
				log.debug("Parameter: {}", entry.getKey());
				String[] values = entry.getValue();
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						log.debug("  Value: {}", values[i]);
					}
				}
			}
			log.debug("---- HTTP ParameterMap Dump END----");
		} else {
			log.debug("Empty Map received");
		}
		return (true);
	}

	public static final boolean dumpHttpRequest(HttpServletRequest request) {

		log.debug("---- HTTP ServletRequest Dump ----");
		log.debug("Method     : {}", request.getMethod());
		log.debug("Request URI: {}", request.getRequestURI());
		log.debug("Protocol   : {}", request.getProtocol());
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			log.debug("Header: {} - {}", headerName, request.getHeader(headerName));
		}
		log.debug("---- End HTTP ServletRequest Dump ----");
		return (true);

	}

	public static String requestContent2String(HttpServletRequest request) throws IOException {
		BufferedReader aReader = request.getReader();
		StringBuilder line = new StringBuilder();
		String aLine = null;
		while ((aLine = aReader.readLine()) != null) {
			line.append(aLine);
		}
		return (line.toString());

	}

	public static byte[] requestContent2ByteArray(HttpServletRequest request) throws IOException {
		InputStream aStream = request.getInputStream();
		byte[] dataBuffer = new byte[request.getContentLength()];
		while (aStream.read(dataBuffer) != -1) {
		}
		return (dataBuffer);

	}

	public static boolean parseUUID(String uuidString) {
		Pattern p = Pattern.compile("[0-9A-Fa-f]{8}(-[0-9A-Fa-f]{4}){3}-[0-9A-Fa-f]{12}");
		Matcher m = p.matcher(uuidString);
		return (m.find());
	}

	public static boolean isNullOrEmpty(String aString) {
		return (aString == null || aString.isEmpty());
	}

	/**
	 * Clamp a given value between a range
	 * 
	 * @param value
	 *            Value to clamp
	 * @param min
	 *            Minimum allowable value
	 * @param max
	 *            Maximum allowable value
	 * @return A value inclusively between lower and upper
	 */
	public static float clamp(float value, float min, float max) {
		// First we check to see if we're greater than the max
		value = (value > max) ? max : value;

		// Then we check to see if we're less than the min.
		value = (value < min) ? min : value;

		// There's no check to see if min > max.
		return value;
	}

	/**
	 * Clamp a given value between a range
	 * 
	 * @param value
	 *            Value to clamp
	 * @param min
	 *            Minimum allowable value
	 * @param max
	 *            Maximum allowable value
	 * @return A value inclusively between lower and upper
	 */
	public static double clamp(double value, double min, double max) {
		// First we check to see if we're greater than the max
		value = (value > max) ? max : value;

		// Then we check to see if we're less than the min.
		value = (value < min) ? min : value;

		// There's no check to see if min > max.
		return value;
	}

	/**
	 * Clamp a given value between a range
	 * 
	 * @param value
	 *            Value to clamp
	 * @param min
	 *            Minimum allowable value
	 * @param max
	 *            Maximum allowable value
	 * @return A value inclusively between lower and upper
	 */
	public static int clamp(int value, int min, int max) {
		// First we check to see if we're greater than the max
		value = (value > max) ? max : value;

		// Then we check to see if we're less than the min.
		value = (value < min) ? min : value;

		// There's no check to see if min > max.
		return value;
	}

}
