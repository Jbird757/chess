import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverURL;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    public AuthData registerUser(String username, String Password, String email) {
        UserData user = new UserData(username, Password, email);
        var path = "/user";
        return null;
    }

    public AuthData login(String username, String Password) {
        var path = "/session";
        return null;
    }

    public void logout(String authToken) {
        var path = "/session";
    }

    public GameData[] listGames() {
        var path = "/game";
        return null;
    }

    public GameData createGame(String gameName) {
        var path = "/game";
        return null;
    }

    public void joinGame(int gameID, String username, String color) {
        var path = "/game";
    }

//    private <T> T makeRequest(String method, String path, Object request, Class<T> responseType) {
//        try {
//            URL url = (new URI(serverURL + path)).toURL();
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            http.setRequestMethod(method);
//            http.setDoOutput(true);
//
//            writeBody(request, http);
//            http.connect();
//            throwIfNotSuccessful(http);
//            return readBody(http, responseClass);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}
