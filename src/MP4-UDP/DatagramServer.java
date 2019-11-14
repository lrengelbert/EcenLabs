import java.net.*;

/**
 * A simple datagram server Shows how to send and receive UDP packets in Java
 *
 */
public class DatagramServer {
  private final static int PACKETSIZE = 100;

  public static void main(String args[]) {
    // Check the arguments
    if (args.length != 1) {
      System.out.println("usage: DatagramServer port");
      return;
    }

    try {
      // Convert the argument to ensure that is it valid
      int port = Integer.parseInt(args[0]);

      // Construct the socket
      DatagramSocket socket = new DatagramSocket(port);

      System.out.println("The server is ready...");

      for (;;) {
        // Create a packet
        DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);

        // Receive a packet (blocking)
        socket.receive(packet);

        // Print the packet
        System.out.println(packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()));

        // Return the packet to the sender
        // socket.send( packet ) ;
      }
    }catch(

  Exception e)
  {
    System.out.println(e);
  }
}}
