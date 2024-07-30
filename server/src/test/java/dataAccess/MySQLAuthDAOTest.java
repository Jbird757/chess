package dataAccess;

import dataaccess.MySQLAuthDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MySQLAuthDAOTest {

    @Test
    void createDB() {
        Assertions.assertDoesNotThrow(() -> {
            MySQLAuthDAO dao = new MySQLAuthDAO();
        });
        System.out.println("Successfully created Database");
    }

}