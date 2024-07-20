package server;

import handler.ClearHandler;
import handler.LoginHandler;
import handler.RegisterHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> new RegisterHandler().registerUser(req, res));
        //Spark.post("/session", (req, res) -> new LoginHandler().loginUser(res, req));
        Spark.delete("/db", ((req, res) -> new ClearHandler().clearDB(req, res)));


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
