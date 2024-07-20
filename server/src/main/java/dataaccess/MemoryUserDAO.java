package dataaccess;

import com.google.gson.Gson;
import dataaccess.Exceptions.AlreadyTakenException;
import dataaccess.Exceptions.BadRequestException;
import model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    private static List<UserData> users = new ArrayList<>();

    @Override
    public UserData getUser(String username) {
        for (UserData userData : users) {
            if (Objects.equals(username, userData.username())) {
                return userData;
            }
        }
        return null;
    }

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        users.add(user);
        return user;
    }

    @Override
    public void clearUserDB() {
        users.clear();
    }
}
