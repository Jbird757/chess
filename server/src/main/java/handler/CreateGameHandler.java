package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.Exceptions.BadRequestException;
import dataaccess.Exceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Set;

public class CreateGameHandler {

    public Object createGame(Request req, Response res) {
        GameService gameService = new GameService();
        var game = new Gson().fromJson(req.body(), GameData.class);
        String authToken = req.headers("Authorization");

        try {
            game = gameService.createGame(game, authToken);
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
        return new Gson().toJson(game);

    }
}
