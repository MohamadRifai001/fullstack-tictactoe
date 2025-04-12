import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getLobbyStatus, startLobbyGame } from '../services/api';

function LobbyPage() {
  const { lobbyCode } = useParams();
  const [players, setPlayers] = useState([]);
  const [ready, setReady] = useState(false);
  const [gameStarted, setGameStarted] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const interval = setInterval(async () => {
        console.log("lobbyCode", lobbyCode);
      const status = await getLobbyStatus(lobbyCode);
      if (status) {
        setPlayers([status.player1, status.player2].filter(Boolean));
        setGameStarted(status.gameStarted);
        if (status.gameStarted) {
          navigate(`/game/${lobbyCode}`);
        }
      }
    }, 1000);

    return () => clearInterval(interval);
  }, [lobbyCode, navigate]);

  const handleStartGame = async () => {
    await startLobbyGame(lobbyCode);
    setReady(true);
  };

  return (
    <div>
      <h2>Lobby Code: {lobbyCode}</h2>
      <p>Waiting for players...</p>
      <ul>
        {players.map((p, i) => (
          <li key={i}>{p}</li>
        ))}
      </ul>
      {players.length === 2 && !ready && (
        <button onClick={handleStartGame}>Start Game</button>
      )}
      {ready && <p>Waiting for other player to start...</p>}
    </div>
  );
}

export default LobbyPage;
