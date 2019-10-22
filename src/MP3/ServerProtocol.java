
public class ServerProtocol {

  public String processInput(String inFromClient) {
    String outToClient = null;
    if (inFromClient == null) {
      return "";
    }
    // String clientSentence = inFromClient.readLine();
    String capitalizedSentence = inFromClient.toUpperCase();

    outToClient = (capitalizedSentence);

    return outToClient;
  }
}