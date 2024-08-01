package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class MySQLAuthDAOTest {

    @Test
    void createDB() {
        Assertions.assertDoesNotThrow(() -> {
            MySQLAuthDAO dao = new MySQLAuthDAO();
        });
        System.out.println("Successfully created/connected to Database");
    }

    @Test
    void setAuthPositive() {
        AuthData newAuth = new AuthData("helloThere", "123456");
        Assertions.assertDoesNotThrow(() -> {
            MySQLAuthDAO dao = new MySQLAuthDAO();
            dao.clearAuthDB();
            dao.createAuth(newAuth);
            var statement = "SELECT * FROM authdata WHERE authToken = ?";
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setString(1, newAuth.authToken());
            var rs = ps.executeQuery();
            if (rs.next()) {
                Assertions.assertEquals(newAuth.authToken(), rs.getString("authToken"));
            }
        });
    }

    @Test
    void setAuthNegative() {
        AuthData newAuth = new AuthData(null, null);
        Assertions.assertThrows(DataAccessException.class, () -> {
            MySQLAuthDAO dao = new MySQLAuthDAO();
            dao.clearAuthDB();
            dao.createAuth(newAuth);
            var statement = "SELECT * FROM authdata WHERE authToken = ?";
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setString(1, newAuth.authToken());
            var rs = ps.executeQuery();
        });
    }

    @Test
    void getAuthPositive() {
        var statement = "INSERT INTO authdata VALUES (?, ?)";
        String authToken = "123456";
        Assertions.assertDoesNotThrow(() -> {
            MySQLAuthDAO dao = new MySQLAuthDAO();
            dao.clearAuthDB();
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setString(1, "helloThere");
            ps.setString(2, authToken);
            ps.executeUpdate();
            dao.getAuth(authToken);
        });
    }

    @Test
    void getAuthNegative() {
        var statement = "INSERT INTO authdata VALUES (?, ?)";
        String authToken = "123456";
        Assertions.assertDoesNotThrow(() -> {
            MySQLAuthDAO dao = new MySQLAuthDAO();
            dao.clearAuthDB();
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setString(1, "helloThere");
            ps.setString(2, authToken);
            ps.executeUpdate();
            AuthData authData = dao.getAuth("11111");
            Assertions.assertNull(authData);
        });
    }

    @Test
    void deleteAuthPositive() {
        var statement = "INSERT INTO authdata VALUES (?, ?)";
        String authToken = "123456";
        Assertions.assertDoesNotThrow(() -> {
            MySQLAuthDAO dao = new MySQLAuthDAO();
            dao.clearAuthDB();
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setString(1, "helloThere");
            ps.setString(2, authToken);
            ps.executeUpdate();
            dao.deleteAuth(authToken);
            AuthData authData = dao.getAuth(authToken);
            Assertions.assertNull(authData);
        });
    }

    @Test
    void deleteAuthNegative() {
        var statement = "INSERT INTO authdata VALUES (?, ?)";
        String authToken = "123456";
        Assertions.assertDoesNotThrow(() -> {
            MySQLAuthDAO dao = new MySQLAuthDAO();
            dao.clearAuthDB();
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setString(1, "helloThere");
            ps.setString(2, authToken);
            ps.executeUpdate();
            dao.deleteAuth("1111");
        });
    }

    @Test
    void clearAuthDB() {
        AuthData newAuth = new AuthData("helloThere", "123456");
        AuthData newAuth2 = new AuthData("helloThere", "12345678");
        AuthData newAuth3 = new AuthData("helloThere", "123456234234");
        var auths = new ArrayList<AuthData>();
        var statement = "SELECT * FROM authdata";

        Assertions.assertDoesNotThrow(() -> {
            MySQLAuthDAO dao = new MySQLAuthDAO();
            dao.clearAuthDB();
            dao.createAuth(newAuth);
            dao.createAuth(newAuth2);
            dao.createAuth(newAuth3);
            dao.clearAuthDB();
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery();
            while (rs.next()) {
                auths.add (new AuthData(rs.getString(1), rs.getString(2)));
            }
            Assertions.assertTrue(auths.isEmpty());
        });
    }
}