package dataaccess;

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

        //Get data from input, create new game, and return only the gameID
        GameData newGame = new GameData(gameID, null, null, game.gameName(), null);
        games.add(newGame);
        return new GameData(gameID, null, null, null, null);
    }

    @Override
    public GameData updateGame(GameData game) {
        //Iterate through games, find the matching gameID, and update it
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
