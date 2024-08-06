package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.UnauthorizedException;
import model.JoinGameModel;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {

    public Object joinGame(Request req, Response res) {
        GameService gameService = new GameService();
        var joinGame = new Gson().fromJson(req.body(), JoinGameModel.class);
        String authToken = req.headers("Authorization");

        try {
            gameService.joinGame(joinGame, authToken);
        } catch (BadRequestException e) {
            res.status(400);
            ErrorMessage message = new ErrorMessage(e.getMessage());
            return new Gson().toJson(message);
        } catch (UnauthorizedException e) {
            res.status(401);
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
        res.body("{}");
        return res.body();
    }
}
