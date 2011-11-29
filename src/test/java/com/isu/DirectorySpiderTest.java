package com.isu;

import static org.junit.Assert.*;

import org.junit.Test;

import com.isu.module.directoryspider.DirectorySpider;
import com.isu.module.directoryspider.PrintFileName;

public class DirectorySpiderTest {

	@Test
	public void test() {
		try
		{
			new DirectorySpider().traverseDirectory("C:\\", PrintFileName.INSTANCE);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
