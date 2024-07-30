package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLGameDAO implements GameDAO {

    public MySQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameId, blackUsername, whiteUsername, gameName, game FROM gamedata WHERE gameId=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(rs.getInt(1), rs.getString(2), rs.getString(3),
                                rs.getString(4), new Gson().fromJson(rs.getString(5), ChessGame.class));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public List<GameData> getAllGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO gamedata (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        updateDatabase(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        return game;
    }

    @Override
    public GameData updateGame(GameData game) throws DataAccessException {
        return null;
    }

    @Override
    public void clearGameDB() throws DataAccessException {
        var statement = "TRUNCATE TABLE gamedata";
        updateDatabase(statement);
    }

    private void updateDatabase(String statement, Object... args) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < args.length; i++) {
                    var param = args[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameData (
              `gameId` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameId`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
