package com.mohamad.tictactoe_backend.controller;


import com.mohamad.tictactoe_backend.model.Lobby;
import com.mohamad.tictactoe_backend.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    private final LobbyService lobbyService;

    @Autowired
    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createLobby(@RequestBody Map<String, String> body) {

        String playerName = body.get("player1Name");
        if(playerName == null || playerName.isEmpty()) {
            return ResponseEntity.badRequest().body("player name is required to create a lobby");
        }
        Lobby lobby = lobbyService.createLobby(playerName);
        return ResponseEntity.ok(lobby);
    }

    @PostMapping("/join/{code}")
    public ResponseEntity<?> joinLobby(@PathVariable String code, @RequestBody Map<String, String> body) {
        String playerName = body.get("player2Name");
        Lobby lobby = lobbyService.joinLobby(code, playerName);
        if (lobby != null) {
            return ResponseEntity.ok(lobby);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lobby not found or already full.");
        }
    }

    @GetMapping("/status/{code}")
    public ResponseEntity<?> getLobbyStatus(@PathVariable String code) {
        Lobby lobby = lobbyService.getLobby(code);
        if (lobby != null) {
            return ResponseEntity.ok(lobby);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lobby not found");
        }
    }


    @PostMapping("/ready/{code}")
    public ResponseEntity<?> playerReadyUp(@PathVariable String code, @RequestBody Map<String, String> body) {
        String playerName = body.get("playerName");
        boolean success = lobbyService.playerReadyUp(code, playerName);

        if(success)  return ResponseEntity.ok(Map.of("status", "success"));

        return ResponseEntity.badRequest().body("Failed to ready up player");
    }

    @PostMapping("/heartbeat/{code}")
    public ResponseEntity<?> processHeartBeat(@PathVariable String code, @RequestBody Map<String, String> body) {
        String playerName = body.get("playerName");

        Lobby lobby = lobbyService.getLobby(code);
        if (lobby == null) return ResponseEntity.badRequest().body("Lobby not found");

        lobbyService.processHeartBeat(code, playerName);
        return ResponseEntity.ok().build();
    }

}
