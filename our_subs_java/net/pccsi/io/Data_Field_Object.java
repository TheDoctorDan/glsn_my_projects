package net.pccsi.io;

public class Data_Field_Object {
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





	Data_Field_Object (String text_Label, String var_Name, Character char_Obj) { 
		init(text_Label, var_Name, Data_Field_Type.CHAR, char_Obj);
	}

	Data_Field_Object (String text_Label, String var_Name, String string_Obj, int length) { 
		init(text_Label, var_Name, Data_Field_Type.CHAR, string_Obj, length);
	}

	Data_Field_Object (String text_Label, String var_Name, String[] string_Obj, int length, int size) { 
		int[] i_array = new int[2];
		i_array[0]=size;
		i_array[1]=length;
		init(text_Label, var_Name, Data_Field_Type.CHAR, string_Obj, i_array);
	}


	Data_Field_Object (String text_Label, String var_Name, Boolean bool_Obj) { 
		init(text_Label, var_Name, Data_Field_Type.BIT, bool_Obj);
	}

	Data_Field_Object (String text_Label, String var_Name, Short short_Obj) { 
		init(text_Label, var_Name, Data_Field_Type.SHORT, short_Obj);
	}

	Data_Field_Object (String text_Label, String var_Name, Integer integer_Obj) { 
		init(text_Label, var_Name, Data_Field_Type.INT, integer_Obj);
	}

	Data_Field_Object (String text_Label, String var_Name, Integer[] integer_Obj) { 
		init(text_Label, var_Name, Data_Field_Type.INT, integer_Obj, integer_Obj.length);
	}

	/*
	public static int C_LONG		= 4;
	public static int T_LADATE		= 5;
	public static int T_DAY_MINUTE		= 6;
	public static int FLOAT			= 7;
	public static int DOUBLE		= 8;
	public static int DATA_FIELD_LIST	= 9;
	public static int UNSIGNED_CHAR		= 10;
	public static int UNSIGNED_SHORT	= 11;
	public static int UNSIGNED_INT		= 12;
	public static int UNSIGNED_C_LONG	= 13;
	public static int TIME_T		= 14;
	public static int UNION_SWITCH_T	= 15;
	*/

	Data_Field_Object (String text_Label, String var_Name, int data_Field_Type, Object data_Obj, int size_or_length) {
		init(text_Label, var_Name, data_Field_Type, data_Obj);
		subscripts=new int[1];
		subscripts[0]=size_or_length;
	}

	Data_Field_Object (String text_Label, String var_Name, int data_Field_Type, Object data_Obj, int[] array_sizes) {
		init(text_Label, var_Name, data_Field_Type, data_Obj);
		subscripts=new int[array_sizes.length];
		for(int i = 0; i<array_sizes.length; i++){
			subscripts[i]=array_sizes[i];;
		}
	}

	private void init(String text_Label, String var_Name, int data_Field_Type, Object data_Obj) {
		this.text_Label =text_Label;
		this.var_Name =var_Name;
		this.data_Field_Type =data_Field_Type;
		this.data_Obj =data_Obj;
		this.size_And_Offset_Array = null;
	}

	private void init(String text_Label, String var_Name, int data_Field_Type, Object data_Obj, int size_Or_Length) {
		this.text_Label =text_Label;
		this.var_Name =var_Name;
		this.data_Field_Type =data_Field_Type;
		this.data_Obj =data_Obj;
		subscripts=new int[1];
		subscripts[0]=size_Or_Length;
	}

	private void init(String text_Label, String var_Name, int data_Field_Type, Object data_Obj, int[] array_Of_Sizes) {
		this.text_Label =text_Label;
		this.var_Name =var_Name;
		this.data_Field_Type =data_Field_Type;
		this.data_Obj =data_Obj;
		subscripts=new int[array_Of_Sizes.length];
		for(int i = 0; i<array_Of_Sizes.length; i++){
			subscripts[i]=array_Of_Sizes[i];;
		}
	}

}
