package dataAccess;

import dataaccess.MySQLGameDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MySQLGameDAOTest {

    @Test
    void createDB() {
        Assertions.assertDoesNotThrow(() -> {
            MySQLGameDAO dao = new MySQLGameDAO();
        });
        System.out.println("Successfully created Database");
    }

}