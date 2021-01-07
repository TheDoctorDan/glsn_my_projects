package net.pccsi.io;

public class JIsam_Data_Field {

	static int MAX_NUM_SUBSCRIPTS;


	private String	text_Label;
	private String	var_Name;
	private int	data_Field_Type;
	private Object	data_Obj;
	private int[]	subscripts;



	public class Size_And_Offset {
		int	byte_Size;
		int	align_Mod;
		int	byte_Offset;
		int	bit_Offset;

		Size_And_Offset (int byte_Size, int align_Mod, int byte_Offset, int bit_Offset){
			this.byte_Size =byte_Size;
			this.align_Mod =align_Mod;
			this.byte_Offset =byte_Offset;
			this.bit_Offset =bit_Offset;
		}
	}


	private Size_And_Offset[] size_And_Offset_Array;

}
