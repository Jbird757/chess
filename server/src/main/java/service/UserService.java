package service;

import dataaccess.DataAccessException;
import dataaccess.Exceptions.AlreadyTakenException;
import dataaccess.Exceptions.BadRequestException;
import dataaccess.Exceptions.UnauthorizedException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    public AuthData registerUser(UserData user) throws DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestException("Error: bad request");
        }

        //Find user in User DB (should return null)
        UserData returnedUser = userDAO.getUser(user.username());
        if (returnedUser != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        //Create user in User DB
        userDAO.createUser(user);

        //Create auth for user
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(user.username(), authToken);
        return authDAO.createAuth(auth);
    }

    public AuthData loginUser(UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null) {
            throw new DataAccessException("Error: Fields are blank");
        }

        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        UserData loginUser = userDAO.getUser(user.username());
        if (loginUser == null) {
            throw new DataAccessException("Error: user not found");
        }

        if (loginUser.password().equals(user.password())) {
            String authToken = UUID.randomUUID().toString();
            AuthData auth = new AuthData(user.username(), authToken);
            return authDAO.createAuth(auth);
        }

        throw new UnauthorizedException("Error: unauthorized");
    }
}
