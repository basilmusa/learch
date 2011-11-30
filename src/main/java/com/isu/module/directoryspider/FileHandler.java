package com.isu.module.directoryspider;

import java.io.File;

public interface FileHandler {
	Direction process(File file);
	
	public enum Direction {
		NORMAL,
		DO_NOT_TRAVERSE
	}
}
