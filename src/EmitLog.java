import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class EmitLog {

  private static String EXCHANGE_NAME = "logs";
  private static Socket socket;
  
  public String receiveMessage(){
	  try
	    {
	        String host = "localhost";
	        int port = 25000;
	        InetAddress address = InetAddress.getByName(host);
	        socket = new Socket(address, port);

	        //Send the message to the server
	        OutputStream os = socket.getOutputStream();
	        OutputStreamWriter osw = new OutputStreamWriter(os);
	        BufferedWriter bw = new BufferedWriter(osw);

//	        String number = "2";


	        //Get the return message from the server
	        InputStream is = socket.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);
	        String message = br.readLine();
	        System.out.println("Message received from the server : " +message);
	        return message;
	    }
	    catch (Exception exception)
	    {
	        exception.printStackTrace();
	    }
	    finally
	    {
	        //Closing the socket
	        try
	        {
	            socket.close();
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	        return "";
	    }
  }
  public EmitLog (String exchangeName) throws Exception {
    String[] messages = {receiveMessage()};
	  EXCHANGE_NAME = exchangeName;
	ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

    String message = getMessage(messages);

    channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }

  private static String getMessage(String[] strings){
    if (strings.length < 1)
    	    return "info: Hello World!";
    return joinStrings(strings, " ");
  }

  private static String joinStrings(String[] strings, String delimiter) {
    int length = strings.length;
    if (length == 0) return "";
    StringBuilder words = new StringBuilder(strings[0]);
    for (int i = 1; i < length; i++) {
        words.append(delimiter).append(strings[i]);
    }
    return words.toString();
  }
}
