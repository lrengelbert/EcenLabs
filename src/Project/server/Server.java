import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

/**
 * A server program which accepts requests from clients to capitalize strings.
 * When a client connects, a new thread is started to handle it. Receiving
 * client data, capitalizing it, and sending the response back is all done on
 * the thread, allowing much greater throughput because more clients can be
 * handled concurrently.
 */
public class Server {

  public enum PLAYER {
    NONE, X, O, SIZE
  }

  public enum ROOM_MESSAGE {
    FULL, OUT_OF_BOUNDS, PLAYER_X, PLAYER_O
  }

  public enum GameState {
    JOINING, WAITING, PLAYING, WON
  }

  /**
   * Runs the server. When a client connects, the server spawns a new thread to do
   * the servicing and immediately returns to listening. The application limits
   * the number of threads via a thread pool (otherwise millions of clients could
   * cause the server to run out of resources by allocating too many threads).
   */
  public static void main(String[] args) throws Exception {
    int PORT_NO = 8000;

    Room room = new Room();

    try (ServerSocket listener = new ServerSocket(PORT_NO)) {
      System.out.println("The capitalization server is running on port " + PORT_NO);
      ExecutorService pool = Executors.newFixedThreadPool(20);
      while (true) {
        pool.execute(new GameThread(listener.accept(), room));
      }
    }
  }

  private static class GameThread implements Runnable {
    private Socket socket;

    private Room room;

    public PLAYER player_id;
    public int room_no;

    public boolean my_turn = false;

    public boolean print_wait = false;

    private static Map<Socket, Integer> playersToRoomsMap;

    static {
      playersToRoomsMap = new HashMap<Socket, Integer>();
    }

    GameThread(Socket socket, Room room) {
      this.socket = socket;
      this.room = room;
      this.room_no = -1;
    }

    @Override
    public void run() {
      System.out.println("Connected: " + socket);
      try {
        Scanner in = new Scanner(socket.getInputStream());

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while (gameLoop(in, out)) {

        }
      } catch (Exception e) {
        System.out.println("Error:" + socket);
      } finally {

        try {
          socket.close();
        } catch (IOException e) {
        }
        System.out.println("Removing: " + room_no);
        room.removeFromRoom(room_no);
        System.out.println("Closed: " + socket);
      }
    }

    public void setPlayerId(Server.ROOM_MESSAGE msg) {
      if (msg == Server.ROOM_MESSAGE.PLAYER_O) {
        player_id = PLAYER.O;
      } else if (msg == Server.ROOM_MESSAGE.PLAYER_X) {
        player_id = PLAYER.X;
      } else {
        System.out.println("Error in SetPlayerId");
      }
    }

    public boolean gameLoop(Scanner in, PrintWriter out) {

      if (playersToRoomsMap.containsKey(socket)) {
        // already connected
        if (room.boards[playersToRoomsMap.get(socket)].getTurn() == player_id) {
          out.println("Enter number 0-8 for move");
          // int board_pos = in.nextInt();
          // boolean isValid = checkValidMove(board_pos);
          // while (!isValid) {

          // }
          int board_pos;
          boolean isValid = false;
          do {
            board_pos = in.nextInt();
            isValid = room.boards[playersToRoomsMap.get(socket)].checkValidMove(board_pos);
          } while (!isValid);
          room.boards[playersToRoomsMap.get(socket)].updateBoard(board_pos, player_id);
          // my_turn = false;
          room.boards[playersToRoomsMap.get(socket)].nextTurn(player_id);
          print_wait = true;

        } else if (room.boards[playersToRoomsMap.get(socket)].getTurn() == Server.PLAYER.NONE) {
          if (print_wait) {
            out.println("Waiting for an opponent to connect...");
            print_wait = false;
          }

        } else {
          if (print_wait) {
            out.println("Waiting on opponent to move...");
            print_wait = false;
          }

        }
      } else {
        // not connected
        out.println("Enter a room number");
        int some_num = in.nextInt();
        System.out.println("GOT INT" + some_num);
        Server.ROOM_MESSAGE room_message = room.joinRoom(some_num);

        if (room_message == Server.ROOM_MESSAGE.PLAYER_O || room_message == Server.ROOM_MESSAGE.PLAYER_X) {
          System.out.println("Entered map print with size " + playersToRoomsMap.size());
          playersToRoomsMap.put(socket, some_num);
          room_no = some_num;
          System.out.println("K: " + socket + " V: " + some_num);
          playersToRoomsMap.forEach((k, v) -> System.out.println("key: " + k + ", value: " + v));

          player_id = (room_message == Server.ROOM_MESSAGE.PLAYER_O) ? Server.PLAYER.O : Server.PLAYER.X;
        }
        print_wait = true;
        // if first player
        // if (room_message == Server.ROOM_MESSAGE.PLAYER_X) {
        // // my_turn = true;
        // print_wait = true;
        // }

      }

      return true;
    }

  }

}