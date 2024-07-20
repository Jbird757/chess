package dataaccess;

import model.UserData;

public interface UserDAO {
    public UserData getUser(String username);
    public UserData createUser(UserData user) throws DataAccessException;
    public void clearUserDB();
}
