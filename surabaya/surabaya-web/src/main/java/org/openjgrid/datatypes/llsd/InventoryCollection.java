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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to serialize a whole inventory for transfer over the network.
 * 
 * @author Akira Sonoda
 */
@LLSDMapping(mapTo="struct", mappedName = "")
public class InventoryCollection {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(InventoryCollection.class);
	
	@LLSDMapping(mapTo="array", mappedName = "Folders")
    public List<InventoryFolderBase> folderList;
	@LLSDMapping(mapTo="array", mappedName = "Items")
    public List<InventoryItemBase> itemList;
	@LLSDMapping(mapTo="uuid", mappedName = "UserID")
    public UUID userId;

	public InventoryCollection() {
		
	}
	
	public InventoryCollection(String xmlString) throws XMLStreamException, InventoryException {
		fromXml(xmlString);
	}
	
	public void fromXml(String xmlString) throws XMLStreamException, InventoryException {
		ArrayList<InventoryItemBase> tmpItemList = new ArrayList<InventoryItemBase>();
		ArrayList<InventoryFolderBase> tmpFolderList = new ArrayList<InventoryFolderBase>();
		String itemName = null;

		InputStreamReader inputStreamReader = new InputStreamReader(IOUtils.toInputStream(xmlString));
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStream = xmlInputFactory.createXMLStreamReader(inputStreamReader);
		xmlStream.next();
		if (!xmlStream.isStartElement() || !xmlStream.getLocalName().equalsIgnoreCase("ServerResponse")) {
			throw new XMLStreamException("Expected <ServerResponse>");
		}
		// First the Folders
		xmlStream.next(); // Brings the Start of folder
		if (xmlStream.isEndElement() && xmlStream.getLocalName().equalsIgnoreCase("ServerResponse")) {
			return;
		}
		
		while(!xmlStream.getLocalName().equalsIgnoreCase("ServerResponse") || !xmlStream.isEndElement()) {
			xmlStream.next();
			while(!xmlStream.getLocalName().equalsIgnoreCase("folders") || !xmlStream.isEndElement()) {
				InventoryFolderBase inventoryFolderBase = new InventoryFolderBase();
				xmlStream.next();
				while(!xmlStream.getLocalName().startsWith("folder_") || !xmlStream.isEndElement()) {
					itemName = xmlStream.getLocalName();
					switch (itemName) {
					case "ParentID":
						inventoryFolderBase.setParentFolderId(UUID.fromString(xmlStream.getElementText().trim()));
						break;
					case "Type":
						inventoryFolderBase.setType(Integer.parseInt(xmlStream.getElementText().trim()));
						break;
					case "Name":
						inventoryFolderBase.setName(xmlStream.getElementText().trim());
						break;
					case "Version":
						inventoryFolderBase.setVersion(Integer.parseInt(xmlStream.getElementText().trim()));
						break;
					case "Owner":
						inventoryFolderBase.setOwnerId(UUID.fromString(xmlStream.getElementText().trim()));
						break;
					case "ID":
						inventoryFolderBase.setId(UUID.fromString(xmlStream.getElementText().trim()));
						break;
					default:
						throw new XMLStreamException("Unable to assign item with name: " + itemName);
					}
					xmlStream.next();
				}
				tmpFolderList.add(inventoryFolderBase);
				xmlStream.next();
			}
			if(xmlStream.getLocalName().equalsIgnoreCase("folders") && xmlStream.isEndElement()) {
				this.folderList = tmpFolderList;
				xmlStream.next();
			}
			xmlStream.next();

			while(!xmlStream.getLocalName().equalsIgnoreCase("items") || !xmlStream.isEndElement()) {
				InventoryItemBase inventoryItemBase = new InventoryItemBase();
				xmlStream.next();
				while(!xmlStream.getLocalName().startsWith("item_") || !xmlStream.isEndElement()) {
					itemName = xmlStream.getLocalName();
					switch(itemName) {
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
					case "ID":
						inventoryItemBase.setId(UUID.fromString(xmlStream.getElementText().trim()));
						break;
					case "InvType":
						inventoryItemBase.setInvType(Integer.parseInt(xmlStream.getElementText().trim()));
						break;
					case "Name":
						inventoryItemBase.setName(xmlStream.getElementText().trim());
						break;
					case "NextPermissions":
						inventoryItemBase.setNextPermissions(Long.parseLong(xmlStream.getElementText().trim()));
						break;
					case "Owner":
						inventoryItemBase.setOwnerId(UUID.fromString(xmlStream.getElementText().trim()));
						break;
					case "SalePrice":
						inventoryItemBase.setSalePrice(Integer.parseInt(xmlStream.getElementText().trim()));
						break;
					case "SaleType":
						inventoryItemBase.setSalePrice(Integer.parseInt(xmlStream.getElementText().trim()));
						break;
					default:
						throw new XMLStreamException("Unable to assign item with name: " + itemName);
					} 				
					xmlStream.next();
				}
				tmpItemList.add(inventoryItemBase);
				xmlStream.next();
			}
			if(xmlStream.getLocalName().equalsIgnoreCase("items") && xmlStream.isEndElement()) {
				this.itemList = tmpItemList;
				xmlStream.next();
			}
		}
	}
}
