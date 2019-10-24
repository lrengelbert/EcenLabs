
import java.io.IOException;
import java.net.ServerSocket;

public class KKMultiServer {
  public static void main(String[] args) throws IOException {

    // if (args.length != 1) {
    // System.err.println("Usage: java KKMultiServer <port number>");
    // System.exit(1);
    // }

    String msg = "Welcome to server";
    int maxConnections = 50;
    int portNumber = 8800;
    if (args.length >= 1) {
      portNumber = Integer.parseInt(args[0]);
    }
    if (args.length >= 2) {
      maxConnections = Integer.parseInt(args[1]);
    }
    if (args.length >= 3) {
      msg = args[2];
    }

    boolean listening = true;

    System.out.println("Welcome message: " + msg);
    System.out.println("Max connections: " + maxConnections);
    System.out.println("Port number    : " + portNumber);

    int nConn = 0;

    try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
      while (listening) {
        new KKMultiServerThread(serverSocket.accept(), msg).start();
        nConn++;
        if (nConn >= maxConnections) {
          break;
        }
      }
    } catch (IOException e) {
      System.err.println("Could not listen on port " + portNumber);
      System.exit(-1);
    }
  }
}