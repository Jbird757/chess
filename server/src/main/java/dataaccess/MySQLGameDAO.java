package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLGameDAO implements GameDAO {

    public MySQLGameDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS gamedata (
              `gameId` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameId`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameId, blackUsername, whiteUsername, gameName, game FROM gamedata WHERE gameId=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(
                                rs.getInt("gameId"),
                                rs.getString("blackUsername"),
                                rs.getString("whiteUsername"),
                                rs.getString("gameName"),
                                new Gson().fromJson(rs.getString("game"), ChessGame.class));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public List<GameData> getAllGames() throws DataAccessException {
        var games = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameId, blackUsername, whiteUsername, gameName, game FROM gamedata";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(new GameData(rs.getInt(1), rs.getString(2), rs.getString(3),
                                rs.getString(4), new Gson().fromJson(rs.getString(5), ChessGame.class)));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return games;
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO gamedata (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        int newGameID = updateDatabase(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        return getGame(newGameID);
    }

    @Override
    public GameData updateGame(GameData game) throws DataAccessException {
        var statement = "UPDATE gamedata SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameId=?";
        updateDatabase(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game(), game.gameID());
        return getGame(game.gameID());
    }

    @Override
    public void clearGameDB() throws DataAccessException {
        var statement = "TRUNCATE TABLE gamedata";
        updateDatabase(statement);
    }

    private int updateDatabase(String statement, Object... args) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < args.length; i++) {
                    var param = args[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) ps.setString(i+1, new Gson().toJson(p));
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    public void configureDatabase(String[] createStatements) throws DataAccessException {
        DBUpdate.test(createStatements);
    }
}
