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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;
import org.openjgrid.datatypes.llsd.LLSDMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to serialize a whole inventory for transfer over the network.
 * 
 * @author Akira Sonoda
 */
@LLSDMapping(mapTo = "struct", mappedName = "")
public class InventoryCollection_2 {

	private static final Logger log = LoggerFactory.getLogger(InventoryCollection_2.class);

	@LLSDMapping(mapTo = "array", mappedName = "Folders")
	public List<InventoryFolderBase> folderList;
	@LLSDMapping(mapTo = "array", mappedName = "Items")
	public List<InventoryItemBase> itemList;
	@LLSDMapping(mapTo = "uuid", mappedName = "UserID")
	public UUID userId;

	public InventoryCollection_2() {
	}

	public InventoryCollection_2(String xmlString) throws XMLStreamException, InventoryException {
		fromXml(xmlString);
	}

	public void fromXml(String xmlString) throws XMLStreamException, InventoryException {
		ArrayList<InventoryItemBase> tmpItemList = new ArrayList<InventoryItemBase>();
		ArrayList<InventoryFolderBase> tmpFolderList = new ArrayList<InventoryFolderBase>();
		String elementName = null;

		InputStreamReader inputStreamReader = new InputStreamReader(IOUtils.toInputStream(xmlString));
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStream = xmlInputFactory.createXMLStreamReader(inputStreamReader);
		
		boolean isFolder = false;
		
		InventoryFolderBase inventoryFolderBase = null;
		InventoryItemBase inventoryItemBase = new InventoryItemBase();
				
		while (xmlStream.hasNext()) {
			  int eventType = xmlStream.next();
			  switch (eventType) {
			    case XMLStreamReader.START_ELEMENT:
			    	elementName = xmlStream.getLocalName();
			    	// Handle the start of a new Group
			    	if (elementName.startsWith("folder_")) {
			    		inventoryFolderBase = new InventoryFolderBase();
			    		isFolder = true;
			    	} else if (elementName.startsWith("item_")) {
			    		inventoryItemBase = new InventoryItemBase();
			    		isFolder = false;
			    	} 
			    	
					switch (elementName) {
					case "ParentID":
						inventoryFolderBase.setParentFolderId(UUID.fromString(xmlStream.getElementText().trim()));
						break;
					case "Type":
						inventoryFolderBase.setType(Integer.parseInt(xmlStream.getElementText().trim()));
						break;
					case "Name":
						if(isFolder) {
							inventoryFolderBase.setName(xmlStream.getElementText().trim());
						} else {
							inventoryItemBase.setName(xmlStream.getElementText().trim());
						}
						break;
					case "Version":
						inventoryFolderBase.setVersion(Integer.parseInt(xmlStream.getElementText().trim()));
						break;
					case "Owner":
						if(isFolder) {
							inventoryFolderBase.setOwnerId(UUID.fromString(xmlStream.getElementText().trim()));
						} else {
							inventoryItemBase.setOwnerId(UUID.fromString(xmlStream.getElementText().trim()));
						}
						break;
					case "ID":
						if(isFolder) {
							inventoryFolderBase.setId(UUID.fromString(xmlStream.getElementText().trim()));
						} else {
							inventoryItemBase.setId(UUID.fromString(xmlStream.getElementText().trim()));
						}
						break;
					case "AssetID":
						inventoryItemBase.setAssetId(UUID.fromString(xmlStream.getElementText().trim()));
						break;
					case "AssetType":
						inventoryItemBase.setAssetType(Integer.parseInt(xmlStream.getElementText().trim()));
						break;
					case "BasePermissions":
						inventoryItemBase.setBasePermissions(Long.parseLong(xmlStream.getElementText().trim()));
						break;
					case "CreationDate":
						inventoryItemBase.setCreationDate(Long.parseLong(xmlStream.getElementText().trim()));
						break;
					case "CreatorId":
						inventoryItemBase.setCreatorId(xmlStream.getElementText().trim());
						break;
					case "CreatorData":
						inventoryItemBase.setCreatorData(xmlStream.getElementText().trim());
						break;
					case "CurrentPermissions":
						inventoryItemBase.setCurrentPermissions(Long.parseLong(xmlStream.getElementText().trim()));
						break;
					case "Description":
						inventoryItemBase.setDescription(xmlStream.getElementText().trim());
						break;
					case "EveryOnePermissions":
						inventoryItemBase.setEveryOnePermissions(Long.parseLong(xmlStream.getElementText().trim()));
						break;
					case "Flags":
						inventoryItemBase.setFlags(Long.parseLong(xmlStream.getElementText().trim()));
						break;
					case "Folder":
						inventoryItemBase.setParentFolderId(UUID.fromString(xmlStream.getElementText().trim()));
						break;
					case "GroupID":
						inventoryItemBase.setGroupId(UUID.fromString(xmlStream.getElementText().trim()));
						break;
					case "GroupOwned":
						inventoryItemBase.isGroupOwned(Boolean.parseBoolean(xmlStream.getElementText().trim()));
						break;
					case "GroupPermissions":
						inventoryItemBase.setGroupPermissions(Long.parseLong(xmlStream.getElementText().trim()));
						break;
					case "InvType":
						inventoryItemBase.setInvType(Integer.parseInt(xmlStream.getElementText().trim()));
						break;
					case "NextPermissions":
						inventoryItemBase.setNextPermissions(Long.parseLong(xmlStream.getElementText().trim()));
						break;
					case "SalePrice":
						inventoryItemBase.setSalePrice(Integer.parseInt(xmlStream.getElementText().trim()));
						break;
					case "SaleType":
						inventoryItemBase.setSalePrice(Integer.parseInt(xmlStream.getElementText().trim()));
						break;
					default:
						log.debug("Unknown Element with name: " + elementName);
					}
			    	break;
			    case XMLStreamReader.END_ELEMENT:
			    	elementName = xmlStream.getLocalName();
			    	if (elementName.startsWith("folder_")) {
			    		tmpFolderList.add(inventoryFolderBase);
			    		isFolder = false;
			    	} else if (elementName.equalsIgnoreCase("folders")) {
			    		this.folderList = tmpFolderList;
			    	} else if (elementName.startsWith("item_")) {
			    		tmpItemList.add(inventoryItemBase);
			    		isFolder = false;
			    	} else if (elementName.equalsIgnoreCase("items")) {
			    		this.itemList = tmpItemList;
			    	} 
			    	break;
			}
		}
	}
}
