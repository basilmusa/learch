package com.bloxware;

import java.io.File;

import com.bloxware.internals.service.Service;
import com.bloxware.internals.service.ServiceManager;
import com.bloxware.module.directoryspider.DirectorySpiderModule;
import com.bloxware.module.luceneindexer.IndexerModule;
import com.bloxware.module.luceneindexer.IndexerServiceAnnotation;
import com.bloxware.module.luceneindexer.IndexingComponent;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

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
		String storageDirectoryForIndexes = LearchCommon.getIndexStorageDirectory();
		
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
}
