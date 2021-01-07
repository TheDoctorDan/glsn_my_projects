package net.pccsi.io;

import java.io.*;
import net.pccsi.*;


public class DataPath {
	static String[]	directories;
	private static DataPath dataPath_Instance = new DataPath();

	public DataPath()
	{
		directories = (System.getProperty("DATAPATH")).split(File.pathSeparator);
	}

	public static DataPath getDataPath() {
		return dataPath_Instance;
	}
		 

	public static String fullPathName(String fileName) throws FileNotFoundException {
		File	file;
		String	fullPathName;

		file = new File(fileName);
		if(file.isAbsolute() && file.exists() && file.isFile() && file.canRead() && file.canWrite()){
			fullPathName = new String(fileName);
			return(fullPathName);
		}

		for(int i = 0; i < directories.length; i++){
			file = new File(directories[i], fileName);
			if(file.exists() && file.isFile() && file.canRead() && file.canWrite()){
				fullPathName = directories[i] + File.separator + fileName;
				return(fullPathName);
			}
		}
		throw new FileNotFoundException("DataPath.FullPathName():\nFailed to find fileName:"+fileName+":\non DATAPATH:\n"+ DataPath.stat_To_String());

	}

	public static String stat_To_String(){
		String	outstring="";
		for(int i = 0; i < directories.length; i++){
			outstring= outstring + (i+1) + "." + directories[i] + "\n";
		}
		return(outstring);
	}

	public String toString(){
		return(stat_To_String());
	}


}
