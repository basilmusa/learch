package com.isu;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LearchSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// Validate that one argument is passed
		if (args.length == 0) {
			System.out.println("Please provide a search string to search.");
			System.exit(0);
		}
		else if (args.length > 1) {
			System.out.println("Only one argument should be passed.");
			System.exit(0);
		}
		
		// Search string
		String searchString = args[0];
		
		// Index Directory
		String indexStorageDirectory = LearchCommon.getIndexStorageDirectory();
		
		Directory dir;
		IndexSearcher is = null;
		
		try 
		{
			dir = FSDirectory.open(new File(indexStorageDirectory));
			is = new IndexSearcher(dir);
			QueryParser parser = new QueryParser(
					Version.LUCENE_34, 
					"contents", 
					new StandardAnalyzer(Version.LUCENE_34));
			Query query = parser.parse(searchString);
			TopDocs results = is.search(query, 100);
			
			for (ScoreDoc scoreDoc : results.scoreDocs) {
				Document doc = is.doc(scoreDoc.doc);
				System.out.println(doc.get("fullpath"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		finally 
		{
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {}
			}
		}
		
	}
}
