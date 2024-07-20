package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    public AuthData createAuth(AuthData auth);
    public void clearAuthDB();
}
