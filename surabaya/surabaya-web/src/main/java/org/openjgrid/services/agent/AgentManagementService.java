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

import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.openjgrid.agents.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This service manages the Agents connected to the various OpenSim Regions
 * 
 * An Agent can accessed using various keys. Currently known are:
 *  
 *  - the Agent ID A string representation of the Agent
 *  - FetchInventoryCAPS ID (UUID which is unique to each agent)
 *  - FetchInventoryDescendantCAPS ID (UUID which is unique to each agent)
 *  - GetTextureCAPS ID (UUID which is unique to each agent)
 *  - GetMeshCAPS ID (UUID which is unique to each Agent) 
 * 
 * Those CAPS ID are a Security Mechanism because they are generated each Time 
 * an Agent is created. 
 * 
 * All of the get and remove Operations are considered to be successful.
 * Therefore an Exception is thrown, whenever such an operation does not succeed.
 * 
 * TODO Implement Housekeeping Functions
 * 
 * @author Akira Sonoda
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class AgentManagementService {
	
	private static final Logger log = LoggerFactory.getLogger(AgentManagementService.class);
	private ConcurrentMap<String, Agent> agentMap = null;
	
	private Vector<String> textureCapsIds = null;
	private Vector<String> meshCapsIds = null;
	private Vector<String> inventoryCapsIds = null;
	private Vector<String> inventoryDescendentsCapsIds = null;
	private Vector<String> closeCapsIds = null;
	
	@PostConstruct
	public void create() throws Exception {
		log.debug("create() called");
		agentMap = new ConcurrentHashMap<String, Agent>(); 
		textureCapsIds = new Vector<String>();
		meshCapsIds = new Vector<String>();
		inventoryCapsIds = new Vector<String>();
		inventoryDescendentsCapsIds = new Vector<String>();
		closeCapsIds = new Vector<String>();
		
	}

	public void setAgent(String agentId, Agent agent) {
		log.debug("setAgent() called: agentMap.size: {}", agentMap.size());
		agentMap.put(agentId, agent);
		textureCapsIds.add(agent.gettexture_caps);
		meshCapsIds.add(agent.getmesh_caps);
		inventoryCapsIds.add(agent.fetchinventory2_caps);
		inventoryDescendentsCapsIds.add(agent.fetchinventorydescendents2_caps);
		closeCapsIds.add(agent.close_caps);
	}

	public boolean hasTextureCapsId(String id) {
		return (textureCapsIds.contains(id));
	}
	
	public boolean hasMeshCapsId(String id) {
		return (meshCapsIds.contains(id));
	}

	public boolean hasInventoryCapsId(String id) {
		return (inventoryCapsIds.contains(id));
	}

	public boolean hasInventoryDescendentsCapsId(String id) {
		return (inventoryDescendentsCapsIds.contains(id));
	}
	
	public boolean hasCloseCapsId(String id) {
		return (closeCapsIds.contains(id));
	}
	/**
	 * @throws AgentNotFoundException 
	 */
	public Agent getAgent(String agentId) throws AgentNotFoundException {
		log.debug("getAgent() called: agentMap.size: {}", agentMap.size());
		if(agentMap.containsKey(agentId)) {
			return (agentMap.get(agentId));
		} else {
			throw(new AgentNotFoundException());
		}
	}

	public Agent getAgent(UUID capsID) throws AgentNotFoundException {
		log.debug("getAgent() called: agentMap.size: {}", agentMap.size());
		if(agentMap.containsKey(capsID.toString())) {
			return (agentMap.get(capsID.toString()));
		} else {
			throw(new AgentNotFoundException());
		}
	}

	public void removeAgent(String agentID) throws AgentNotFoundException {
		log.debug("removeAgent() called: agentMap.size: {}", agentMap.size());
		if(agentMap.containsKey(agentID)) {
			Agent anAgent = agentMap.get(agentID);
			removeAllAgentEntries(anAgent);
		} else {
			throw(new AgentNotFoundException());
		}		
	}

	public void removeAgent(UUID capsID) throws AgentNotFoundException {
		log.debug("removeAgent() called: agentMap.size: {}", agentMap.size());
		if(agentMap.containsKey(capsID.toString())) {
			Agent anAgent = agentMap.get(capsID.toString());
			removeAllAgentEntries(anAgent);
		} else {
			throw(new AgentNotFoundException());
		}		
	}

	public boolean hasAgent(UUID capsID) {
		log.debug("hasAgent() called: agentMap.size: {}", agentMap.size());
		if(agentMap.containsKey(capsID.toString())) {
			return(true);
		} else {
			return(false);
		}		
	}
	
	private void removeAllAgentEntries(Agent anAgent) {
		// First remove all CAPS entries
		if(anAgent.fetchinventory2_caps != null) {
			inventoryCapsIds.remove(anAgent.fetchinventory2_caps);
		}
		if(anAgent.fetchinventorydescendents2_caps != null) {
			inventoryDescendentsCapsIds.remove(anAgent.fetchinventorydescendents2_caps);
		}
		if(anAgent.getmesh_caps != null) {
			meshCapsIds.remove(anAgent.getmesh_caps);
		}
		if(anAgent.gettexture_caps != null) {
			textureCapsIds.remove(anAgent.gettexture_caps);
		}
		if(anAgent.close_caps != null) {
			closeCapsIds.remove(anAgent.close_caps);
		}
		// Then remove the Agent itself
		agentMap.remove(anAgent.agent_id);
	}
}
