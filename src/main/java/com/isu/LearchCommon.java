package com.isu;

import java.io.File;

public class LearchCommon {

	/**
	 * The index storage directory is determined by appending /index to the home directory.
	 * If we are in development mode, then the
	 * @return
	 */
	static String getIndexStorageDirectory() {
		
		String indexStorageDirectory;
		final String INDEX_SUBDIR_NAME = File.separator + "index";
		
		if (figureOutRunMode() == RunMode.DEVELOPMENT)
		{
			String tempDirectory = System.getProperty("java.io.tmpdir");
			
			tempDirectory = chompPathSeparator(tempDirectory);
			indexStorageDirectory = tempDirectory + INDEX_SUBDIR_NAME;
		}
		else
		{
			String homeDirectory = getHomeDirectory();
			indexStorageDirectory = homeDirectory + INDEX_SUBDIR_NAME;
		}
		return indexStorageDirectory;
	}

	/**
	 * Given a path that ends with a /, this method removes the last slash in the path.
	 * Example: 
	 * 
	 * If passed: C:\Users\babbas\AppData\Local\Temp\
	 * It will return: C:\Users\babbas\AppData\Local\Temp (without a slash at the end)
	 * 
	 * @param pathToChomp
	 * @return
	 */
	static String chompPathSeparator(String pathToChomp) {
		if (pathToChomp.endsWith(File.separator)) {
			pathToChomp = pathToChomp.substring(0, pathToChomp.length() - 1);
		}
		return pathToChomp;
	}

	/**
	 * Get home directory of installation directory.
	 * The home directory is figured out using the directory the jar file is in, then moving
	 * one parent above the current directory.
	 * @return
	 */
	static String getHomeDirectory() {
		return new File(getJarFileCurrentDirectory()).getParent();
	}

	/** 
	 * Figure out the jar file's current directory.
	 * This method is necessary to be used to figure out the home directory in the
	 * getHomeDirectory method
	 * 
	 * @return
	 */
	private static String getJarFileCurrentDirectory() {
		return LearchIndex.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		// return ClassLoader.getSystemClassLoader().getResource(".").getPath();
	}
	
	/**
	 * Figure out if development or running on a production deployment
	 * We are in development mode if the jar file path ends with "/classes/".
	 */
	static RunMode figureOutRunMode() 
	{
		RunMode runMode = RunMode.PRODUCTION;
		
		if (getJarFileCurrentDirectory().endsWith("/classes/")) {
			runMode = RunMode.DEVELOPMENT;
		}
		
		return runMode;
	}
	
	enum RunMode {
		DEVELOPMENT,
		PRODUCTION
	}
}
