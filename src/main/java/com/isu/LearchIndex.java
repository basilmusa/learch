package com.isu;

import java.io.File;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.isu.internals.service.Service;
import com.isu.internals.service.ServiceManager;
import com.isu.module.directoryspider.DirectorySpiderModule;
import com.isu.module.luceneindexer.IndexerModule;
import com.isu.module.luceneindexer.IndexerServiceAnnotation;
import com.isu.module.luceneindexer.IndexingComponent;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class LearchIndex 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// . Figure out the directory the jar file is in
		// . Move up two directories and enter the index directory to create the index
		String storageDirectoryForIndexes = getIndexStorageDirectory();
		
		// TODO . Read the option -exclude-directories to exclude directories
		// TODO . Read the option -exclude-files to exclude files
		// TODO . Read the option -maxfilesize-to-index

		// . Read the option -d which is the directory to index, if not supplied use the default which is '/'
		OptionParser parser = new OptionParser("d:");
		OptionSet options = parser.parse(args);
		
		if (!options.has("d")) {
			System.out.println("Please specify the directory to index using the -d option.\n" +
					"Example: learch_index -d /");
			System.exit(0);
		}
		
		String indexDirectory = (String) options.valueOf("d");
		
		if (!new File(indexDirectory).isDirectory()) {
			System.out.println("Argument passed is not a directory.");
			System.exit(0);
		}

		if (!new File(indexDirectory).exists()) {
			System.out.println("The directory specified does not exist.");
			System.exit(0);
		}
		
		if (!new File(indexDirectory).canRead()) {
			System.out.println("The directory specified is not readable.");
			System.exit(0);
		}
		
		// Start the indexing process
		Injector injector = Guice.createInjector(
				new DirectorySpiderModule(),
				new IndexerModule(storageDirectoryForIndexes));
		
		Service indexerService = injector.getInstance(Key.get(Service.class, IndexerServiceAnnotation.class));
		ServiceManager.addShutdownHook(indexerService);
		indexerService.start();
		IndexingComponent indexingComponent = injector.getInstance(IndexingComponent.class);
		indexingComponent.indexAllFiles(indexDirectory);
		ServiceManager.shutdownAllServices();
	}

	/**
	 * The index storage directory is determined by appending /index to the home directory.
	 * If we are in development mode, then the
	 * @return
	 */
	private static String getIndexStorageDirectory() {
		
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
	private static String chompPathSeparator(String pathToChomp) {
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
	private static String getHomeDirectory() {
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
	private static RunMode figureOutRunMode() 
	{
		RunMode runMode = RunMode.PRODUCTION;
		
		if (getJarFileCurrentDirectory().endsWith("/classes/")) {
			runMode = RunMode.DEVELOPMENT;
		}
		
		return runMode;
	}
	
	private enum RunMode {
		DEVELOPMENT,
		PRODUCTION
	}
}
