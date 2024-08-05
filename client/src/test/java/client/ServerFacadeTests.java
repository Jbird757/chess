package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import service.ClearService;


public class ServerFacadeTests {

    static ServerFacade serverFacade;
    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:"+port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clearDB() {
        ClearService clearService = new ClearService();
        Assertions.assertDoesNotThrow(clearService::clear);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTestPositive() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData newAuth = serverFacade.registerUser("user1", "password", "myemail");
            System.out.println(newAuth);
        });
    }

    @Test
    public void loginTestPositive() {
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.registerUser("user1", "password", "myemail");
            AuthData loginAuth = serverFacade.login("user1", "password");
            System.out.println(loginAuth);
        });
    }

    @Test
    public void logoutTestPositive() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData auth = serverFacade.registerUser("user1", "password", "myemail");
            String authToken = auth.authToken();
            serverFacade.logout(authToken);
        });
    }

    @Test
    public void creatGameTestPositive() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData auth = serverFacade.registerUser("user1", "password", "myemail");
            GameData newGame = serverFacade.createGame("game1", auth.authToken());
            System.out.println(newGame);
        });
    }

    @Test
    public void listGamesTestPositive() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData auth = serverFacade.registerUser("user1", "password", "myemail");
            GameData newGame = serverFacade.createGame("game1", auth.authToken());
            GameData newGame2 = serverFacade.createGame("game2", auth.authToken());
            GameData newGame3 = serverFacade.createGame("game3", auth.authToken());
            GameData[] games = {newGame, newGame2, newGame3};
            GameData[] returnedGames = serverFacade.listGames(auth.authToken());
            Assertions.assertArrayEquals(games, returnedGames);
        });
    }

    @Test
    public void joinGameTestPositive() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData auth = serverFacade.registerUser("user1", "password", "myemail");
            GameData newGame = serverFacade.createGame("game1", auth.authToken());
            serverFacade.joinGame(1, "WHITE", auth.authToken());
            serverFacade.joinGame(1, "BLACK", auth.authToken());
            GameData[] games = serverFacade.listGames(auth.authToken());
            Assertions.assertEquals(games[0], new GameData(1, "user1", "user1", "game1", new ChessGame()));
        });
    }
}
