package service;

import Exceptions.DataAccessException;
import dataaccess.*;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.UnauthorizedException;
import model.JoinGameModel;
import model.AuthData;
import model.GameData;

import java.util.List;
import java.util.Objects;

public class GameService {

    public List<GameData> getAllGames(String authToken) throws DataAccessException {
        GameDAO gameDAO = new MySQLGameDAO();

        //Check if auth exists
        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        //Check if auth is valid
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        AuthData authy = authDAO.getAuth(authToken);

        if (authy == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return gameDAO.getAllGames();
    }

    public GameData createGame(GameData gameData, String authToken) throws DataAccessException {
        GameDAO gameDAO = new MySQLGameDAO();

        //Check input formatting
        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        } else if (gameData == null) {
            throw new BadRequestException("Error: bad request");
        }

        //Check if auth is valid
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        AuthData authy = authDAO.getAuth(authToken);

        if (authy == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return gameDAO.createGame(gameData);
    }

    public void joinGame(JoinGameModel gameData, String authToken) throws DataAccessException {
        GameDAO gameDAO = new MySQLGameDAO();

        //Check input formatting
        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        } else if (!Objects.equals(gameData.playerColor(), "BLACK") && !Objects.equals(gameData.playerColor(), "WHITE")) {
            throw new BadRequestException("Error: bad request");
        }

        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        AuthData authy = authDAO.getAuth(authToken);

        //Check if auth is valid
        if (authy == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        //Check if gameID is valid
        GameData game = gameDAO.getGame(gameData.gameID());
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }

        //Depending on playerColor, check if side is taken, if not, add to game in that color
        if (gameData.playerColor().equals("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }

            GameData updatedGame = new GameData(game.gameID(), authy.username(), game.blackUsername(), game.gameName(), game.game());
            gameDAO.updateGame(updatedGame);
        } else {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }

            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), authy.username(), game.gameName(), game.game());
            gameDAO.updateGame(updatedGame);
        }
    }
}
