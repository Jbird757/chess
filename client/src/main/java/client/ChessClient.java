package client;

import model.AuthData;
import model.GameData;

import java.util.Scanner;

public class ChessClient {
    private String username = "LOGGED_OUT";
    private String authToken = null;
    private ServerFacade server;
    private Scanner scanner;

    public ChessClient(String serverURL){
        this.server = new ServerFacade(serverURL);
        this.scanner = new Scanner(System.in);
    }

    public void clientStart() {
        System.out.println("Welcome to online Chess. Choose one of the options below to get started.");
        printPreLoginHelp();
        preLoginConsole();
    }

    private void preLoginConsole() {
        boolean quit = false;

        while (!quit) {
            System.out.print("["+username+"] >> ");
            String input = scanner.nextLine();
            String[] words = input.split(" ");

            switch (words[0].toLowerCase()) {
                case "register":
                    if (words.length < 4) {
                        System.out.println("Error: not enough arguments provided");
                        break;
                    }
                    System.out.println("Registering user...");
                    registerUser(words[1], words[2], words[3]);
                    break;
                case "login":
                    if (words.length < 3) {
                        System.out.println("Error: not enough arguments provided");
                        break;
                    }
                    System.out.println("Logging in...");
                    login(words[1], words[2]);
                    break;
                case "quit":
                    quit = true;
                    break;
                case "help":
                    printPreLoginHelp();
                    break;
                case "print":
                    printBoard(words[1]);
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }

    private void postLoginConsole() {
        System.out.println("Welcome "+ username +"!");
        printPostLoginHelp();
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {
            System.out.print("["+username+"] >> ");
            String input = scanner.nextLine();
            String[] words = input.split(" ");

            switch (words[0].toLowerCase()) {
                case "create":
                    if (words.length < 2) {
                        System.out.println("Error: not enough arguments provided");
                        break;
                    }
                    createGame(words[1]);
                    break;

                case "list":
                    listGames();
                    break;

                case "join":
                    if (words.length < 3 || (!words[2].equalsIgnoreCase("BLACK") && !words[2].equalsIgnoreCase("WHITE"))) {
                        System.out.println("Error: not enough arguments provided");
                        break;
                    }

                    try {
                        int gameID = Integer.parseInt(words[1]);
                        joinGame(gameID, words[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: game ID must be an integer");
                    }
                    break;

                case "observe":
                    if (words.length < 2) {
                        System.out.println("Error: not enough arguments provided");
                        break;
                    }
                    try {
                        int gameID = Integer.parseInt(words[1]);
                        observeGame(gameID);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: game ID must be an integer");
                    }
                    break;

                case "logout":
                    logout();
                    quit = true;
                    break;

                case "help":
                    printPostLoginHelp();
                    break;

                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }

    private void printBoard(String playerColor) {
        DisplayChessBoard board = new DisplayChessBoard(playerColor);
        board.printBoard();
    }

    private void registerUser(String username, String password, String email) {
        try {
            this.authToken = server.registerUser(username, password, email).authToken();
            this.username = username;
            postLoginConsole();
        } catch (Exception e) {
            if (e.getMessage().equals("403")) {
                System.out.println("Username already taken");
            }
        }
    }

    private void login(String username, String password) {
        try {
            this.authToken = server.login(username, password).authToken();
            this.username = username;
            postLoginConsole();
        } catch (Exception e) {
            if (e.getMessage().equals("403")) {
                System.out.println("Username already taken");
            } else if (e.getMessage().equals("401")) {
                System.out.println("Invalid username or password");
            }
        }
    }

    private void createGame(String gameName) {
        try {
            server.createGame(gameName, this.authToken);
        } catch (Exception e) {
            var msg = e.getMessage();
            System.out.println(msg);
        }
    }

    private void listGames() {
        try {
            GameData[] games = server.listGames(this.authToken);

            if (games.length == 0) {
                System.out.println("No games");
                return;
            }

            System.out.println("\nGame List:");
            System.out.println("Game Number\t\tGame Name\t\tWhite Player Name\tBlack Player Name");

            for (int i = 0; i < games.length; i++) {
                System.out.println((i+1)+"\t\t\t"+games[i].gameName()+"\t\t\t"+games[i].whiteUsername()+"\t\t\t\t"+games[i].blackUsername());
            }

        } catch (Exception e) {
            var msg = e.getMessage();
            System.out.println(msg);
        }
    }

    private void joinGame(int gameID, String playerColor) {
        try {
            server.joinGame(gameID, playerColor.toUpperCase(), this.authToken);
        } catch (Exception e) {
            if (e.getMessage().equals("403")) {
                System.out.println("Color already taken");
            } else {
                System.out.println("Error: game ID does not exist");
            }
        }
    }

    private void observeGame(int gameID) {
        try {
            server.observeGame(gameID, this.authToken);
        } catch (Exception e) {
            var msg = e.getMessage();
            System.out.println(msg);
        }
    }

    private void logout() {
        try {
            server.logout(this.authToken);
            this.username = "LOGGED_OUT";
        } catch (Exception e) {
            var msg = e.getMessage();
            System.out.println(msg);
        }
    }

    private void printPreLoginHelp() {
        System.out.println("\nCOMMANDS:");
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL>\t\tCreates a new account");
        System.out.println("login <USERNAME> <PASSWORD> \t\t\t\tLogs in an existing user");
        System.out.println("quit \t\t\t\t\t\t\t\t\t\tExit Application");
        System.out.println("help \t\t\t\t\t\t\t\t\t\tView these commands at any time");
        System.out.print("\n\n");
    }

    private void printPostLoginHelp() {
        System.out.println("\nCOMMANDS:");
        System.out.println("create <GAMENAME> \t\t\tCreates a new game with the specified name");
        System.out.println("list \t\t\t\t\t\tDisplays a list of active games");
        System.out.println("join <ID> [WHITE|BLACK] \tJoins a game with the specified ID");
        System.out.println("observe <ID> \t\t\t\tWatch a game with the specified ID");
        System.out.println("logout \t\t\t\t\t\tLogs out the current user");
        System.out.println("help \t\t\t\t\t\tView these commands at any time");
        System.out.print("\n\n");
    }
}
