package com.mohamad.tictactoe_backend.model;


import java.util.UUID;

/*
stores player information
 */
public class Player {
    private final String id;
    private final String name;
    private String symbol;

    public Player(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    // getters -- needed for Spring boot serialization

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getSymbol() { return this.symbol; }
}
