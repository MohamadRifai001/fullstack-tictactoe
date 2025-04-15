import React from 'react';
import Cell from './Cell';

const Board = ({ board, onCellClick, gameOver}) => {
  return (
    <div className="board">
      {board.map((row, rIndex) => (
        <div className="board-row" key={rIndex}>
          {row.map((cell, cIndex) => (
            <Cell
              key={`${rIndex}-${cIndex}`}
              value={cell}
              onClick={() => onCellClick(rIndex, cIndex)}
              gameOver={gameOver}
            />
          ))}
        </div>
      ))}
    </div>
  );
};

export default Board;
