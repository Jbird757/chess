package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTestPositive() {
        ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
        Assertions.assertDoesNotThrow(() -> {
            AuthData newAuth = serverFacade.registerUser("user1", "password", "myeamil");
            System.out.println(newAuth);
        });
    }

    @Test
    public void loginTestPositive() {
        ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.registerUser("user1", "password", "myeamil");
            AuthData loginAuth = serverFacade.login("user1", "password");
            System.out.println(loginAuth);
        });
    }

    @Test
    public void logoutTestPositive() {
        ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
        Assertions.assertDoesNotThrow(() -> {
            AuthData auth = serverFacade.registerUser("user1", "password", "myeamil");
            System.out.println(auth);
            String authToken = auth.authToken();
            serverFacade.logout(authToken);
        });
    }

}
