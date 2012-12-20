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
@LLSDMapping(mapTo="map", mappedName = "")
public class LLSDFetchInventoryDescendents {
	
	private static final Logger log = LoggerFactory.getLogger(LLSDFetchInventoryDescendents.class);

	public UUID folder_id;
    public UUID owner_id;
    public int sort_order;
    public boolean fetch_folders;
    public boolean fetch_items;

    
    /**
	 * @param inventoryHashMap
	 */
	public LLSDFetchInventoryDescendents( HashMap<String, Object> inventoryHashMap) {
		fromLLSDMap(inventoryHashMap);	
	}


	private void fromLLSDMap(HashMap<String, Object> map) {
		try {
			
			this.fetch_folders = (Boolean) map.get("fetch_folders");
			this.fetch_items = (Boolean) map.get("fetch_items");
			this.folder_id = (UUID) map.get("folder_id");
			this.owner_id = (UUID) map.get("owner_id");
			this.sort_order = (Integer) map.get("sort_order");
			
		} catch (Exception ex) {
			log.error("Exception during conversion from LLSDMap: {}", ex.getMessage());
		}
    }
}

