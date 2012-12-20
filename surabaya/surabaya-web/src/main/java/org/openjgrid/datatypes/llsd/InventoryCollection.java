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
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;

/**
 * Used to serialize a whole inventory for transfer over the network.
 * 
 * @author Akira Sonoda
 */
@LLSDMapping(mapTo="struct", mappedName = "")
public class InventoryCollection {

	@LLSDMapping(mapTo="array", mappedName = "Folders")
    public List<InventoryFolderBase> folderList;
	@LLSDMapping(mapTo="array", mappedName = "Items")
    public List<InventoryItemBase> itemList;
	@LLSDMapping(mapTo="uuid", mappedName = "UserID")
    public UUID userId;

	public InventoryCollection() {
		
	}
	
	public void fromXml(String xmlString) throws XMLStreamException, InventoryException {
		InputStreamReader inputStreamReader = new InputStreamReader(IOUtils.toInputStream(xmlString));
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStream = xmlInputFactory.createXMLStreamReader(inputStreamReader);
		xmlStream.next();
		if (!xmlStream.isStartElement() || !xmlStream.getLocalName().equalsIgnoreCase("ServerResponse")) {
			throw new XMLStreamException("Expected <ServerResponse>");
		}
		// First the Folders
		xmlStream.next(); // Brings the Start of folder
		if(xmlStream.isStartElement()) {
			System.out.println("xml: " +xmlStream.getLocalName());
		}
		xmlStream.next();
		if(!xmlStream.isEndElement()) { // Folder End Element is reported
			createFolderList(xmlStream);
		} else {
			xmlStream.next();
			createItemsList(xmlStream);
		}

	}
	
	private void createFolderList(XMLStreamReader xmlStream) {
		System.out.println("Folder xml: " +xmlStream.getLocalName());
		
		
	}
	
	private void createItemsList(XMLStreamReader xmlStream) throws XMLStreamException, InventoryException {
		System.out.println("f::Item xml: " +xmlStream.getLocalName());
		this.itemList = new ArrayList<InventoryItemBase>();
		xmlStream.next();
		while(!xmlStream.getLocalName().equalsIgnoreCase("items") || !xmlStream.isEndElement()) {
			System.out.println("w1::Item xml: " +xmlStream.getLocalName());
			InventoryItemBase inventoryItemBase = new InventoryItemBase();
			xmlStream.next();
			while(!xmlStream.getLocalName().startsWith("item_") || !xmlStream.isEndElement()) {
				String itemName = xmlStream.getLocalName();
				System.out.println("w2::Item xml: " + itemName);
				if( itemName.equalsIgnoreCase("AssetID")) {
					inventoryItemBase.setAssetId(UUID.fromString(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("AssetType")) {
					inventoryItemBase.setAssetType(Integer.parseInt(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("BasePermissions")) {
					inventoryItemBase.setBasePermissions(Long.parseLong(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("CreationDate")) {
					inventoryItemBase.setCreationDate(Long.parseLong(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("CreatorId")) {
					inventoryItemBase.setCreatorId(UUID.fromString(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("CreatorData")) {
					inventoryItemBase.setCreatorData(xmlStream.getElementText().trim());
				} else if (itemName.equalsIgnoreCase("CurrentPermissions")) {
					inventoryItemBase.setCurrentPermissions(Long.parseLong(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("Description")) {
					inventoryItemBase.setDescription(xmlStream.getElementText().trim());
				} else if (itemName.equalsIgnoreCase("EveryOnePermissions")) {
					inventoryItemBase.setEveryOnePermissions(Long.parseLong(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("Flags")) {
					inventoryItemBase.setFlags(Long.parseLong(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("Folder")) {
					inventoryItemBase.setParentFolderId(UUID.fromString(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("GroupID")) {
					inventoryItemBase.setGroupId(UUID.fromString(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("GroupOwned")) {
					inventoryItemBase.isGroupOwned(Boolean.parseBoolean(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("GroupPermissions")) {
					inventoryItemBase.setGroupPermissions(Long.parseLong(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("ID")) {
					inventoryItemBase.setId(UUID.fromString(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("InvType")) {
					inventoryItemBase.setInvType(Integer.parseInt(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("Name")) {
					inventoryItemBase.setName(xmlStream.getElementText().trim());
				} else if (itemName.equalsIgnoreCase("NextPermissions")) {
					inventoryItemBase.setNextPermissions(Long.parseLong(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("Owner")) {
					inventoryItemBase.setOwnerId(UUID.fromString(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("SalePrice")) {
					inventoryItemBase.setSalePrice(Integer.parseInt(xmlStream.getElementText().trim()));
				} else if (itemName.equalsIgnoreCase("SaleType")) {
					inventoryItemBase.setSalePrice(Integer.parseInt(xmlStream.getElementText().trim()));
				} else {
					throw new XMLStreamException("Unable to assign item with name: " + itemName);
				} 				
				xmlStream.next();
				System.out.println("w3::Item xml: " + xmlStream.getLocalName());

			}
			this.itemList.add(inventoryItemBase);
			xmlStream.next();
		}
	}
}
