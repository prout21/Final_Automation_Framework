package Mccm.Pega.DMPRealTime.PegaTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.testng.Assert;
import org.testng.annotations.Test;

import Mccm.Pega.Outbound.PegaTestBase.TestBase;
import Mccm.Pega.excel.utility.Excel_Reader;


public class DMPRTPNBAProOffRltsTest extends TestBase  {

	public static String KeystorePath;
	public static String hostName;
	public static String port;
	public static String Keystorepassword;
	public static String json;
	public static String mccminternaltrust;
	public static String css1identity;


	@Test

	public void VerifyDMPRTProcessOffResltsAPIcallSuccessfully() {

		//   public static void main(String[] args) {

		try {
			//          String hostName="mccm-191102761.eu-central-1.elb.amazonaws.com";
			//          String hostName = "192.57.138.25";
			//          String port = "18576";// 8573


			
			hostName=general_ReadProperty("HTTPS_OSF_HOSTNAME");
		//	log.info("NBA_hostName : " +hostName);
			port=general_ReadProperty("HTTPS_OSF_PORT");
	//		log.info("NBA_port : " +port);
			KeystorePath=general_ReadProperty("KeystorePath");
	//		log.info("KeystorePath : " +KeystorePath);
			Keystorepassword=general_ReadProperty("Keystorepassword");
	//		log.info("Keystorepassword : " +Keystorepassword);

			mccminternaltrust=general_ReadProperty("KEYSTORE_TRUST_FILENAME");
	//		log.info("Keystorepassword : " +mccminternaltrust);
			css1identity=general_ReadProperty("KEYSTORE_CLIENT_FILENAME");
	//		log.info("Keystorepassword : " +css1identity);
	//		log.info("**** Getting the OSF CaptureResponse URL ****");

			URL urlForGetRequest = new URL("https://" + hostName + ":" + port
					+ "/prweb/PRRestService/CSM/customerMarketingProductAPI/processOffersResult");
			String readLine = null;
			//		System.setProperty("javax.net.ssl.keyStore",(KeystorePath+"/css1identity.jks")); 
			System.setProperty("javax.net.ssl.keyStore",(KeystorePath+css1identity));   
			System.setProperty("javax.net.ssl.keyStorePassword", Keystorepassword);
			System.setProperty("javax.net.ssl.keyStoreType", "JKS");
			//			System.setProperty("javax.net.ssl.trustStore",(KeystorePath+"/mccminternaltrust.jks"));
			System.setProperty("javax.net.ssl.trustStore",(KeystorePath+mccminternaltrust));
			System.setProperty("javax.net.ssl.trustStorePassword", Keystorepassword);
			System.setProperty("javax.net.ssl.trustStoreType", "JKS");


			json = general_ReadProperty("NBA_json_offerRslts");



			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			HttpsURLConnection connection = (HttpsURLConnection) urlForGetRequest.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			StringBuffer response = new StringBuffer();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			OutputStream os = connection.getOutputStream();
			os.write(json.getBytes());
			os.flush();
			os.close();
			int responseCode = connection.getResponseCode();

			BufferedReader in = null;
			if (responseCode == 200)
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			else
				in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			System.out.println("response: " + response.toString());

			System.out.println(responseCode);

			Assert.assertEquals(responseCode, 200 );

		} catch (Exception e) {
			e.printStackTrace();


		}


	}

}

