/*******************************************************************************
 * Copyright (c) 2014 Akira Sonoda.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Akira Sonoda - initial API and implementation
 ******************************************************************************/
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

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryFolder extends InventoryFolderBase {
	private static final Logger log = LoggerFactory.getLogger(InventoryFolder.class);

	public static final String PATH_DELIMITER = "/";

	/**
	 * Items that are contained in this folder
	 */
	public ConcurrentHashMap<UUID, InventoryItemBase> Items = new ConcurrentHashMap<UUID, InventoryItemBase>();

	/**
	 * Child folders that are contained in this folder
	 * 
	 */
	protected ConcurrentHashMap<UUID, InventoryFolder> childFolders = new ConcurrentHashMap<UUID, InventoryFolder>();

	// Constructors
	public InventoryFolder(InventoryFolderBase folderbase) throws InventoryException {
		this.setOwnerId(folderbase.getOwnerId());
		this.setId(folderbase.getId());
		this.setName(folderbase.getName());
		this.setParentFolderId(folderbase.getParentFolderId());
		this.setType(folderbase.getType());
		this.setVersion(folderbase.getVersion());
	}

	public InventoryFolder() {
	}

	/**
	 * Create a new subfolder.
	 * 
	 * @param folderID
	 * @param folderName
	 * @param type
	 * @return The newly created subfolder. Returns null if the folder already
	 *         exists
	 * @throws InventoryException
	 */
	public InventoryFolder createChildFolder(UUID folderID, String folderName, short type) throws InventoryException {
		if (!childFolders.containsKey(folderID)) {
			InventoryFolder subFold = new InventoryFolder();
			subFold.setName(folderName);
			subFold.setId(folderID);
			subFold.setType((short) type);
			subFold.setParentFolderId(this.getId());
			subFold.setOwnerId(this.getOwnerId());
			childFolders.put(subFold.getId(), subFold);

			return subFold;
		}

		return null;
	}

	/**
	 * Add a folder that already exists.
	 * 
	 * @param folder
	 */
	public void addChildFolder(InventoryFolder folder) {
		folder.setParentFolderId(this.getId());
		childFolders.put(folder.getId(), folder);
	}

	/**
	 * Does this folder contain the given child folder?
	 * 
	 * @param folderID
	 * @return
	 */
	public boolean hasChildFolder(UUID folderID) {
		return childFolders.containsKey(folderID);
	}

	/**
	 * Get a child folder
	 * 
	 * @param folderID
	 * @return The folder if it exists, null if it doesn't
	 */
	public InventoryFolder getChildFolder(UUID folderID) {
		InventoryFolder folder = null;

		folder = childFolders.get(folderID);

		return folder;
	}

	/**
	 * Removes the given child subfolder.
	 * 
	 * @param folderID
	 * @return The folder removed, or null if the folder was not present.
	 */
	public InventoryFolder removeChildFolder(UUID folderID) {
		InventoryFolder removedFolder = null;

		synchronized (childFolders) {
			if (childFolders.containsKey(folderID)) {
				removedFolder = childFolders.get(folderID);
				childFolders.remove(folderID);
			}
		}

		return removedFolder;
	}

	/**
	 * Delete all the folders and items in this folder.
	 */
	public void purge() {
		for (InventoryFolder folder : childFolders.values()) {
			folder.purge();
		}

		childFolders.clear();
		Items.clear();
	}

	/**
	 * Returns the item if it exists in this folder or in any of this folder's
	 * descendant folders
	 * 
	 * @param itemID
	 * @return null if the item is not found
	 */
	public InventoryItemBase findItem(UUID itemID) {
		if (Items.containsKey(itemID)) {
			return Items.get(itemID);
		}

		for (InventoryFolder folder : childFolders.values()) {
			InventoryItemBase item = folder.findItem(itemID);

			if (item != null) {
				return item;
			}
		}

		return null;
	}

	public InventoryItemBase findAsset(UUID assetID) {
		for (InventoryItemBase item : Items.values()) {
			if (item.getAssetId() == assetID) {
				return item;
			}
		}

		synchronized (childFolders) {
			for (InventoryFolder folder : childFolders.values()) {
				InventoryItemBase item = folder.findAsset(assetID);

				if (item != null) {
					return item;
				}
			}
		}

		return null;
	}

	/**
	 * Deletes an item if it exists in this folder or any children
	 * 
	 * @param folderID
	 * @return
	 */
	public boolean deleteItem(UUID itemID) {
		boolean found = false;

		if (Items.containsKey(itemID)) {
			Items.remove(itemID);
			return true;
		}

		for (InventoryFolder folder : childFolders.values()) {
			found = folder.deleteItem(itemID);

			if (found == true) {
				break;
			}
		}

		return found;
	}

	/**
	 * Returns the folder requested if it is this folder or is a descendent of
	 * this folder. The search is depth first.
	 * 
	 * @return The requested folder if it exists, null if it does not.
	 */
	public InventoryFolder findFolder(UUID folderID) {
		if (folderID.equals(this.getId())) {
			return this;
		}

		for (InventoryFolder folder : childFolders.values()) {
			InventoryFolder returnFolder = folder.findFolder(folderID);

			if (returnFolder != null) {
				return returnFolder;
			}
		}

		return null;
	}

	/**
	 * Look through all child subfolders for a folder marked as one for a
	 * particular asset type, and return it.
	 * 
	 * @param type
	 * @return Returns null if no such folder is found
	 */
	public InventoryFolder findFolderForType(int type) {
		synchronized (childFolders) {
			for (InventoryFolder f : childFolders.values()) {
				if (f.getType() == type) {
					return f;
				}
			}
		}

		return null;
	}

	/**
	 * Find a folder given a PATH_DELIMITER delimited path starting from this
	 * folder
	 * 
	 * 
	 * This method does not handle paths that contain multiple delimitors
	 * 
	 * FIXME: We do not yet handle situations where folders have the same name.
	 * We could handle this by some XPath like expression
	 * 
	 * FIXME: Delimitors which occur in names themselves are not currently
	 * escapable.
	 * 
	 * @param path
	 *            The path to the required folder. It this is empty or consists
	 *            only of the PATH_DELIMTER then this folder itself is returned.
	 * 
	 * @return null if the folder is not found
	 */
	public InventoryFolder findFolderByPath(String path) {
		if (path.isEmpty()) {
			return this;
		}

		path = path.trim();

		if (path.equals(PATH_DELIMITER)) {
			return this;
		}

		String[] components = path.split(PATH_DELIMITER);

		for (InventoryFolder folder : childFolders.values()) {
			if (folder.getName().equals(components[0])) {
				if (components.length == 2) {
					return folder.findFolderByPath(components[1]);
				} else if (components.length == 1) {
					return folder;
				} else {
					log.error("Path: {} contains more than two Elements", path); 					
				}
			}
		}

		// We didn't find a folder with the given name
		return null;
	}

	/**
	 * Find an item given a PATH_DELIMITOR delimited path starting from this
	 * folder.
	 * 
	 * This method does not handle paths that contain multiple delimitors
	 * 
	 * FIXME: We do not yet handle situations where folders or items have the
	 * same name. We could handle this by some XPath like expression
	 * 
	 * FIXME: Delimitors which occur in names themselves are not currently
	 * escapable.
	 * 
	 * @param path
	 *            The path to the required item.
	 * 
	 * @return null if the item is not found
	 */
	public InventoryItemBase findItemByPath(String path) {
		String[] components = path.split(PATH_DELIMITER);

		if (components.length == 1) {
			for (InventoryItemBase item : Items.values()) {
				if (item.getName().equals(components[0])) {
					return item;
				}
			}
		} else if (components.length == 2) {
			for (InventoryFolder folder : childFolders.values()) {
				if (folder.getName().equals(components[0])) {
					return folder.findItemByPath(components[1]);
				}
			}
		} else {
			log.error("Path: {} contains more than two Elements", path); 
		}

		// We didn't find an item or intermediate folder with the given name
		return null;
	}

	/**
	 * Return a copy of the list of child items in this folder. The items
	 * themselves are the originals.
	 */
	public ArrayList<InventoryItemBase> getListOfItems() {
		ArrayList<InventoryItemBase> itemList = new ArrayList<InventoryItemBase>();

		for (InventoryItemBase item : Items.values()) {
			itemList.add(item);
		}

		return itemList;
	}

	/**
	 * Return a copy of the list of child folders in this folder. The folders
	 * themselves are the originals.
	 */
	public ArrayList<InventoryFolderBase> getListOfFolders() {
		ArrayList<InventoryFolderBase> folderList = new ArrayList<InventoryFolderBase>();

		for (InventoryFolderBase folder : childFolders.values()) {
			folderList.add(folder);
		}

		return folderList;
	}

	public ArrayList<InventoryFolder> getListOfFolderImpls() {
		ArrayList<InventoryFolder> folderList = new ArrayList<InventoryFolder>();

		for (InventoryFolder folder : childFolders.values()) {
			folderList.add(folder);
		}

		return folderList;
	}

	/**
	 * The total number of items in this folder and in the immediate child
	 * folders (though not from other descendants).
	 */
	public int getTotalCount() {
		int total = Items.size();

		for (InventoryFolder folder : childFolders.values()) {
			total = total + folder.getTotalCount();
		}

		return total;
	}
}
