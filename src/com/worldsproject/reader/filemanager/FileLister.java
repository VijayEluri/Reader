package com.worldsproject.reader.filemanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FileLister 
{
	private List<File> getFileList(File aStartingDir) throws FileNotFoundException 

	{
		LinkedList<File> result = new LinkedList<File>();
		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		for(File file : filesDirs) 
		{
			
			if(file.isFile() == false) 
			{
				result.add(file);
				List<File> deeperList = getFileList(file);
				result.addAll(deeperList);
			}
			else
			{
				if(file.getName().endsWith(".txt"))
				{
					result.add(file);
				}
			}
		}
		
		Iterator<File> it = result.iterator();
		
		while(it.hasNext())
		{
			if(it.next().isFile() == false)
			{
				it.remove();
			}
		}
		
		return result;
	}
}
