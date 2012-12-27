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

import java.io.InputStreamReader;
import java.util.UUID;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inventory Item - contains all the properties associated with an individual inventory piece.
 * 
 * @author Akira Sonoda
 *
 */
public class InventoryItemBase extends InventoryNodeBase implements Cloneable {

	private static final Logger log = LoggerFactory.getLogger(InventoryItemBase.class);
	
	/**
	 * The inventory type of the item.  This is slightly different from the asset type in some situations.
	 */
	private int invType;
	
	/**
	 * The folder this item is contained in
	 */
	private UUID parentFolderId;
	
	/**
	 * The creator of this item
	 */
	private String creatorId;
	

	/**
	 * Extended creator information of the form <profile url>;<name>
	 */
	private String creatorData;
	
	/**
	 * The description of the inventory item (must be less than 64 characters)
	 */
	private String description;
	
	/**
	 * 
	 */
	private long currentPermissions;

	/**
	 * A mask containing permissions for the current owner (cannot be enforced)
	 */
	private long nextPermissions;
	
	/**
	 * 
	 */
	private long basePermissions;
	
	/**
	 * 
	 */
	private long everyOnePermissions;
	
	/**
	 * 
	 */
	private long groupPermissions;

	/**
	 * This is an enumerated value determining the type of asset (eg Notecard, Sound, Object, etc)
	 */
	private int assetType;
	
	/**
	 * The UUID of the associated asset on the asset server
	 */
	private UUID assetId;
	
	/**
	 * 
	 */
	private UUID groupId;
	
	/**
	 * 
	 */
	private boolean isGroupOwned;
	
	/**
	 * 
	 */
	private int salePrice;
	
	/**
	 * 
	 */
	private byte saleType;
	
	/**
	 * 
	 */
	private long flags;
	
	/**
	 * 
	 */
	private long creationDate;

	/**
	 * @return the type
	 */
	public int getInvType() {
		return invType;
	}

	/**
	 * @param type the type to set
	 */
	public void setInvType(int invType) {
		this.invType = invType;
	}

	/**
	 * @return the parentFolderId
	 */
	public UUID getParentFolderId() {
		return parentFolderId;
	}

