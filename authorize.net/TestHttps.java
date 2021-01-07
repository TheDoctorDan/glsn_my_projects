//import java.net.*; // For URL.
import javax.net.ssl.*; // For HttpsURLConnection.
import java.security.cert.Certificate;
import java.io.*;
import java.net.*;

public class TestHttps {

	public static void main(String[] args) {

		String spec="";
		URL url=null;

		HttpsURLConnection con=null;
		//HttpURLConnection con=null;

		OutputStream theControl = null;


		try {
			spec = "http://www.pccsi.net/cgi-bin/prospect.sh";			
			spec = "https://certification.authorize.net/gateway/transact.dll";			

			url = new URL(spec);

			con = (HttpsURLConnection)url.openConnection();
			//con = (HttpURLConnection)url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");


			
			/*
			con.setRequestProperty("x_login","TRIM123");
			con.setRequestProperty("x_tran_key","5");
			con.setRequestProperty("x_version", "3.1");
			con.setRequestProperty("x_test_request","True");
			con.setRequestProperty("x_delim_data", "True");
			con.setRequestProperty("x_delim_char", ",");
			con.setRequestProperty("x_encap_char", "\"");
			con.setRequestProperty("x_relay_response", "False");
			con.setRequestProperty("x_amount","5.00");
			con.setRequestProperty("x_method Required","CC");
			con.setRequestProperty("x_type","AUTH_CAPTURE");
			con.setRequestProperty("x_card_num","370000000000002");
			con.setRequestProperty("x_exp_date","06/08");
			*/

			theControl = con.getOutputStream();


			try {
				BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(theControl));
				String encoded = null;

				out.write("x_login=");
				encoded = URLEncoder.encode("TRIM123", "UTF-8");
				out.write(encoded);

				out.write("&x_tran_key=");
				encoded = URLEncoder.encode("5", "UTF-8");
				out.write(encoded);

				out.write("&x_version=");
				encoded = URLEncoder.encode("3.1", "UTF-8");
				out.write(encoded);

				out.write("&x_test_request=");
				encoded = URLEncoder.encode("True", "UTF-8");
				out.write(encoded);

				out.write("&x_delim_data=");
				encoded = URLEncoder.encode("True", "UTF-8");
				out.write(encoded);


				out.write("&x_relay_response=");
				encoded = URLEncoder.encode("False", "UTF-8");
				out.write(encoded);

				out.write("&x_amount=");
				encoded = URLEncoder.encode("5.00", "UTF-8");
				out.write(encoded);

				out.write("&x_method=");
				encoded = URLEncoder.encode("CC", "UTF-8");
				out.write(encoded);

				out.write("&x_type=");
				encoded = URLEncoder.encode("AUTH_CAPTURE", "UTF-8");
				out.write(encoded);

				out.write("&x_card_num=");
				encoded = URLEncoder.encode("370000000000002", "UTF-8");
				out.write(encoded);

				out.write("&x_exp_date=");
				encoded = URLEncoder.encode("06/08", "UTF-8");
				out.write(encoded);

				out.write("&x_first_name=");
				encoded = URLEncoder.encode("John,smith", "UTF-8");
				out.write(encoded);

				out.write("&submit=");
				encoded = URLEncoder.encode("submit", "UTF-8");
				out.write(encoded);

				out.write("\n");

				out.close();


				/*
				theData = con.getInputStream();

				String contentType = con.getContentType();
				if (contentType.toLowerCase().startsWith("text")) {
					BufferedReader in = new BufferedReader(
					new InputStreamReader(theData));
					String line;
					while ((line = in.readLine()) != null) {
						System.out.println(line);

					}
				} else {
					System.out.println("This program only handles text responses");
					System.out.println("I got " + contentType);
				}
				*/
			} catch (IOException e) {
				System.err.println("Trouble with IO" + e);
				System.exit(2);
			}


			StringBuffer buffer = new StringBuffer();
			InputStream input = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String line;
			line = br.readLine();
			System.out.println(line);
			String[] response = line.split(",");
			for(int i=0;i<response.length;i++){
				System.out.println(i +": " + response[i]);
				
			}

			/*
			while ((line = br.readLine()) != null) {
				buffer.append(line);
				buffer.append('\n');
			}
			System.out.println(buffer);
			*/
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
