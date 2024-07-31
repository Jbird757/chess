package dataAccess;

import dataaccess.MySQLAuthDAO;
import dataaccess.MySQLUserDAO;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySQLUserDAOTest {

    @Test
    void createDB() {
        Assertions.assertDoesNotThrow(() -> {
            MySQLUserDAO dao = new MySQLUserDAO();
        });
        System.out.println("Successfully created Database");
    }

    @Test
    void test2() {
        UserData newUser = new UserData("hellothere", "12345678", "hello@helo.com");
        Assertions.assertDoesNotThrow(() -> {
            MySQLUserDAO dao = new MySQLUserDAO();
            dao.clearUserDB();
            dao.createUser(newUser);
            UserData authInDB = dao.getUser(newUser.username());
            Assertions.assertEquals(newUser.username(), authInDB.username());
        });
    }

}