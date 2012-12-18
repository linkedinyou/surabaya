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
package org.openjgrid.datatypes.llsd;

import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Akira Sonoda
 *
 */
public class LLSDHelpers {
	
	private static final Logger log = LoggerFactory.getLogger(LLSDHelpers.class);
	
	public static LLSDFetchInventoryDescendents fromLLSDMap(HashMap<String, Object> map) {
		LLSDFetchInventoryDescendents reply = new LLSDFetchInventoryDescendents();
		try {
			
			reply.fetch_folders = (Boolean) map.get("fetch_folders");
			reply.fetch_items = (Boolean) map.get("fetch_items");
			reply.folder_id = (UUID) map.get("folder_id");
			reply.owner_id = (UUID) map.get("owner_id");
			reply.sort_order = (Integer) map.get("sort_order");
			
		} catch (Exception ex) {
			log.error("Exception during conversion from LLSDMap: {}", ex.getMessage());
		}
		return(reply);
	}

	/**
	 * @param inventoryReply
	 * @return
	 */
	public static String toLLSDString(LLSDInventoryDescendents inventoryReply) {
		// TODO Auto-generated method stub
		return null;
	}
}
