package com.mohamad.tictactoe_backend.service;

import com.mohamad.tictactoe_backend.model.Lobby;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LobbyService {
    private Map<String, Lobby> lobbies = new ConcurrentHashMap<>();

    public List<String> getAllLobbies() {
        List<String> lobbyCodes = new ArrayList<>();

        for(Lobby lobby : lobbies.values()) {
            lobbyCodes.add(lobby.getLobbyCode());
        }
        return lobbyCodes;
    }


    public Lobby createLobby(String player1Name) {
        String lobbyCode = generateUniqueCode();
        Lobby lobby = new Lobby(lobbyCode, player1Name);
        lobbies.put(lobbyCode, lobby);
        return lobby;
    }

    public Lobby joinLobby(String lobbyCode, String player2Name) {
        Lobby lobby = lobbies.get(lobbyCode);
        if (lobby == null) return null;
        if(lobby.getPlayer2() != null) {
            return null;
        }

        lobby.player2Join(player2Name);

        return lobby;
    }

    public Lobby getLobby(String lobbyCode) {
        System.out.println(lobbies.keySet());
        return lobbies.get(lobbyCode);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (lobbies.containsKey(code));
        return code;
    }

    public boolean playerReadyUp(String lobbyCode, String playerName) {
        Lobby lobby = lobbies.get(lobbyCode);
        return lobby.playerReadyUp(playerName);
    }

    public void processHeartBeat(String lobbyCode, String playerName) {
        Lobby lobby = lobbies.get(lobbyCode);
        long now = System.currentTimeMillis();
        if (playerName.equals(lobby.getPlayer1())) {
            lobby.setPlayer1LastSeen(now);
        } else if (playerName.equals(lobby.getPlayer2())) {
            lobby.setPlayer2LastSeen(now);
        }
    }




    /*
    deletes a lobby if heartbeat was not sent within the past 30s(heartbeat should be sent every 10seconds)
     */
    @Scheduled(fixedRate = 30000)
    public void cleanupInactiveLobbies() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Lobby>> iterator = lobbies.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Lobby> entry = iterator.next();
            Lobby lobby = entry.getValue();

            boolean player1Inactive = now - lobby.getPlayer1LastSeen() > 30000;
            boolean player2Inactive = lobby.getPlayer2() == null || now - lobby.getPlayer2LastSeen() > 30000;

            if (player1Inactive && player2Inactive) {
                System.out.println("gothere?" + lobbies.keySet());
                iterator.remove();
            }
        }
    }

}
