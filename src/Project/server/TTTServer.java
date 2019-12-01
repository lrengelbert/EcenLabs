import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.spi.ResourceBundleControlProvider;

import org.omg.IOP.RMICustomMaxStreamFormat;

import java.util.concurrent.*;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class TTTServer {

  public static void main(String[] args) throws Exception {
    int PORT_NO = 8000;

    GameHandler gameHandler = new GameHandler();

    try (ServerSocket listener = new ServerSocket(PORT_NO)) {
      System.out.println("The capitalization server is running on port " + PORT_NO);
      ExecutorService pool = Executors.newFixedThreadPool(20);
      while (true) {
        pool.execute(new GameThr(listener.accept(), gameHandler));
      }
    }
  }

  // thread dedicated to handling a socket with an Game associated
  public static class GameThr implements Runnable {
    public Game myGame;
    // Sempahore myTurn;
    public Scanner in;
    public PrintWriter out;
    public GameHandler gameHandler;
    public Socket socket;
    public GameHandler.PIECE myPiece;

    GameThr(Socket socket, GameHandler _gameHandler) {
      this.socket = socket;
      this.myGame = null;
      this.gameHandler = _gameHandler;
      this.myPiece = null;
      // this.myTurn = null;
      // this.myTurn = null;
    }

    @Override
    public void run() {
      System.out.println("Connected: " + socket);
      try {
        in = new Scanner(socket.getInputStream());

        out = new PrintWriter(socket.getOutputStream(), true);

        while (gameLoop()) {

        }
      } catch (Exception e) {
        System.out.println("Error:" + socket);
      } finally {

        try {
          socket.close();
        } catch (IOException e) {
        }
        // handle player leaving game
        System.out.println("Closed: " + socket);
      }
    }

    public boolean gameLoop() {
      if (this.myGame == null) {
        connectToGame();
      } else {

        handleMove();
      }

      // make this false when game is done
      return true;
    }

    public void connectToGame() {
      out.println("Enter a room number");
      int game_num = in.nextInt();
      GameHandler.RoomMessage message = gameHandler.checkGame(game_num);
      handleRoomMessage(message, game_num);
    }

    public void handleMove() {

      if (myGame.playerCount == 1) {
        out.println("Waiting for opponent to connect...");
      }
      try {
        out.println("Waiting for opponent to move...");
        myGame.waitPlayerTurn(myPiece);

        int board_pos;
        boolean isValid = false;
        do {
          myGame.sendBoard(out);
          out.println("It is your turn, pick a position on the board 0-8");
          board_pos = in.nextInt();
          isValid = myGame.checkValidMove(board_pos);

          if (!isValid) {
            out.println("A piece has been placed there, try again");
            // room.boards[playersToRoomsMap.get(socket)].sendBoard(out);
          }
        } while (!isValid);

        System.out.println("Valid move made!");
        myGame.makeMove(board_pos, myPiece);
        myGame.sendBoard(out);

      } catch (InterruptedException intEx) {
        System.out.println(intEx);
      } catch (Exception e) {
        System.out.println(e);
      }

    }

    public void handleRoomMessage(GameHandler.RoomMessage message, int game_num) {
      if (message == GameHandler.RoomMessage.PLAYER_O || message == GameHandler.RoomMessage.PLAYER_X) {
        System.out.println(socket + "Joined room!");
        this.myGame = gameHandler.joinGame(game_num);

        out.println("joined game room " + game_num);
      } else if (message == GameHandler.RoomMessage.FULL) {
        System.out.println(socket + " failed to join full game number" + game_num);
        out.println("failed to join game number " + game_num + " because it was full");
      } else if (message == GameHandler.RoomMessage.OUT_OF_BOUNDS) {
        System.out.println(socket + " failed to join out of bounds game " + game_num);
        out.println("game room " + game_num + " was out of bounds");
      } else {
        System.out.println("Unknown room message error");
      }

      if (message == GameHandler.RoomMessage.PLAYER_O) {
        myPiece = GameHandler.PIECE.O;
      } else if (message == GameHandler.RoomMessage.PLAYER_X) {
        myPiece = GameHandler.PIECE.X;
      }

      System.out.println("HandleRoomMessage: " + message);
      System.out.println("myPiece: " + myPiece);
    }

  }

  // contains mapping of games
  public static class GameHandler {
    public enum RoomMessage {
      FULL, OUT_OF_BOUNDS, PLAYER_X, PLAYER_O, NONE;
    }

    public enum PIECE {
      X, O, N;
    }

    private static Map<Socket, Game> mapSocketToGame;
    private static int MAX_NUM_GAMES = 5;
    private static Game[] games;

    static {
      games = new Game[MAX_NUM_GAMES];
      for (int i = 0; i < games.length; ++i) {
        games[i] = new Game(i);
        games[i].printBoard();
      }

    }

    public RoomMessage checkGame(int gameNumber) {

      GameHandler.RoomMessage roomMessage = GameHandler.RoomMessage.NONE;
      if (gameNumber < 0 || gameNumber > (games.length - 1)) {
        roomMessage = RoomMessage.OUT_OF_BOUNDS;
        return roomMessage;
      }

      Game theGame = games[gameNumber];

      if (theGame.playerCount >= 2) {
        roomMessage = RoomMessage.FULL;
        return roomMessage;
      }

      GameHandler.PIECE resultPiece = theGame.addPlayer();
      if (resultPiece == GameHandler.PIECE.X) {
        roomMessage = RoomMessage.PLAYER_X;
      } else if (resultPiece == GameHandler.PIECE.O) {
        roomMessage = RoomMessage.PLAYER_O;
      }
      System.out.println("checkGame" + roomMessage);

      return roomMessage;
    }

    public GameHandler.PIECE whoami(int gameNumber) {
      GameHandler.PIECE resultPiece = GameHandler.PIECE.N;
      if (games[gameNumber].playerCount == 0) {
        resultPiece = GameHandler.PIECE.O;
      } else if (games[gameNumber].playerCount == 1) {
        resultPiece = GameHandler.PIECE.X;
      }
      return resultPiece;
    }

    public Game joinGame(int gameNumber) {
      printPlayerCounts();
      return games[gameNumber];
    }

    public void printPlayerCounts() {

      for (int i = 0; i < games.length; ++i) {
        System.out.printf(" ( %d : %d ) ", i, games[i].playerCount);
      }
      System.out.println();
    }
  }

  // handles game logic
  public static class Game {

    GameHandler.PIECE[][] board;
    Semaphore playerOTurn;
    Semaphore playerXTurn;
    public int playerCount;

    PrintWriter playerOout;
    PrintWriter playerXout;

    public int roomNumber;

    Game(int _roomNumber) {

      roomNumber = _roomNumber;
      playerOTurn = new Semaphore(1);
      playerXTurn = new Semaphore(1);
      try {
        playerOTurn.acquire();
        playerXTurn.acquire();

        System.out.println("playerOTurn: " + playerOTurn.availablePermits());
        System.out.println("playerXTurn: " + playerXTurn.availablePermits());
      } catch (InterruptedException e) {
        System.out.println(e);
      } catch (Exception e) {
        System.out.println(e);
      }

      playerOout = null;
      playerXout = null;

      board = new GameHandler.PIECE[3][3];
      setBoardEmpty();

    }

    public void setBoardEmpty() {
      for (int i = 0; i < board.length; ++i) {
        for (int j = 0; j < board[i].length; ++j) {
          board[i][j] = GameHandler.PIECE.N;
        }
      }
    }

    public void printBoard() {
      System.out.println("Room: " + roomNumber);
      for (int i = 0; i < board.length; ++i) {
        for (int j = 0; j < board[i].length; ++j) {
          System.out.print(board[i][j] + " ");
        }
        System.out.println();
      }
    }

    public void waitPlayerTurn(GameHandler.PIECE piece) throws InterruptedException {
      if (piece == GameHandler.PIECE.X) {
        playerXTurn.acquire();
      } else if (piece == GameHandler.PIECE.O) {
        playerOTurn.acquire();
      }
    }

    public GameHandler.PIECE addPlayer() {
      // first player

      GameHandler.PIECE newPlayerPiece = GameHandler.PIECE.N;
      if (playerCount == 0) {
        newPlayerPiece = GameHandler.PIECE.O;
      } else if (playerCount == 1) {
        newPlayerPiece = GameHandler.PIECE.X;
        playerOTurn.release();

      } else {
        System.out.println("ERROR in adding player");
      }

      playerCount += 1;

      return newPlayerPiece;

    }

    public boolean checkValidMove(int board_pos) {
      int x = board_pos / 3;
      int y = board_pos % 3;
      System.out.printf("Checking (%d, %d)\n", x, y);
      printBoard();
      if (board[x][y] == GameHandler.PIECE.N) {
        return true;
      } else {
        return false;
      }
    }

    public void sendBoard(PrintWriter out) {
      System.out.println("Room: " + roomNumber);
      for (int i = 0; i < board.length; ++i) {
        for (int j = 0; j < board[i].length; ++j) {
          out.print(board[i][j] + " ");
        }
        out.println();
      }
    }

    public void updateBoard(int board_pos, GameHandler.PIECE p) {
      int x = board_pos / 3;
      int y = board_pos % 3;
      board[x][y] = p;
      System.out.println("updateBoard player: " + p);

      printBoard();
    }

    public void makeMove(int board_pos, GameHandler.PIECE p) {
      updateBoard(board_pos, p);
      if (p == GameHandler.PIECE.O) {
        playerXTurn.release();
      } else if (p == GameHandler.PIECE.X) {
        playerOTurn.release();
      }

    }

  }
}
