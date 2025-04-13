
const API_BASE = "http://localhost:8080/api"; //replace name with backend, put localhost:8080 if locally run

/*
following methods are for interacting with the backend for game management
*/
// Tells the backend server to start a new game with the given player names
export async function startGame(player1, player2) {
  const res = await fetch(`${API_BASE}/game/start`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ player1Name: player1, player2Name: player2 }),
  });
  return await res.text(); // gameId
}

// Tells the backend the move the specified player made
export async function makeMove(gameId, row, col, playerId) {
  const res = await fetch(`${API_BASE}/game/${gameId}/move`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ row, col, playerId }),
  });
  return await res.json(); // GameState
}

// Tells the backend when the minigame is over and who won
export async function notifyMinigameResult(gameId) {
  const res = await fetch(`${API_BASE}/game/${gameId}/expandBoard`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
  });
  return await res.json(); // updated GameState
}

// retrieves the current game state from the backend server
export async function getGameState(gameId) {
  const res = await fetch(`${API_BASE}/game/${gameId}`);
  if (!res.ok) {
    throw new Error("Failed to fetch game state");
  }
  return await res.json(); // GameState
}

/*
the following methods are for interacting with the backend for lobby management
*/

export const getLobbyStatus = async (lobbyCode) => {
  const res = await fetch(`${API_BASE}/lobby/status/${lobbyCode}`);
  if (!res.ok) {
    throw new Error("Failed to fetch lobby status");
  }
  return await res.json(); // LobbyStatus
}

export async function createLobby(player1) {
  const res = await fetch(`${API_BASE}/lobby/create`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ player1Name: player1}),
  });
  if (!res.ok) {
    throw new Error("Failed to create lobby");
  }
  return await res.json();
}

export const joinLobby = async (code, player2) => {
  const res = await fetch(`${API_BASE}/lobby/join/${code}`, {
     method: 'POST',
     headers: { 'Content-Type': 'application/json' },
     body: JSON.stringify({ player2Name: player2}), // Replace with actual player name if needed
    });
  return res.json();
};

export const startLobbyGame = async (code, playerName) => {
  const res = await fetch(`${API_BASE}/lobby/start/${code}`, { method: 'POST' });
  return res.json();
};

export const playerReadyUp = async (code, player) => { 
  const res = await fetch(`${API_BASE}/lobby/ready/${code}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ playerName: player}),
  });
  return res.json();
};

export const sendHeartbeat = async (code, player) => {
  return fetch(`${API_BASE}/lobby/heartbeat/${code}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ playerName: player}),
  });
};