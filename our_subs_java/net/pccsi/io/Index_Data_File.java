package net.pccsi.io;
import net.pccsi.io.isam.*;

public class Index_Data_File{
	private String	data_Base_Chosen;
	private Isam_File	isam_File;
	private long	isam_Record_Location;

	public Index_Data_File(){
	}

	public Index_Data_File(String data_base, String file_Name){
	}
	public boolean open(String data_base, String file_Name){
	}

	public Index_Data_File(String data_base, String file_Name, int key_Length, int record_Length){
	}
	public boolean create(String data_base, String file_Name, int key_Length, int record_Length) {
	}

	public boolean find(String key) throws No_Data_Base_Chosen_Exception {
		if(data_Base_Chosen.equal("isam")){
			try {
				if((isam_Record_Location = isam_File.find(key)) ==0l)
					return(false);
				else
					return(true);
			}catch (Exception e){
			}
		}else{
			throw new No_Data_Base_Chosen_Exception();
		}
	}

	public boolean insert(String key) throws No_Data_Base_Chosen_Exception {
		if(data_Base_Chosen.equal("isam")){
			try {
				if((isam_Record_Location = isam_File.insert(key)) ==0l)
					return(false);
				else
					return(true);
			}catch (Exception e){
			}
		}else{
			throw new No_Data_Base_Chosen_Exception();
		}
	}

	public boolean delete(String key) throws No_Data_Base_Chosen_Exception {
		if(data_Base_Chosen.equal("isam")){
			try {
				if((isam_Record_Location = isam_File.delete(key)) ==0l)
					return(false);
				else
					return(true);
			}catch (Exception e){
			}
		}else{
			throw new No_Data_Base_Chosen_Exception();
		}
	}

}
