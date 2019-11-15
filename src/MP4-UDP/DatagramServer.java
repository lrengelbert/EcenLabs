import java.net.*;
import java.util.Random;

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

    Random rand = new Random();

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
        System.out.println(
            packet.getAddress() + " " + packet.getPort() + ": received packet 0: " + new String(packet.getData()));

        socket.send(packet);

        String strnPackets = new String(packet.getData()).trim();
        // System.out.println("str n pack: " + strnPackets + " len: " +
        // strnPackets.length());
        // System.out.println("matches: " + strnPackets.matches("-?\\d+(\\.\\d+)?"));
        int nPackets = Integer.parseInt(strnPackets);
        System.out.println("n packets to be received: " + nPackets);

        boolean allReceived = false;

        String[] str_message = new String[nPackets];

        int i = 0;
        while (!allReceived) {
          packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
          socket.receive(packet);

          // socket.send(packet);
          int r = rand.nextInt(2);
          if (r == 1) {
            System.out.println(packet.getAddress() + " " + packet.getPort() + ": " + "received packet " + (i + 1) + ": "
                + new String(packet.getData()));
            // System.out.println("Sending ACK");
            byte[] msg = Integer.toString(i + 1).getBytes();
            DatagramPacket serverToClientACK = new DatagramPacket(msg, msg.length, packet.getAddress(),
                packet.getPort());

            socket.send(serverToClientACK);
            str_message[i] = new String(packet.getData());
            i += 1;
          } else {
            System.out.println("Dropping packet");
          }

          if (i >= nPackets) {
            System.out.println("Finished receiving!");
            allReceived = true;
          }

        }

        System.out.println("Final message: ");
        for (int j = 0; j < nPackets; j++) {
          System.out.print(str_message[j]);
        }
        System.out.println();

        // Return the packet to the sender
        // int r = rand.nextInt(2);
        // if (r == 1) {
        // System.out.println("ACK sent");
        // socket.send(packet);
        // } else {
        // System.out.println("ACK dropped");
        // }

      }
    } catch (

    Exception e) {
      System.out.println(e);
    }
  }
}
