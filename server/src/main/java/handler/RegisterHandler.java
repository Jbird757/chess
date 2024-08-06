package handler;

import com.google.gson.Gson;
import Exceptions.DataAccessException;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    public Object registerUser(Request req, Response res) {
        UserService userService = new UserService();
        var user = new Gson().fromJson(req.body(), UserData.class);
        AuthData userAuth;

        try {
            userAuth = userService.registerUser(user);
        } catch (BadRequestException e) {
            res.status(400);
            ErrorMessage message = new ErrorMessage(e.getMessage());
            return new Gson().toJson(message);
        } catch (AlreadyTakenException e) {
            res.status(403);
            ErrorMessage message = new ErrorMessage(e.getMessage());
            return new Gson().toJson(message);
        } catch (DataAccessException e) {
            res.status(500);
            ErrorMessage message = new ErrorMessage(e.getMessage());
            return new Gson().toJson(message);
        }

        res.status(200);
        return new Gson().toJson(userAuth);
    }
}
