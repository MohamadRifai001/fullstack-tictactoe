package com.mohamad.tictactoe_backend.model;

import java.util.Objects;

public class Lobby {
    private final String lobbyCode;
    private String player1;
    private String player2;
    private boolean player1Ready = false;
    private boolean player2Ready = false;
    private long player1LastSeen;
    private long player2LastSeen;
    private boolean started;

    public Lobby(String lobbyCode, String player1) {
        this.lobbyCode = lobbyCode;
        this.player1 = player1;
        this.started = false;

    }

    /*
    helper methods
     */
    public boolean isFull() {
        return player1 != null && player2 != null;
    }

    public void player2Join(String player2) {
        this.player2 = player2;
    }

    public boolean playerReadyUp(String playerName) {
        if(playerName.equals(player1)) {
            player1Ready = true;
            this.started = player2Ready;
            return true;
        }
        else if (playerName.equals(player2)) {
            player2Ready = true;
            this.started = player1Ready;
            return true;
        }
        return false;
    }

    //getters and setters
    public String getLobbyCode() { return lobbyCode; }

    //public void setLobbyCode(String lobbyCode) { this.lobbyCode = lobbyCode; }

    public String getPlayer1() { return player1; }

    public void setPlayer1(String player1) { this.player1 = player1; }

    public String getPlayer2() { return player2; }

    public void setPlayer2(String player2) { this.player2 = player2; }

    public boolean getStarted() { return this.started; }

    public void setStarted(boolean started) { this.started = started; }

    public boolean isPlayer1Ready() { return player1Ready; }

    public void setPlayer1Ready(boolean player1Ready) { this.player1Ready = player1Ready; }

    public boolean isPlayer2Ready() { return player2Ready; }

    public void setPlayer2Ready(boolean player2Ready) { this.player2Ready = player2Ready; }

    public long getPlayer1LastSeen() { return player1LastSeen; }

    public void setPlayer1LastSeen(long player1LastSeen) { this.player1LastSeen = player1LastSeen; }

    public long getPlayer2LastSeen() { return player2LastSeen; }

    public void setPlayer2LastSeen(long player2LastSeen) { this.player2LastSeen = player2LastSeen; }
}
