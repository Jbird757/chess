package dataaccess;

import Exceptions.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    public AuthData createAuth(AuthData auth) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void clearAuthDB() throws DataAccessException;
}
