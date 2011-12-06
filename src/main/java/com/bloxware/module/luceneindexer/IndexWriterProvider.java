package com.bloxware.module.luceneindexer;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class IndexWriterProvider implements Provider<IndexWriter> {
	
	private final String indexDirectoryPath;
	
	IndexWriter writer = null;
	
	@Inject
	public IndexWriterProvider(@IndexDirectoryPath String indexDirectoryPath) {
		this.indexDirectoryPath = indexDirectoryPath;
	}

	@Override 
	public IndexWriter get() 
	{
		if (writer != null) return writer;
		
		try 
		{
			Directory dir = FSDirectory.open(new File(this.indexDirectoryPath));
			
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_34, new StandardAnalyzer(Version.LUCENE_34));
			iwc.setOpenMode(OpenMode.CREATE);
			writer = new IndexWriter(dir,iwc);
			
			System.out.println("IndexWriter instance has been created [" + writer.toString() + "].");
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return writer;
	}
	
	/**
	 * 
	 */
	public void close() {
		try {
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer = null;
	}
}
