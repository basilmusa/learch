package com.isu.module.luceneindexer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.isu.module.directoryspider.FileHandler;

@Singleton
public class FileIndexer implements FileHandler 
{
	private final long MAX_FILE_SIZE_TO_INDEX = (long) (0.5 * 1024 * 1024);
	private final ImmutableSet<String> EXCLUDE_DIRECTORIES = ImmutableSet.of(".svn", ".git");
	private final ImmutableSet<String> EXCLUDE_DIRECTORY_FULL_PATH = ImmutableSet.of("/proc", "/sys", "/opt", "/dev", "/mnt");
	private final ImmutableSet<String> EXCLUDE_FILES = ImmutableSet.of();
	private final ImmutableSet<String> EXCLUDE_EXTENSIONS = ImmutableSet.of(".rrd", ".zip", ".tar.gz", ".log", ".git", ".png", ".jpg");
	
	private IndexWriter indexWriter;
	
	@Inject
	FileIndexer(IndexWriterProvider indexWriterProvider) {
		this.indexWriter = indexWriterProvider.get();
	}
	
	@Override
	public Direction process(File file) 
	{
		// Handle symbolic links
		try {
			if (Files.isSymbolicLink(Paths.get(file.getCanonicalPath()))) {
				return Direction.DO_NOT_TRAVERSE;
			}
			if (!Files.isReadable(Paths.get(file.getCanonicalPath())))
			{
				return Direction.DO_NOT_TRAVERSE;
			}
		} catch (IOException e) {
			return Direction.DO_NOT_TRAVERSE;
		}

		// Check if directory
		if (file.isDirectory()) {
			// Exclude .svn and .git folders
			if (EXCLUDE_DIRECTORIES.contains(file.getName())) 
			{
				System.out.println("Returning do not traverse");
				return Direction.DO_NOT_TRAVERSE;
			}
			else if (EXCLUDE_DIRECTORY_FULL_PATH.contains(file.getAbsolutePath()))
			{
				System.out.println("Returning do not traverse");
				return Direction.DO_NOT_TRAVERSE;
			}
			else
			{
				return Direction.NORMAL;
			}
		}

		// If file is zero length then ignore
		if (file.length() == 0) {
			System.out.println("File [" + file.getName() + "] is zero size.");
			return Direction.NORMAL;
		}
		
		// Files to exclude
		if (EXCLUDE_FILES.contains(file.getName())) {
			return Direction.NORMAL;
		}
		
		// Exclude extensions
		for (String extension : EXCLUDE_EXTENSIONS) {
			if (file.getName().endsWith(extension)) {
				// Since it could be a directory with an extension, we will
				// return DO_NOT_TRAVERSE instead of NORMAL
				return Direction.DO_NOT_TRAVERSE;
			}
		}
		
		// File should be readable
		if (!file.canRead()) {
			System.out.println("File [" + file.getName() + "]is not readable, ignoring.");
			return Direction.NORMAL;
		}
		
		// File should should be less than MAX_FILE_SIZE_TO_INDEX in bytes
		if (file.length() > MAX_FILE_SIZE_TO_INDEX) {
			System.out.println("File [" + file.getName() + "] exceed maximum size, ignoring.");
			return Direction.NORMAL;
		}

			
		// Now perform the indexing, if an error occurs, show an ugly
		// stack trace, just print an ERROR and go to the next file
		indexFile(file);
		
		return Direction.NORMAL;
	}
	
	private void indexFile(File file) {
		System.out.println("Indexing document: [" + file.getAbsolutePath() + "]");
		
		FileReader fileReader;
		
		try {
			fileReader = new FileReader(file); // To get the error as soon as possible
		}
		catch (IOException e) {
			// Cant be read for some reason, just return
			System.out.println("File cant be read for some reason.");
			return;
		}
		
		Document doc = new Document();

		try {
			doc.add(new Field("contents", fileReader));
			doc.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("fullpath", file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			indexWriter.addDocument(doc);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally 
		{
			try {
				fileReader.close();
			} catch (IOException e) {}
		}
	}
}
