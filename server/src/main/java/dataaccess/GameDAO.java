package dataaccess;

import model.GameData;

public interface GameDAO {
    public GameData getGame(int id);
    public GameData createGame(GameData game);
    public GameData updateGame(GameData game);
    public void deleteGame(int id);
    public void clearGameDB();
}
