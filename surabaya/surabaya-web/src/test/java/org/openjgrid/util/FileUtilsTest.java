package org.openjgrid.util;

import static org.junit.Assert.*;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

public class FileUtilsTest {

	@Test
	public void testPathCombine() {
		String filepath = FilenameUtils.concat("inventory", "Library.xml");
		assert(filepath.equals("inventory/Library.xml"));
	}

}
