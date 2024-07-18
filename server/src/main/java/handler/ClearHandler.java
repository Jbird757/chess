package handler;

import service.ClearService;
import service.UserService;
import service.GameService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public Object clearDB(Request req, Response res) {
        UserService userService = new UserService();
        GameService gameService = new GameService();
        ClearService clearService = new ClearService();
        userService.cleaUserDB();
        gameService.clearGameDB();
        res.status(200);
        res.body("{}");
        return res.body();
    }
}
