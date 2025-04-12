package com.mohamad.tictactoe_backend.service;

import com.mohamad.tictactoe_backend.model.Lobby;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LobbyService {
    private Map<String, Lobby> lobbies = new ConcurrentHashMap<>();


    public Lobby createLobby(String player1Name) {
        String lobbyCode = generateUniqueCode();
        Lobby lobby = new Lobby(lobbyCode, player1Name);
        lobbies.put(lobbyCode, lobby);
        return lobby;
    }

    public Lobby joinLobby(String lobbyCode, String player2Name) {
        Lobby lobby = lobbies.get(lobbyCode);
        if (lobby == null) throw new IllegalArgumentException("Lobby not found");

        lobby.player2Join(player2Name);

        return lobby;
    }

    public Lobby getLobby(String lobbyCode) {
        return lobbies.get(lobbyCode);
    }

    public boolean startLobbyGame(String code) {
        Lobby lobby = lobbies.get(code);
        if (lobby != null && lobby.isFull()) {
            lobby.setStarted(true);
            lobbies.remove(code); // Remove lobby when game starts
            return true;
        }
        return false;
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (lobbies.containsKey(code));
        return code;
    }


}
