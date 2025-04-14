import React, { useState, useEffect } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import { getGameState, makeMove } from '../services/api';
import Board from '../components/Board';
import StatusBar from '../components/StatusBar';
import "../styles/GamePage.css";

function GamePage() {
    const { lobbyCode } = useParams();
    const location = useLocation();
    const playerName = location.state?.playerName;
    const [gameState, setGameState] = useState(null);

    useEffect(() => {
        const interval = setInterval(async () => {
          const state = await getGameState(lobbyCode);
          setGameState(state);
        }, 1000);
        return () => clearInterval(interval);
    }, [lobbyCode]);

    const handleMove = async (row , col) => {
        console.log("Clicked cell:", row, col);
        console.log("Current player:", gameState.currentPlayer);
        console.log("Current player Name:", gameState.currentPlayer.name);
        console.log("Player name:", playerName);
        if (gameState && gameState.currentPlayer.name == playerName) {
            const updatedState = await makeMove(lobbyCode, row, col, playerName);
            setGameState(updatedState);
        }
    }

    if(!gameState) {
        return <div>Loading...</div>;
    }

    return (
        <div className="game-page">
            <h2>Lobby: {lobbyCode}</h2>
            <h2 >Player: {playerName}</h2>
            <StatusBar currentPlayer={gameState.currentPlayer.name} status={gameState.status} />
            <Board board={gameState.board} onCellClick={handleMove} />
        </div>
    );
}

export default GamePage;