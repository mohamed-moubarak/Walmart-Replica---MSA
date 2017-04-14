import com.rabbitmq.client.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;

public class Recv {

	private static Socket socket;
  private final static String QUEUE_NAME = "hello";
  public static String messages;

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String message = new String(body, "UTF-8");
        messages = message;
        System.out.println(" [x] Received '" + message + "'");
      }
    };
    channel.basicConsume(QUEUE_NAME, true, consumer);
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

//        String number = "2";

//        String sendMessage = number + "\n";
        bw.write(messages);
        bw.flush();
        System.out.println("Message sent to the server : "+ messages);

        //Get the return message from the server
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String message = br.readLine();
        System.out.println("Message received from the server : " +message);
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
    }
  }
}