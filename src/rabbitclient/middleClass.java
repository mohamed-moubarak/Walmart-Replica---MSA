package rabbitclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class middleClass {

	public static void main(String[] args) {

		try {
			ServerSocket serverSocket = new ServerSocket(8086);
			Socket clientSocket;
			BufferedReader br;
			while (true) {
				clientSocket = serverSocket.accept();

				br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				StringBuilder response = new StringBuilder(); // or StringBuffer
																// if
				// Java version 5+
				String line;
				String params = null;
				while ((line = br.readLine()) != null) {
					response.append(line);
					response.append('\r');
					System.out.println(line);
					if (line.startsWith("Srv"))
						params = line;
				}
				System.out.println('h');
				br.close();

				System.out.println(response.toString());
				
				executePost("http://localhost:8080", params);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		executePost("http://localhost:8080", "{jsonObject: {}}");
	}

	public static String executePost(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;

		try {
			// Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if
															// Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

}
