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

        });
    }

    @Test
    public void joinGameTestPositive() {
        Assertions.assertDoesNotThrow(() -> {

        });
    }
}
