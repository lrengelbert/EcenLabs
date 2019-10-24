import java.net.*;
import java.io.*;

public class KKMultiServerThread extends Thread {
  private Socket socket = null;
  private String welcomeMsg = "";

  public KKMultiServerThread(Socket socket, String msg) {
    super("KKMultiServerThread");
    this.socket = socket;
    this.welcomeMsg = msg;
  }

  public void run() {

    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader buffInFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader buffInFromStdin = new BufferedReader(new InputStreamReader(System.in));) {
      String inFromClient, outputLine, inFromStdin;
      // boolean inFromClientReady = false;
      // boolean inFromStdinReady = false;

      // inFromStdin = null;
      ServerProtocol kkp = new ServerProtocol(welcomeMsg);
      outputLine = kkp.processInput(null);
      out.println(outputLine);
      System.out.println("initial: " + outputLine);

      while ((inFromClient = buffInFromClient.readLine()) != null) {
        // while (buffInFromClient.ready() || buffInFromStdin.ready()) {

        // inFromStdinReady = buffInFromStdin.ready();
        // System.out.println("isready: " + inFromStdinReady);
        // System.out.printf("clientReady: %s stdinReady: %s\n", inFromClient != null,
        // inFromStdinReady);

        // if (inFromStdinReady) {
        // System.out.println("inFromStdinReady ");
        // inFromStdin = buffInFromStdin.readLine();
        // System.out.println("serverStdin:" + inFromStdin);
        // out.println(inFromStdin);
        // }
        if (inFromClient != null) {
          // System.out.println("inFromClientReady ");
          // inFromClient = buffInFromClient.readLine();
          outputLine = kkp.processInput(inFromClient);
          System.out.println(socket.getRemoteSocketAddress() + ":" + inFromClient);
          out.println(outputLine);

        }
        // inFromClient = null;

        // System.out.println("inFromClient: " + inFromClient);

        // if (inFromStdinReady) {
        // // inFromStdin = buffInFromStdin.readLine();
        // // System.out.println("inFromStdin: " + inFromStdin);

        // } else if (inFromClientReady) {

        // }
        // System.out.println("Sending: " + outputLine);
        if (outputLine.equals("Bye"))
          break;
      }
      System.out.println("closing socket");
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}