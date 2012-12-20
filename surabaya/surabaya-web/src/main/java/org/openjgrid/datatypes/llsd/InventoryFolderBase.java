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

import java.util.UUID;

/**
 * User inventory folder
 * 
 * @author Akira Sonoda
 */
@LLSDMapping(mapTo="struct", mappedName = "")
public class InventoryFolderBase extends InventoryNodeBase {

	/**
	 * The folder this folder is contained in
	 */
	
	@LLSDMapping(mapTo="uuid", mappedName = "ParentID")
	private UUID parentFolderId;
	/**
	 * Type of items normally stored in this folder
	 */
	@LLSDMapping(mapTo="integer", mappedName = "Type")	
	private int type;
	/**
	 * This is used to denote the version of the client, needed
     * because of the changes clients have with inventory from
     * time to time (1.19.1 caused us some fits there).
	 */
	@LLSDMapping(mapTo="integer", mappedName = "Version")
	private int version;
	
	
	/**
	 * @return the parent_id
	 */
	public UUID getParentFolderId() {
		return parentFolderId;
	}
	/**
	 * @param parent_id the parent_id to set
	 */
	public void setParentFolderId(UUID parentFolderId) {
		this.parentFolderId = parentFolderId;
	}
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}
	
    public InventoryFolderBase(UUID id) {
        super.setId(id);
    }

    public InventoryFolderBase(UUID id, UUID owner) {
    	super.setId(id);
        super.setOwnerId(owner);
    }

    public InventoryFolderBase(UUID id, String name, UUID ownerId, UUID parentFolderId) throws InventoryException {
    	super.setId(id);
    	super.setName(name);
    	super.setOwnerId(ownerId);
    	this.parentFolderId = parentFolderId;
    }
    
    public InventoryFolderBase(UUID id, String name, UUID ownerId, short type, UUID parentFolderId, short version) throws InventoryException {
    	super.setId(id);
    	super.setName(name);
    	super.setOwnerId(ownerId);
    	this.parentFolderId = parentFolderId;
    	this.type = type;
    	this.version = version;
    }
	
	
}
