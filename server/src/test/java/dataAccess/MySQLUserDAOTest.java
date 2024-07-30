package dataAccess;

import dataaccess.MySQLUserDAO;
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

}