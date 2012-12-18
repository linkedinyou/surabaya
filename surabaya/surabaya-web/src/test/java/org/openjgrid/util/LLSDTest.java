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

import org.junit.Before;
import org.junit.Test;
import org.openjgrid.datatypes.llsd.LLSD;

/**
 * @author markusgasser
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

}
