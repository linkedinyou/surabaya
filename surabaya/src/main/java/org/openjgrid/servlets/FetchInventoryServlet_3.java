/**
 * Surabaya - a replacement http server for the OpenSimulator Copyright (C) 2012
 * Akira Sonoda
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openjgrid.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import org.openjgrid.datatypes.inventory.InventoryException;
import org.openjgrid.datatypes.inventory.InventoryItemBase;
import org.openjgrid.datatypes.llsd.LLSD;
import org.openjgrid.datatypes.llsd.LLSDFetchInventory;
import org.openjgrid.datatypes.llsd.LLSDInventoryItem;
import org.openjgrid.services.inventory.InventoryService_3;
import org.openjgrid.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FetchInventory Servlet
 *
 * This Servlet will handle the FetchInventory3 OpenSim CAPS
 * TODO implement encrypted Transport
 *
 * Author: Akira Sonoda
 */
@WebServlet(name = "FetchInventoryServlet_3", urlPatterns = {"/fetchinventory3/*"}, asyncSupported = true)
public class FetchInventoryServlet_3 extends HttpServlet {


	private static final long serialVersionUID = 5279104662612723064L;

	private static final Logger log = LoggerFactory.getLogger(FetchInventoryServlet_3.class);

    @EJB
    private InventoryService_3 inventoryService;

    private void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        final AsyncContext context = request.startAsync();
        final ServletOutputStream outputStream = response.getOutputStream();

        try {
            log.info("FetchInventoryServlet_3");
            final long startTime = System.currentTimeMillis();

            assert (Util.dumpHttpRequest(request));

            String inventoryServerName = null;
            String inventoryServerPort = null;

            String url = request.getRequestURI();
            Pattern p = Pattern.compile("^/fetchinventory3/(.*)/(.*)$");
            Matcher m = p.matcher(url);
            if (m.find()) {
                inventoryServerName = m.group(1);
                inventoryServerPort = m.group(2);
            } else {
                log.error("Unexpected URL Format of Inventory Server: " + url);
            }

            String inventoryServerURL = "http://" + inventoryServerName + ":" + inventoryServerPort;

            response.setContentType(request.getContentType());
            final String reply = fetchInventory(request, inventoryServerURL);
            if (reply != null && !reply.isEmpty()) {
                outputStream.setWriteListener(new WriteListener() {

                    @Override
                    public synchronized void onWritePossible() throws IOException {
                        outputStream.write(reply.getBytes());
                        context.complete();
                        long endTime = System.currentTimeMillis();
                        log.info("FetchInventoryServlet_2 took {} ms", endTime - startTime);
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
            context.complete();
        }
    }

    @SuppressWarnings("unchecked")
    private String fetchInventory(HttpServletRequest request, String inventoryServerURL)
            throws IOException, XMLStreamException, InventoryException {

        log.debug("fetchInventory() called");
        String requestString = Util.requestContent2String(request);
        log.debug("Content: {}", requestString);

        HashMap<String, Object> llsdRequestMap = null;
        try {
            llsdRequestMap = (HashMap<String, Object>) LLSD.llsdDeserialize(requestString);
        } catch (Exception ex) {
            log.error("Fetch error: {} {}" + ex.getMessage(), ex.getCause());
            log.error("Request {}: ", request);
        }
        ArrayList<Object> itemsrequested = (ArrayList<Object>) llsdRequestMap.get("items");

        LLSDFetchInventory llsdReply = new LLSDFetchInventory();

        Iterator<Object> itemsIterator = itemsrequested.iterator();
        while (itemsIterator.hasNext()) {
            Map<String, Object> itemMap = (Map<String, Object>) itemsIterator.next();
            UUID itemID = (UUID) itemMap.get("item_id");

            InventoryItemBase item = inventoryService.getItem(new InventoryItemBase(itemID), inventoryServerURL);
            if (item != null) {
                // We don't know the agent that this request belongs to so we'll use the agent id of the item
                // which will be the same for all items.
                llsdReply.agent_id = item.getOwnerId();
                llsdReply.items.add(new LLSDInventoryItem(item));
            }
        }

        String response = new String();
        try {
            response = LLSD.LLSDSerialize(llsdReply);
        } catch (Exception ex) {
            log.error("LLSD serialize error: {} {}" + ex.getMessage(), ex.getCause());
            log.error("Request {}: ", request);
        }

        log.debug("outgoingresponse: {}", response);

        return (response);
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

}
