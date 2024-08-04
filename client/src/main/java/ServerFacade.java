import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverURL;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    public AuthData registerUser(String username, String Password, String email) throws DataAccessException {
        UserData user = new UserData(username, Password, email);
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public AuthData login(String username, String password) throws DataAccessException {
        UserData user = new UserData(username, password, null);
        var path = "/session";
        System.out.println("You are logged in");
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public void logout() throws DataAccessException {
        var path = "/session";
        System.out.println("You are logged out");
        this.makeRequest("DELETE", path, null, null);
    }

    public GameData[] listGames() throws DataAccessException {
        var path = "/game";
        System.out.println("List of games");
        return null;
    }

    public GameData createGame(String gameName) throws DataAccessException {
        var path = "/game";
        System.out.println("Creating game " + gameName);
        return null;
    }

    public void joinGame(int gameID, String playerColor) throws DataAccessException {
        System.out.println("Joining game " + gameID);
        var path = "/game";
    }

    public GameData observeGame(int gameID) throws DataAccessException {
        var path = "/game";
        return null;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws DataAccessException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new DataAccessException("failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
