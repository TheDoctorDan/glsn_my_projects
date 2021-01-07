package	net.pccsi;

public class Pccsi_System{
	public static String	program_Name;

	Pccsi_System(String program_Name){
		this.program_Name = new String(program_Name);
	}

	public static void error_Message(String message){
		System.out.println(message);
	}

	public static void error_Message(String message, Throwable throwable){
		System.out.println(message);
		throwable.printStackTrace();
	}
}
