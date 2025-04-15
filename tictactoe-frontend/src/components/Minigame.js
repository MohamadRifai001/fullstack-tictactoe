import React from 'react';
import { notifyMinigameResult } from '../services/api';

const MiniGame = ({ lobbyCode, playerId, onWin }) => {
  const handleWin = async () => {
    try {
        const res = await notifyMinigameResult(lobbyCode, playerId);
        if(res.status === 409) {
            alert("Too late! other player already won.")
        }
        onWin();
    } catch (error) {
        console.error("Error notifying minigame result:", error);
        alert("Failed to notify minigame result. Please try again.");
    }
  };

  return (
    <div className="minigame">
      <p>🏃 First to click gets their turn first on the new board</p>
      <button onClick={handleWin}>Click fast!</button>
    </div>
  );
};

export default MiniGame;