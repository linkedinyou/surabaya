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
package org.openjgrid.services.infrastructure;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Akira Sonoda
 *
 */
public class XmlConfigurationTest {

	String xmlString = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<Nini>\n");
		sb.append("  <Section Name=\"OpenSim Standard Library\">\n");
		sb.append("    <Key Name=\"foldersFile\" Value=\"OpenSimLibrary/OpenSimLibraryFolders.xml\"/>\n");
		sb.append("    <Key Name=\"itemsFile\" Value=\"OpenSimLibrary/OpenSimLibrary.xml\"/>\n");
		sb.append("  </Section>");
		sb.append("</Nini>");
		xmlString = sb.toString();
	}

	@Test
	public void testNiniConfiguration() throws IOException, ConfigurationException {
		Assert.assertTrue(xmlString != null);
		InputStream file = IOUtils.toInputStream(xmlString, "UTF-8");
		XMLConfiguration xmlConfiguration = new XMLConfiguration();
		xmlConfiguration.load(file);
		Iterator<String> keys = xmlConfiguration.getKeys();
		while(keys.hasNext()) {
			System.out.println("Key: " + keys.next());
		}
		
	}

}
