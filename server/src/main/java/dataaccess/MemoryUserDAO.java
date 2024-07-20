package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO {
    private static List<UserData> users = new ArrayList<>();

    @Override
    public UserData getUser(UserData user) {
        for (UserData userData : users) {
            if (user.equals(userData)) {
                return userData;
            }
        }
        return null;
    }

    @Override
    public void addUser(UserData user) throws DataAccessException {
        if (users.contains(user)) {
            throw new DataAccessException("Error: already taken");
        } else {
            users.add(user);
        }
    }

    @Override
    public void clearUserDB() {
        users.clear();
    }
}
