import React from 'react';

const Cell = ({ value, onClick, gameOver }) => {
  return (
    <button className="cell" 
    onClick={onClick} 
    disabled={gameOver || value === "X" || value === "O"}
    >
      {value}
    </button>
  );
};

export default Cell;
