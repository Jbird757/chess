package service;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClearServiceTest {

    @Test
    void clearPopulatedDB() {
        UserData newUser = new UserData("newUser1", "byu1234", "newemail@email.com");
        GameData newGame = new GameData(1, null, null, "game1", null);
        AuthData newAuth = new AuthData("newUser1", "newemail@email.com");
        UserService newUserService = new UserService();
        GameService newGameService = new GameService();
        ClearService clearService = new ClearService();
        Assertions.assertDoesNotThrow(clearService::clear);


        try {
            newUserService.registerUser(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            newAuth = newUserService.loginUser(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            newGameService.createGame(newGame, newAuth.authToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assertions.assertDoesNotThrow(clearService::clear);
    }
}