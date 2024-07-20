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

public class LoginHandler {

    public Object loginUser(Request req, Response res) {
        UserService userService = new UserService();
        var user = new Gson().fromJson(req.body(), UserData.class);
        AuthData userAuth;
        Set<String> headers = req.headers();

        try {
            userAuth = userService.loginUser(user);
        } catch (BadRequestException e) {
            res.status(400);
            return new Gson().toJson("{message: "+e.getMessage()+"}");
        } catch (UnauthorizedException e) {
            res.status(401);
            return new Gson().toJson("{message: "+e.getMessage()+"}");
        } catch (DataAccessException e) {
            res.status(500);
            return new Gson().toJson("{message: "+e.getMessage()+"}");
        }

        res.status(200);
        return new Gson().toJson(userAuth);

    }
}
