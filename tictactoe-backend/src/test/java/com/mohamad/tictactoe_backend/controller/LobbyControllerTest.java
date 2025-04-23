package com.mohamad.tictactoe_backend.controller;

import com.mohamad.tictactoe_backend.model.Lobby;
import com.mohamad.tictactoe_backend.service.LobbyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LobbyController.class)
@AutoConfigureMockMvc
class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LobbyService lobbyService;

    @Test
    void createLobby() throws Exception {
        Map<String, String> player1 = Map.of("player1Name", "Thomas");

        String mockLobbyId = "lobby123";
        Lobby lobby = new Lobby(mockLobbyId, player1.get("player1Name"));
        when(lobbyService.createLobby(eq(player1.get("player1Name"))))
                .thenReturn(lobby);

        System.out.println(lobby);

        String requestJson = """
            {
                "player1Name": "Thomas"
            }
            """;

        mockMvc.perform(post("/api/lobby/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.started").value(false))
                .andExpect(jsonPath("$.player1Ready").value(false))
                .andExpect(jsonPath("$.player1").value(player1.get("player1Name")))
                .andExpect(jsonPath("$.lobbyCode").value(mockLobbyId));

    }

    @Test
    void joinLobby_success() throws Exception {
        String lobbyCode = "ABC123";
        String player1Name = "Tom";
        String player2Name = "Alice";

        Lobby lobby = new Lobby(lobbyCode, player1Name);
        lobby.player2Join(player2Name);

        Mockito.when(lobbyService.joinLobby(eq(lobbyCode), eq(player2Name))).thenReturn(lobby);

        String requestBody = "{\"player2Name\":\"Alice\"}";

        mockMvc.perform(post("/api/lobby/join/{code}", lobbyCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lobbyCode").value(lobbyCode))
                .andExpect(jsonPath("$.player2").value(player2Name));
    }

    @Test
    void joinLobby_notFound() throws Exception {
        String lobbyCode = "XYZ999";
        String playerName = "Bob";

        Mockito.when(lobbyService.joinLobby(lobbyCode, playerName)).thenReturn(null);

        String requestBody = "{\"player2Name\":\"Bob\"}";

        mockMvc.perform(post("/api/lobby/join/{code}", lobbyCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lobby not found or already full."));
    }

    @Test
    void getLobbyStatus_success() throws Exception {
        String lobbyCode = "ABC123";
        String playerName = "Alice";
        Lobby lobby = new Lobby(lobbyCode, playerName);

        Mockito.when(lobbyService.getLobby(lobbyCode)).thenReturn(lobby);

        mockMvc.perform(get("/api/lobby/status/{code}", lobbyCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lobbyCode").value(lobbyCode))
                .andExpect(jsonPath("$.player1").value(playerName))
                .andExpect(jsonPath("$.player1Ready").value(false));

    }

    @Test
    void getLobbyStatus_notFound() throws Exception {
        String lobbyCode = "XYZ999";

        Mockito.when(lobbyService.getLobby(lobbyCode)).thenReturn(null);

        mockMvc.perform(get("/api/lobby/status/{code}", lobbyCode))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lobby not found"));
    }

    @Test
    void playerReadyUp_success() throws Exception {
        String lobbyCode = "ABC123";
        String playerName = "Alice";

        Mockito.when(lobbyService.playerReadyUp(lobbyCode, playerName)).thenReturn(true);

        String requestBody = "{\"playerName\":\"Alice\"}";

        mockMvc.perform(post("/api/lobby/ready/{code}", lobbyCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void playerReadyUp_failure() throws Exception {
        String lobbyCode = "XYZ999";
        String playerName = "Bob";

        Mockito.when(lobbyService.playerReadyUp(lobbyCode, playerName)).thenReturn(false);

        String requestBody = "{\"playerName\":\"Bob\"}";

        mockMvc.perform(post("/api/lobby/ready/{code}", lobbyCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to ready up player"));
    }

    @Test
    void processHeartBeat_success() throws Exception {
        String lobbyCode = "ABC123";
        String playerName = "Alice";

        Lobby lobby = new Lobby(lobbyCode, playerName);

        Mockito.when(lobbyService.getLobby(lobbyCode)).thenReturn(lobby);
        Mockito.doNothing().when(lobbyService).processHeartBeat(lobbyCode, playerName);

        String requestBody = "{\"playerName\":\"Alice\"}";

        mockMvc.perform(post("/api/lobby/heartbeat/{code}", lobbyCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void processHeartBeat_lobbyNotFound() throws Exception {
        String lobbyCode = "XYZ999";
        String playerName = "Bob";

        Mockito.when(lobbyService.getLobby(lobbyCode)).thenReturn(null);

        String requestBody = "{\"playerName\":\"Bob\"}";

        mockMvc.perform(post("/api/lobby/heartbeat/{code}", lobbyCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Lobby not found"));
    }
}