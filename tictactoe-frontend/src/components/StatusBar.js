import React from 'react';
import '../styles/StatusBar.css';

const StatusBar = ({ currentPlayer, status}) => {
  const renderMessage = () => {
    //if (status === "WIN") return "🎉 You won!";
    //if (status === "LOSE") return "💀 You lost!";
    //if (status === "TIE") return "🤝 It's a tie!";
    return `Waiting for ${currentPlayer}'s move...`;
  };

  return (
    <div className="status-bar">
      <p>{renderMessage()}</p>
    </div>
  );
};

export default StatusBar;