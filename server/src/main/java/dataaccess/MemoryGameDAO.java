package dataaccess;

import dataaccess.Exceptions.BadRequestException;
import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryGameDAO implements GameDAO {
    private static List<GameData> games = new ArrayList<GameData>();

    @Override
    public GameData getGame(int id) {
        for (GameData gameData : games) {
            if (id == gameData.gameID()) {
                return gameData;
            }
        }
        return null;
    }

    @Override
    public List<GameData> getAllGames() {
        return games;
    }

    @Override
    public GameData createGame(GameData game) {
        int gameID;
        if (games.isEmpty()) {
            gameID = 1;
        } else {
            gameID = games.getLast().gameID() + 1;
        }
        GameData newGame = new GameData(gameID, null, null, game.gameName(), null);
       games.add(newGame);
        return new GameData(gameID, null, null, null, null);
    }

    @Override
    public GameData updateGame(GameData game) {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).gameID() == game.gameID()) {
                games.set(i, game);

            }
        }
        return game;
    }

    @Override
    public void deleteGame(int id) {
        games.removeIf(gameData -> gameData.gameID() == id);
    }

    @Override
    public void clearGameDB() {
        games.clear();
    }
}
