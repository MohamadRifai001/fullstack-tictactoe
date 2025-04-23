package com.mohamad.tictactoe_backend.service;

import com.mohamad.tictactoe_backend.model.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LobbyServiceTest {

    private LobbyService lobbyService;

    @BeforeEach
    void setUp() {
        lobbyService = new LobbyService();
    }
    @Test
    void getAllLobbies_Empty() {
        List<String> lobbies = lobbyService.getAllLobbies();
        assertNotNull(lobbies);
        assertTrue(lobbies.isEmpty());
    }

    @Test
    void createLobby() {
        Lobby lobby = lobbyService.createLobby("tom");
        assertNotNull(lobby);
    }

    /*
    --------the following test cases require "create lobby" to work.-----------
     */
    @Test
    void getAllLobbies() {
        Lobby lobby1 = lobbyService.createLobby("Tom");
        Lobby lobby2 = lobbyService.createLobby("Jake");


        List<String> result = lobbyService.getAllLobbies();

        assertEquals(2, result.size());
        assertTrue(result.contains(lobby1.getLobbyCode()));
        assertTrue(result.contains(lobby2.getLobbyCode()));
    }



    @Test
    void joinLobby() {
        Lobby lobby = lobbyService.createLobby("Tom");
        lobbyService.joinLobby(lobby.getLobbyCode(), "Martha");

        assertEquals("Martha", lobby.getPlayer2());
    }

    @Test
    void getLobby() {
        Lobby lobby = lobbyService.createLobby("Tom");
        Lobby lobby2 = lobbyService.getLobby(lobby.getLobbyCode());

        assertEquals(lobby, lobby2);
    }

    @Test
    void playerReadyUp() {
        Lobby lobby = lobbyService.createLobby("Tom");
        lobbyService.playerReadyUp(lobby.getLobbyCode(), "Tom");

        assertTrue(lobby.isPlayer1Ready());
    }

    @Test
    void processHeartBeat_updatesPlayer1LastSeen() {
        Lobby lobby = lobbyService.createLobby("Martha");

        long before = System.currentTimeMillis();
        lobbyService.processHeartBeat(lobby.getLobbyCode(), "Martha");
        long after = System.currentTimeMillis();

        long lastSeen = lobby.getPlayer1LastSeen();
        assertTrue(lastSeen >= before && lastSeen <= after, "Player1 last seen should be within heartbeat window");
    }

    @Test
    void processHeartBeat_updatesPlayer2LastSeen() {
        Lobby lobby = lobbyService.createLobby("Martha");
        lobby.setPlayer2("Tom");

        long before = System.currentTimeMillis();
        lobbyService.processHeartBeat(lobby.getLobbyCode(), "Tom");
        long after = System.currentTimeMillis();

        long lastSeen = lobby.getPlayer2LastSeen();
        assertTrue(lastSeen >= before && lastSeen <= after, "Player2 last seen should be within heartbeat window");
    }

    @Test
    void processHeartBeat_ignoresUnknownPlayer() {
        Lobby lobby = lobbyService.createLobby("Martha");
        lobby.setPlayer2("Tom");

        lobby.setPlayer1LastSeen(123L);
        lobby.setPlayer2LastSeen(456L);

        lobbyService.processHeartBeat(lobby.getLobbyCode(), "Charlie"); // Not a player

        assertEquals(123L, lobby.getPlayer1LastSeen());
        assertEquals(456L, lobby.getPlayer2LastSeen());
    }


    @Test
    void cleanupInactiveLobbies_removesOnlyFullyInactiveLobbies() {
        long now = System.currentTimeMillis();

        Lobby activeLobby = lobbyService.createLobby("Alice");
        String active = activeLobby.getLobbyCode();
        lobbyService.joinLobby(active, "Bob");
        activeLobby.setPlayer1LastSeen(now);
        activeLobby.setPlayer2LastSeen(now);

        Lobby halfActiveLobby = lobbyService.createLobby("Charlie");
        String halfActive = halfActiveLobby.getLobbyCode();
        lobbyService.joinLobby(halfActive, "Dana");
        halfActiveLobby.setPlayer1LastSeen(now);
        halfActiveLobby.setPlayer2LastSeen(now - 60000); // inactive

        Lobby fullyInactiveLobby = lobbyService.createLobby("Eve");
        String fullInactive = fullyInactiveLobby.getLobbyCode();
        lobbyService.joinLobby(fullInactive, "Frank");
        fullyInactiveLobby.setPlayer1LastSeen(now - 60000); // inactive
        fullyInactiveLobby.setPlayer2LastSeen(now - 60000); // inactive

        lobbyService.cleanupInactiveLobbies();

        Map<String, Lobby> remaining = lobbyService.getLobbies();

        assertTrue(remaining.containsKey(active));
        assertTrue(remaining.containsKey(halfActive));
        assertFalse(remaining.containsKey(fullInactive));
    }
}