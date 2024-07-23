package service;

import dataaccess.DataAccessException;
import dataaccess.Exceptions.BadRequestException;
import dataaccess.Exceptions.UnauthorizedException;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;

public class GameService {

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
}
