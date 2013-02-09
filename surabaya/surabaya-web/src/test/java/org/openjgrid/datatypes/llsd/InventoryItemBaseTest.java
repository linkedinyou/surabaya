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

import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.openjgrid.datatypes.exceptions.NotImplementedException;
import org.openjgrid.datatypes.inventory.InventoryException;
import org.openjgrid.datatypes.inventory.InventoryItemBase;

/**
 * @author Akira Sonoda
 *
 */
public class InventoryItemBaseTest {

		
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.openjgrid.datatypes.inventory.InventoryItemBase#fromXml(java.lang.String)}.
	 * @throws XMLStreamException 
	 * @throws InventoryException 
	 */
	@Test
	public void testFromXml() throws NotImplementedException, XMLStreamException, InventoryException {
		String xmlString = "<?xml version=\"1.0\"?><ServerResponse><item type=\"List\"><AssetID>c75b5707-a07b-4dfd-b195-6a9a51b59fee</AssetID><AssetType>6</AssetType><BasePermissions>581633</BasePermissions><CreationDate>1350721185</CreationDate><CreatorId>3dcad562-c070-4d58-b735-2f04f790a76c</CreatorId><CreatorData></CreatorData><CurrentPermissions>581641</CurrentPermissions><Description>(No Description)</Description><EveryOnePermissions>32768</EveryOnePermissions><Flags>524544</Flags><Folder>477f085e-1fcd-4044-aa1c-7c151ba9391c</Folder><GroupID>00000000-0000-0000-0000-000000000000</GroupID><GroupOwned>False</GroupOwned><GroupPermissions>0</GroupPermissions><ID>64889d18-9e79-46d2-962c-83fa89dae2df</ID><InvType>6</InvType><Name>Akra Necklace</Name><NextPermissions>581632</NextPermissions><Owner>3dcad562-c070-4d58-b735-2f04f790a76c</Owner><SalePrice>0</SalePrice><SaleType>0</SaleType></item></ServerResponse>";
		InventoryItemBase inventoryItemBase = new InventoryItemBase();
		inventoryItemBase.fromXml(xmlString);
		assert(inventoryItemBase.getAssetId().equals(UUID.fromString("c75b5707-a07b-4dfd-b195-6a9a51b59fee")));
		assert(inventoryItemBase.getSaleType() == (byte) 0);
	}

	@Test
	public void testConstructorUUID() {
		InventoryItemBase inventoryItemBase = new InventoryItemBase(UUID.fromString("c75b5707-a07b-4dfd-b195-6a9a51b59fee"));
		UUID result = inventoryItemBase.getId();
		assert(result != null);
		assert(result.equals(UUID.fromString("c75b5707-a07b-4dfd-b195-6a9a51b59fee")));
		result = inventoryItemBase.getAssetId();
		assert(result == null);
	}
	
	@Test
	public void testEmptyXML() throws XMLStreamException, InventoryException {
		String xmldata = "<?xml version=\"1.0\"?><ServerResponse />";
		InventoryItemBase collection = new InventoryItemBase();
		collection.fromXml(xmldata.toString());
	}

}
