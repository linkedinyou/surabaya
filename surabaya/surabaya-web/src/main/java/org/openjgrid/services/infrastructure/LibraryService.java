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
package org.openjgrid.services.infrastructure;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.commons.io.FilenameUtils;
import org.openjgrid.datatypes.Constants;
import org.openjgrid.datatypes.asset.AssetType;
import org.openjgrid.datatypes.inventory.InventoryException;
import org.openjgrid.datatypes.inventory.InventoryFolder;
import org.openjgrid.datatypes.inventory.InventoryItemBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton implementation class LibraryService which offers the grid's default
 * Library Inventory TODO: Actually it is only possible to maintain the default
 * Library for one Grid. Maybe in future the LibraryService has to be multi Grid
 * capable
 * 
 * Author: Akira Sonoda
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class LibraryService {

	private static final Logger log = LoggerFactory
			.getLogger(LibraryService.class);

	@EJB(mappedName = "java:module/ConfigurationService")
	private ConfigurationService configurationService;

	private InventoryFolder libraryRootFolder = null;
	private UUID libOwner = UUID
			.fromString("11111111-1111-0000-0000-000100bba000");

	/**
	 * Holds the root library folder and all its descendents. This is really
	 * only used during inventory setup so that we don't have to repeatedly
	 * search the tree of library folders.
	 */
	protected ConcurrentHashMap<UUID, InventoryFolder> libraryFolders = new ConcurrentHashMap<UUID, InventoryFolder>();

	/**
	 * @throws InventoryException
	 */
	public LibraryService() throws InventoryException {
		String librariesLocation = FilenameUtils.concat("inventory",
				"Libraries.xml");
		String libraryName = "OpenSim Library";

		librariesLocation = configurationService.getProperty("LibraryService",
				"DefaultLibrary", librariesLocation);
		libraryName = configurationService.getProperty("LibraryService",
				"DefaultLibrary", libraryName);

		libraryRootFolder = new InventoryFolder();
		libraryRootFolder.setOwnerId(libOwner);
		libraryRootFolder.setId(UUID
				.fromString("00000112-000f-0000-0000-000100bba000"));
		libraryRootFolder.setName(libraryName);
		libraryRootFolder.setParentFolderId(Constants.UUID_ZERO);
		libraryRootFolder.setType(8);
		libraryRootFolder.setVersion(1);

		libraryFolders.put(libraryRootFolder.getId(), libraryRootFolder);

		loadLibraries(librariesLocation);

	}

	/**
	 * @param inventoryID
	 * @param assetID
	 * @param name
	 * @param description
	 * @param assetType
	 * @param invType
	 * @param parentFolderID
	 * @return
	 * @throws InventoryException
	 */
	public InventoryItemBase createItem(UUID inventoryID, UUID assetID,
			String name, String description, int assetType, int invType,
			UUID parentFolderID) throws InventoryException {
		
		InventoryItemBase item = new InventoryItemBase();
		item.setOwnerId(libOwner);
		item.setCreatorId(libOwner.toString());
		item.setId(inventoryID);
		item.setAssetId(assetID);
		item.setDescription(description);
		item.setName(name);
		item.setAssetType(assetType);
		item.setInvType(invType);
		item.setParentFolderId(parentFolderID);
		item.setBasePermissions(0x7FFFFFFF);
		item.setEveryOnePermissions(0x7FFFFFFF);
		item.setCurrentPermissions(0x7FFFFFFF);
		item.setNextPermissions(0x7FFFFFFF);
		return (item);
		
	}

	/**
	 * @param librariesLocation
	 */
	private void loadLibraries(String librariesLocation) {
        log.info("Loading library control file {}", librariesLocation);
        LoadFromFile(librariesLocation, "Libraries control", ReadLibraryFromConfig);

	}

	// Getter & Setter

	public InventoryFolder getLibraryRootFolder() {
		return libraryRootFolder;
	}

}
