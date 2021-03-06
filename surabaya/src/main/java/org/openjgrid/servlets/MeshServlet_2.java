/**
 * Surabaya - a replacement http server for the OpenSimulator Copyright (C) 2012 Akira Sonoda
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
package org.openjgrid.servlets;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openjgrid.datatypes.asset.AssetBase;
import org.openjgrid.datatypes.asset.AssetType;
import org.openjgrid.services.asset.AssetServiceException;
import org.openjgrid.services.asset.AssetService_2;
import org.openjgrid.services.infrastructure.SLTypeMappingService;
import org.openjgrid.util.IntRange;
import org.openjgrid.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mesh_2 Servlet
 *
 * This Servlet will handle getMesh requests from the viewer
 *
 * Because of their static nature the received textures can easaily be cached and for performance purposes they will be stored in a
 * local cache
 *
 * Author: Akira Sonoda
 */
@WebServlet(name = "MeshServlet_2", urlPatterns = {"/mesh2/*"}, asyncSupported = true)
public class MeshServlet_2 extends HttpServlet {

    private static final long serialVersionUID = 7904169852381829113L;
    private static final Logger log = LoggerFactory
            .getLogger(MeshServlet_2.class);

    @EJB
    private AssetService_2 assetService;

    @EJB(mappedName = "java:module/SLTypeMappingService")
    private SLTypeMappingService slTypeMappingService;

    private void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        final AsyncContext context = request.startAsync();
        final ServletOutputStream outputStream = response.getOutputStream();

