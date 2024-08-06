package handler;

import com.google.gson.Gson;
import Exceptions.DataAccessException;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public Object clearDB(Request req, Response res) {
        ClearService clearService = new ClearService();
        try {
            clearService.clear();
        } catch (DataAccessException e) {
            res.status(500);
            ErrorMessage message = new ErrorMessage(e.getMessage());
            return new Gson().toJson(message);
        }
        res.status(200);
        res.body("{}");
        return res.body();
    }
}
