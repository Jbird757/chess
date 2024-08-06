package dataaccess;

import Exceptions.DataAccessException;
import model.UserData;

public interface UserDAO {
    public UserData getUser(String username) throws DataAccessException;
    public void createUser(UserData user) throws DataAccessException;
    public void clearUserDB() throws DataAccessException;
}
