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

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.openjgrid.datatypes.asset.AssetBase;
import org.openjgrid.util.HexDump;
import org.openjgrid.util.Util;

/**
 * This test actually does not test the Servlet itself,
 * but is useful to resolve issues from the servlet, whenever
 * a possibly incorrect asset is requested.
 * 
 * @author Akira Sonoda
 */
public class MeshServletTest {


	@Test
	public void testGetMesh() throws ClientProtocolException, IOException, XMLStreamException {

		String content = null;
		String assetID = "28ede4f8-1e19-4f9a-a88b-9d79572ca78c";
		AssetBase assetBase = new AssetBase();
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://assets.osgrid.org/assets/" + assetID);
		HttpResponse httpResponse = httpclient.execute(httpget);
		HttpEntity entity = httpResponse.getEntity();

		long contentLength = entity.getContentLength();
		Header contentType = entity.getContentType();
		content = IOUtils.toString(entity.getContent());
		System.out.println("ContentLength: " + contentLength);
		System.out.println("ContentType: " + contentType);
		if(!Util.isNullOrEmpty(content)) {
			System.out.println("80 bytes respstring: " + content);
		}
		
		assetBase.fromXml(content);
		
		System.out.println("Mesh requested: " + assetID + " Type received: " + assetBase.getType());
		System.out.println("     with Name: " + assetBase.getName() );
		System.out.println("   Description: " + assetBase.getDescription() );
		System.out.println("  Data decided: " + HexDump.format(assetBase.getData()));
	
	}

}
