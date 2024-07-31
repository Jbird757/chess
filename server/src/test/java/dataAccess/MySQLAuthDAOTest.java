package dataAccess;

import dataaccess.DataAccessException;
import dataaccess.MySQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MySQLAuthDAOTest {

    @Test
    void createDB() {
        Assertions.assertDoesNotThrow(() -> {
            MySQLAuthDAO dao = new MySQLAuthDAO();
        });
        System.out.println("Successfully created/connected to Database");
    }

    @Test
    void getAndSetAuth() {
        AuthData newAuth = new AuthData("helloThere", "123456");
        Assertions.assertDoesNotThrow(() -> {
            MySQLAuthDAO dao = new MySQLAuthDAO();
            dao.clearAuthDB();
            dao.createAuth(newAuth);
            AuthData authInDB = dao.getAuth(newAuth.authToken());
            Assertions.assertEquals(newAuth.authToken(), authInDB.authToken());
            dao.deleteAuth(newAuth.authToken());
            AuthData authNotInDB = dao.getAuth(newAuth.authToken());
            Assertions.assertNull(authNotInDB);
        });
    }

}