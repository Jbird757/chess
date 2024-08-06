package dataaccess;

import Exceptions.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDAO {
    public GameData getGame(int id) throws DataAccessException;
    public List<GameData> getAllGames() throws DataAccessException;
    public GameData createGame(GameData game) throws DataAccessException;
    public GameData updateGame(GameData game) throws DataAccessException;
    public void clearGameDB() throws DataAccessException;
}
