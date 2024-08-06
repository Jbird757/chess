package handler;

import com.google.gson.Gson;
import Exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import model.GameData;
import model.JoinGameModel;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.List;

public class ListGamesHandler {

    public Object listGames(Request req, Response res) {
        GameService gameService = new GameService();
        var joinGame = new Gson().fromJson(req.body(), JoinGameModel.class);
        String authToken = req.headers("Authorization");
        List<GameData> games;
        try {
            games = gameService.getAllGames(authToken);
        } catch (UnauthorizedException e) {
            res.status(401);
            ErrorMessage message = new ErrorMessage(e.getMessage());
            return new Gson().toJson(message);
        } catch (DataAccessException e) {
            res.status(500);
            ErrorMessage message = new ErrorMessage(e.getMessage());
            return new Gson().toJson(message);
        }

        GamesListModel gamesListModel = new GamesListModel(games);
        res.status(200);
        return new Gson().toJson(gamesListModel);
    }
}
