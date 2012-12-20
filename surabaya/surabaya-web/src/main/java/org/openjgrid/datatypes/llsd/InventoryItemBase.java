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
@LLSDMapping(mapTo="struct", mappedName = "")
public class InventoryItemBase extends InventoryNodeBase implements Cloneable {

	private static final Logger log = LoggerFactory.getLogger(InventoryItemBase.class);
	
	/**
	 * The inventory type of the item.  This is slightly different from the asset type in some situations.
	 */
	@LLSDMapping(mapTo="integer", mappedName = "InvType")
	private int invType;
	
	/**
	 * The folder this item is contained in
	 */
	@LLSDMapping(mapTo="uuid", mappedName = "Folder")
	private UUID parentFolderId;
	
	/**
	 * The creator of this item
	 */
	@LLSDMapping(mapTo="string", mappedName = "CreatorId")
	private String creator;
	
	/**
	 * The CreatorId expressed as a UUID
	 */
	@LLSDMapping(mapTo="uuid", mappedName = "CreatorIdAsUuid")
	private UUID creatorId;

	/**
	 * Extended creator information of the form <profile url>;<name>
	 */
	@LLSDMapping(mapTo="string", mappedName = "CreatorData")
	private String creatorData;
	
	/**
	 * The description of the inventory item (must be less than 64 characters)
	 */
	@LLSDMapping(mapTo="string", mappedName = "Description")
	private String description;
	
	/**
	 * 
	 */
	@LLSDMapping(mapTo="integer", mappedName = "CurrentPermissions")
	private long currentPermissions;

	/**
	 * A mask containing permissions for the current owner (cannot be enforced)
	 */
	@LLSDMapping(mapTo="integer", mappedName = "NextPermissions")
	private long nextPermissions;
	
	/**
	 * 
	 */
	@LLSDMapping(mapTo="integer", mappedName = "BasePermissions")
	private long basePermissions;
	
	/**
	 * 
	 */
	@LLSDMapping(mapTo="integer", mappedName = "EveryOnePermissions")
	private long everyOnePermissions;
	
	/**
	 * 
	 */
	@LLSDMapping(mapTo="integer", mappedName = "GroupPermissions")
	private long groupPermissions;

	/**
	 * This is an enumerated value determining the type of asset (eg Notecard, Sound, Object, etc)
	 */
	@LLSDMapping(mapTo="integer", mappedName = "AssetType")
	private int assetType;
	
	/**
	 * The UUID of the associated asset on the asset server
	 */
	@LLSDMapping(mapTo="uuid", mappedName = "AssetID")
	private UUID assetId;
	
	/**
	 * 
	 */
	@LLSDMapping(mapTo="integer", mappedName = "GroupID")
	private UUID groupId;
	
	/**
	 * 
	 */
	@LLSDMapping(mapTo="boolean", mappedName = "GroupOwned")
	private boolean isGroupOwned;
	
	/**
	 * 
	 */
	@LLSDMapping(mapTo="integer", mappedName = "SalePrice")
	private int salePrice;
	
	/**
	 * 
	 */
	@LLSDMapping(mapTo="integer", mappedName = "SaleType")
	private byte saleType;
	
	/**
	 * 
	 */
	@LLSDMapping(mapTo="integer", mappedName = "Flags")
	private long flags;
	
	/**
	 * 
	 */
	@LLSDMapping(mapTo="integer", mappedName = "CreationDate")
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
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the creatorId
	 */
	public UUID getCreatorId() {
		return creatorId;
	}

	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(UUID creatorId) {
		this.creatorId = creatorId;
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
            creatorId = UUID.fromString(creatorIdentification);
        }  else {
        	// <uuid>[;<endpoint>[;name]]
            String  name = "Unknown User";
            String[] parts = creatorIdentification.split(";");
            if (parts.length >= 1)
                creatorId = UUID.fromString(parts[0]);
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
		if (description.length() > 64) {
			throw new InventoryException("InventoryItemBase: Description exceeds 64 chars"); 
		}
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
		clone.setCreator(this.getCreator());
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
    
    

}
