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

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openjgrid.datatypes.AssetBase;
import org.openjgrid.datatypes.AssetType;
import org.openjgrid.datatypes.Constants;
import org.openjgrid.services.asset.AssetService;
import org.openjgrid.services.asset.AssetServiceException;
import org.openjgrid.util.IntRange;
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
	 * @throws AssetServiceException 
	 */
	private void getTexture(HttpServletRequest request, HttpServletResponse response, HttpClient httpclient) throws IOException, AssetServiceException {
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
				if (fetchTexture(request, response, textureID, formatArray[i])) {
					break;
				}
			}
		} else {
			log.error("Failed to parse texture ID");
		}

	}

	private boolean fetchTexture(HttpServletRequest httpRequest, HttpServletResponse httpResponse, UUID textureID, String format) throws AssetServiceException, IOException {
		log.debug("fetchTexture() called");
		AssetBase texture = null;
		String fullID = textureID.toString();
		if (!format.equals(DEFAULT_FORMAT)) {
			fullID = fullID + "-" + format;
		}

		// TODO implement caching: texture = m_assetService.GetCached(fullID);
		if (texture == null) {

			// Fetch locally or remotely. Misses return a 404
			texture = assetService.getAsset(textureID.toString());

			if (texture != null) {
				if (texture.getType() != AssetType.Texture.getAssetType()) {
					httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return(true);
				}
				if (format.equals(DEFAULT_FORMAT)) {
					writeTextureData(httpRequest, httpResponse, texture, format);
					return(true);
				} else {
					// TODO implement AssetBase newTexture = new
					// AssetBase(texture.ID + "-" + format, texture.Name,
					// (sbyte)AssetType.Texture, texture.Metadata.CreatorID);
					// newTexture.Data = ConvertTextureData(texture, format);
					// if (newTexture.Data.Length == 0)
					// return false; // !!! Caller try another codec, please!
					//
					// newTexture.Flags = AssetFlags.Collectable;
					// newTexture.Temporary = true;
					// m_assetService.Store(newTexture);
					// WriteTextureData(httpRequest, httpResponse, newTexture,
					// format);
					log.error("Format {} handling not yet implemented", format);
					return(true);
				}
			}
		} // else {
			// m_log.DebugFormat("[GETTEXTURE]: texture was in the cache");
			// WriteTextureData(httpRequest, httpResponse, texture, format);
			// log.error("Caching not yet implemented");
			// return (false);
		// }

		return (false);
	}

	/**
	 * @param httpRequest
	 * @param httpResponse
	 * @param texture
	 * @param format
	 * @throws AssetServiceException 
	 * @throws IOException 
	 */
	private void writeTextureData(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AssetBase texture, String format) throws AssetServiceException, IOException {
		log.debug("() called");
        String range = httpRequest.getHeader("Range");
        log.debug("Range Header: {}", range);

        // JP2's only
        if (!Util.isNullOrEmpty(range)) {
            // Range request
            IntRange intRange = tryParseRange(range);
            if (intRange.isValid) {
                // Before clamping start make sure we can satisfy it in order to avoid
                // sending back the last byte instead of an error status
                if (intRange.start >= texture.getData().length) {
                    log.debug(
                        "Client requested range for texture "+texture.getID()+
                        " starting at "+intRange.start+" but texture has end of"+texture.getDataLength());

                    // Stricly speaking, as per http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html, we should be sending back
                    // Requested Range Not Satisfiable (416) here.  However, it appears that at least recent implementations
                    // of the Linden Lab viewer (3.2.1 and 3.3.4 and probably earlier), a viewer that has previously
                    // received a very small texture  may attempt to fetch bytes from the server past the
                    // range of data that it received originally.  Whether this happens appears to depend on whether
                    // the viewer's estimation of how large a request it needs to make for certain discard levels
                    // (http://wiki.secondlife.com/wiki/Image_System#Discard_Level_and_Mip_Mapping), chiefly discard
                    // level 2.  If this estimate is greater than the total texture size, returning a RequestedRangeNotSatisfiable
                    // here will cause the viewer to treat the texture as bad and never display the full resolution
                    // However, if we return PartialContent (or OK) instead, the viewer will display that resolution.

                    httpResponse.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    httpResponse.setContentType(texture.getContentType());
                } else {
                    // Handle the case where no second range value was given.  This is equivalent to requesting
                    // the rest of the entity.
                    if (intRange.end == -1)
                        intRange.end = Integer.MAX_VALUE;

                    intRange.end = Util.clamp(intRange.end, 0, texture.getDataLength() - 1);
                    intRange.start = Util.clamp(intRange.start, 0, intRange.end);
                    int len = intRange.end - intRange.start + 1;

                    log.debug("Serving " + intRange.start + " to " + intRange.end + " of " + texture.getDataLength() + " bytes for texture " + texture.getID());

                    // Always return PartialContent, even if the range covered the entire data length
                    // We were accidentally sending back 404 before in this situation
                    // https://issues.apache.org/bugzilla/show_bug.cgi?id=51878 supports sending 206 even if the
                    // entire range is requested, and viewer 3.2.2 (and very probably earlier) seems fine with this.
                    //
                    // We also do not want to send back OK even if the whole range was satisfiable since this causes
                    // HTTP textures on at least Imprudence 1.4.0-beta2 to never display the final texture quality.

                    httpResponse.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

                    httpResponse.setContentLength(len);
                    httpResponse.setContentType(texture.getContentType());
                    httpResponse.addHeader("Content-Range", "bytes "+intRange.start+"-"+intRange.end+"/"+texture.getDataLength());

                    OutputStream out = httpResponse.getOutputStream();

                    out.write(texture.getData(), intRange.start, len);
                    out.flush();
                    out.close();
                }
            } else {
                log.warn("Malformed Range header: " + range);
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            // Full content request
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            httpResponse.setContentLength(texture.getDataLength());
            if (format.equals(DEFAULT_FORMAT)) {
                httpResponse.setContentType(texture.getContentType());
            } else {
                httpResponse.setContentType("image/" + format);
            }
            OutputStream out = httpResponse.getOutputStream();

            out.write(texture.getData());
            out.flush();
            out.close();
        }
	}

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
    private IntRange tryParseRange(String header) {
    	IntRange range = new IntRange();

        if (header.startsWith("bytes=")) {
            String[] rangeValues = header.substring(6).split("-");

            if (rangeValues.length == 2) {
            	if(StringUtils.isNumeric(rangeValues[0])) {
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
