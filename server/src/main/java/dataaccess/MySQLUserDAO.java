package dataaccess;

import Exceptions.DataAccessException;
import model.UserData;

import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS userdata (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM userdata WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO userdata (username, password, email) VALUES (?, ?, ?)";
        DBUpdate.updateDatabase(statement, user.username(), user.password(), user.email());
    }

    @Override
    public void clearUserDB() throws DataAccessException {
        var statement = "TRUNCATE TABLE userdata";
        DBUpdate.updateDatabase(statement);
    }

    public void configureDatabase(String[] createStatements) throws DataAccessException {
        DBUpdate.configureDatabase(createStatements);
    }
}
