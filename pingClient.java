import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/*
* Server to process ping requests over UDP. java PingClient host port
*/
public class pingClient
{

	public static void main(String[] args) throws Exception
	{
	// Cheking the arguments 
	if (args.length != 2) {
	System.out.println("Required arguments: host port");
	return;
	}
	//Checking hostname and Port number
	String hostName =args[0];	
	int port = Integer.parseInt(args[1]);


	// Create a datagram socket for receiving and sending UDP packets
	// through the port specified on the command line.
	DatagramSocket socket = new DatagramSocket();
	InetAddress IPAddress =InetAddress.getByName(hostName);

	// Processing loop for 10 times.
	for(int i=0;i<10;i++){
	//Each message contains
	// a payload of data that includes the keyword PING,
	// a sequence number,a timestamp and a random message string.
	long time1 = System.nanoTime();
	String msg=getSaltString();
	String Message = "Ping "+ i + " " + time1+" "+msg.toLowerCase()+"\n";
	// Create a datagram packet to hold outgoing UDP packet.
	DatagramPacket request = new DatagramPacket(Message.getBytes(), Message.length(),IPAddress,port );
	//Sending the request packet
	socket.send(request);
	System.out.println("Actual Message sent from Client -- > "+Message);
	//Create a datagram packet to hold incoming UDP packet.
	DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);

	// After sending each packet, the client waits up to one second to receive a reply.
	// If one seconds goes by without a reply from the server,then the client assumes that its packet or the server's reply packet has been lost in the network.

	socket.setSoTimeout(1000);

	try
	{

		socket.receive(receivedPacket);
		String sentence = new String(receivedPacket.getData(),0, receivedPacket.getLength());
		long time2 = System.nanoTime();
		long time=(time2-time1)/1000;
		System.out.println("Round trip time for PING "+i+"--> "+time+"\n Message "+sentence);
	}catch(IOException E)
	{
		//If socket doesnt get response in 1 second following message will pop
		System.out.println("Requested Time Out");
	}

	Thread.sleep(1000);

	}

}

/*
	Function to generate Random String
*/
public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}