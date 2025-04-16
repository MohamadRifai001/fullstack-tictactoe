import React, { useState, useEffect } from "react";
import { notifyMinigameResult } from "../services/api";

const MiniGame = ({ lobbyCode, playerId, onWin }) => {
  const [position, setPosition] = useState({ top: 100, left: 100 });

  const handleWin = async () => {
    try {
      const res = await notifyMinigameResult(lobbyCode, playerId);
      if (res.status === 409) {
        alert("Too late! other player already won.");
      }
      onWin();
    } catch (error) {
      console.error("Error notifying minigame result:", error);
      alert("Failed to notify minigame result. Please try again.");
    }
  };

  const moveButton = () => {
    const containerHeight = 500; // Height of the .minigame container
    const buttonHeight = 50; // Approximate height of the button
    const bottomOffset = 100; // Space from the bottom of the container
  
    const randomTop = Math.floor(
      Math.random() * (containerHeight - bottomOffset - buttonHeight) + (containerHeight - bottomOffset)
    ); // Constrain to the bottom area
    const randomLeft = Math.floor(Math.random() * 400); // Adjust based on container width
  
    setPosition({ top: randomTop, left: randomLeft });
  };

  useEffect(() => {
    const interval = setInterval(moveButton, 1000); // Move button every second
    return () => clearInterval(interval); // Cleanup on component unmount
  }, []);

  return (
    <div className="minigame">
      <p>First to click gets their turn first on the new board</p>
      <button
        className="button-slide"
        style={{
          top: `${position.top}px`,
          left: `${position.left}px`,
          position: "absolute",
        }}
        onClick={handleWin}
      >
        Click fast!
      </button>
    </div>
  );
};

export default MiniGame;
