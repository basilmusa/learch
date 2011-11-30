package com.isu.module.directoryspider;

import java.io.File;

import com.google.inject.Singleton;

@Singleton
public class PrintFileName implements FileHandler 
{
	public Direction process(File file) {
		System.out.println(file.getAbsolutePath());
		return Direction.NORMAL;
	}
}
