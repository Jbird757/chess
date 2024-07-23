package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    public GameData getGame(int id);
    public List<GameData> getAllGames();
    public GameData createGame(GameData game);
    public GameData updateGame(GameData game);
    public void clearGameDB();
}
