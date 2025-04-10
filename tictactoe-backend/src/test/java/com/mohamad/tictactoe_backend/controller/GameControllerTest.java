package com.mohamad.tictactoe_backend.controller;

import com.mohamad.tictactoe_backend.model.GameState;
import com.mohamad.tictactoe_backend.model.Player;
import com.mohamad.tictactoe_backend.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameService gameService;

    private GameState gameState;

    @BeforeEach
    public void setUp() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player1");
        gameState = new GameState(player1, player2);
    }


//    @Test
//    void startGame() {
//    }
//
//    @Test
//    void makeMove() {
//    }
//
//    @Test
//    void expandBoard() {
//    }
//
//    @Test
//    void getGame() {
//    }
}