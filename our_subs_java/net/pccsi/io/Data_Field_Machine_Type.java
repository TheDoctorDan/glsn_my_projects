package net.pccsi.io;

public class Data_Field_Machine_Type {
	public static int MAX_NUM	= 8;
	public static int NCR		= 0;
	public static int HP9000	= 1;
	public static int MIPS		= 2;
	public static int MOT88K	= 3;
	public static int DEC_ALPHA	= 4;
	public static int UNIXWARE	= 5;
	public static int MOTAIX	= 6;
	public static int LINUX	= 7;
	private static Data_Field_Machine_Type data_Field_Machine_Type_Instance = new Data_Field_Machine_Type();


	public class Info {
		public String	Name;
		public boolean	pad_Struct_Size_To_Max_Alignment;
		public int	min_Struct_Size;
		public String	sys_Mfg;
		public String	if_Def;

		Info(String Name, boolean pad_Struct_Size_To_Max_Alignment, int min_Struct_Size, String sys_Mfg, String if_Def){
			this.Name =Name;
			this.pad_Struct_Size_To_Max_Alignment =pad_Struct_Size_To_Max_Alignment;
			this.min_Struct_Size =min_Struct_Size;
			this.sys_Mfg =sys_Mfg;
			this.if_Def =if_Def;
		
		}
	}

	public static Info[]	info_Array;


	public Data_Field_Machine_Type() {

		info_Array = new Info[MAX_NUM];


		info_Array[NCR] = new Info("NCR", false, 2, "ncr", "ncr");
		info_Array[HP9000] = new Info("HP9000", false, 2, "hp", "hpmc68");
		info_Array[MIPS] = new Info("MIPS", true, 4, "mips", "mips");
		info_Array[MOT88K] = new Info("MOT88K", true, 4, "motorola", "motorola");
		info_Array[DEC_ALPHA] = new Info("DEC_ALPHA", false, 8, "dec", "DEC_ALPHA");
		info_Array[UNIXWARE] = new Info("UNIXWARE", false, 4, "intel", "intel");
		info_Array[MOTAIX] = new Info("MOTAIX", true, 4, "aix", "aix");
		info_Array[LINUX] = new Info("LINUX", true, 1, "linux", "linux");

	}

}
