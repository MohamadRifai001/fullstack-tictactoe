package com.mohamad.tictactoe_backend.model;

public class Lobby {
    private final String lobbyCode;
    private String player1;
    private String player2;
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

    //getters and setters
    public String getLobbyCode() { return lobbyCode; }

    //public void setLobbyCode(String lobbyCode) { this.lobbyCode = lobbyCode; }

    public String getPlayer1() { return player1; }

    //public void setPlayer1(String player1) { this.player1 = player1; }

    public String getPlayer2() { return player2; }

    //public void setPlayer2(String player2) { this.player2 = player2; }

    public boolean getStarted() { return this.started; }

    public void setStarted(boolean started) { this.started = started; }


}
