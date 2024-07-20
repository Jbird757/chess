package handler;

import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public Object clearDB(Request req, Response res) {
        ClearService clearService = new ClearService();
        clearService.clear();
        res.status(200);
        res.body("{}");
        return res.body();
    }
}
