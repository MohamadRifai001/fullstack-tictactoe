package com.mohamad.tictactoe_backend.service;

import com.mohamad.tictactoe_backend.model.GameState;
import com.mohamad.tictactoe_backend.model.Player;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/*
Handles the gameState, currently there is only one instance of the game running,
however it is stored as a mapping with key being the games ID incase it will be
scaled to be run as a multiplayer game
 */
@Service
public class GameService {

    private final Map<String, GameState> games = new ConcurrentHashMap<>();

    public GameState createGame(String gameId, Player player1, Player player2) {
        GameState game = new GameState(player1, player2);
        games.put(gameId, game);
        return game;
    }

    /*

     */
    public GameState makeMove(String gameId, int row, int col, String playerId) {
        GameState game = games.get(gameId);
        game.makeMove(row, col, playerId);
        return game;
    }
    public GameState expandBoard(String gameId) {
        GameState game = games.get(gameId);
        game.expandBoard();
        return game;
    }

    public GameState getGame(String gameId) {
        return games.get(gameId);
    }

    public GameState setMinigameWinner(String code, String name) {
        GameState game = games.get(code);
        game.handleMinigame(name);
        return game;
    }
}
