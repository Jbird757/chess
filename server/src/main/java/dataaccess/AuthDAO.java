package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    public AuthData createAuth(AuthData auth);
    public AuthData getAuth(String authToken);
    public void deleteAuth(String authToken);
    public void clearAuthDB();
}
