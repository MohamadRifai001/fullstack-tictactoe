package com.mohamad.tictactoe_backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private GameState gameState;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        this.player1 = new Player("player1");
        this.player2 = new Player("player1");
        gameState = new GameState(player1, player2);
    }

    /*
    make sure the board initializes as empty
     */
    @Test
    public void testInitBoard() {
        String[][] board = gameState.getBoard();
        for(String[] row : board) {
            for(String cell : row) {
                assertEquals("-", cell);
            }
        }
    }

    /*
    makes sure that player1 places an X in the specified spot,
    and player2 places O in the specified spot.
     */
    @Test
    public void testMakeMoveValid() {
        gameState.makeMove(0, 0, player1.getId());
        gameState.makeMove(0, 1, player2.getId());
        assertEquals("X", gameState.getBoard()[0][0]);
        assertEquals("O", gameState.getBoard()[0][1]);
    }
    /*
    throws exception if a player tries to use a tile that is already being used.
     */
    @Test
    public void testMakeMoveInvalid() {
        gameState.makeMove(0, 0, player1.getId());
        assertThrows(IllegalArgumentException.class, () -> gameState.makeMove(0, 0, player2.getId()));
    }


    /*
    makes sure that winner is found for the following board:
    X|X|O
    O|X|O
    X| |O
     */
    @Test
    public void testWinnerFoundCol() {
        gameState.makeMove(0,0, player1.getId());
        gameState.makeMove(0,1, player1.getId());
        gameState.makeMove(1,1, player1.getId());
        gameState.makeMove(2,0, player1.getId());
        gameState.makeMove(0, 2, player2.getId());
        gameState.makeMove(1, 0, player2.getId());
        gameState.makeMove(1, 2, player2.getId());
        gameState.makeMove(2, 2, player2.getId());

        assertEquals("O", gameState.checkWinner());
    }

    /*
    makes sure that winner is found for the following board:
    X|X|O
    O|X|O
    O| |X
     */
    @Test
    public void testWinnerFoundDia() {
        gameState.makeMove(0,0, player1.getId());
        gameState.makeMove(0,1, player1.getId());
        gameState.makeMove(1,1, player1.getId());
        gameState.makeMove(2,2, player1.getId());
        gameState.makeMove(0, 2, player2.getId());
        gameState.makeMove(1, 0, player2.getId());
        gameState.makeMove(1, 2, player2.getId());
        gameState.makeMove(2, 0, player2.getId());

        assertEquals("X", gameState.checkWinner());
    }

    /*
    tests if board has no empty spaces
    X|X|O
    O|O|X
    X| |O
    should return false here

    X|X|O
    O|O|X
    X|X|O

    should return true here
     */
    @Test
    public void TestIsBoardFull() {
        gameState.makeMove(0,0, player1.getId());
        gameState.makeMove(0,1, player1.getId());
        gameState.makeMove(1,2, player1.getId());
        gameState.makeMove(2,0, player1.getId());
        gameState.makeMove(0, 2, player2.getId());
        gameState.makeMove(1, 0, player2.getId());
        gameState.makeMove(1, 1, player2.getId());
        gameState.makeMove(2, 2, player2.getId());

        assertFalse(gameState.isBoardFull()); // is not full here
        gameState.makeMove(2,1, player1.getId());
        assertTrue(gameState.isBoardFull());
    }

    @Test
    public void invalidExpansion() {
        gameState.expandBoard();
        assertThrows(IllegalArgumentException.class, () -> gameState.expandBoard());
    }

    /*
    manually expand the board to 4x4, and make sure a win is still registered for 3 in a row
     */
    @Test
    public void testWinnerAfterExpansion() {
        gameState.expandBoard();
        gameState.makeMove(0, 0, player2.getId());
        gameState.makeMove(0, 1, player2.getId());
        gameState.makeMove(0, 2, player2.getId());

        assertEquals("O", gameState.checkWinner());
    }



}