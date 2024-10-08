package client;

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
        });
    }

    @Test
    public void registerTestNegative() {
        Assertions.assertThrows(Exception.class, () -> {
            serverFacade.registerUser("user1", "password", "myemail");
            AuthData newAuth = serverFacade.registerUser("user1", "password2", "myemail2");
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
    public void loginTestNegative() {
        Assertions.assertThrows(Exception.class, () -> {
            serverFacade.login("user1", "password");
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
    public void logoutTestNegative() {
        Assertions.assertThrows(Exception.class, () -> {
            serverFacade.logout("user1");
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
    public void creatGameTestNegative() {
        Assertions.assertThrows(Exception.class, () -> {
            serverFacade.createGame("1234", "1234");
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
    public void listGamesTestNegative() {
        Assertions.assertThrows(Exception.class, () -> {
            serverFacade.listGames("1234");
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
            Assertions.assertEquals(new GameData(1, "user1", "user1", "game1", null), games[0]);
        });
    }

    @Test
    public void joinGameTestNegative() {
        Assertions.assertThrows(Exception.class, () -> {
            AuthData auth = serverFacade.registerUser("user1", "password", "myemail");
            GameData newGame = serverFacade.createGame("game1", auth.authToken());
            serverFacade.joinGame(1, "WHITE", auth.authToken());
            serverFacade.joinGame(1, "WHITE", auth.authToken());
        });
    }
}
