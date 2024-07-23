package server;

import handler.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> new RegisterHandler().registerUser(req, res));
        Spark.post("/session", (req, res) -> new LoginHandler().loginUser(req, res));
        Spark.delete("/session", (req, res) -> new LogoutHandler().logoutUser(req, res));
        Spark.get("/game", (req, res) -> new ListGamesHandler().listGames(req, res));
        Spark.post("/game", (req, res) -> new CreateGameHandler().createGame(req, res));
        Spark.put("/game", (req, res) -> new JoinGameHandler().joinGame(req, res));
        Spark.delete("/db", (req, res) -> new ClearHandler().clearDB(req, res));



        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
