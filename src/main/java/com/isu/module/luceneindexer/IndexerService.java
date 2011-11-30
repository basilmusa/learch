package com.isu.module.luceneindexer;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.isu.internals.service.Service;
import com.isu.module.directoryspider.DirectorySpider;
import com.isu.module.directoryspider.FileHandler;

@Singleton
public class IndexerService implements Service, IndexingComponent
{
	private final IndexWriterProvider indexWriterProvider;
	private final DirectorySpider directorySpider;
	private final FileHandler fileHandler;
	private IndexWriter indexWriter = null;
	
	@Inject
	public IndexerService(
			IndexWriterProvider indexWriterProvider, 
			DirectorySpider directorySpider,
			FileHandler fileHandler) {
		this.directorySpider = directorySpider;
		this.indexWriterProvider = indexWriterProvider;
		this.fileHandler = fileHandler;
	}
	
	@Override
	public void start() {
		System.out.println("Starting indexing module.");
		indexWriter = indexWriterProvider.get();
	}

	@Override
	public void shutdown() {
		System.out.println("Closing index storage.");
		
		indexWriterProvider.close();
	}

	@Override
	public void indexAllFiles(String directoryPath) {
		directorySpider.traverseDirectory(directoryPath, fileHandler);
	}
}
