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

import javax.ejb.EJB;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openjgrid.datatypes.Constants;
import org.openjgrid.datatypes.asset.AssetBase;
import org.openjgrid.datatypes.asset.AssetType;
import org.openjgrid.services.asset.AssetService_2;
import org.openjgrid.services.asset.AssetServiceException;
import org.openjgrid.services.infrastructure.SLTypeMappingService;
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
@WebServlet(name = "TextureServlet_2", urlPatterns = { "/texture2/*" }, asyncSupported = true)
public class TextureServlet_2 extends HttpServlet {
	private static final long serialVersionUID = -7144097163138378596L;
	private static final Logger log = LoggerFactory.getLogger(TextureServlet_2.class);
	private static final String DEFAULT_FORMAT = "x-j2c";

	@EJB
	private AssetService_2 assetService;

	@EJB(mappedName = "java:module/SLTypeMappingService")
	private SLTypeMappingService slTypeMappingService;

	// @EJB(mappedName = "java:module/AgentManagementService")
	// private AgentManagementService agentManagementService;
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final AsyncContext context = request.startAsync();
        final ServletOutputStream outputStream = response.getOutputStream();

		try {
			log.debug("TextureServlet_2");
			final long startTime = System.currentTimeMillis();

			assert(Util.dumpHttpRequest(request));

			String uri = request.getRequestURI();
			log.debug("RequestURL: {}", uri);
			response.setContentType(request.getHeader("Accept"));
            final byte[] buffer = getTexture(request, response);
            if (buffer != null) {
                outputStream.setWriteListener(new WriteListener() {

                    @Override
                    public synchronized void onWritePossible() throws IOException {
                        outputStream.write(buffer);
                        context.complete();
                        long endTime = System.currentTimeMillis();
                        log.info("TextureServlet_2 took {} ms", endTime - startTime);
                    }

                    @Override
                    public void onError(Throwable ex) {
                        log.error("Exception during Write to Output: ", ex);
                    }

                });
            }
		} catch (Exception ex) {
			log.error("Exception {} occurred", ex.getClass().toString());
		}
	}

	/**
	 * @param request
	 * @param httpclient
	 * @throws IOException
	 * @throws AssetServiceException 
	 */
	private byte[] getTexture(HttpServletRequest request, HttpServletResponse response) throws IOException, AssetServiceException {
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
				return(fetchTexture( request, response, textureID, DEFAULT_FORMAT ));
			} else {
                return(fetchTexture( request, response, textureID, formatString.toLowerCase() ));
			}

		} else {
			log.error("Failed to parse texture ID");
		}
		
		return(null);

	}

	private byte[] fetchTexture(HttpServletRequest httpRequest, HttpServletResponse httpResponse, UUID textureID, String format) throws AssetServiceException, IOException {
		log.debug("fetchTexture() called ID: {}", textureID.toString());
		AssetBase texture = null;
		String fullID = textureID.toString();
		if (!format.equals(DEFAULT_FORMAT)) {
			fullID = fullID + "-" + format;
		}

		if (texture == null) {

			// Fetch locally or remotely. Misses return a 404
			texture = assetService.getAsset(textureID.toString());

			if (texture != null) {
				if (texture.getType() != AssetType.Texture.getAssetType()) {
					httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return(null);
				}
				if (format.equals(DEFAULT_FORMAT)) {
					return(getTextureData(httpRequest, httpResponse, texture, format));
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
					return(null);
				}
			}
		} 
		
		return (null);
	}

	/**
	 * @param httpRequest
	 * @param httpResponse
	 * @param texture
	 * @param format
	 * @throws AssetServiceException 
	 * @throws IOException 
	 */
	private byte[] getTextureData(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AssetBase texture, String format) throws AssetServiceException, IOException {
		log.debug("writeTextureData() called");
        String range = httpRequest.getHeader("Range");
        
        // JP2's only
        if (!Util.isNullOrEmpty(range)) {
            log.debug("Range Header: {}", range);
            // Range request
            IntRange intRange = Util.tryParseRange(range);
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
                    String contentType = texture.getContentType();
                    if(Util.isNullOrEmpty(contentType)) {
                    	contentType = slTypeMappingService.slAssetTypeToContentType(texture.getType());
                    } 
                    httpResponse.setContentType(contentType);
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
                    String contentType = texture.getContentType();
                    if(Util.isNullOrEmpty(contentType)) {
                    	contentType = slTypeMappingService.slAssetTypeToContentType(texture.getType());
                    } 
                    httpResponse.setContentType(contentType);
                    httpResponse.addHeader("Content-Range", "bytes "+intRange.start+"-"+intRange.end+"/"+texture.getDataLength());
                    
                    httpResponse.addHeader("Content-Range", "bytes "+intRange.start+"-"+intRange.end+"/"+texture.getDataLength());
                    return(texture.getData(intRange.start, len+intRange.start));
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
                String contentType = texture.getContentType();
                if(Util.isNullOrEmpty(contentType)) {
                	contentType = slTypeMappingService.slAssetTypeToContentType(texture.getType());
                } 
                httpResponse.setContentType(contentType);
            } else {
                httpResponse.setContentType("image/" + format);
            }

            return(texture.getData());
        }
        
        return(null);
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
