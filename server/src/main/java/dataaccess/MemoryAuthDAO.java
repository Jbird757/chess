package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {
    private static List<AuthData> auths = new ArrayList<>();

    @Override
    public AuthData createAuth(AuthData auth) {
        auths.add(auth);
        return auth;
    }

    @Override
    public AuthData getAuth(String authToken) {
        for (AuthData auth : auths) {
            if (Objects.equals(auth.authToken(), authToken)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.removeIf(auth -> Objects.equals(auth.authToken(), authToken));
    }

    @Override
    public void clearAuthDB() {
        auths.clear();
    }
}
