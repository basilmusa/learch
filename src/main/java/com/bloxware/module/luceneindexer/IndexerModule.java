package com.bloxware.module.luceneindexer;

import com.bloxware.internals.service.Service;
import com.bloxware.module.directoryspider.FileHandler;
import com.google.inject.AbstractModule;

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
