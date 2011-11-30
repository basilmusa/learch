package com.isu.module.luceneindexer;

import com.google.inject.AbstractModule;
import com.isu.internals.service.Service;
import com.isu.module.directoryspider.FileHandler;

public class IndexerModule extends AbstractModule {

	private final String storageDirectoryForIndexes;
	
	public IndexerModule(String storageDirectoryForIndexes) {
		this.storageDirectoryForIndexes = storageDirectoryForIndexes;
	}
	
	@Override
	protected void configure() {
		
		// Bind the Service to IndexerService
		bind(Service.class).annotatedWith(IndexerServiceAnnotation.class).to(IndexerService.class);
		bind(FileHandler.class).to(FileIndexer.class);
		bind(IndexingComponent.class).to(IndexerService.class);
		bindConstant().annotatedWith(IndexDirectoryPath.class).to(this.storageDirectoryForIndexes);
	}
}
