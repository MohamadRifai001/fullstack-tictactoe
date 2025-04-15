import React from "react";
import "../styles/StatusBar.css";

const StatusBar = ({ currentPlayer, status, winner }) => {
  const renderMessage = () => {
    if (status === "WIN") return `🎉 ${winner} won!`;
    if (status === "WAITING_FOR_MINIGAME")
      return "It's a tie! Please wait for the minigame";
    return `Waiting for ${currentPlayer}'s move...`;
  };

  return (
    <div className="status-bar">
      <p>{renderMessage()}</p>
    </div>
  );
};

export default StatusBar;
