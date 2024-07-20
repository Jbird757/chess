package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    public AuthData registerUser(UserData user) throws DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        //Find user in User DB (should return null)
        UserData returnedUser = userDAO.getUser(user);
        if (returnedUser != null) {
            throw new DataAccessException("Error: already taken");
        }

        //Create user in User DB
        userDAO.addUser(user);

        //Create auth for user
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, user.username());
        return authDAO.createAuth(auth);
    }
}
