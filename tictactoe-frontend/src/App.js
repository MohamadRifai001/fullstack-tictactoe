import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import HomePage from './pages/HomePage';
import GamePage from './pages/GamePage';
import LobbyPage from './pages/LobbyPage';

import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/game/:lobbyCode" element={<GamePage />} />
        <Route path="/lobby/:lobbyCode" element={<LobbyPage />} />
      </Routes>
    </Router>
  );
}

export default App;
