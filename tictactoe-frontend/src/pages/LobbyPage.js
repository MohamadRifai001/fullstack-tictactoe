import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getLobbyStatus, startLobbyGame, playerReadyUp, sendHeartbeat } from '../services/api';
import { useLocation } from 'react-router-dom';
import "../styles/LobbyPage.css";

function LobbyPage() {
  const { lobbyCode } = useParams();
  const location = useLocation();
  const playerName = location.state?.playerName;
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
        setGameStarted(status.started);
        if (status.started) {
          navigate(`/game/${lobbyCode}`);
        }
      }
    }, 1000);
    return () => clearInterval(interval);
  }, [lobbyCode, navigate]);

  useEffect(() => {
    const heartbeat = setInterval(() => {
      sendHeartbeat(lobbyCode, playerName.name);
    }, 10000); 
    
    return () => clearInterval(heartbeat);
  }, [lobbyCode, playerName]);


  const handleReady = async () => {
    if (!playerName) {
      alert("Player name is required.");
      return;
    }

    await playerReadyUp(lobbyCode, playerName.name);
    setReady(true);
  }

  const handleStartGame = async () => {
    if(!playerName) {
      alert("player name missing");
      return;
    }

    await startLobbyGame(lobbyCode, playerName.name);
    setReady(true);
  };

  return (
    <div className="lobby-page">
      <h2> Share this lobby code to invite a friend: {lobbyCode}</h2>
      <p className="waiting-text dots">Waiting for Players</p>
      <ul>
        {players.map((p, i) => (
          <li key={i}>{p}</li>
        ))}
      </ul>
      {players.length === 2 && !ready && (
        <button className="button-slide" onClick={handleReady}>Ready</button>
      )}
      {ready && <p>Waiting for other player to start...</p>}
    </div>
  );
}

export default LobbyPage;
