package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    GameData addWhitePlayer(String whiteUsername) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
    GameData addBlackPlayer(String blackUsername) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
