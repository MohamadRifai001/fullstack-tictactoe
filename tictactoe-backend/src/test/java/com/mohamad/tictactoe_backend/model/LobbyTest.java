package com.mohamad.tictactoe_backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {


    private Lobby lobby;
    private String player1;
    private String player2;

    @BeforeEach
    public void setUp() {
        player1 = "Tom";
        player2 = "Martha";
        lobby = new Lobby("ABC123", player1);
    }

    @Test
    void isFull() {
        assertFalse(lobby.isFull());
        lobby.player2Join(player2);
        assertTrue(lobby.isFull());

    }

    @Test
    void player2Join() {
        lobby.player2Join(player2);
        assertEquals(lobby.getPlayer2(), player2);
    }

    @Test
    void playerReadyUp() {
        lobby.playerReadyUp(player1);
        assertTrue(lobby.isPlayer1Ready());
    }
}