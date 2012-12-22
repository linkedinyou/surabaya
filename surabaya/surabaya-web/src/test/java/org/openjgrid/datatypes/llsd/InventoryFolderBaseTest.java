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
	 * Test method for {@link org.openjgrid.datatypes.llsd.InventoryFolderBase#fromXml(java.lang.String)}.
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

}
