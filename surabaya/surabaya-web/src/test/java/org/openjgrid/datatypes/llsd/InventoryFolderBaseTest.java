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
package org.openjgrid.datatypes.llsd;

import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.openjgrid.datatypes.inventory.InventoryException;
import org.openjgrid.datatypes.inventory.InventoryFolderBase;

/**
 * @author Akira Sonoda
 *
 */
public class InventoryFolderBaseTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.openjgrid.datatypes.inventory.InventoryFolderBase#fromXml(java.lang.String)}.
	 * @throws XMLStreamException 
	 * @throws InventoryException 
	 */
	@Test
	public void testFromXml() throws XMLStreamException, InventoryException {
		String xmlString = "<?xml version=\"1.0\"?><ServerResponse><folder type=\"List\"><ParentID>10a7b798-806e-4418-9816-eb75ad3ae9f7</ParentID><Type>46</Type><Version>30</Version><Name>Current Outfit</Name><Owner>3dcad562-c070-4d58-b735-2f04f790a76c</Owner><ID>4ba2cf15-8178-293d-fccb-645e7d148d45</ID></folder></ServerResponse>";
		InventoryFolderBase invntoryFolderBase = new InventoryFolderBase();
		invntoryFolderBase.fromXml(xmlString);
		assert(invntoryFolderBase.getId().equals(UUID.fromString("4ba2cf15-8178-293d-fccb-645e7d148d45")));
	}
	
	@Test
	public void test2FromXml() throws XMLStreamException, InventoryException {
		String xmlString = "<?xml version=\"1.0\"?><ServerResponse><folder type=\"List\"><ParentID>fb1cb1cd-c9fa-4cfc-86e3-42b9459424d0</ParentID><Type>-1</Type><Version>1</Version><Name>****</Name><Owner>3dcad562-c070-4d58-b735-2f04f790a76c</Owner><ID>2ee9f743-6256-48b0-a5e1-bbd4ffe37d85</ID></folder></ServerResponse>";
		InventoryFolderBase invntoryFolderBase = new InventoryFolderBase();
		invntoryFolderBase.fromXml(xmlString);
		assert(invntoryFolderBase.getId().equals(UUID.fromString("2ee9f743-6256-48b0-a5e1-bbd4ffe37d85")));
	}
	
	@Test
	public void testEmptyXML() throws XMLStreamException, InventoryException {
		String xmldata = "<?xml version=\"1.0\"?><ServerResponse />";
		InventoryFolderBase collection = new InventoryFolderBase();
		collection.fromXml(xmldata.toString());
	}


}
