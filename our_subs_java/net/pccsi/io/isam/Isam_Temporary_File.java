package  net.pccsi.io.isam;

import java.io.*;

public class Isam_Temporary_File extends Isam_File {

	boolean	is_a_temp_file;



	/** 
	* This Constructor is for creating Temporary Isam Files.
	* Mode is always rw
	* Filenames starting with / will use filename as full path name.
	* Filenames not starting with / will be created in $DATAPATH first directory.
	*/
	public Isam_Temporary_File(String filename_prefix, int key_length, int record_length){
	}


	public void	close(){
		super.close();
	}



}


