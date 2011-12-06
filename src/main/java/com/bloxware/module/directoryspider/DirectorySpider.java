package com.bloxware.module.directoryspider;

import java.io.File;

import com.bloxware.module.directoryspider.FileHandler.Direction;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DirectorySpider 
{
	/**
	 * 
	 * @param initialDirectory
	 */
	@Inject
	public DirectorySpider() {
	}
	
	/**
	 * 
	 */
	public void traverseDirectory(String initialDirectory, FileHandler fileHander) {
		Preconditions.checkArgument(checkIsDirectory(initialDirectory), "Argument passed should be a valid directory.");
		
		// Iterate through all the files in the directory and process them one by one
		traverseDirectoryRecursively(new File(initialDirectory), fileHander);
	}
	
	/**
	 * 
	 * @param startingDirectory
	 * @param fileHandler
	 */
	private void traverseDirectoryRecursively(File startingFile, FileHandler fileHandler) 
	{
		Direction returnedDirection = fileHandler.process(startingFile);
		
		if (returnedDirection == Direction.DO_NOT_TRAVERSE) {
			return; // Do not enter into the directory since it has been excluded
		}
		
		if (startingFile.isDirectory()) 
		{
            String[] children = startingFile.list();
            
            if (children == null) {
            	return;
            }
            
            for (int i = 0; i < children.length; i++) 
            {
            	traverseDirectoryRecursively(new File(startingFile, children[i]), fileHandler);
            }
        }
	}
	
	/**
	 * 
	 * @param initialDirectory
	 * @return
	 */
	private boolean checkIsDirectory(String initialDirectory) {
		File file = new File(initialDirectory);
		if (file.exists() && file.isDirectory()) {
			return true;
		}
		return false;
	}
}
