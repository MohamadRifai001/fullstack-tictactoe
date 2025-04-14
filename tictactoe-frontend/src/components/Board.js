import React from 'react';
import Cell from './Cell';

const Board = ({ board, onCellClick }) => {
  return (
    <div className="board">
      {board.map((row, rIndex) => (
        <div className="board-row" key={rIndex}>
          {row.map((cell, cIndex) => (
            <Cell
              key={`${rIndex}-${cIndex}`}
              value={cell}
              onClick={() => onCellClick(rIndex, cIndex)}
            />
          ))}
        </div>
      ))}
    </div>
  );
};

export default Board;
