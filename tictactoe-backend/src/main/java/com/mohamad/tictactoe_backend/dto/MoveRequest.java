package com.mohamad.tictactoe_backend.dto;

public class MoveRequest {
    private int row;
    private int col;
    private String playerId;

    // Getters and setters
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }

    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }

    public String getPlayerId() { return playerId; }
    public void setPlayer(String playerID) { this.playerId = playerID; }
}
