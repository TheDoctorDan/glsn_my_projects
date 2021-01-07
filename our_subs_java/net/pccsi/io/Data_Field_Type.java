package net.pccsi.io;

public class Data_Field_Type{
	public static int MAX_NUM		= 16;

	public static int CHAR			= 0;
	public static int BIT			= 1;
	public static int SHORT			= 2;
	public static int INT			= 3;
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
	private static Data_Field_Type data_Field_Type_Instance = new Data_Field_Type();

	public class Info {
		public String	code;
		public String	description;
		public int[]	byte_Size; /* Data_Field_Machine_Type.MAX_NUM */
		public int[]	align_Mod; /* Data_Field_Machine_Type.MAX_NUM */

		Info(String code, String description,
			int	index0, int byte_Size0, int align_Mod0,
			int	index1, int byte_Size1, int align_Mod1,
			int	index2, int byte_Size2, int align_Mod2,
			int	index3, int byte_Size3, int align_Mod3,
			int	index4, int byte_Size4, int align_Mod4,
			int	index5, int byte_Size5, int align_Mod5,
			int	index6, int byte_Size6, int align_Mod6,
			int	index7, int byte_Size7, int align_Mod7
		){

			this.code =code;
			this.description =description;

			this.byte_Size = new int[Data_Field_Machine_Type.MAX_NUM];
			this.align_Mod = new int[Data_Field_Machine_Type.MAX_NUM];

			this.byte_Size[index0] = byte_Size0;
			this.byte_Size[index1] = byte_Size1;
			this.byte_Size[index2] = byte_Size2;
			this.byte_Size[index3] = byte_Size3;
			this.byte_Size[index4] = byte_Size4;
			this.byte_Size[index5] = byte_Size5;
			this.byte_Size[index6] = byte_Size6;
			this.byte_Size[index7] = byte_Size7;

			this.align_Mod[index0] = align_Mod0;
			this.align_Mod[index1] = align_Mod1;
			this.align_Mod[index2] = align_Mod2;
			this.align_Mod[index3] = align_Mod3;
			this.align_Mod[index4] = align_Mod4;
			this.align_Mod[index5] = align_Mod5;
			this.align_Mod[index6] = align_Mod6;
			this.align_Mod[index7] = align_Mod7;
		}
	}

	public static Info[]	info_Array;

