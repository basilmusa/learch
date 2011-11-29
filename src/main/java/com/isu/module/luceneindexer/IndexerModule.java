package com.isu.module.luceneindexer;

import com.google.inject.AbstractModule;
import com.isu.internals.service.Service;
import com.isu.module.directoryspider.FileHandler;
import com.isu.module.directoryspider.PrintFileName;

public class IndexerModule extends AbstractModule {

	@Override
	protected void configure() {
		
		// Bind the Service to IndexerService
		bind(Service.class).annotatedWith(IndexerServiceAnnotation.class).to(IndexerService.class);
		bindConstant().annotatedWith(IndexDirectoryPath.class).to("D:\\temp\\search");
		bind(FileHandler.class).to(FileIndexer.class);
		bind(IndexingComponent.class).to(IndexerService.class);
	}
}
