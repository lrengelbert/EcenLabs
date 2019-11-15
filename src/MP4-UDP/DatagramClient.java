import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;

/**
 * A simple datagram client Shows how to send and receive UDP packets in Java
 */
public class DatagramClient {
  private final static int PACKETSIZE = 100;

  public static void main(String args[]) {
    // Check the arguments
    if (args.length != 2) {
      System.out.println("usage: java DatagramClient host port");
      return;
    }

    DatagramSocket socket = null;

    try {
      // Convert the arguments first, to ensure that they are valid
      InetAddress host = InetAddress.getByName(args[0]);
      int port = Integer.parseInt(args[1]);

      // Construct the socket
      socket = new DatagramSocket();

      while (true) {
        BufferedReader buffIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\n\nEnter a message for " + host + ":" + port + ":");
        String userIn = buffIn.readLine();

        if (userIn.equalsIgnoreCase("q") || userIn.contentEquals("exit")) {
          System.out.println("exiting...");
          break;
        } else if (userIn.length() < 1) {
          continue;
        }
        SendMessages(socket, host, port, userIn);
      }
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      if (socket != null)
        socket.close();
    }
  }

  public static void SendMessages(DatagramSocket sock, InetAddress host, int port, String strIn) {
    // String[] ss = Talker.splitMessage("some test here is this even a valid
    // message can you imageine1");
    String[] ss = Talker.splitMessage(strIn);

    System.out.println(Arrays.toString(ss));

    // send initial message
    // boolean message0ACKd = false;
    // while (!message0ACKd) {
    // try {
    // byte[] message0 = Integer.toString(ss.length).getBytes();
    // DatagramPacket packet0 = new DatagramPacket(message0, message0.length, host,
    // port);
    // sock.send(packet0);

    // sock.setSoTimeout(2000);

    // packet0.setData(new byte[PACKETSIZE]);

    // sock.receive(packet0);

    // System.out.println("message 0 acked: " + new String(packet0.getData()));
    // message0ACKd = true;

    // } catch (SocketTimeoutException se) {
    // System.out.println(se.getMessage());
    // } catch (Exception e) {
    // System.out.println(e);
    // }
    // }

    // send all other messages
    int i = 0;
    boolean done = false;
    while (!done) {
      try {
        byte[] data = ss[i].getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, host, port);

        // Send it
        sock.send(packet);

        // Set a receive timeout, 2000 milliseconds
        sock.setSoTimeout(2000);

        // Prepare the packet for receive
        packet.setData(new byte[PACKETSIZE]);

        // Wait for a response from the server
        sock.receive(packet);

        // Print the response
        System.out.println("Received from server: " + new String(packet.getData()));

        i += 1;

        if (i >= ss.length) {
          done = true;
        }

      } catch (SocketTimeoutException se) {
        System.out.println(se.getMessage());
      } catch (Exception e) {
        System.out.println(e);
      }
    }

  }
}
