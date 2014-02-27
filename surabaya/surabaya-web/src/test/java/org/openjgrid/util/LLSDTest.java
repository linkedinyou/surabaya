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
package org.openjgrid.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.openjgrid.datatypes.inventory.InventoryFolderBase;
import org.openjgrid.datatypes.inventory.InventoryItemBase;
import org.openjgrid.datatypes.llsd.LLSD;
import org.openjgrid.datatypes.llsd.LLSDInventoryFolder;
import org.openjgrid.datatypes.llsd.LLSDInventoryFolderContents;
import org.openjgrid.datatypes.llsd.LLSDInventoryItem;

/**
 * @author Akira Sonoda
 *
 */
public class LLSDTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.openjgrid.util.LLSD#llsdDeserialize(java.lang.String)}.
	 */
	@Test
	public void testLLSDDeserializeString() throws Exception {
		String llsdString = "<llsd><map><key>folders</key><array><map><key>fetch_folders</key><integer>1</integer><key>fetch_items</key><boolean>1</boolean><key>folder_id</key><uuid>4ba2cf15-8178-293d-fccb-645e7d148d45</uuid><key>owner_id</key><uuid>3dcad562-c070-4d58-b735-2f04f790a76c</uuid><key>sort_order</key><integer>1</integer></map></array></map></llsd>";
		LLSD.llsdDeserialize(llsdString);
	}

	/**
	 * Test method for {@link org.openjgrid.util.LLSD#llsdDeserialize(java.lang.String)}.
	 */
	@Test
	public void testLLSDDeserializeStringWithStringDatatype() throws Exception {
		String llsdString = "<llsd><map><key>agent_id</key><uuid>09a23ec1-d8af-4375-bd4d-3fc0d92e2174</uuid><key>cap_name</key><string>FetchInventory2</string><key>items</key><array><map><key>item_id</key><uuid>16c7d979-c48f-47bb-b677-93201cc266b4</uuid><key>owner_id</key><uuid>09a23ec1-d8af-4375-bd4d-3fc0d92e2174</uuid></map><map><key>item_id</key><uuid>278af03c-d8ac-4dce-afcb-e3bb287753ed</uuid><key>owner_id</key><uuid>09a23ec1-d8af-4375-bd4d-3fc0d92e2174</uuid></map><map><key>item_id</key><uuid>3a8ea189-78ae-409b-9211-103e2a09ea83</uuid><key>owner_id</key><uuid>09a23ec1-d8af-4375-bd4d-3fc0d92e2174</uuid></map><map><key>item_id</key><uuid>3b7e0413-c7d4-45c1-9feb-c230d979f0af</uuid><key>owner_id</key><uuid>09a23ec1-d8af-4375-bd4d-3fc0d92e2174</uuid></map><map><key>item_id</key><uuid>3db7fa6b-fc7f-428c-a671-bf60031fde20</uuid><key>owner_id</key><uuid>09a23ec1-d8af-4375-bd4d-3fc0d92e2174</uuid></map><map><key>item_id</key><uuid>66af8145-fb6a-4087-8a2c-b020d45d88e8</uuid><key>owner_id</key><uuid>09a23ec1-d8af-4375-bd4d-3fc0d92e2174</uuid></map><map><key>item_id</key><uuid>6d55f714-2008-43bb-ba99-1b99abe73ba3</uuid><key>owner_id</key><uuid>09a23ec1-d8af-4375-bd4d-3fc0d92e2174</uuid></map><map><key>item_id</key><uuid>a33277ba-c0dc-4dfa-bd6c-368f4c5fec93</uuid><key>owner_id</key><uuid>09a23ec1-d8af-4375-bd4d-3fc0d92e2174</uuid></map><map><key>item_id</key><uuid>f71430ba-3575-477e-8548-79dece3cec3c</uuid><key>owner_id</key><uuid>09a23ec1-d8af-4375-bd4d-3fc0d92e2174</uuid></map></array></map></llsd>";
		LLSD.llsdDeserialize(llsdString);
	}
	
	@Test
	public void testLLSDSerializeStructure() throws Exception {
		LLSDInventoryFolderContents contents = new LLSDInventoryFolderContents();
		contents.agent_id = UUID.randomUUID();
		contents.descendents = 5;
		contents.folder_id = UUID.randomUUID();
		contents.owner_id = UUID.randomUUID();
		contents.version = 1;
	
		ArrayList<InventoryFolderBase> categories = new ArrayList<InventoryFolderBase>();
		InventoryFolderBase base = new InventoryFolderBase();
		String inventoryFolderString = "<?xml version=\"1.0\"?><ServerResponse><folder type=\"List\"><ParentID>10a7b798-806e-4418-9816-eb75ad3ae9f7</ParentID><Type>46</Type><Version>30</Version><Name>Current Outfit</Name><Owner>3dcad562-c070-4d58-b735-2f04f790a76c</Owner><ID>4ba2cf15-8178-293d-fccb-645e7d148d45</ID></folder></ServerResponse>";
		base.fromXml(inventoryFolderString);
		categories.add(base);
		categories.add(base);
		Iterator<InventoryFolderBase> categoriesIter = categories.iterator();
		while( categoriesIter.hasNext() ) {
			contents.categories.add(new LLSDInventoryFolder(categoriesIter.next()) );
		}
		
		ArrayList<InventoryItemBase> items = new ArrayList<InventoryItemBase>();
		InventoryItemBase itembase = new InventoryItemBase();
		String itemString = "<?xml version=\"1.0\"?><ServerResponse><item type=\"List\"><AssetID>c75b5707-a07b-4dfd-b195-6a9a51b59fee</AssetID><AssetType>6</AssetType><BasePermissions>581633</BasePermissions><CreationDate>1350721185</CreationDate><CreatorId>3dcad562-c070-4d58-b735-2f04f790a76c</CreatorId><CreatorData></CreatorData><CurrentPermissions>581641</CurrentPermissions><Description>(No Description)</Description><EveryOnePermissions>32768</EveryOnePermissions><Flags>524544</Flags><Folder>477f085e-1fcd-4044-aa1c-7c151ba9391c</Folder><GroupID>00000000-0000-0000-0000-000000000000</GroupID><GroupOwned>False</GroupOwned><GroupPermissions>0</GroupPermissions><ID>64889d18-9e79-46d2-962c-83fa89dae2df</ID><InvType>6</InvType><Name>Akra Necklace</Name><NextPermissions>581632</NextPermissions><Owner>3dcad562-c070-4d58-b735-2f04f790a76c</Owner><SalePrice>0</SalePrice><SaleType>0</SaleType></item></ServerResponse>";
		itembase.fromXml(itemString);
		items.add(itembase);
		items.add(itembase);
		Iterator<InventoryItemBase> itemsIter = items.iterator();
		while( itemsIter.hasNext() ) {
			contents.items.add(new LLSDInventoryItem(itemsIter.next()) );
		}
		
		String result = LLSD.LLSDSerialize(contents);
		System.out.println(result);
	}
	
	@Test
	public void testInheritedFields() throws Exception {
		InventoryItemBase itembase = new InventoryItemBase();
		String itemString = "<?xml version=\"1.0\"?><ServerResponse><item type=\"List\"><AssetID>c75b5707-a07b-4dfd-b195-6a9a51b59fee</AssetID><AssetType>6</AssetType><BasePermissions>581633</BasePermissions><CreationDate>1350721185</CreationDate><CreatorId>3dcad562-c070-4d58-b735-2f04f790a76c</CreatorId><CreatorData></CreatorData><CurrentPermissions>581641</CurrentPermissions><Description>(No Description)</Description><EveryOnePermissions>32768</EveryOnePermissions><Flags>524544</Flags><Folder>477f085e-1fcd-4044-aa1c-7c151ba9391c</Folder><GroupID>00000000-0000-0000-0000-000000000000</GroupID><GroupOwned>False</GroupOwned><GroupPermissions>0</GroupPermissions><ID>64889d18-9e79-46d2-962c-83fa89dae2df</ID><InvType>6</InvType><Name>Akra Necklace</Name><NextPermissions>581632</NextPermissions><Owner>3dcad562-c070-4d58-b735-2f04f790a76c</Owner><SalePrice>0</SalePrice><SaleType>0</SaleType></item></ServerResponse>";
		itembase.fromXml(itemString);
		List<Field> fields = LLSD.getInheritedPrivateFields(itembase.getClass());
		Iterator<Field> fieldsIter = fields.iterator();
		while(fieldsIter.hasNext()) {
			Field aField = fieldsIter.next();
			System.out.println("Fieldname:" + aField.getName());
		}
	}
}
