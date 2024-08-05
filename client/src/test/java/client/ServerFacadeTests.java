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
    public void registerTest() {
        ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
        Assertions.assertDoesNotThrow(() -> {
            AuthData newAuth = serverFacade.registerUser("user1", "password", "myeamil");
            System.out.println(newAuth);
        });
    }

    @Test
    public void loginTest() {
        ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
        Assertions.assertDoesNotThrow(() -> {
            
        });
    }

}
