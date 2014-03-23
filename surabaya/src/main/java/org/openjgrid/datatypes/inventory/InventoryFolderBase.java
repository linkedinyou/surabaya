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
package org.openjgrid.datatypes.inventory;

import java.io.InputStreamReader;
import java.util.UUID;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;

/**
 * User inventory folder
 * 
 * @author Akira Sonoda
 */
public class InventoryFolderBase extends InventoryNodeBase {

	/**
	 * The folder this folder is contained in
	 */
	private UUID parentFolderId;

	/**
	 * Type of items normally stored in this folder
	 */
	private int type;
	/**
	 * This is used to denote the version of the client, needed
     * because of the changes clients have with inventory from
     * time to time (1.19.1 caused us some fits there).
	 */
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
	
	public InventoryFolderBase() {
		
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
	
    public void fromXml(String xmlString) throws XMLStreamException, InventoryException {
		InputStreamReader inputStreamReader = new InputStreamReader(IOUtils.toInputStream(xmlString));
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStream = xmlInputFactory.createXMLStreamReader(inputStreamReader);
		xmlStream.next();
		if (!xmlStream.isStartElement() || !xmlStream.getLocalName().equalsIgnoreCase("ServerResponse")) {
			throw new XMLStreamException("Expected <ServerResponse>");
		}
		xmlStream.next();
		if (xmlStream.isEndElement() && xmlStream.getLocalName().equalsIgnoreCase("ServerResponse")) {
			return;
		}
		xmlStream.next();
		while(!xmlStream.getLocalName().equalsIgnoreCase("folder") || !xmlStream.isEndElement()) {
			String itemName = xmlStream.getLocalName();
			if( itemName.equalsIgnoreCase("ParentID")) {
				this.setParentFolderId(UUID.fromString(xmlStream.getElementText().trim()));
			} else if (itemName.equalsIgnoreCase("Type")) {
				this.setType(Integer.parseInt(xmlStream.getElementText().trim()));
			} else if (itemName.equalsIgnoreCase("Version")) {
				this.setVersion(Integer.parseInt(xmlStream.getElementText().trim()));
			} else if (itemName.equalsIgnoreCase("Name")) {
				this.setName(xmlStream.getElementText().trim());
			} else if( itemName.equalsIgnoreCase("Owner")) {
				this.setOwnerId(UUID.fromString(xmlStream.getElementText().trim()));
			} else if( itemName.equalsIgnoreCase("ID")) {
				this.setId(UUID.fromString(xmlStream.getElementText().trim()));
			} else {
				throw new XMLStreamException("Unable to assign item with name: " + itemName);				
			}
			xmlStream.next();
		}
    }
}
