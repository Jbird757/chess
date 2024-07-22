package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.Exceptions.BadRequestException;
import dataaccess.Exceptions.UnauthorizedException;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Set;

public class LogoutHandler {

    public Object logoutUser(Request req, Response res) {
        UserService userService = new UserService();
        //var user = new Gson().fromJson(req.body(), UserData.class);
        String authToken = req.headers("Authorization");
        AuthData userAuth = new AuthData(null, authToken);

        try {
            userService.logoutUser(userAuth);
        } catch (BadRequestException e) {
            res.status(400);
            ErrorMessage message = new ErrorMessage(e.getMessage());
            return new Gson().toJson(message);
        } catch (UnauthorizedException e) {
            res.status(401);
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
