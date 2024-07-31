package service;

import dataaccess.DataAccessException;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.MySQLAuthDAO;
import dataaccess.MySQLUserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    public AuthData registerUser(UserData user) throws DataAccessException {
        MySQLUserDAO userDAO = new MySQLUserDAO();
        MySQLAuthDAO authDAO = new MySQLAuthDAO();

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

        MySQLUserDAO userDAO = new MySQLUserDAO();
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        UserData loginUser = userDAO.getUser(user.username());
        if (loginUser == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        if (loginUser.password().equals(user.password())) {
            String authToken = UUID.randomUUID().toString();
            AuthData auth = new AuthData(user.username(), authToken);
            AuthData returnedAuth = authDAO.createAuth(auth);
            return returnedAuth;
        }

        throw new UnauthorizedException("Error: unauthorized");
    }

    public void logoutUser(AuthData auth) throws DataAccessException {
        if (auth.authToken() == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        AuthData authy = authDAO.getAuth(auth.authToken());

        if (authy == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        authDAO.deleteAuth(auth.authToken());
    }


}
