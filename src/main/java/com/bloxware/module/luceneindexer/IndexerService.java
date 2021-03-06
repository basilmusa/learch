package com.bloxware.module.luceneindexer;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

import com.bloxware.internals.service.Service;
import com.bloxware.module.directoryspider.DirectorySpider;
import com.bloxware.module.directoryspider.FileHandler;
import com.google.inject.Inject;
import com.google.inject.Singleton;

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
