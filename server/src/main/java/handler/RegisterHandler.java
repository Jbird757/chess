package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.Exceptions.AlreadyTakenException;
import dataaccess.Exceptions.BadRequestException;
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
        } catch (BadRequestException e) {
            res.status(400);
            return new Gson().toJson("{message: "+e.getMessage()+"}");
        } catch (AlreadyTakenException e) {
            res.status(403);
            return new Gson().toJson("{message: "+e.getMessage()+"}");
        } catch (DataAccessException e) {
            res.status(500);
            return new Gson().toJson("{message: "+e.getMessage()+"}");
        }

        res.status(200);
        return new Gson().toJson(userAuth);
    }
}
