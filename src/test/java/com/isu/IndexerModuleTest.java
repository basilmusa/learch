package com.isu;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bloxware.internals.service.Service;
import com.bloxware.internals.service.ServiceManager;
import com.bloxware.module.directoryspider.DirectorySpiderModule;
import com.bloxware.module.luceneindexer.IndexerModule;
import com.bloxware.module.luceneindexer.IndexerServiceAnnotation;
import com.bloxware.module.luceneindexer.IndexingComponent;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

public class IndexerModuleTest {

	@Test
	public void test() 
	{
/**
		Injector injector = Guice.createInjector(
				new DirectorySpiderModule(),
				new IndexerModule("D:\\temp\\search"));
		
		Service indexerService = injector.getInstance(Key.get(Service.class, IndexerServiceAnnotation.class));
		ServiceManager.addShutdownHook(indexerService);
		indexerService.start();
		
		IndexingComponent indexingComponent = injector.getInstance(IndexingComponent.class);
		
		indexingComponent.indexAllFiles("D:\\basil\\source_code\\personal");
		
		ServiceManager.shutdownAllServices();
*/
	}
}
