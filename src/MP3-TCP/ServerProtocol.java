
public class ServerProtocol {

  public String welcomeMsg;

  public ServerProtocol(String strMsg) {
    welcomeMsg = strMsg;
  }

  public String processInput(String inFromClient) {
    String outToClient = null;
    if (inFromClient == null) {
      return welcomeMsg;
    }
    // String clientSentence = inFromClient.readLine();
    String capitalizedSentence = inFromClient.toUpperCase();

    outToClient = (capitalizedSentence);

    return outToClient;
  }
}