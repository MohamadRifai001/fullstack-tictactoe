package com.mohamad.tictactoe_backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    /*
    makes sure a UUID is created
     */
    @Test
    void testPlayerCreation() {
        Player player = new Player("player1");
        assertNotNull(player.getId());
        assertEquals("player1", player.getName());
    }

    @Test
    void testUniqueIds() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        assertNotEquals(player1.getId(), player2.getId());
    }
}