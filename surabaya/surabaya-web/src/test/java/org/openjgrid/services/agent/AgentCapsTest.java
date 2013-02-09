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
package org.openjgrid.services.agent;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Akira Sonoda
 *
 */
public class AgentCapsTest {

	ObjectMapper mapper; 

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		mapper = new ObjectMapper();
	}

	@Test
	public void testObj2JSON() throws Exception {
		String jsonString = "{\"capsMap\":{\"GetTexture\":\"26cecb27-ea2c-4152-b319-d06dd0b516ae\",\"FetchInventoryDescendents2\":\"d5cb3302-2db8-4881-94de-161ce09df1a6\",\"GetMesh\":\"24f57f22-59b0-4c4a-a2c2-95a8858de972\",\"FetchInventory2\":\"e1563faf-a081-4410-b160-ea1639aef192\"}}";
		AgentCaps agentCaps = new AgentCaps();
		agentCaps.capsMap.put("FetchInventoryDescendents2", "d5cb3302-2db8-4881-94de-161ce09df1a6");
		agentCaps.capsMap.put("FetchInventory2", "e1563faf-a081-4410-b160-ea1639aef192");
		agentCaps.capsMap.put("GetTexture", "26cecb27-ea2c-4152-b319-d06dd0b516ae");
		agentCaps.capsMap.put("GetMesh", "24f57f22-59b0-4c4a-a2c2-95a8858de972");
				
		assert(mapper.writeValueAsString(agentCaps).equals(jsonString));

	}

	@Test
	public void testObjFromJSON() throws Exception {
		String jsonString = "{\"capsMap\":{\"GetTexture\":\"26cecb27-ea2c-4152-b319-d06dd0b516ae\",\"FetchInventoryDescendents2\":\"d5cb3302-2db8-4881-94de-161ce09df1a6\",\"GetMesh\":\"24f57f22-59b0-4c4a-a2c2-95a8858de972\",\"FetchInventory2\":\"e1563faf-a081-4410-b160-ea1639aef192\"}}";
		AgentCaps agentCaps = mapper.readValue(jsonString, AgentCaps.class);
		assert(agentCaps.capsMap.size() == 4);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testObjFromOsdJSON() throws Exception {
		String jsonString = "{\"FetchInventoryDescendents2\":\"d5cb3302-2db8-4881-94de-161ce09df1a6\",\"FetchInventory2\":\"e1563faf-a081-4410-b160-ea1639aef192\",\"GetTexture\":\"26cecb27-ea2c-4152-b319-d06dd0b516ae\",\"GetMesh\":\"24f57f22-59b0-4c4a-a2c2-95a8858de972\"}";
		AgentCaps agentCaps = new AgentCaps();
		agentCaps.capsMap = mapper.readValue(jsonString, agentCaps.capsMap.getClass());
		assert(agentCaps.capsMap.get("GetMesh").equals("24f57f22-59b0-4c4a-a2c2-95a8858de972"));
		assert(agentCaps.capsMap.get("FetchInventoryDescendents2").equals("d5cb3302-2db8-4881-94de-161ce09df1a6"));
		assert(agentCaps.capsMap.get("FetchInventory2").equals("e1563faf-a081-4410-b160-ea1639aef192"));
		assert(agentCaps.capsMap.get("GetMesh").equals("24f57f22-59b0-4c4a-a2c2-95a8858de972"));

		
		
	}
}
