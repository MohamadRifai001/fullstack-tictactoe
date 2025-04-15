package com.mohamad.tictactoe_backend.model;


import java.util.Random;

/*
stores the current GameState information and handles game moves.
 */
public class GameState {
    private String[][] board;
    private int boardSize;
    private Player player1;
    private Player player2;
    private Player currentPlayer; //player1 or player2
    private GameStatus status;
    private String winner;
    private String minigameWinner;

    public enum GameStatus {
        IN_PROGRESS,
        WIN,
        TIE,
        EXPANDED_TO_4x4,
        WAITING_FOR_MINIGAME
    }

    public GameState(Player p1, Player p2) {
        this.boardSize = 3;
        this.board = new String[boardSize][boardSize];
        this.player1 = p1;
        p1.setSymbol("X");
        this.player2 = p2;
        p2.setSymbol("O");
        this.currentPlayer = p1;
        this.status = GameStatus.IN_PROGRESS;
        initBoard();
    }
    /*
    set the board to empty at first
     */
    private void initBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = "-";
            }
        }
    }

    /*
    Player takes their turn, places 'X' or 'O' in the designated cell
     */

    public void makeMove(int row, int col, String playerId) {
        if (row < 0 || col < 0 || row >= boardSize || col >= boardSize) {
            throw new IllegalArgumentException("Out of bounds");
        };
        if (!board[row][col].equals("-")) {
            throw new IllegalArgumentException("This tile is already used!");
        }

        currentPlayer = findPlayer(playerId);

        board[row][col] = currentPlayer.getSymbol();

        switchPlayer();

        updateGameStatus();
    }
    /*
    checks if a player won, if the board is a 3x3 tie, or a 4x4 tie, and updates status accordingly
     */
    public void updateGameStatus() {
        String winner = checkWinner();
        if (winner != null) {
            status = GameStatus.WIN;
            this.winner = winner;
        }
        else if (isBoardFull()) {
            if (boardSize == 3) {
                status = GameStatus.WAITING_FOR_MINIGAME;
                minigameWinner = null;
            }
            else {
                status = GameStatus.TIE;
            }
        }
    }

    /*
    expands 3x3 board towards a random direction and turns it into a 4x4 board
    1-top left
    example top left:
    X X X _
    X X X _
    X X X _
    _ _ _ _
    2-top right
    3-bottom left
    4-bottom right
     */
    public void expandBoard() {
        if(boardSize == 4) {
            throw new IllegalArgumentException("board is already at Max size");
        }
        boardSize = 4;
        String[][] newBoard = new String[boardSize][boardSize];

        // Initialize new board with empty spaces
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                newBoard[i][j] = "-";
            }
        }

        // Randomly choose one of the four expansion directions (1, 2, 3, 4)
        Random random = new Random();
        int direction = random.nextInt(4) + 1;

        // Expand the 3x3 board based on the chosen direction
        switch (direction) {
            case 1: // Top-left expansion
                for (int i = 0; i < 3; i++) {
                    System.arraycopy(board[i], 0, newBoard[i], 0, 3);
                }
                break;

            case 2: // Top-right expansion
                for (int i = 0; i < 3; i++) {
                    System.arraycopy(board[i], 0, newBoard[i], 1, 3);
                }
                break;

            case 3: // Bottom-left expansion
                for (int i = 0; i < 3; i++) {
                    System.arraycopy(board[i], 0, newBoard[i + 1], 0, 3);
                }
                break;

            case 4: // Bottom-right expansion
                for (int i = 0; i < 3; i++) {
                    System.arraycopy(board[i], 0, newBoard[i + 1], 1, 3);
                }
                break;
        }

        // Update the board to the new expanded board
        board = newBoard;
        status = GameStatus.EXPANDED_TO_4x4;
    }

    /*
    checks if there is any 3 in a row combination for example:
    x|x|x <- three in a row 'X' O|Xhere diagonal 3 in a row with 'O'
    O|O|                        X|O|X
     | |                        O|X|O

     -|-|-|O <- after 4x4 expansion there is a 3 in a row here with 'O'
     O|X|O|-
     X|O|X|-
     X|O|X|-

     */
    public String checkWinner() {
        int sequenceLength = 3;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                String current = board[i][j];
                if (current.equals("-")) continue;

                // Check horizontal (right)
                if (j + sequenceLength - 1 < boardSize &&
                        board[i][j + 1].equals(current) &&
                        board[i][j + 2].equals(current)) {
                    return current;
                }

                // Check vertical (down)
                if (i + sequenceLength - 1 < boardSize &&
                        board[i + 1][j].equals(current) &&
                        board[i + 2][j].equals(current)) {
                    return current;
                }

                // Check diagonal down-right
                if (i + sequenceLength - 1 < boardSize &&
                        j + sequenceLength - 1 < boardSize &&
                        board[i + 1][j + 1].equals(current)&&
                        board[i + 2][j + 2].equals(current)) {
                    return current;
                }

                // Check diagonal up-right
                if (i - sequenceLength + 1 >= 0 &&
                        j + sequenceLength - 1 < boardSize &&
                        board[i - 1][j + 1].equals(current) &&
                        board[i - 2][j + 2].equals(current)) {
                    return current;
                }
            }
        }

        return null;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j].equals("-")) return false;
            }
        }
        return true;
    }

    private Player findPlayer(String playerId) {
        if (playerId.equals(player1.getId())) return player1;
        if (playerId.equals(player2.getId())) return player2;
        throw new IllegalArgumentException("Player not found");
    }

    private void switchPlayer() {
        if(currentPlayer.getId().equals(player1.getId())) currentPlayer = player2;
        else if(currentPlayer.getId().equals(player2.getId())) currentPlayer = player1;
    }

    public void handleMinigame(String name) {
        winner = name;

        currentPlayer = findPlayer(name);
        status = GameStatus.IN_PROGRESS;
    }

    // Getters -- needed for Spring boot serialization

    public String[][] getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameStatus getStatus() {
        return status;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String getWinner() {
        return winner;
    }

    public String getMinigameWinner() {
        return minigameWinner;
    }
}
