package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MySQLGameDAOTest {

    @Test
    void createDB() {
        Assertions.assertDoesNotThrow(() -> {
            MySQLGameDAO dao = new MySQLGameDAO();
        });
        System.out.println("Successfully created Database");
    }

    @Test
    void getAndSetGames() {
        GameData newGame1 = new GameData(null, null, null, "game1", new ChessGame());

        Assertions.assertDoesNotThrow(() -> {
            MySQLGameDAO dao = new MySQLGameDAO();
            dao.clearGameDB();
            GameData createdGame1 = dao.createGame(newGame1);

            GameData game2 = new GameData(createdGame1.gameID(), "player1", null, "game1", new ChessGame());
            dao.updateGame(game2);

            var games = dao.getAllGames();
            System.out.println(games);
        });
    }

}