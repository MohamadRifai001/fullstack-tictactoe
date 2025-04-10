package com.mohamad.tictactoe_backend.service;

import com.mohamad.tictactoe_backend.model.GameState;
import com.mohamad.tictactoe_backend.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private GameService gameService;
    private String gameId;
    private Player player1;
    private Player player2;

    /*
    setup uses "createGame" method, if that method does not function correctly it can cause issues to all methods
     */
    @BeforeEach
    public void setUp() {
        this.player1 = new Player("player1");
        this.player2 = new Player("player2");
        gameService = new GameService();
        gameId = gameService.createGame(player1, player2);
    }

    @Test
    void makeMove() {
        gameService.makeMove(gameId, 0, 0, player1.getId());
        GameState gameState = gameService.getGame(gameId);
        assertEquals('X', gameState.getBoard()[0][0]);
    }

    @Test
    void expandBoard() {
        gameService.expandBoard(gameId);
        GameState gameState = gameService.getGame(gameId);
        assertEquals(4, gameState.getBoardSize());
    }
}