	/**
	 * @param parentFolderId the parentFolderId to set
	 */
	public void setParentFolderId(UUID parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	/**
	 * @return the creator
	 */
	public String getCreatorId() {
		return creatorId;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	/**
	 * @return the creatorId
	 */
	public UUID getCreatorIdAsUUID() {
		return UUID.fromString(creatorId);
	}


	/**
	 * @return the creatorData
	 */
	public String getCreatorData() {
		return creatorData;
	}

	/**
	 * @param creatorData the creatorData to set
	 */
	public void setCreatorData(String creatorData) {
		this.creatorData = creatorData;
	}

	/**
	 * Used by the DB layer to retrieve / store the entire user identification.
     * The identification can either be a simple UUID or a string of the form
     * uuid[;profile_url[;name]]
     * 
	 * @return the creatorIdentification
	 */
	public String getCreatorIdentification() {
        if (creatorData != null && !creatorData.isEmpty()) {
            return (creatorId.toString() + ';' + creatorData);
        } else {
            return (creatorId.toString());
        }
	}

	/**
	 * Used by the DB layer to retrieve / store the entire user identification.
     * The identification can either be a simple UUID or a string of the form
     * uuid[;profile_url[;name]]
     * 
	 * @param creatorIdentification the creatorIdentification to set
	 */
	public void setCreatorIdentification(String creatorIdentification) {
        if ((creatorIdentification == null) || (creatorIdentification != null && creatorIdentification.isEmpty())) {
            creatorData = "";
            return;
        }

        if (!creatorIdentification.contains(";")) {
            // plain UUID
            creatorId = creatorIdentification;
        }  else {
        	// <uuid>[;<endpoint>[;name]]
            String  name = "Unknown User";
            String[] parts = creatorIdentification.split(";");
            if (parts.length >= 1)
                creatorId = parts[0];
            if (parts.length >= 2)
                creatorData = parts[1];
            if (parts.length >= 3)
                name = parts[2];

            creatorData += ';' + name;
        }
    }

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 * @throws InventoryException 
	 */
	public void setDescription(String description) throws InventoryException {
		this.description = description;
	}

	/**
	 * @return the currentPermissions
	 */
	public long getCurrentPermissions() {
		return currentPermissions;
	}

	/**
	 * @param currentPermissions the currentPermissions to set
	 */
	public void setCurrentPermissions(long currentPermissions) {
		this.currentPermissions = currentPermissions;
	}

	/**
	 * @return the nextPermissions
	 */
	public long getNextPermissions() {
		return nextPermissions;
	}

	/**
	 * @param nextPermissions the nextPermissions to set
	 */
	public void setNextPermissions(long nextPermissions) {
		this.nextPermissions = nextPermissions;
	}

	/**
	 * @return the basePermissions
	 */
	public long getBasePermissions() {
		return basePermissions;
	}

	/**
	 * @param basePermissions the basePermissions to set
	 */
	public void setBasePermissions(long basePermissions) {
		this.basePermissions = basePermissions;
	}

	/**
	 * @return the everyOnePermissions
	 */
	public long getEveryOnePermissions() {
		return everyOnePermissions;
	}

	/**
	 * @param everyOnePermissions the everyOnePermissions to set
	 */
	public void setEveryOnePermissions(long everyOnePermissions) {
		this.everyOnePermissions = everyOnePermissions;
	}

	/**
	 * @return the groupPermissions
	 */
	public long getGroupPermissions() {
		return groupPermissions;
	}

	/**
	 * @param groupPermissions the groupPermissions to set
	 */
	public void setGroupPermissions(long groupPermissions) {
		this.groupPermissions = groupPermissions;
	}

	/**
	 * @return the assetType
	 */
	public int getAssetType() {
		return assetType;
	}

	/**
	 * @param assetType the assetType to set
	 */
	public void setAssetType(int assetType) {
		this.assetType = assetType;
	}

	/**
	 * @return the assetId
	 */
	public UUID getAssetId() {
		return assetId;
	}

	/**
	 * @param assetId the assetId to set
	 */
	public void setAssetId(UUID assetId) {
		this.assetId = assetId;
	}

	/**
	 * @return the groupId
	 */
	public UUID getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(UUID groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the isGroupOwned
	 */
	public boolean isGroupOwned() {
		return isGroupOwned;
	}

	/**
	 * @param isGroupOwned the isGroupOwned to set
	 */
	public void isGroupOwned(boolean isGroupOwned) {
		this.isGroupOwned = isGroupOwned;
	}

	/**
	 * @return the salePrice
	 */
	public int getSalePrice() {
		return salePrice;
	}

	/**
	 * @param salePrice the salePrice to set
	 */
	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * @return the saleType
	 */
	public byte getSaleType() {
		return saleType;
	}

	/**
	 * @param saleType the saleType to set
	 */
	public void setSaleType(byte saleType) {
		this.saleType = saleType;
	}

	/**
	 * @return the flags
	 */
	public long getFlags() {
		return flags;
	}

	/**
	 * @param flags the flags to set
	 */
	public void setFlags(long flags) {
		this.flags = flags;
	}

	/**
	 * @return the creationDate
	 */
	public long getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	
    public InventoryItemBase() {
    	creationDate = secondsSinceEpoch();
    }
    
    public InventoryItemBase(UUID id) {
    	super.setId(id);
    	creationDate = secondsSinceEpoch();
    }
    
    public InventoryItemBase(UUID id, UUID ownerId) {
    	super.setId(id);
    	super.setOwnerId(ownerId);
    	creationDate = secondsSinceEpoch();
    }
    
    
    private long secondsSinceEpoch() {
    	MutableDateTime epoch = new MutableDateTime();
    	epoch.setDate(0);
    	DateTime now = DateTime.now();
    	Seconds seconds = Seconds.secondsBetween(epoch, now);
    	return(seconds.getSeconds());
    }

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		InventoryItemBase clone = new InventoryItemBase();
		try {
		clone.setAssetId(this.getAssetId());
		clone.setAssetType(this.getAssetType());
		clone.setBasePermissions(this.getBasePermissions());
		clone.setCreationDate(this.getCreationDate());
		clone.setCreatorData(this.getCreatorData());
		clone.setCreatorId(this.getCreatorId());
		clone.setCreatorIdentification(this.getCreatorIdentification());
		clone.setCurrentPermissions(this.getCurrentPermissions());
		clone.setDescription(this.getDescription());
		clone.setEveryOnePermissions(this.getEveryOnePermissions());
		clone.setFlags(this.getFlags());
		clone.setGroupId(this.getGroupId());
		clone.isGroupOwned(this.isGroupOwned());
		clone.setGroupPermissions(this.getGroupPermissions());
		clone.setId(this.getId());
		clone.setName(this.getName());
		clone.setNextPermissions(this.getNextPermissions());
		clone.setOwnerId(this.getOwnerId());
		clone.setParentFolderId(this.getParentFolderId());
		clone.setSalePrice(this.getSalePrice());
		clone.setSaleType(this.getSaleType());
		clone.setInvType(this.getInvType());
		} catch ( InventoryException ex ) {
			log.error("InventoryException in clone() - this really should not happpen here" );  
		}
		return(clone);
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
		while(!xmlStream.getLocalName().equalsIgnoreCase("item") || !xmlStream.isEndElement()) {
			String itemName = xmlStream.getLocalName();
			if( itemName.equalsIgnoreCase("AssetID")) {
				this.setAssetId(UUID.fromString(xmlStream.getElementText().trim()));
			} else if (itemName.equalsIgnoreCase("AssetType")) {
				this.setAssetType(Integer.parseInt(xmlStream.getElementText().trim()));
			} else if (itemName.equalsIgnoreCase("BasePermissions")) {
				this.setBasePermissions(Long.parseLong(xmlStream.getElementText().trim()));
			} else if (itemName.equalsIgnoreCase("CreationDate")) {
				this.setCreationDate(Long.parseLong(xmlStream.getElementText().trim()));
			} else if( itemName.equalsIgnoreCase("CreatorId")) {
				this.setCreatorId(xmlStream.getElementText().trim());
			} else if( itemName.equalsIgnoreCase("CreatorData")) {
				this.setCreatorData(xmlStream.getElementText().trim());
			} else if (itemName.equalsIgnoreCase("CurrentPermissions")) {
				this.setCurrentPermissions(Long.parseLong(xmlStream.getElementText().trim()));
			} else if( itemName.equalsIgnoreCase("Description")) {
				this.setDescription(xmlStream.getElementText().trim());
			} else if (itemName.equalsIgnoreCase("EveryOnePermissions")) {
				this.setEveryOnePermissions(Long.parseLong(xmlStream.getElementText().trim()));
			} else if (itemName.equalsIgnoreCase("Flags")) {
				this.setFlags(Long.parseLong(xmlStream.getElementText().trim()));
			} else if( itemName.equalsIgnoreCase("Folder")) {
				this.setParentFolderId(UUID.fromString(xmlStream.getElementText().trim()));
			} else if( itemName.equalsIgnoreCase("GroupID")) {
				this.setGroupId(UUID.fromString(xmlStream.getElementText().trim()));
			} else if( itemName.equalsIgnoreCase("GroupOwned")) {
				this.isGroupOwned(Boolean.parseBoolean(xmlStream.getElementText().trim()));
			} else if (itemName.equalsIgnoreCase("GroupPermissions")) {
				this.setGroupPermissions(Long.parseLong(xmlStream.getElementText().trim()));
			} else if( itemName.equalsIgnoreCase("ID")) {
				this.setId(UUID.fromString(xmlStream.getElementText().trim()));
			} else if( itemName.equalsIgnoreCase("InvType")) {
				this.setInvType(Integer.parseInt(xmlStream.getElementText().trim()));
			} else if( itemName.equalsIgnoreCase("Name")) {
				this.setName(xmlStream.getElementText().trim());
			} else if (itemName.equalsIgnoreCase("NextPermissions")) {
				this.setNextPermissions(Long.parseLong(xmlStream.getElementText().trim()));
			} else if (itemName.equalsIgnoreCase("Owner")) {
				this.setOwnerId(UUID.fromString(xmlStream.getElementText().trim()));
			} else if (itemName.equalsIgnoreCase("SalePrice")) {
				this.setSalePrice(Integer.parseInt(xmlStream.getElementText().trim()));
			} else if (itemName.equalsIgnoreCase("SaleType")) {
				this.setSaleType(Byte.parseByte(xmlStream.getElementText().trim()));
			} else {
				throw new XMLStreamException("Unable to assign item with name: " + itemName);				
			}
			xmlStream.next();
		}
		
	}
    

}
