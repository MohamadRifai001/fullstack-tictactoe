import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createLobby, joinLobby } from '../services/api';
import "../styles/HomePage.css";
import { validatePlayerName } from '../Utils/validators';

function HomePage() {
    const [playerName, setPlayerName] = useState('');
    const[joinCode, setJoinCode] = useState('');
    const navigate = useNavigate();

    const handleCreateLobby = async () => {
        const result = validatePlayerName(playerName);
        if (!result.valid) {
            alert(result.message);
            return;
        }

        try {
            const lobby = await createLobby(result.name);
            navigate(`/lobby/${lobby.lobbyCode}`, { state: { playerName: result } });
        } catch (error) {
            console.error('Error creating lobby:', error);
            alert('Failed to create lobby. Please try again.');
        }
    };

    const handleJoinLobby = async () => {
        const result = validatePlayerName(playerName);
        if (!result.valid) {
            alert(result.message);
            return;
        }
        try {
            const lobby = await joinLobby(joinCode, playerName);
            navigate(`/lobby/${lobby.lobbyCode}`, { state: { playerName: result } });
        } catch (error) {
            console.error('Error joining lobby:', error);
            alert('Failed to join lobby. Please check the code and try again.');
        }
    };

    return (
        <div className="home-page">
            <h1>TicTacToe</h1>
            <input
                type="text"
                value={playerName}
                onChange={(e) => setPlayerName(e.target.value)}
                placeholder="Enter your username"
            />
            <button className="button-slide" onClick={handleCreateLobby}>Create Lobby</button>
            <input
                type="text"
                value={joinCode}
                onChange={(e) => setJoinCode(e.target.value)}
                placeholder="Enter lobby code"
            />
            <button className="button-slide" onClick={handleJoinLobby}>Join Lobby</button>
        </div>
    );
}

export default HomePage;