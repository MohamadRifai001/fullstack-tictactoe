package com.mohamad.tictactoe_backend.controller;

import com.mohamad.tictactoe_backend.model.GameState;
import com.mohamad.tictactoe_backend.model.Player;
import com.mohamad.tictactoe_backend.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GameController.class)
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;


    /*
    just makes sure that startGame calls gameService with 2 player objects
     */
    @Test
    void startGame() throws Exception {
        String mockGameId = "game123";
        when(gameService.createGame(any(Player.class), any(Player.class)))
                .thenReturn(mockGameId);

        String requestJson = """
            {
                "player1Name": "player1",
                "player2Name": "player2"
            }
            """;

        mockMvc.perform(post("/api/game/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(mockGameId));
    }

    /*
    tests if the information needed is passed through from the controller to gameService
    and the GameState is being serialized correctly when returned
     */
    @Test
    void MakeMove() throws Exception {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");

        String mockGameId = "game123";
        GameState mockGameState = new GameState(player1, player2);

        when(gameService.makeMove(eq(mockGameId), eq(0), eq(0), eq(player1.getId())))
                .thenReturn(mockGameState);

        String requestBody = String.format(
                "{\"row\":0, \"col\":0, \"playerId\":\"%s\"}",
                player1.getId()
        );
        mockMvc.perform(post("/api/game/{gameId}/move", mockGameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.board").exists())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.playerX.id").value(player1.getId()))
                .andExpect(jsonPath("$.playerO.id").value(player2.getId()));
    }

    /*
    Makes sure that the correct gameState is being returned
     */
    @Test
    void getGame() throws Exception {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        String mockGameId = "game123";
        GameState mockGameState = new GameState(player1, player2);
        when(gameService.getGame(eq(mockGameId))).thenReturn(mockGameState);


        mockMvc.perform(get("/api/game/{gameId}", mockGameId))
                .andExpect(jsonPath("$.board").exists())
                .andExpect(jsonPath("$.playerX.id").value(player1.getId()));
    }
}