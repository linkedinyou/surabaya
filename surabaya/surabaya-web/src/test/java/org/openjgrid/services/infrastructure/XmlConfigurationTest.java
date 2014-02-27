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
package org.openjgrid.services.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Akira Sonoda
 *
 */
public class XmlConfigurationTest {

	String xmlString = null;
	String singleSectionXmlString = null;
	String emptySectionXmlString = null;
	String itemXmlString = null;
	
	
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
		sb.append("  <Section Name=\"OpenSim NonStandard Library\">\n");
		sb.append("    <Key Name=\"foldersFile\" Value=\"OpenSimLibrary/OpenSimNonLibraryFolders.xml\"/>\n");
		sb.append("    <Key Name=\"itemsFile\" Value=\"OpenSimLibrary/OpenSimNonLibrary.xml\"/>\n");
		sb.append("  </Section>");
		sb.append("</Nini>");
		xmlString = sb.toString();

		StringBuilder ssb = new StringBuilder();
		ssb.append("<Nini>\n");
		ssb.append("  <Section Name=\"OpenSim Standard Library\">\n");
		ssb.append("    <Key Name=\"foldersFile\" Value=\"OpenSimLibrary/OpenSimLibraryFolders.xml\"/>\n");
		ssb.append("    <Key Name=\"itemsFile\" Value=\"OpenSimLibrary/OpenSimLibrary.xml\"/>\n");
		ssb.append("  </Section>");
		ssb.append("</Nini>");
		singleSectionXmlString = ssb.toString();

		StringBuilder esb = new StringBuilder();
		esb.append("<Nini>\n");
		esb.append("</Nini>");
		emptySectionXmlString = esb.toString();

		StringBuilder isb = new StringBuilder();
		isb.append("<Nini>\n");
		isb.append("  <Section Name=\"OpenSim Standard Library\">\n");
		isb.append("    <Key Name=\"Name\" Value=\"Akira Sonoda\"/>\n");
		isb.append("    <Key Name=\"AgeInDays\" Value=\"1500\"/>\n");
		isb.append("  </Section>");
		isb.append("</Nini>");
		itemXmlString = isb.toString();
		
	}

	@Test
	public void testNiniConfiguration() throws IOException, ConfigurationException {
		Assert.assertTrue(xmlString != null);
		InputStream file = IOUtils.toInputStream(xmlString, "UTF-8");
		XMLConfiguration xmlConfiguration = new XMLConfiguration();
		xmlConfiguration.load(file);
		Iterator<String> keys = xmlConfiguration.getKeys();
		Assert.assertTrue(keys.hasNext());
		while(keys.hasNext()) {
			System.out.println("Key: " + keys.next());
		}
	}

	
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetValuesOutOfNiniConfiguration() throws IOException, ConfigurationException {
		Assert.assertTrue(xmlString != null);
		InputStream file = IOUtils.toInputStream(xmlString, "UTF-8");
		XMLConfiguration xmlConfiguration = new XMLConfiguration();
		xmlConfiguration.load(file);
		Object prop = xmlConfiguration.getProperty("Section[@Name]");
		int numOfSections = ((Collection) prop).size();
		Assert.assertTrue(numOfSections == 2);
		for(int i=0; i<numOfSections; i++) {
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(0)[@Name]"));
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(0)[@Value]"));
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(1)[@Name]"));
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(1)[@Value]"));	
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetValuesOutOfNiniConfigurationSingleSection() throws IOException, ConfigurationException {
		Assert.assertTrue(singleSectionXmlString != null);
		InputStream file = IOUtils.toInputStream(singleSectionXmlString, "UTF-8");
		XMLConfiguration xmlConfiguration = new XMLConfiguration();
		xmlConfiguration.load(file);
		Object prop = xmlConfiguration.getProperty("Section[@Name]");
		int numOfSections = 0;
		if(prop instanceof Collection) {
			numOfSections = ((Collection) prop).size();
		} else if (prop == null ) {
			numOfSections = 0;
		} else {
			numOfSections = 1;			
		}
		Assert.assertTrue(numOfSections == 1);
		for(int i=0; i<numOfSections; i++) {
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(0)[@Name]"));
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(0)[@Value]"));
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(1)[@Name]"));
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(1)[@Value]"));	
		}
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetValuesOutOfNiniConfigurationEmptySection() throws IOException, ConfigurationException {
		Assert.assertTrue(emptySectionXmlString != null);
		InputStream file = IOUtils.toInputStream(emptySectionXmlString, "UTF-8");
		XMLConfiguration xmlConfiguration = new XMLConfiguration();
		xmlConfiguration.load(file);
		Object prop = xmlConfiguration.getProperty("Section[@Name]");
		int numOfSections = 0;
		if(prop instanceof Collection) {
			numOfSections = ((Collection) prop).size();
		} else if (prop == null ) {
			numOfSections = 0;
		} else {
			numOfSections = 1;			
		}
		Assert.assertTrue(numOfSections == 0);
		for(int i=0; i<numOfSections; i++) {
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(0)[@Name]"));
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(0)[@Value]"));
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(1)[@Name]"));
			System.out.println("Name: " + xmlConfiguration.getString("Section("+i+").Key(1)[@Value]"));	
		}
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetNumberOfKeys() throws IOException, ConfigurationException {
		Assert.assertTrue(xmlString != null);
		InputStream file = IOUtils.toInputStream(xmlString, "UTF-8");
		XMLConfiguration xmlConfiguration = new XMLConfiguration();
		xmlConfiguration.load(file);
		Object prop = xmlConfiguration.getProperty("Section[@Name]");
		int numOfSections = 0;
		if(prop instanceof Collection) {
			numOfSections = ((Collection) prop).size();
		} else if (prop == null ) {
			numOfSections = 0;
		} else {
			numOfSections = 1;			
		}
		for(int i=0; i<numOfSections; i++) {
			Object keyProp = xmlConfiguration.getProperty("Section("+i+").Key[@Name]");
			if(keyProp instanceof Collection) {
				int numKeys = ((Collection) keyProp).size();
				System.out.println("Number of Keys: " + numKeys);
				Assert.assertTrue( numKeys == 2 );
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetInteger() throws IOException, ConfigurationException {
		Assert.assertTrue(itemXmlString != null);
		InputStream file = IOUtils.toInputStream(itemXmlString, "UTF-8");
		XMLConfiguration xmlConfiguration = new XMLConfiguration();
		xmlConfiguration.load(file);
		Object prop = xmlConfiguration.getProperty("Section[@Name]");
		int numOfSections = 0;
		if(prop instanceof Collection) {
			numOfSections = ((Collection) prop).size();
		} else if (prop == null ) {
			numOfSections = 0;
		} else {
			numOfSections = 1;			
		}
		for(int i=0; i<numOfSections; i++) {
			String name = xmlConfiguration.getString("Section("+i+").Key(0)[@Value]");
			System.out.println("Name: " + name);
			int age = xmlConfiguration.getInt("Section("+i+").Key(1)[@Value]");
			System.out.println("Age in Days: " + age);
			Assert.assertEquals("Akira Sonoda", name);
			Assert.assertEquals(1500, age);
		}
	}
	
}
