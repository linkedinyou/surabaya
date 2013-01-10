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

}
