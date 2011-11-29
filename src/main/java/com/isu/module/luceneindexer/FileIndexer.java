package com.isu.module.luceneindexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.isu.module.directoryspider.FileHandler;

@Singleton
public class FileIndexer implements FileHandler 
{
	private final long MAX_FILE_SIZE_TO_INDEX = (long) (0.5 * 1024 * 1024);
	private IndexWriter indexWriter;
	
	@Inject
	FileIndexer(IndexWriterProvider indexWriterProvider) {
		this.indexWriter = indexWriterProvider.get();
	}
	
	@Override
	public void process(File file) 
	{
		if (file.isDirectory()) {
			return;
		}
		
		// File should be readable
		if (!file.canRead()) {
			System.out.println("File [" + file.getName() + "]is not readable, ignoring.");
			return;
		}
		
		// File should should be less than MAX_FILE_SIZE_TO_INDEX in bytes
		if (file.length() > MAX_FILE_SIZE_TO_INDEX) {
			System.out.println("File [" + file.getName() + "] exceed maximum size, ignoring.");
			return;
		}
		
		// Now read the text inside it
		indexFile(file);
	}
	
	private void indexFile(File file) {
		System.out.println("Indexing document: [" + file.getAbsolutePath() + "]");
		Document doc = new Document();
		try 
		{
			doc.add(new Field("contents", new FileReader(file)));
			doc.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("fullpath", file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			indexWriter.addDocument(doc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
