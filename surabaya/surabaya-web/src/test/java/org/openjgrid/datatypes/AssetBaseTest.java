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
package org.openjgrid.datatypes;

import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.openjgrid.datatypes.asset.AssetBase;
import org.openjgrid.datatypes.asset.AssetFlags;

/**
 * @author Akira Sonoda
 *
 */
public class AssetBaseTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFromXML() throws XMLStreamException {
		StringBuilder sb = new StringBuilder();
		sb.append("?<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<AssetBase xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
        sb.append("<Data>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</Data>");
        sb.append("<FullID>");
        sb.append("\n");
        sb.append("    <Guid>cf4d1622-d382-437e-b567-450cff2a32ff</Guid>");
        sb.append("\n");
        sb.append("</FullID>");
        sb.append("\n");
        sb.append("<ID>cf4d1622-d382-437e-b567-450cff2a32ff</ID>");
        sb.append("\n");
        sb.append("<Name>From IAR</Name>");
        sb.append("\n");
        sb.append("<Description />");
        sb.append("\n");
        sb.append("<Type>0</Type>");
        sb.append("\n");
        sb.append("<Local>false</Local>");
        sb.append("\n");
        sb.append("<Temporary>false</Temporary>");
        sb.append("<CreatorID>00000000-0000-0000-0000-000000000000</CreatorID>");
        sb.append("<Flags>Normal</Flags>");
        sb.append("</AssetBase>");
        AssetBase assetBase = new AssetBase();
        assetBase.fromXml(sb.toString());
        assert(assetBase.getDataLength() > 0);
        assert(assetBase.getFullID().equals(UUID.fromString("cf4d1622-d382-437e-b567-450cff2a32ff")));
        assert(assetBase.getID().equalsIgnoreCase("cf4d1622-d382-437e-b567-450cff2a32ff"));
        assert(assetBase.getFlags() == AssetFlags.Normal.getType());
        
	}

}