        try {
            log.info("MeshServlet_2");
            final long startTime = System.currentTimeMillis();

            assert (Util.dumpHttpRequest(request));

            String uri = request.getRequestURI();
            log.debug("RequestURL: {}", uri);
            response.setContentType(request.getHeader("Accept"));
            final byte[] buffer = getMesh(request, response);
            if (buffer != null) {
                outputStream.setWriteListener(new WriteListener() {

                    @Override
                    public synchronized void onWritePossible() throws IOException {
                        outputStream.write(buffer);
                        context.complete();
                        long endTime = System.currentTimeMillis();
                        log.info("MeshServlet_2 took {} ms", endTime - startTime);
                    }

                    @Override
                    public void onError(Throwable ex) {
                        try {
                            log.error("Exception during Write to Output: ", ex);
                            context.complete();
                            outputStream.close();
                        } catch (IOException ex1) {
                            log.error("Exception during Close of the Output Stream", ex1);
                        }
                    }

                });
            } else {
                context.complete();
            }
        } catch (Exception ex) {
            log.error("Exception {} occurred", ex.getClass().toString());
            log.warn("Dumping HTTPRequest Info");
            Util.dumpUnexpectedHttpRequest(request);
            Util.dumpUnexpectedParameterMap(request.getParameterMap());
            context.complete();
        }
    }

    /**
     * @param request
     * @param httpclient
     * @throws IOException
     * @throws AssetServiceException
     */
    private byte[] getMesh(HttpServletRequest request,
            HttpServletResponse response) throws IOException,
            AssetServiceException {
        log.debug("getMesh() called");
        Map<String, String[]> parameterMap = request.getParameterMap();

        assert (Util.dumpParameterMap(parameterMap));

        String[] meshIds = null;
        String meshIdString = null;

        if (parameterMap.containsKey("mesh_id")) {
            meshIds = parameterMap.get("mesh_id");
            meshIdString = meshIds[0];
        } else {
            log.error("getMesh() no mesh_id received");
        }

        if (!Util.isNullOrEmpty(meshIdString) && Util.parseUUID(meshIdString)) {

            AssetBase mesh = assetService.getAsset(meshIdString);

            if (mesh != null) {
                log.debug("Mesh requested: " + meshIdString
                        + " and found Data-Size: " + mesh.getDataLength());
                if (mesh.getType() == AssetType.Mesh.getAssetType()) {
                    return (getMeshData(request, response, mesh));
                } else {

                    log.error("Mesh requested: " + meshIdString
                            + " Type received: " + mesh.getType());
                    log.error("     with Name: " + mesh.getName());
                    log.error("   Description: " + mesh.getDescription());

                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.setContentType("text/plain");
                    return (String
                            .valueOf("Unfortunately, this asset isn't a mesh.")
                            .getBytes());
                }
            } else {

                log.error("Mesh requested: " + meshIdString + " notFound");

                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("text/plain");
                return (String.valueOf("Your Mesh wasn't found.  Sorry!")
                        .getBytes());
            }

        } else {
            log.error("Failed to parse mesh ID");
        }

        return (null);
    }

    /**
     * @param httpRequest
     * @param httpResponse
     * @param mesh
     * @throws AssetServiceException
     * @throws IOException
     */
    private byte[] getMeshData(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, AssetBase mesh)
            throws AssetServiceException, IOException {
        log.debug("writeMeshData() called");
        String range = httpRequest.getHeader("Range");

        // JP2's only
        if (!Util.isNullOrEmpty(range)) {
            log.debug("Range Header: {}", range);
            // Range request
            IntRange intRange = Util.tryParseRange(range);
            if (intRange.isValid) {
				// Before clamping start make sure we can satisfy it in order to
                // avoid
                // sending back the last byte instead of an error status
                if (intRange.start >= mesh.getData().length) {
                    log.debug("Client requested range for mesh " + mesh.getID()
                            + " starting at " + intRange.start
                            + " but mesh has end of" + mesh.getDataLength());

					// Stricly speaking, as per
                    // http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html,
                    // we should be sending back
                    // Requested Range Not Satisfiable (416) here. However, it
                    // appears that at least recent implementations
                    // of the Linden Lab viewer (3.2.1 and 3.3.4 and probably
                    // earlier), a viewer that has previously
                    // received a very small texture may attempt to fetch bytes
                    // from the server past the
                    // range of data that it received originally. Whether this
                    // happens appears to depend on whether
                    // the viewer's estimation of how large a request it needs
                    // to make for certain discard levels
                    // (http://wiki.secondlife.com/wiki/Image_System#Discard_Level_and_Mip_Mapping),
                    // chiefly discard
                    // level 2. If this estimate is greater than the total
                    // texture size, returning a RequestedRangeNotSatisfiable
                    // here will cause the viewer to treat the texture as bad
                    // and never display the full resolution
                    // However, if we return PartialContent (or OK) instead, the
                    // viewer will display that resolution.
                    httpResponse
                            .setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    String contentType = mesh.getContentType();
                    if (Util.isNullOrEmpty(contentType)) {
                        contentType = slTypeMappingService
                                .slAssetTypeToContentType(mesh.getType());
                    }
                    httpResponse.setContentType(contentType);
                } else {
					// Handle the case where no second range value was given.
                    // This is equivalent to requesting
                    // the rest of the entity.
                    if (intRange.end == -1) {
                        intRange.end = Integer.MAX_VALUE;
                    }

                    intRange.end = Util.clamp(intRange.end, 0,
                            mesh.getDataLength() - 1);
                    intRange.start = Util
                            .clamp(intRange.start, 0, intRange.end);
                    int len = intRange.end - intRange.start + 1;

                    log.debug("Serving " + intRange.start + " to "
                            + intRange.end + " of " + mesh.getDataLength()
                            + " bytes for mesh " + mesh.getID());

					// Always return PartialContent, even if the range covered
                    // the entire data length
                    // We were accidentally sending back 404 before in this
                    // situation
                    // https://issues.apache.org/bugzilla/show_bug.cgi?id=51878
                    // supports sending 206 even if the
                    // entire range is requested, and viewer 3.2.2 (and very
                    // probably earlier) seems fine with this.
                    //
                    // We also do not want to send back OK even if the whole
                    // range was satisfiable since this causes
                    // HTTP textures on at least Imprudence 1.4.0-beta2 to never
                    // display the final texture quality.
                    httpResponse
                            .setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

                    httpResponse.setContentLength(len);
                    String contentType = mesh.getContentType();
                    if (Util.isNullOrEmpty(contentType)) {
                        contentType = slTypeMappingService
                                .slAssetTypeToContentType(mesh.getType());
                    }
                    httpResponse.setContentType(contentType);
                    httpResponse.addHeader("Content-Range",
                            "bytes " + intRange.start + "-" + intRange.end
                            + "/" + mesh.getDataLength());

                    return (mesh.getData(intRange.start, len + intRange.start));
                }
            } else {
                log.warn("Malformed Range header: " + range);
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            // Full content request
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            httpResponse.setContentLength(mesh.getDataLength());
            String contentType = mesh.getContentType();
            if (Util.isNullOrEmpty(contentType)) {
                contentType = slTypeMappingService
                        .slAssetTypeToContentType(mesh.getType());
            }
            httpResponse.setContentType(contentType);

            return (mesh.getData());
        }

        return (null);
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

}
