package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    public Object registerUser(Request req, Response res) {
        UserService userService = new UserService();
        var user = new Gson().fromJson(req.body(), UserData.class);
        AuthData userAuth = new AuthData(null, null);
        try {
            userAuth = userService.registerUser(user);
        } catch (DataAccessException e) {
            res.status(403);
        }

        res.status(200);
        return new Gson().toJson(userAuth);
    }
}
