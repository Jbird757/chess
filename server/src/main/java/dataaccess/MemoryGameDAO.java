package dataaccess;

import dataaccess.Exceptions.BadRequestException;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

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
    public GameData createGame(GameData game) {
       games.add(game);
       return game;
    }

    @Override
    public GameData updateGame(GameData game) {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).gameID() == game.gameID()) {
                games.set(i, game);

            }
        }
        return games.get(game.gameID());
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
