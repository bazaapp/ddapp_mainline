package com.duco.ducoplayer.utils;

import java.io.File;
import java.util.Vector;

public class FileUtil {
	
	public static final int FILES_ONLY = 0;
	public static final int DIRECTORIES_ONLY = 1;
	public static final int FILES_DIRECTORIES = 2;
	
	public static Vector<String> dirList(String path, int type)  {
		Vector <String> list = new Vector<String>();
		if(path == null || "".equals(path))
			return list;
		File files[] = new File(path).listFiles();
		for(File file : files)  {	
			if(file.getName().startsWith("."))
				continue;
			if(type == DIRECTORIES_ONLY)  {
				if(file.isDirectory())
					list.add(file.getName());
			}
			else if(type == FILES_ONLY)  {
				if(file.isFile())
					list.add(file.getName());
			}
			else if(type == FILES_DIRECTORIES) {
				list.add(file.getName());
			}
		}
		
		return list;
	}
	
	public static boolean exists(String path)  {
		File file = new File(path);
		return file == null ? false : file.exists();
	}
}
