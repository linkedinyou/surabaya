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

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.openjgrid.datatypes.Constants;
import org.openjgrid.datatypes.asset.PermissionMask;
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
@DependsOn("ConfigurationService")
public class LibraryService {

	private static final Logger log = LoggerFactory.getLogger(LibraryService.class);

	@EJB(mappedName = "java:module/ConfigurationService")
	private ConfigurationService configurationService;

	private InventoryFolder libraryRootFolder = null;
	private UUID libOwner = UUID.fromString("11111111-1111-0000-0000-000100bba000");

	/**
	 * Holds the root library folder and all its descendents. This is really
	 * only used during inventory setup so that we don't have to repeatedly
	 * search the tree of library folders.
	 */
	protected ConcurrentHashMap<UUID, InventoryFolder> libraryFolders = new ConcurrentHashMap<UUID, InventoryFolder>();

	/**
	 * @throws InventoryException
	 * @throws ConfigurationException
	 */
	@PostConstruct
	public void initialize() throws InventoryException, ConfigurationException {
		String librariesLocation = FilenameUtils.concat("/etc/surabaya/inventory", "Libraries.xml");
		String libraryName = "OpenSim Library";

		librariesLocation = configurationService.getProperty("LibraryService", "default_library", librariesLocation);
		libraryName = configurationService.getProperty("LibraryService", "library_name", libraryName);

		libraryRootFolder = new InventoryFolder();
		libraryRootFolder.setOwnerId(libOwner);
		libraryRootFolder.setId(UUID.fromString("00000112-000f-0000-0000-000100bba000"));
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
	public InventoryItemBase createItem(UUID inventoryID, UUID assetID, String name, String description, int assetType, int invType,
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
	 * @throws ConfigurationException
	 * @throws InventoryException
	 */
	@SuppressWarnings("rawtypes")
	private void loadLibraries(String librariesLocation) throws ConfigurationException, InventoryException {
		log.info("Loading library control file {}", librariesLocation);
		String filePath = FilenameUtils.getPathNoEndSeparator(librariesLocation);
		filePath = FilenameUtils.concat("/", filePath);
		XMLConfiguration xmlConfiguration = new XMLConfiguration(librariesLocation);
		Object property = xmlConfiguration.getProperty("Section[@Name]");
		if (property instanceof Collection) {
			int numOfSections = ((Collection) property).size();
			for (int i = 0; i < numOfSections; i++) {

				String filetype1 = xmlConfiguration.getString("Section(" + i + ").Key(0)[@Name]");
				String filename1 = xmlConfiguration.getString("Section(" + i + ").Key(0)[@Value]");
				if (filetype1.equals("foldersFile")) {
					loadFoldersFromFile(FilenameUtils.concat(filePath, filename1));
				} else {
					throw new ConfigurationException("Expecting \"foldersFile\" but received: " + filetype1);
				}

				String filetype2 = xmlConfiguration.getString("Section(" + i + ").Key(1)[@Name]");
				String filename2 = xmlConfiguration.getString("Section(" + i + ").Key(1)[@Value]");
				if (filetype2.equals("itemsFile")) {
					loadItemsFromFile(FilenameUtils.concat(filePath, filename2));
				} else {
					throw new ConfigurationException("Expecting \"itemsFile\" but received: " + filetype2);
				}
			}
		} else {
			throw new ConfigurationException("Expecting multiple Sections in " + librariesLocation);
		}
	}

	@SuppressWarnings("rawtypes")
	private void loadItemsFromFile(String itemsLocation) throws ConfigurationException, InventoryException {
		log.debug("loadItemsFromFile: {}", itemsLocation);
		UUID id = libraryRootFolder.getId();
		UUID assetId = id;
		UUID parentFolderId = id;
		String name = "";
		String description = name;
		int invType = 0;
		int assetType = invType;
		long currentPermissions = PermissionMask.All.getPermissionMask();
		long nextPermissions = PermissionMask.All.getPermissionMask();
		long everyOnePermissions = PermissionMask.All.getPermissionMask() - PermissionMask.Modify.getPermissionMask();
		long basePermissions = PermissionMask.All.getPermissionMask();
		long flags = 0;

		XMLConfiguration xmlConfiguration = new XMLConfiguration(itemsLocation);
		Object propSection = xmlConfiguration.getProperty("Section[@Name]");

		int numOfSections = 0;
		if (propSection instanceof Collection) {
			numOfSections = ((Collection) propSection).size();
		} else if (propSection == null) {
			numOfSections = 0;
		} else {
			numOfSections = 1;
		}

		// / now get the data out of the configuration
		if (numOfSections > 0) {
			for (int i = 0; i < numOfSections; i++) {
				int numOfKeys = 0;
				Object propKey = xmlConfiguration.getProperty("Section(" + i + ").Key[@Name]");
				if (propKey instanceof Collection) {
					numOfKeys = ((Collection) propKey).size();
				} else if (propKey == null) {
					numOfKeys = 0;
				} else {
					numOfKeys = 1;
				}

				for (int j = 0; j < numOfKeys; j++) {
					String itemProperty = xmlConfiguration.getString("Section(" + i + ").Key(" + j + ")[@Name]");
					switch (itemProperty) {
					case ("inventoryID"):
						id = UUID.fromString(xmlConfiguration.getString("Section(" + i + ").Key(" + j + ")[@Value]", 
								libraryRootFolder.getId().toString()));
						break;
					case ("assetID"):
						assetId = UUID.fromString(xmlConfiguration.getString("Section(" + i + ").Key(" + j + ")[@Value]", id.toString()));
						break;
					case ("folderID"):
						parentFolderId = UUID.fromString(xmlConfiguration.getString(
								"Section(" + i + ").Key(" + j + ")[@Value]", libraryRootFolder.getId().toString()));
						break;
					case ("name"):
						name = xmlConfiguration.getString("Section(" + i + ").Key(" + j + ")[@Value]", "");
						break;
					case ("description"):
						description = xmlConfiguration.getString("Section(" + i + ").Key(" + j + ")[@Value]", name);
						break;
					case ("inventoryType"):
						invType = xmlConfiguration.getInt("Section(" + i + ").Key(" + j + ")[@Value]", 0);
						break;
					case ("assetType"):
						assetType = xmlConfiguration.getInt("Section(" + i + ").Key(" + j + ")[@Value]", invType);
						break;
					case ("currentPermissions"):
						currentPermissions = xmlConfiguration.getLong("Section(" + i + ").Key(" + j + ")[@Value]",
								PermissionMask.All.getPermissionMask());
						break;
					case ("nextPermissions"):
						nextPermissions = xmlConfiguration.getLong("Section(" + i + ").Key(" + j + ")[@Value]",
								PermissionMask.All.getPermissionMask());
						break;
					case ("everyonePermissions"):
						everyOnePermissions = xmlConfiguration.getLong("Section(" + i + ").Key(" + j + ")[@Value]",
								PermissionMask.All.getPermissionMask() - PermissionMask.Modify.getPermissionMask());
						break;
					case ("basePermissions"):
						basePermissions = xmlConfiguration.getLong("Section(" + i + ").Key(" + j + ")[@Value]",
								PermissionMask.All.getPermissionMask());
						break;
					case ("flags"):
						flags = xmlConfiguration.getLong("Section(" + i + ").Key(" + j + ")[@Value]", 0);
						break;
					default:
						throw new ConfigurationException("Unknown ItemProperty: " + itemProperty);

					}
				}
				addItemToLibrary(id, assetId, parentFolderId, name, description, invType, assetType, currentPermissions,
						nextPermissions, everyOnePermissions, basePermissions, flags);
			}
		}

	}

	private void addItemToLibrary(UUID id, UUID assetId, UUID parentFolderId, String name, String description, int invType,
			int assetType, long currentPermissions, long nextPermissions, long everyOnePermissions, long basePermissions, long flags)
			throws InventoryException {
		InventoryItemBase item = new InventoryItemBase();
		item.setOwnerId(libOwner);
		item.setCreatorId(libOwner.toString());
		item.setId(id);
		item.setAssetId(assetId);
		item.setParentFolderId(parentFolderId);
		item.setName(name);
		item.setDescription(description);
		item.setInvType(invType);
		item.setAssetType(assetType);
		item.setCurrentPermissions(currentPermissions);
		item.setNextPermissions(nextPermissions);
		item.setEveryOnePermissions(everyOnePermissions);
		item.setBasePermissions(basePermissions);
		item.setFlags(flags);

		if (libraryFolders.containsKey(item.getParentFolderId())) {
			InventoryFolder parentFolder = libraryFolders.get(item.getParentFolderId());
			try {
				parentFolder.Items.put(item.getId(), item);
			} catch (Exception ex) {
				log.warn("Item {} [{}] not added, duplicate item", item.getId(), item.getName());
			}
		} else {
			log.warn("Couldn't add item {} since parent folder with ID {} does not exist!", item.getName(), item.getParentFolderId());
		}

	}

	@SuppressWarnings("rawtypes")
	private void loadFoldersFromFile(String foldersLocation) throws ConfigurationException, InventoryException {
		log.debug("loadFoldersFromFile: {}", foldersLocation);
		UUID folderId = libraryRootFolder.getId();
		String folderName = "unknown";
		UUID parentFolderId = libraryRootFolder.getId();
		int type = 8;
		
		XMLConfiguration xmlConfiguration = new XMLConfiguration(foldersLocation);
		Object propSection = xmlConfiguration.getProperty("Section[@Name]");
		
		int numOfSections = 0;
		if (propSection instanceof Collection) {
			numOfSections = ((Collection) propSection).size();
		} else if (propSection == null) {
			numOfSections = 0;
		} else {
			numOfSections = 1;
		}

		// / now get the data out of the configuration
		if (numOfSections > 0) {
			for (int i = 0; i < numOfSections; i++) {
				int numOfKeys = 0;
				Object propKey = xmlConfiguration.getProperty("Section(" + i + ").Key[@Name]");
				if (propKey instanceof Collection) {
					numOfKeys = ((Collection) propKey).size();
				} else if (propKey == null) {
					numOfKeys = 0;
				} else {
					numOfKeys = 1;
				}

				for (int j = 0; j < numOfKeys; j++) {
					String itemProperty = xmlConfiguration.getString("Section(" + i + ").Key(" + j + ")[@Name]");
					switch (itemProperty) {
					case ("folderID"):
						folderId = UUID.fromString(xmlConfiguration.getString("Section(" + i + ").Key(" + j + ")[@Value]", 
								     libraryRootFolder.getId().toString()));
						break;
					case ("name"):
						folderName = xmlConfiguration.getString("Section(" + i + ").Key(" + j + ")[@Value]", "unknown");
						break;
					case ("parentFolderID"):
						parentFolderId = UUID.fromString(xmlConfiguration.getString("Section(" + i + ").Key(" + j + ")[@Value]", 
							     libraryRootFolder.getId().toString()));
						break;
					case ("type"):
						type = xmlConfiguration.getInt("Section(" + i + ").Key(" + j + ")[@Value]", 8);
						break;
					default:
						throw new ConfigurationException("Unknown ItemProperty: " + itemProperty);

					}
				}
				addFolderToLibrary(folderId, folderName, parentFolderId, type);
			}
		}
	}

	private void addFolderToLibrary(UUID folderId, String folderName, UUID parentFolderId, int type) throws InventoryException {
		InventoryFolder folderInfo = new InventoryFolder();

		folderInfo.setId(folderId);
		folderInfo.setName(folderName);
		folderInfo.setParentFolderId(parentFolderId);
		folderInfo.setType(type);

		folderInfo.setOwnerId(libOwner);
		folderInfo.setVersion(1);

		if (libraryFolders.containsKey(folderInfo.getParentFolderId())) {
			InventoryFolder parentFolder = libraryFolders.get(folderInfo.getParentFolderId());
			libraryFolders.put(folderInfo.getId(), folderInfo);
			parentFolder.addChildFolder(folderInfo);
			log.debug("Adding folder {} ({})", folderInfo.getName(), folderInfo.getId());
		} else {
			log.warn("Couldn't add folder {} since parent folder with ID {} does not exist!", folderInfo.getName(),
					folderInfo.getParentFolderId());
		}
	}

	public UUID getLibraryRootFolderOwner() {
		if(libraryRootFolder != null) {
			return(libraryRootFolder.getOwnerId());
		} else {
			return(Constants.UUID_ZERO);
		}
	}
	
	public boolean hasRootFolder() {
		return (libraryRootFolder != null);
	}
	
	// Getter & Setter

	public InventoryFolder getLibraryRootFolder() {
		return libraryRootFolder;
	}

}
