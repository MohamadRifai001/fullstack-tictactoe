package com.mohamad.tictactoe_backend.controller;

import com.mohamad.tictactoe_backend.dto.CreateGameRequest;
import com.mohamad.tictactoe_backend.dto.MoveRequest;
import com.mohamad.tictactoe_backend.model.GameState;
import com.mohamad.tictactoe_backend.model.Player;
import com.mohamad.tictactoe_backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @PostMapping("/{gameId}/start")
    public ResponseEntity<GameState> startGame(@PathVariable String gameId, @RequestBody CreateGameRequest request) {
        Player player1 = new Player(request.getPlayer1Name());
        Player player2 = new Player(request.getPlayer2Name());
        GameState game = gameService.createGame(gameId, player1, player2);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameState> makeMove(@PathVariable String gameId, @RequestBody MoveRequest request) {
        GameState game = gameService.makeMove(gameId, request.getRow(), request.getCol(), request.getPlayerId());
        return ResponseEntity.ok(game);
    }
    @PostMapping("/{gameId}/expandBoard")
    public ResponseEntity<GameState> expandBoard(@PathVariable String gameId) {
        GameState game = gameService.expandBoard(gameId);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameState> getGame(@PathVariable String gameId) {
        GameState game = gameService.getGame(gameId);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameId}/minigame/winner")
    public ResponseEntity<?> setMinigameWinner(@PathVariable String gameId, @RequestBody Map<String, String> body) {
        String winner = body.get("playerId");
        GameState game = gameService.getGame(gameId);
        if(game.getMinigameWinner() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("minigame winner already declared");
        }
        gameService.expandBoard(gameId);
        GameState updatedgame = gameService.setMinigameWinner(gameId, winner);
        return ResponseEntity.ok(updatedgame);
    }

    @PostMapping ("/{gameId}/rematch")
    public ResponseEntity<?> rematch(@PathVariable String gameId, @RequestBody Map<String, String> body) {
        String playerId = body.get("playerId");
        GameState game = gameService.rematch(gameId, playerId);

        return ResponseEntity.ok(game);
    }

}
