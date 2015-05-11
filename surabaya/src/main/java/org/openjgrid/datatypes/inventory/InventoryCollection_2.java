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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
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
public class InventoryCollection_2 extends DefaultHandler {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(InventoryCollection_2.class);

	@LLSDMapping(mapTo = "array", mappedName = "Folders")
	public List<InventoryFolderBase> folderList;
	@LLSDMapping(mapTo = "array", mappedName = "Items")
	public List<InventoryItemBase> itemList;
	@LLSDMapping(mapTo = "uuid", mappedName = "UserID")
	public UUID userId;

	public InventoryCollection_2() {
	}

	public InventoryCollection_2(String xmlString) {
		fromXml(xmlString);
	}

	public void fromXml(String xmlString) {
		try {
			InputStream inputStream = IOUtils.toInputStream(xmlString);
			// obtain and configure a SAX based parser
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

			// obtain object for SAX parser
			SAXParser saxParser = saxParserFactory.newSAXParser();

			DefaultHandler defaultHandler = new DefaultHandler() {

				String fidTag = "close";
				String versionTag = "close";

				String foldersTag = "close";
				String folder_Tag = "close";
				String parentIDTag = "close";
				String typeTag = "close";
				String nameTag = "close";
				String folderVersionTag = "close";
				String ownerTag = "close";
				String iDTag = "close";

				String itemsTag = "close";
				String item_Tag = "close";
				String assetIDTag = "close";
				String assetTypeTag = "close";
				String basePermissionsTag = "close";
				String creationDateTag = "close";
				String creatorIdTag = "close";
				String creatorDataTag = "close";
				String currentPermissionsTag = "close";
				String descriptionTag = "close";
				String everyOnePermissionsTag = "close";
				String flagsTag = "close";
				String folderTag = "close";
				String groupIDTag = "close";
				String groupOwnedTag = "close";
				String groupPermissionsTag = "close";
				String itemIDTag = "close";
				String invTypeTag = "close";
				String itemNameTag = "close";
				String nextPermissionsTag = "close";
				String itemOwnerTag = "close";
				String salePriceTag = "close";
				String saleTypeTag = "close";

				InventoryFolderBase inventoryFolderBase = null;
				InventoryItemBase inventoryItemBase = null;

				ArrayList<InventoryItemBase> tmpItemList = new ArrayList<InventoryItemBase>();
				ArrayList<InventoryFolderBase> tmpFolderList = new ArrayList<InventoryFolderBase>();

				// this method is called every time the parser gets an open tag
				// '<'
				// identifies which tag is being open at time by assigning an
				// open flag
				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {

					if (qName.equalsIgnoreCase("FID")) {
						fidTag = "open";
					}
					if (qName.equalsIgnoreCase("VERSION")) {
						// The Version Tag can be both in the Top Level Tags or
						// inside the Folders Tags
						if (foldersTag.equals("close")) {
							versionTag = "open";
						} else {
							folderVersionTag = "open";
						}
					}

					// Handle the folders Tags
					if (qName.equalsIgnoreCase("FOLDERS")) {
						foldersTag = "open";
					}
					if (qName.startsWith("folder_")) {
						inventoryFolderBase = new InventoryFolderBase();
						folder_Tag = "open";
					}
					if (qName.equalsIgnoreCase("ParentID")) {
						parentIDTag = "open";
					}
					if (qName.equalsIgnoreCase("Type")) {
						typeTag = "open";
					}
					if (qName.equalsIgnoreCase("Name")) {
						// The Name Tag can be both in the Folders Tags or
						// inside the Items Tags
						if (foldersTag.equals("close")) {
							itemNameTag = "open";
						} else {
							nameTag = "open";
						}
					}
					if (qName.equalsIgnoreCase("Owner")) {
						// The Owner Tag can be both in the Folders Tags or
						// inside the Items Tags
						if (foldersTag.equals("close")) {
							itemOwnerTag = "open";
						} else {
							ownerTag = "open";
						}
					}
					if (qName.equalsIgnoreCase("ID")) {
						// The ID Tag can be both in the Folders Tags or
						// inside the Items Tags
						if (foldersTag.equals("close")) {
							itemIDTag = "open";
						} else {
							iDTag = "open";
						}
					}

					// Handle the items Tags
					if (qName.equalsIgnoreCase("items")) {
						inventoryItemBase = new InventoryItemBase();
						itemsTag = "open";
					}
					if (qName.startsWith("item_")) {
						item_Tag = "open";
					}
					if (qName.equalsIgnoreCase("AssetID")) {
						assetIDTag = "open";
					}
					if (qName.equalsIgnoreCase("AssetType")) {
						assetTypeTag = "open";
					}
					if (qName.equalsIgnoreCase("BasePermissions")) {
						basePermissionsTag = "open";
					}
					if (qName.equalsIgnoreCase("CreationDate")) {
						creationDateTag = "open";
					}
					if (qName.equalsIgnoreCase("CreatorId")) {
						creatorIdTag = "open";
					}
					if (qName.equalsIgnoreCase("CreatorData")) {
						creatorDataTag = "open";
					}
					if (qName.equalsIgnoreCase("CurrentPermissions")) {
						currentPermissionsTag = "open";
					}
					if (qName.equalsIgnoreCase("Description")) {
						descriptionTag = "open";
					}
					if (qName.equalsIgnoreCase("EveryOnePermissions")) {
						everyOnePermissionsTag = "open";
					}
					if (qName.equalsIgnoreCase("Flags")) {
						flagsTag = "open";
					}
					if (qName.equalsIgnoreCase("Folder")) {
						folderTag = "open";
					}
					if (qName.equalsIgnoreCase("GroupID")) {
						groupIDTag = "open";
					}
					if (qName.equalsIgnoreCase("GroupOwned")) {
						groupOwnedTag = "open";
					}
					if (qName.equalsIgnoreCase("GroupPermissions")) {
						groupPermissionsTag = "open";
					}
					if (qName.equalsIgnoreCase("InvType")) {
						invTypeTag = "open";
					}
					if (qName.equalsIgnoreCase("NextPermissions")) {
						nextPermissionsTag = "open";
					}
					if (qName.equalsIgnoreCase("SalePrice")) {
						salePriceTag = "open";
					}
					if (qName.equalsIgnoreCase("SaleType")) {
						saleTypeTag = "open";
					}

				}

				// prints data stored in between '<' and '>' tags
				public void characters(char ch[], int start, int length)
						throws SAXException {
					try {
						// TODO Handle the new fields here

						// Handle the folders Stuff here
						if (parentIDTag.equals("open")) {
							inventoryFolderBase.setParentFolderId(UUID
									.fromString(new String(ch, start, length)));
						}
						if (typeTag.equals("open")) {
							inventoryFolderBase.setType(Integer
									.parseInt(new String(ch, start, length)));
						}
						if (nameTag.equals("open")) {
							inventoryFolderBase.setName(new String(ch, start,
									length));
						}
						if (folderVersionTag.equals("open")) {
							inventoryFolderBase.setVersion(Integer
									.parseInt(new String(ch, start, length)));
						}
						if (ownerTag.equals("open")) {
							inventoryFolderBase.setOwnerId(UUID
									.fromString(new String(ch, start, length)));
						}
						if (iDTag.equals("open")) {
							inventoryFolderBase.setId(UUID
									.fromString(new String(ch, start, length)));
						}

						// Handle the items Stuff here
						if (assetIDTag.equals("open")) {
							inventoryItemBase.setAssetId(UUID
									.fromString(new String(ch, start, length)));
						}
						if (assetTypeTag.equals("open")) {
							inventoryItemBase.setAssetType(Integer
									.parseInt(new String(ch, start, length)));
						}
						if (basePermissionsTag.equals("open")) {
							inventoryItemBase.setBasePermissions(Long
									.parseLong(new String(ch, start, length)));
						}
						if (creationDateTag.equals("open")) {
							inventoryItemBase.setCreationDate(Long
									.parseLong(new String(ch, start, length)));
						}
						if (creatorDataTag.equals("open")) {
							inventoryItemBase.setCreatorData(new String(ch,
									start, length));
						}						
						if (creatorIdTag.equals("open")) {
							inventoryItemBase.setCreatorId(new String(ch,
									start, length));
						}
						if (currentPermissionsTag.equals("open")) {
							inventoryItemBase.setCurrentPermissions(Long
									.parseLong(new String(ch, start, length)));
						}
						if (descriptionTag.equals("open")) {
							inventoryItemBase.setDescription(new String(ch,
									start, length));
						}
						if (everyOnePermissionsTag.equals("open")) {
							inventoryItemBase.setEveryOnePermissions(Long
									.parseLong(new String(ch, start, length)));
						}
						if (flagsTag.equals("open")) {
							inventoryItemBase.setFlags(Long
									.parseLong(new String(ch, start, length)));
						}
						if (folderTag.equals("open")) {
							inventoryItemBase.setParentFolderId(UUID
									.fromString(new String(ch, start, length)));
						}
						if (groupIDTag.equals("open")) {
							inventoryItemBase.setGroupId(UUID
									.fromString(new String(ch, start, length)));
						}
						if (groupOwnedTag.equals("open")) {
							inventoryItemBase
									.isGroupOwned(Boolean
											.parseBoolean(new String(ch, start,
													length)));
						}
						if (groupPermissionsTag.equals("open")) {
							inventoryItemBase.setGroupPermissions(Long
									.parseLong(new String(ch, start, length)));
						}
						if (itemIDTag.equals("open")) {
							inventoryItemBase.setId(UUID.fromString(new String(
									ch, start, length)));
						}
						if (invTypeTag.equals("open")) {
							inventoryItemBase.setInvType(Integer
									.parseInt(new String(ch, start, length)));
						}
						if (itemNameTag.equals("open")) {
							inventoryItemBase.setName(new String(ch, start,
									length));
						}
						if (nextPermissionsTag.equals("open")) {
							inventoryItemBase.setNextPermissions(Long
									.parseLong(new String(ch, start, length)));
						}
						if (itemOwnerTag.equals("open")) {
							inventoryItemBase.setOwnerId(UUID
									.fromString(new String(ch, start, length)));
						}
						if (salePriceTag.equals("open")) {
							inventoryItemBase.setSalePrice(Integer
									.parseInt(new String(ch, start, length)));
						}
						if (saleTypeTag.equals("open")) {
							inventoryItemBase.setSalePrice(Integer
									.parseInt(new String(ch, start, length)));
						}
					} catch (Exception ex) {
						throw new SAXException(ex);
					}

				}

				// calls by the parser whenever '>' end tag is found in xml
				// makes tags flag to 'close'
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					if (qName.equalsIgnoreCase("FID")) {
						fidTag = "close";
					}
					if (qName.equalsIgnoreCase("VERSION")) {
						// The Version Tag can be both in the Top Level Tags or
						// inside the Folders Tags
						if (foldersTag.equals("close")) {
							versionTag = "close";
						} else {
							folderVersionTag = "close";
						}
					}

					// Handle the folders Tags
					if (qName.equalsIgnoreCase("FOLDERS")) {
						folderList = tmpFolderList;
						foldersTag = "close";
					}
					if (qName.startsWith("folder_")) {
						tmpFolderList.add(inventoryFolderBase);
						folder_Tag = "close";
					}
					if (qName.equalsIgnoreCase("ParentID")) {
						parentIDTag = "close";
					}
					if (qName.equalsIgnoreCase("Type")) {
						typeTag = "close";
					}
					if (qName.equalsIgnoreCase("Name")) {
						// The Name Tag can be both in the Folders Tags or
						// inside the Items Tags
						if (foldersTag.equals("close")) {
							itemNameTag = "close";
						} else {
							nameTag = "close";
						}
					}
					if (qName.equalsIgnoreCase("Owner")) {
						// The Owner Tag can be both in the Folders Tags or
						// inside the Items Tags
						if (foldersTag.equals("close")) {
							itemOwnerTag = "close";
						} else {
							ownerTag = "close";
						}
					}
					if (qName.equalsIgnoreCase("ID")) {
						// The ID Tag can be both in the Folders Tags or
						// inside the Items Tags
						if (foldersTag.equals("close")) {
							itemIDTag = "close";
						} else {
							iDTag = "close";
						}
					}

					// Handle the items Tags
					if (qName.equalsIgnoreCase("items")) {
						itemList = tmpItemList;
						itemsTag = "close";
					}
					if (qName.startsWith("item_")) {
						tmpItemList.add(inventoryItemBase);
						item_Tag = "close";
					}
					if (qName.equalsIgnoreCase("AssetID")) {
						assetIDTag = "close";
					}
					if (qName.equalsIgnoreCase("AssetType")) {
						assetTypeTag = "close";
					}
					if (qName.equalsIgnoreCase("BasePermissions")) {
						basePermissionsTag = "close";
					}
					if (qName.equalsIgnoreCase("CreationDate")) {
						creationDateTag = "close";
					}
					if (qName.equalsIgnoreCase("CreatorId")) {
						creatorIdTag = "close";
					}
					if (qName.equalsIgnoreCase("CreatorData")) {
						creatorDataTag = "close";
					}
					if (qName.equalsIgnoreCase("CurrentPermissions")) {
						currentPermissionsTag = "close";
					}
					if (qName.equalsIgnoreCase("Description")) {
						descriptionTag = "close";
					}
					if (qName.equalsIgnoreCase("EveryOnePermissions")) {
						everyOnePermissionsTag = "close";
					}
					if (qName.equalsIgnoreCase("Flags")) {
						flagsTag = "close";
					}
					if (qName.equalsIgnoreCase("Folder")) {
						folderTag = "close";
					}
					if (qName.equalsIgnoreCase("GroupID")) {
						groupIDTag = "close";
					}
					if (qName.equalsIgnoreCase("GroupOwned")) {
						groupOwnedTag = "close";
					}
					if (qName.equalsIgnoreCase("GroupPermissions")) {
						groupPermissionsTag = "close";
					}
					if (qName.equalsIgnoreCase("InvType")) {
						invTypeTag = "close";
					}
					if (qName.equalsIgnoreCase("NextPermissions")) {
						nextPermissionsTag = "close";
					}
					if (qName.equalsIgnoreCase("SalePrice")) {
						salePriceTag = "close";
					}
					if (qName.equalsIgnoreCase("SaleType")) {
						saleTypeTag = "close";
					}

				}

			};

			// parse the XML specified in the given path and uses supplied
			// handler to parse the document
			// this calls startElement(), endElement() and character() methods
			// accordingly
			saxParser.parse(inputStream, defaultHandler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
