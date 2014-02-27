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
package org.openjgrid.agents;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple POJO that holds the information of a connected Agent.
 * Not sure if it is ever needed
 * 
 * @author Akira Sonoda 
 */
public final class Agent {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(Agent.class);

	public UUID agent_id = null;
	public String host = null;
	public String port = null;
	public String regionName = null;
	public String fetchinventory2_caps = null;
	public String fetchinventorydescendents2_caps = null;
	public String gettexture_caps = null;
	public String getmesh_caps = null;
	public String close_caps = null;

	public Agent() { }
	
	public Agent(Map<String, String> values) {
		this.agent_id = UUID.fromString(values.get("AgentID"));
		this.host = values.get("Host");
		this.port = values.get("Port");
		this.regionName = values.get("RegionName");
		this.fetchinventory2_caps = values.get("FetchInventory2");
		this.fetchinventorydescendents2_caps = values.get("FetchInventoryDescendents2");
		this.gettexture_caps = values.get("GetTexture");
		this.getmesh_caps = values.get("GetMesh");
		this.close_caps = values.get("AgentClose");
	}
	
}
