Tic Tac Toe with Reaction time Minigame

A multiplayer web game that adds a twist to classic Tic Tac Toe:
- If the 3x3 board ends in a tie, the board expands to 4x4.
- A Reaction time minigame determines who gets to play first on the new 4x4 board

Built with **Java Spring Boot**, **React.js**, and real-time game syncing.

---

## Features

-  2-player online Tic Tac Toe
- Lobby system with player names and codes
-  minigame on tie to break stalemates
- Heartbeat system to detect disconnected players
- Rematch button (both players must agree)
- Profanity filter on player usernames

---

## Tech Stack

| Frontend | Backend |
|----------|---------|
| React.js + CSS | Java + Spring Boot |
| React Router DOM | RESTful API |
| Bad-words (username filter) | In-memory lobby/game management |
| Custom 2D minigame | JSON-based game state |


### Prerequisites

- Node.js + npm
- Java 17+
- Maven
