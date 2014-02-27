/*******************************************************************************
 * Copyright (c) 2014 Akira Sonoda.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Akira Sonoda - initial API and implementation
 ******************************************************************************/
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.openjgrid.services.asset.AssetService;
import org.openjgrid.services.asset.SerializedAssetCaps;
import org.openjgrid.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TextureCacheServlet
 * 
 * Whenever an Agent lands on a Region or changes its outfit. all the texture Layers will 
 * be baked to a single Texture and this Texture is stored in the cache of the Region Server
 * 
 * Because of the separated Caches of the RegionServer (Flotsam) and the Surabaya Infinispan
 * cache the baked Textures have to be copied from the RegionServer to Surabaya in order to
 * make them accessible using the GetTexture CAPS
 * 
 * Author: Akira Sonoda
 */
@WebServlet(name = "TextureCacheServlet", urlPatterns = { "/cachetexture" })
public class TextureCacheServlet extends HttpServlet {
	private static final long serialVersionUID = 6389366551364552515L;
	private static final Logger log = LoggerFactory.getLogger(TextureCacheServlet.class);

	@EJB
	private AssetService assetService;
	
	@SuppressWarnings("unchecked")
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("TextureCacheServlet");
		long stime = System.currentTimeMillis();
        response.setContentType("text/xml;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        Map<String, String> result = new HashMap<String, String>();
		ObjectMapper objectMapper = new ObjectMapper();

        try {
        	if (request.getContentType().equalsIgnoreCase("application/json")) {
        		String jsonString = Util.requestContent2String(request);
        		SerializedAssetCaps serializedAssetCaps = new SerializedAssetCaps();
        		serializedAssetCaps.capsMap = objectMapper.readValue(jsonString, serializedAssetCaps.capsMap.getClass());
        		String assetID =  serializedAssetCaps.capsMap.get("assetID");

        		boolean isTemporary = Boolean.parseBoolean(serializedAssetCaps.capsMap.get("temporary"));
        		String serializedAsset = serializedAssetCaps.capsMap.get("serializedAsset");
        		
        		if (!Util.isNullOrEmpty(assetID) && !Util.isNullOrEmpty(serializedAsset)) {
        			if(isTemporary) {
        				assetService.cacheAsset(assetID, serializedAsset, 1, TimeUnit.DAYS);
        			} else {
        				assetService.cacheAsset(assetID, serializedAsset);        				
        			}
                	result.put("result", "ok");
        		} else {
                	result.put("result", "fail");
                	result.put("reason", "assetID or serializedAsset empty");
        		}
        		
        	} else {
        		log.error("Unexpected ContentType: {}", request.getContentType());
                Util.dumpHttpRequest(request);
                result.put("result", "fail");
                result.put("reason", "invalid ContentType : "+ request.getContentType());
        	}
        	
        	StringEntity resultStringEntity = new StringEntity(objectMapper.writeValueAsString(result));
        	resultStringEntity.writeTo(out);
        	out.flush();
        	out.close();
        	
        } catch (Exception ex) {
        	log.debug("Exception occurred in AgentServlet.processRequest():",ex);
        }

        long rtime = System.currentTimeMillis();
		log.info("TextureCacheServlet took {} ms", rtime-stime);

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
