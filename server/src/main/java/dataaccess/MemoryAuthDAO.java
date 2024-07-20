package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;

public class MemoryAuthDAO implements AuthDAO {
    private static List<AuthData> auths = new ArrayList<>();

    @Override
    public AuthData createAuth(AuthData auth) {
        if (auths.contains(auth)) {
            return null;
        } else {
            auths.add(auth);
        }
        return auth;
    }

    @Override
    public void clearAuthDB() {
        auths.clear();
    }
}
