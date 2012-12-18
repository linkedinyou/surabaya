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
	
	@PostConstruct
	public void create() throws Exception {
		log.debug("create() called");
		agentMap = new ConcurrentHashMap<String, Agent>(); 
	}

	public void setAgent(String agentId, Agent agent) {
		log.debug("setAgent() called: agentMap.size: {}", agentMap.size());
		agentMap.put(agentId, agent);
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
		if(anAgent.getFetchinventory2_caps() != null) {
			agentMap.remove(anAgent.getFetchinventory2_caps().toString());
		}
		if(anAgent.getFetchinventorydescendents2_caps() != null) {
			agentMap.remove(anAgent.getFetchinventorydescendents2_caps().toString());
		}
		if(anAgent.getGetmesh_caps() != null) {
			agentMap.remove(anAgent.getGetmesh_caps().toString());
		}
		if(anAgent.getGettexture_caps() != null) {
			agentMap.remove(anAgent.getGettexture_caps().toString());
		}
		if(anAgent.getCaps_path() != null) {
			agentMap.remove(anAgent.getCaps_path());
		}
		// Then remove the Agent itself
		agentMap.remove(anAgent.getAgent_id());
	}
}
