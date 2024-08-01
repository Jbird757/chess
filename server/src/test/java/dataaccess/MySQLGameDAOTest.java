package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static dataaccess.DBUpdate.updateDatabase;

class MySQLGameDAOTest {

    @Test
    void createDB() {
        Assertions.assertDoesNotThrow(() -> {
            MySQLGameDAO dao = new MySQLGameDAO();
        });
        System.out.println("Successfully created Database");
    }

    @Test
    void setGamePositive() {
        GameData newGame = new GameData(null, null, null, "game1", new ChessGame());
        Assertions.assertDoesNotThrow(() -> {
            MySQLGameDAO dao = new MySQLGameDAO();
            dao.clearGameDB();
            GameData createdGame = dao.createGame(newGame);
            var statement = "SELECT * FROM gamedata WHERE gameid = ?";
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setInt(1, createdGame.gameID());
            var rs = ps.executeQuery();
            if (rs.next()) {
                Assertions.assertEquals(createdGame.gameID(), rs.getInt("gameid"));
            }
        });
    }

    @Test
    void setGameNegative() {
        GameData newGame = new GameData(null, null, null, null, new ChessGame());
        Assertions.assertThrows(DataAccessException.class, () -> {
            MySQLGameDAO dao = new MySQLGameDAO();
            dao.clearGameDB();
            GameData createdGame = dao.createGame(newGame);
            var statement = "SELECT * FROM gamedata WHERE gameid = ?";
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setInt(1, createdGame.gameID());
            var rs = ps.executeQuery();
        });
    }

    @Test
    void getGamePositive() {
        Assertions.assertDoesNotThrow(() -> {
            MySQLGameDAO dao = new MySQLGameDAO();
            dao.clearGameDB();
            GameData createdGame = dao.createGame(new GameData(null, null,
                    null, "game1", new ChessGame()));
            GameData returnedGame = dao.getGame(createdGame.gameID());
            GameData newGame = new GameData(createdGame.gameID(), null, null, "game1", new ChessGame());
            Assertions.assertEquals(newGame, returnedGame);
        });
    }

    @Test
    void getGameNegative() {
        Assertions.assertDoesNotThrow(() -> {
            MySQLGameDAO dao = new MySQLGameDAO();
            dao.clearGameDB();
            GameData createdGame = dao.createGame(new GameData(null, null,
                    null, "game1", new ChessGame()));
            Assertions.assertNull(dao.getGame(111));
        });
    }

    @Test
    void getAllGamesPositive() throws DataAccessException, SQLException {
        GameData newGame = new GameData(null, null, null, "game1", new ChessGame());
        GameData newGame2 = new GameData(null, null, null, "game2", new ChessGame());
        GameData newGame3 = new GameData(null, null, null, "game3", new ChessGame());
        var premadeGamesList = new ArrayList<GameData>();
        var games = new ArrayList<GameData>();
        var statement = "SELECT * FROM gamedata";

            MySQLGameDAO dao = new MySQLGameDAO();
            dao.clearGameDB();
            int gameID1 = dao.createGame(newGame).gameID();
            int gameID2 = dao.createGame(newGame2).gameID();
            int gameID3 = dao.createGame(newGame3).gameID();
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery();
            while (rs.next()) {
                games.add (new GameData(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), new Gson().fromJson(rs.getString(5), ChessGame.class)));
            }
            premadeGamesList.add(new GameData(gameID1, null, null, "game1", new ChessGame()));
            premadeGamesList.add(new GameData(gameID2, null, null, "game2", new ChessGame()));
            premadeGamesList.add(new GameData(gameID3, null, null, "game3", new ChessGame()));
            Assertions.assertEquals(premadeGamesList, games);
    }

    @Test
    void getAllGamesNegative() {
        var games = new ArrayList<GameData>();
        var statement = "SELECT * FROM gamedata";
        Assertions.assertDoesNotThrow(() -> {
            MySQLGameDAO dao = new MySQLGameDAO();
            dao.clearGameDB();
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery();
            while (rs.next()) {
                games.add (new GameData(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), new Gson().fromJson(rs.getString(5), ChessGame.class)));
            }
            Assertions.assertTrue(games.isEmpty());
        });
    }

    @Test
    void updateGameNegative() {
        var statement = "UPDATE gamedata SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameId=?";
        GameData newGame = new GameData(null, null, null, "game1", new ChessGame());

        Assertions.assertThrows(DataAccessException.class, () -> {
            MySQLGameDAO dao = new MySQLGameDAO();
            dao.clearGameDB();
            GameData createdGame = dao.createGame(newGame);
            GameData updatedVersion = new GameData(createdGame.gameID(), "whiteplayer",
                    "blackplayer", "game1", new ChessGame());
            updateDatabase(statement, updatedVersion.whiteUsername(), updatedVersion.blackUsername(),
                    newGame.gameName(), newGame.game(), "2222");
        });
    }

    @Test
    void clearGameDB() {
        GameData newGame = new GameData(null, null, null, "game1", new ChessGame());
        GameData newGame2 = new GameData(null, null, null, "game2", new ChessGame());
        GameData newGame3 = new GameData(null, null, null, "game3", new ChessGame());
        var games = new ArrayList<GameData>();
        var statement = "SELECT * FROM gamedata";

        Assertions.assertDoesNotThrow(() -> {
            MySQLGameDAO dao = new MySQLGameDAO();
            dao.clearGameDB();
            dao.createGame(newGame);
            dao.createGame(newGame2);
            dao.createGame(newGame3);
            dao.clearGameDB();
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery();
            while (rs.next()) {
                games.add (new GameData(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), new Gson().fromJson(rs.getString(5), ChessGame.class)));
            }
            Assertions.assertTrue(games.isEmpty());
        });
    }

}