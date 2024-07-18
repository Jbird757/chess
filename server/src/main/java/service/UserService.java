package service;

import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    public AuthData registerUser(UserData user) {
        //Find user in User DB (should return null)
        //Create user in User DB
        //Create auth for user
        return null;
    }

    public void cleaUserDB() {
        MemoryUserDAO dao = new MemoryUserDAO();
        dao.clearUserDB();
    }
}
