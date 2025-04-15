import React, { useState, useEffect } from "react";
import { useParams, useLocation, useNavigate } from "react-router-dom";
import { getGameState, makeMove, rematch } from "../services/api";
import Board from "../components/Board";
import StatusBar from "../components/StatusBar";
import Minigame from "../components/Minigame";
import "../styles/GamePage.css";

function GamePage() {
  const { lobbyCode } = useParams();
  const location = useLocation();
  const playerName = location.state?.playerName;
  const [gameState, setGameState] = useState(null);
  const [showMinigame, setShowMinigame] = useState(false);
  const [fetchFailures, setFetchFailures] = useState(0);
  const [rematchRequested, setRematchRequested] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const interval = setInterval(async () => {
      try {
        const state = await getGameState(lobbyCode);
        setGameState(state);
        if (state.status === "WAITING_FOR_MINIGAME") {
          setShowMinigame(true);
        } else {
          setShowMinigame(false);
        }
        if(state.status === "IN_PROGRESS") {
            setRematchRequested(false);
        }
        setFetchFailures(0); // Reset fetch failures on successful fetch
      } catch (error) {
        console.error("Error fetching game state:", error);
        setFetchFailures((prev) => prev + 1);
        if (fetchFailures > 3) {
          alert("Game error, sending you to the home page.");
          clearInterval(interval);
          navigate("/");
        }
      }
    }, 1000);
    return () => clearInterval(interval);
  }, [lobbyCode, fetchFailures, playerName, navigate]);

  const handleMove = async (row, col) => {
    if (gameState && gameState.currentPlayer.name === playerName) {
      const updatedState = await makeMove(
        lobbyCode,
        row,
        col,
        gameState.currentPlayer.id
      );
      setGameState(updatedState);
    }
  };

  const handleMinigameWin = async () => {
    setShowMinigame(false);
  };

  const playerId =
    gameState?.player1?.name === playerName
      ? gameState.player1.id
      : gameState?.player2?.name === playerName
      ? gameState.player2.id
      : null;

  const handleRematch = async () => {
    try {
      const updatedState = await rematch(lobbyCode, playerId);
      setGameState(updatedState);
      setRematchRequested(true);
    } catch (error) {
      console.error("Error processing rematch request:", error);
    }
  };

  if (!gameState) {
    return <div>Loading...</div>;
  }

  return (
    <div className="game-page">
      <h2>Lobby: {lobbyCode}</h2>
      <h2>Player: {playerName}</h2>
      <StatusBar
        currentPlayer={gameState.currentPlayer.name}
        status={gameState.status}
        winner={gameState.winner}
      />
      {rematchRequested && <h1>Waiting for the other player to rematch</h1>}
      {(gameState.status === "WIN" || gameState.status === "TIE") &&
        !rematchRequested && (
          <button className="rematch-button" onClick={handleRematch}>
            Rematch
          </button>
        )}
      {showMinigame ? (
        <Minigame
          lobbyCode={lobbyCode}
          playerId={playerId}
          onWin={handleMinigameWin}
        />
      ) : (
        <Board
          board={gameState.board}
          onCellClick={handleMove}
          gameOver={gameState.status !== "IN_PROGRESS"}
        />
      )}
    </div>
  );
}

export default GamePage;