	Data_Field_Type(){
		info_Array = new Info[MAX_NUM];

		info_Array[CHAR] = new Info("c", "char", 
					Data_Field_Machine_Type.NCR,		1,	1,
					Data_Field_Machine_Type.HP9000,		1,	1,
					Data_Field_Machine_Type.MIPS,		1,	1,
					Data_Field_Machine_Type.MOT88K,		1,	1,
					Data_Field_Machine_Type.DEC_ALPHA,	1,	1,
					Data_Field_Machine_Type.UNIXWARE,	1,	1,
					Data_Field_Machine_Type.MOTAIX,		1,	1,
					Data_Field_Machine_Type.LINUX,		1,	1
		);


		info_Array[BIT] = new Info("b", "bit", 
					Data_Field_Machine_Type.NCR,		2,	2,
					Data_Field_Machine_Type.HP9000,		2,	2,
					Data_Field_Machine_Type.MIPS,		1,	4,
					Data_Field_Machine_Type.MOT88K,		4,	4,
					Data_Field_Machine_Type.DEC_ALPHA,	4,	4,
					Data_Field_Machine_Type.UNIXWARE,	4,	4,
					Data_Field_Machine_Type.MOTAIX,		1,	4,
					Data_Field_Machine_Type.LINUX,		4,	4
		);

		info_Array[SHORT] = new Info("s", "short", 
					Data_Field_Machine_Type.NCR,		2,	2,
					Data_Field_Machine_Type.HP9000,		2,	2,
					Data_Field_Machine_Type.MIPS,		2,	2,
					Data_Field_Machine_Type.MOT88K,		2,	2,
					Data_Field_Machine_Type.DEC_ALPHA,	2,	2,
					Data_Field_Machine_Type.UNIXWARE,	2,	2,
					Data_Field_Machine_Type.MOTAIX,		2,	2,
					Data_Field_Machine_Type.LINUX,		2,	2
		);

		info_Array[INT] = new Info("i", "int", 
					Data_Field_Machine_Type.NCR,		4,	2,
					Data_Field_Machine_Type.HP9000,		4,	2,
					Data_Field_Machine_Type.MIPS,		4,	4,
					Data_Field_Machine_Type.MOT88K,		4,	4,
					Data_Field_Machine_Type.DEC_ALPHA,	4,	4,
					Data_Field_Machine_Type.UNIXWARE,	4,	4,
					Data_Field_Machine_Type.MOTAIX,		4,	4,
					Data_Field_Machine_Type.LINUX,		4,	4
		);

		info_Array[C_LONG] = new Info("l", "c_long", 
					Data_Field_Machine_Type.NCR,		4,	2,
					Data_Field_Machine_Type.HP9000,		4,	2,
					Data_Field_Machine_Type.MIPS,		4,	4,
					Data_Field_Machine_Type.MOT88K,		4,	4,
					Data_Field_Machine_Type.DEC_ALPHA,	8,	8,
					Data_Field_Machine_Type.UNIXWARE,	4,	4,
					Data_Field_Machine_Type.MOTAIX,		4,	4,
					Data_Field_Machine_Type.LINUX,		4,	4
		);

		info_Array[T_LADATE] = new Info("L", "t_ladate", 
					Data_Field_Machine_Type.NCR,		4,	2,
					Data_Field_Machine_Type.HP9000,		4,	2,
					Data_Field_Machine_Type.MIPS,		4,	4,
					Data_Field_Machine_Type.MOT88K,		4,	4,
					Data_Field_Machine_Type.DEC_ALPHA,	8,	8,
					Data_Field_Machine_Type.UNIXWARE,	4,	4,
					Data_Field_Machine_Type.MOTAIX,		4,	4,
					Data_Field_Machine_Type.LINUX,		4,	4
		);

		info_Array[T_DAY_MINUTE] = new Info("C", "t_day_minute", 
					Data_Field_Machine_Type.NCR,		4,	2,
					Data_Field_Machine_Type.HP9000,		4,	2,
					Data_Field_Machine_Type.MIPS,		4,	4,
					Data_Field_Machine_Type.MOT88K,		4,	4,
					Data_Field_Machine_Type.DEC_ALPHA,	8,	8,
					Data_Field_Machine_Type.UNIXWARE,	4,	4,
					Data_Field_Machine_Type.MOTAIX,		4,	4,
					Data_Field_Machine_Type.LINUX,		4,	4
		);

		info_Array[FLOAT] = new Info("f", "float", 
					Data_Field_Machine_Type.NCR,		4,	2,
					Data_Field_Machine_Type.HP9000,		4,	2,
					Data_Field_Machine_Type.MIPS,		4,	4,
					Data_Field_Machine_Type.MOT88K,		4,	4,
					Data_Field_Machine_Type.DEC_ALPHA,	4,	4,
					Data_Field_Machine_Type.UNIXWARE,	4,	4,
					Data_Field_Machine_Type.MOTAIX,		4,	4,
					Data_Field_Machine_Type.LINUX,		4,	4
		);

		info_Array[DOUBLE] = new Info("d", "double", 
					Data_Field_Machine_Type.NCR,		8,	2,
					Data_Field_Machine_Type.HP9000,		8,	2,
					Data_Field_Machine_Type.MIPS,		8,	8,
					Data_Field_Machine_Type.MOT88K,		8,	8,
					Data_Field_Machine_Type.DEC_ALPHA,	8,	8,
					Data_Field_Machine_Type.UNIXWARE,	8,	4,
					Data_Field_Machine_Type.MOTAIX,		8,	4,
					Data_Field_Machine_Type.LINUX,		8,	4
		);

		info_Array[DATA_FIELD_LIST] = new Info("ex", "external type", 
					Data_Field_Machine_Type.NCR,		0,	2,
					Data_Field_Machine_Type.HP9000,		0,	2,
					Data_Field_Machine_Type.MIPS,		0,	1,
					Data_Field_Machine_Type.MOT88K,		0,	1,
					Data_Field_Machine_Type.DEC_ALPHA,	0,	1,
					Data_Field_Machine_Type.UNIXWARE,	0,	1,
					Data_Field_Machine_Type.MOTAIX,		0,	1,
					Data_Field_Machine_Type.LINUX,		0,	1
		);

		info_Array[UNSIGNED_CHAR] = new Info("uc", "unsigned char", 
					Data_Field_Machine_Type.NCR,		1,	1,
					Data_Field_Machine_Type.HP9000,		1,	1,
					Data_Field_Machine_Type.MIPS,		1,	1,
					Data_Field_Machine_Type.MOT88K,		1,	4,
					Data_Field_Machine_Type.DEC_ALPHA,	1,	1,
					Data_Field_Machine_Type.UNIXWARE,	1,	1,
					Data_Field_Machine_Type.MOTAIX,		1,	1,
					Data_Field_Machine_Type.LINUX,		1,	1
		);

		info_Array[UNSIGNED_SHORT] = new Info("us", "unsigned short", 
					Data_Field_Machine_Type.NCR,		2,	2,
					Data_Field_Machine_Type.HP9000,		2,	2,
					Data_Field_Machine_Type.MIPS,		2,	2,
					Data_Field_Machine_Type.MOT88K,		2,	2,
					Data_Field_Machine_Type.DEC_ALPHA,	2,	2,
					Data_Field_Machine_Type.UNIXWARE,	2,	2,
					Data_Field_Machine_Type.MOTAIX,		2,	2,
					Data_Field_Machine_Type.LINUX,		2,	2
		);

		info_Array[UNSIGNED_INT] = new Info("ui", "unsigned int", 
					Data_Field_Machine_Type.NCR,		4,	2,
					Data_Field_Machine_Type.HP9000,		4,	2,
					Data_Field_Machine_Type.MIPS,		4,	4,
					Data_Field_Machine_Type.MOT88K,		4,	4,
					Data_Field_Machine_Type.DEC_ALPHA,	4,	4,
					Data_Field_Machine_Type.UNIXWARE,	4,	4,
					Data_Field_Machine_Type.MOTAIX,		4,	4,
					Data_Field_Machine_Type.LINUX,		4,	4
		);

		info_Array[UNSIGNED_C_LONG] = new Info("ul", "unsigned c_long", 
					Data_Field_Machine_Type.NCR,		4,	2,
					Data_Field_Machine_Type.HP9000,		4,	2,
					Data_Field_Machine_Type.MIPS,		4,	4,
					Data_Field_Machine_Type.MOT88K,		4,	4,
					Data_Field_Machine_Type.DEC_ALPHA,	8,	8,
					Data_Field_Machine_Type.UNIXWARE,	4,	4,
					Data_Field_Machine_Type.MOTAIX,		4,	4,
					Data_Field_Machine_Type.LINUX,		4,	4
		);

		info_Array[TIME_T] = new Info("l", "time_t", 
					Data_Field_Machine_Type.NCR,		4,	2,
					Data_Field_Machine_Type.HP9000,		4,	2,
					Data_Field_Machine_Type.MIPS,		4,	4,
					Data_Field_Machine_Type.MOT88K,		4,	4,
					Data_Field_Machine_Type.DEC_ALPHA,	8,	8,
					Data_Field_Machine_Type.UNIXWARE,	4,	4,
					Data_Field_Machine_Type.MOTAIX,		4,	4,
					Data_Field_Machine_Type.LINUX,		4,	4
		);

		info_Array[UNION_SWITCH_T] = new Info("su", "uinion_switch_t", 
					Data_Field_Machine_Type.NCR,		4,	2,
					Data_Field_Machine_Type.HP9000,		4,	2,
					Data_Field_Machine_Type.MIPS,		4,	4,
					Data_Field_Machine_Type.MOT88K,		4,	4,
					Data_Field_Machine_Type.DEC_ALPHA,	4,	4,
					Data_Field_Machine_Type.UNIXWARE,	4,	4,
					Data_Field_Machine_Type.MOTAIX,		4,	4,
					Data_Field_Machine_Type.LINUX,		4,	4
		);



	}

}
