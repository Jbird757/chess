package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    public AuthData createAuth(AuthData auth) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void clearAuthDB() throws DataAccessException;
}
