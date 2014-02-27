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
package org.openjgrid.util;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.Assert;

public class FileUtilsTest {

	@Test
	public void testPathCombine() {
		String filepath = FilenameUtils.concat("inventory", "Library.xml");
		Assert.assertEquals(filepath, "inventory/Library.xml");
	}

	@Test
	public void testGetBaseDir() {
		String filepath = FilenameUtils.concat("inventory", "Library.xml");
		String basedir = FilenameUtils.getPathNoEndSeparator(filepath);
		Assert.assertEquals(basedir, "inventory");
	}

}
