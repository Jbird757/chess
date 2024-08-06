package service;

import Exceptions.DataAccessException;
import dataaccess.*;

public class ClearService {

    public void clear() throws DataAccessException {
        MySQLUserDAO userDAO = new MySQLUserDAO();
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        userDAO.clearUserDB();
        gameDAO.clearGameDB();
        authDAO.clearAuthDB();
    }
}
