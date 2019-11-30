// import Project.server.Server.PLAYER;

// import Project.server.Server.*;

public class Room {

  private static int[] room_nos;

  public static Board[] boards;

  private static int MAX_NUM_ROOMS = 5;

  Room() {
    room_nos = new int[MAX_NUM_ROOMS];
    boards = new Board[MAX_NUM_ROOMS];
    for (int i = 0; i < MAX_NUM_ROOMS; ++i) {
      boards[i] = new Board();
    }

    printRooms();

    System.out.println("Room created.");
  }

  public Server.ROOM_MESSAGE joinRoom(int room_no) {
    System.out.println("Join room called with : " + room_no);
    Server.ROOM_MESSAGE result_message;
    if (room_no < 0 || room_no > (MAX_NUM_ROOMS - 1)) {
      System.out.println("Out of bounds!");
      result_message = Server.ROOM_MESSAGE.OUT_OF_BOUNDS;
      return result_message;
    }

    if (room_nos[room_no] >= 2) {
      System.out.println("Room Full!");
      result_message = Server.ROOM_MESSAGE.FULL;
      return result_message;
    }

    room_nos[room_no] += 1;

    if (room_nos[room_no] == 1) {
      result_message = Server.ROOM_MESSAGE.PLAYER_O;

    } else {
      result_message = Server.ROOM_MESSAGE.PLAYER_X;

      boards[room_no].setTurn(Server.PLAYER.O);
    }

    printRooms();

    return result_message;
  }

  public void removeFromRoom(int room_no) {
    if (room_no < 0) {
      System.out.println("No room assigned yet, exiting Room.removeFromRoom()");

    } else {
      room_nos[room_no] -= 1;
    }

    printRooms();

  }

  public void printRooms() {

    for (int i = 0; i < MAX_NUM_ROOMS; ++i) {
      System.out.print(room_nos[i] + " ");
    }
    System.out.println();
  }

  public class Board {
    Server.PLAYER[][] board;

    Server.PLAYER whoseTurn;

    Board() {
      whoseTurn = Server.PLAYER.NONE;
      board = new Server.PLAYER[3][3];

      setBoardEmpty();

      System.out.println("New Board: ");
      printBoard();
    }

    public void printBoard() {
      for (int i = 0; i < board.length; ++i) {
        for (int j = 0; j < board[i].length; ++j) {
          System.out.print(board[i][j] + " ");
        }
        System.out.println();
      }
    }

    public void setBoardEmpty() {
      for (int i = 0; i < board.length; ++i) {
        for (int j = 0; j < board[i].length; ++j) {
          board[i][j] = Server.PLAYER.NONE;
        }
      }
    }

    public boolean checkValidMove(int board_pos) {
      int x = board_pos / 3;
      int y = board_pos % 3;
      System.out.printf("Checking (%d, %d)\n", x, y);
      printBoard();
      if (board[x][y] == Server.PLAYER.NONE) {
        return true;
      } else {
        return false;
      }
    }

    public void updateBoard(int board_pos, Server.PLAYER p) {
      int x = board_pos / 3;
      int y = board_pos % 3;
      board[x][y] = p;
      System.out.println("updateBoard player: " + p);

      printBoard();
    }

    public void setTurn(Server.PLAYER player) {
      whoseTurn = player;
      System.out.println("Setting to " + player + "'s turn");
    }

    public void nextTurn(Server.PLAYER player) {
      if (player == Server.PLAYER.X) {
        whoseTurn = Server.PLAYER.O;
      } else if (player == Server.PLAYER.O) {
        whoseTurn = Server.PLAYER.X;
      } else {
        System.out.println("Error in nextTurn");
      }
      System.out.println("next turn is: " + player);
    }

    public Server.PLAYER getTurn() {
      return whoseTurn;
    }
  }

}