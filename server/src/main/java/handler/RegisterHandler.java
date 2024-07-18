package handler;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    public Object registerUser(Request req, Response res) {
        UserService userService = new UserService();
        var user = new Gson().fromJson(req.body(), UserData.class);
        AuthData userAuth = userService.registerUser(user);

        return new Gson().toJson(userAuth);
    }
}
