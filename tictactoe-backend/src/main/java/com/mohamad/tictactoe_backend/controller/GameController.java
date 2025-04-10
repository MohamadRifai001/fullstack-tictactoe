package com.mohamad.tictactoe_backend.controller;

import com.mohamad.tictactoe_backend.dto.CreateGameRequest;
import com.mohamad.tictactoe_backend.dto.MoveRequest;
import com.mohamad.tictactoe_backend.model.GameState;
import com.mohamad.tictactoe_backend.model.Player;
import com.mohamad.tictactoe_backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @PostMapping("/start")
    public ResponseEntity<String> startGame(@RequestBody CreateGameRequest request) {
        Player player1 = new Player(request.getPlayer1Name());
        Player player2 = new Player(request.getPlayer2Name());
        String gameId = gameService.createGame(player1, player2);

        return ResponseEntity.ok((gameId));
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameState> makeMove(@PathVariable String gameId, @RequestBody MoveRequest request) {
        GameState game = gameService.makeMove(gameId, request.getRow(), request.getCol(), request.getPlayerID());
        return ResponseEntity.ok(game);
    }
    @PostMapping("/{gameId}/expandBoard")
    public ResponseEntity<GameState> expandBoard(@PathVariable String gameId) {
        GameState game = gameService.expandBoard(gameId);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameState> getGame(@PathVariable String gameId) {
        return ResponseEntity.ok(gameService.getGame(gameId));
    }
}
