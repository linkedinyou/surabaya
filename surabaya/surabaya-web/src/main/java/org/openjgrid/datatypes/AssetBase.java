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
package org.openjgrid.datatypes;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Asset class.
 * 
 * All Assets are reference by this class or a class derived from this class
 * 
 * @author Akira Sonoda
 */
public class AssetBase {
	private static final Logger log = LoggerFactory.getLogger(AssetBase.class);

	/**
	 * Data of the Asset
	 */
	private byte[] data;

	/**
	 * MetaData of the Asset
	 */
	private AssetMetadata metadata;

	public AssetBase() {
		metadata = new AssetMetadata();
		metadata.setFullID(Constants.UUID_ZERO);
		metadata.setID(Constants.UUID_ZERO.toString());
		metadata.setType(AssetType.Unknown.getAssetType());
		metadata.setCreatorID("");
	}

	public AssetBase(UUID assetID, String name, byte assetType, String creatorID) {
		if (assetType == AssetType.Unknown.getAssetType()) {
			log.error("Creating asset '{}' ({}) with an unknown asset type", name, assetID);
		}

		metadata = new AssetMetadata();
		metadata.setFullID(assetID);
		metadata.setName(name);
		metadata.setType(assetType);
		metadata.setCreatorID(creatorID);
	}

	public AssetBase(String assetID, String name, byte assetType, String creatorID) {
		if (assetType == AssetType.Unknown.getAssetType()) {
			log.error("Creating asset '{}' ({}) with an unknown asset type\n", name, assetID);
		}

		metadata = new AssetMetadata();
		metadata.setID(assetID);
		metadata.setName(name);
		metadata.setType(assetType);
		metadata.setCreatorID(creatorID);
	}

	public boolean hasReferences() {
		int type = metadata.getType();
		return (isTextualAsset() && (type != AssetType.Notecard.getAssetType() && type != AssetType.CallingCard.getAssetType()
				&& type != AssetType.LSLText.getAssetType() && type != AssetType.Landmark.getAssetType()));
	}

	public boolean isTextualAsset() {
		return (!isBinaryAsset());
	}

	/**
	 * <summary> Checks if this asset is a binary or text asset
	 */
	public boolean isBinaryAsset() {
		int type = metadata.getType();
		return (type == AssetType.Animation.getAssetType() || type == AssetType.Gesture.getAssetType()
				|| type == AssetType.Simstate.getAssetType() || type == AssetType.Unknown.getAssetType()
				|| type == AssetType.Object.getAssetType() || type == AssetType.Sound.getAssetType()
				|| type == AssetType.SoundWAV.getAssetType() || type == AssetType.Texture.getAssetType()
				|| type == AssetType.TextureTGA.getAssetType() || type == AssetType.Folder.getAssetType()
				|| type == AssetType.RootFolder.getAssetType() || type == AssetType.LostAndFoundFolder.getAssetType()
				|| type == AssetType.SnapshotFolder.getAssetType() || type == AssetType.TrashFolder.getAssetType()
				|| type == AssetType.ImageJPEG.getAssetType() || type == AssetType.ImageTGA.getAssetType() || type == AssetType.LSLBytecode
					.getAssetType());
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
	
    /// <summary>
    /// Asset MetaData ID (transferring from UUID to string ID)
    /// </summary>
    public String getID() { 
    	return (metadata.getID()); 
    }
    public void setID(String value) { 
    	metadata.setID(value); 
    }
   
    public String getName() {
    	return (metadata.getName()); 
    }
    public void setName(String value) { 
    	metadata.setName(value); 
    }

    public String getDescription() {
    	return (metadata.getDescription()); 
    }
    public void setDescription(String value) { 
    	metadata.setDescription(value); 
    }

    /** 
     *  AssetType enum
     */ 
    public byte getType() {
       return(metadata.getType()); 
    }
    public void setType(byte value) { 
    	metadata.setType(value);
    }

    /**
     * Is this a region only asset, or does this exist on the asset server also
     */ 
    public boolean isLocal() {
        return(metadata.isLocal()); 
    }
    
    public void isLocal(boolean value) { 
    	metadata.isLocal(value); 
    }

    /**
     * Is this asset going to be saved to the asset database?
     */
    public boolean isTemporary() {
    	return (metadata.isTemporary()); 
    }
    public void isTemporary(boolean value) { 
    	metadata.isTemporary(value); 
    }

    public String getCreatorID() {
        return (metadata.getCreatorID()); 
    }
    public void setCreatorID(String value) { 
    	metadata.setCreatorID(value); 
    }

    public int getFlags() {
        return( metadata.getFlags()); 
    }
    public void setFlags(int value) { 
    	metadata.setFlags(value); 
    }

    public AssetMetadata getMetadata() {
        return (metadata); 
    }
    public void setMetadata(AssetMetadata value) { 
    	metadata = value; 
    }
}
