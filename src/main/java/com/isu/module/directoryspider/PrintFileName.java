package com.isu.module.directoryspider;

import java.io.File;

public class PrintFileName implements FileHandler {
	
	public static final PrintFileName INSTANCE = new PrintFileName();
	
	private PrintFileName() {};
	
	public void process(File file) {
		System.out.println(file.getAbsolutePath());
	}
}
