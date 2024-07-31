package dataaccess;

import model.AuthData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO {

    public MySQLAuthDAO() throws DataAccessException {
        String[] createStatements = {
                """
             CREATE TABLE IF NOT EXISTS authdata (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
    }

    @Override
    public AuthData createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO authdata (authToken, username) VALUES (?, ?)";
        DBUpdate.updateDatabase(statement, auth.authToken(), auth.username());
        return auth;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM authdata WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString(2), rs.getString(1));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authdata WHERE authToken=?";
        DBUpdate.updateDatabase(statement, authToken);
    }

    @Override
    public void clearAuthDB() throws DataAccessException {
        var statement = "TRUNCATE TABLE authdata";
        DBUpdate.updateDatabase(statement);
    }

    public void configureDatabase(String[] createStatements) throws DataAccessException {
        DBUpdate.test(createStatements);
    }
}
