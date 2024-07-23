package service;

import dataaccess.DataAccessException;
import dataaccess.Exceptions.AlreadyTakenException;
import dataaccess.Exceptions.BadRequestException;
import dataaccess.Exceptions.UnauthorizedException;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import handler.JoinGameModel;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameService {

    public List<GameData> getAllGames(String authToken) throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();

        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        AuthData authy = authDAO.getAuth(authToken);

        if (authy == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return gameDAO.getAllGames();
    }

    public GameData createGame(GameData gameData, String authToken) throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();

        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        } else if (gameData == null) {
            throw new BadRequestException("Error: bad request");
        }

        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        AuthData authy = authDAO.getAuth(authToken);

        if (authy == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return gameDAO.createGame(gameData);
    }

    public void joinGame(JoinGameModel gameData, String authToken) throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();

        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        } else if (!Objects.equals(gameData.playerColor(), "BLACK") && !Objects.equals(gameData.playerColor(), "WHITE")) {
            throw new BadRequestException("Error: bad request");
        }

        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        AuthData authy = authDAO.getAuth(authToken);

        if (authy == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        GameData game = gameDAO.getGame(gameData.gameID());
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }

        if (gameData.playerColor().equals("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            GameData updatedGame = new GameData(game.gameID(), authy.username(), game.blackUsername(), game.gameName(), game.game());
            gameDAO.updateGame(updatedGame);
        } else if (gameData.playerColor().equals("BLACK")) {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), authy.username(), game.gameName(), game.game());
            gameDAO.updateGame(updatedGame);
        }
    }
}
