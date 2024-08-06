package dataaccess;

import Exceptions.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class MySQLUserDAOTest {

    @Test
    void createDB() {
        Assertions.assertDoesNotThrow(() -> {
            MySQLUserDAO dao = new MySQLUserDAO();
        });
        System.out.println("Successfully created Database");
    }

    @Test
    void setUserPositive() {
        UserData newUser = new UserData("helloThere", "123456", "myemail");
        Assertions.assertDoesNotThrow(() -> {
            MySQLUserDAO dao = new MySQLUserDAO();
            dao.clearUserDB();
            dao.createUser(newUser);
            var statement = "SELECT * FROM userdata WHERE username = ?";
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setString(1, newUser.username());
            var rs = ps.executeQuery();
            if (rs.next()) {
                Assertions.assertEquals(newUser.username(), rs.getString("username"));
            }
        });
    }

    @Test
    void setUserNegative() {
        UserData newUser = new UserData(null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> {
            MySQLUserDAO dao = new MySQLUserDAO();
            dao.clearUserDB();
            dao.createUser(newUser);
            var statement = "SELECT * FROM Userdata WHERE username = ?";
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setString(1, newUser.username());
            var rs = ps.executeQuery();
            if (rs.next()) {
                Assertions.assertEquals(newUser.username(), rs.getString("username"));
            }
        });
    }

    @Test
    void getUserPositive() {
        var statement = "INSERT INTO userdata VALUES (?, ?, ?)";
        String username = "helloThere";
        Assertions.assertDoesNotThrow(() -> {
            MySQLUserDAO dao = new MySQLUserDAO();
            dao.clearUserDB();
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setString(1, "helloThere");
            ps.setString(2, username);
            ps.setString(3, "myemail");
            ps.executeUpdate();
            dao.getUser(username);
        });
    }

    @Test
    void getUserNegative() {
        var statement = "INSERT INTO Userdata VALUES (?, ?, ?)";
        String username = "123456";
        Assertions.assertDoesNotThrow(() -> {
            MySQLUserDAO dao = new MySQLUserDAO();
            dao.clearUserDB();
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            ps.setString(1, "helloThere");
            ps.setString(2, username);
            ps.setString(3, "myemail");
            ps.executeUpdate();
            UserData userData = dao.getUser("11111");
            Assertions.assertNull(userData);
        });
    }

    @Test
    void clearUserDB() {
        UserData newUser = new UserData("helloThere", "123456", "myemail1");
        UserData newUser2 = new UserData("helloThere2", "12345678", "myemail1");
        UserData newUser3 = new UserData("helloThere3", "123456234234", "myemail1");
        var users = new ArrayList<UserData>();
        var statement = "SELECT * FROM Userdata";

        Assertions.assertDoesNotThrow(() -> {
            MySQLUserDAO dao = new MySQLUserDAO();
            dao.clearUserDB();
            dao.createUser(newUser);
            dao.createUser(newUser2);
            dao.createUser(newUser3);
            dao.clearUserDB();
            var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery();
            while (rs.next()) {
                users.add (new UserData(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
            Assertions.assertTrue(users.isEmpty());
        });
    }

}