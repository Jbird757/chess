package client;

import chess.ChessGame;
import model.JoinGameModel;
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

    public AuthData registerUser(String username, String password, String email) throws Exception {
        UserData user = new UserData(username, password, email);
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData login(String username, String password) throws Exception {
        UserData user = new UserData(username, password, null);
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public void logout(String authToken) throws Exception {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public GameData createGame(String gameName, String authToken) throws Exception {
        GameData newGame = new GameData(null, null, null, gameName, null);
        var path = "/game";
        return this.makeRequest("POST", path, newGame, GameData.class, authToken);
    }

    public GameData[] listGames(String authToken) throws Exception {
        var path = "/game";
        record ListGames(GameData[] games) {}
        var response = this.makeRequest("GET", path, null, ListGames.class, authToken);
        return response.games();
    }

    public void joinGame(int gameID, String playerColor, String authToken) throws Exception {
        GameData[] games = listGames(authToken);
        if (gameID > games.length) {
            throw new Exception("Game does not exist");
        }
        JoinGameModel joinInfo = new JoinGameModel(playerColor, games[gameID-1].gameID());
        var path = "/game";
        this.makeRequest("PUT", path, joinInfo, null, authToken);

        DisplayChessBoard boardWhite = new DisplayChessBoard("WHITE");
        DisplayChessBoard boardBlack = new DisplayChessBoard("BLACK");

        boardWhite.printBoard();
        System.out.print("\n");
        boardBlack.printBoard();
    }

    public GameData observeGame(int gameID, String authToken) throws Exception {
        DisplayChessBoard boardWhite = new DisplayChessBoard("WHITE");
        DisplayChessBoard boardBlack = new DisplayChessBoard("BLACK");

        boardWhite.printBoard();
        System.out.print("\n");
        boardBlack.printBoard();
        return new GameData(1, "whiteplayer", "blackplayer", "game1", new ChessGame());
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authHeader) throws Exception {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authHeader != null) {
                http.setRequestProperty("Authorization", authHeader);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
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

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        var msg = http.getResponseMessage();
        if (!isSuccessful(status)) {
            //throw new Exception("failure: " + status+"\t"+msg);
            throw new Exception(String.valueOf(status));
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
