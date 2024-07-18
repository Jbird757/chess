package dataaccess;

import model.UserData;

public interface UserDAO {
    public UserData getUser(UserData user);
    public void addUser(UserData user) throws DataAccessException;
    public void clearUserDB();
}
