import java.util.Scanner;

public class ChessClient {
    private String username = "LOGGED_OUT";
    private ServerFacade server;

    public ChessClient(String serverURL){
        this.server = new ServerFacade(serverURL);
    }

    public void clientStart() {
        System.out.println("Welcome to online Chess. Choose one of the options below to get started.");
        printPreLoginHelp();
        preLoginConsole();
    }

    private void preLoginConsole() {
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {
            System.out.print("["+username+"] >> ");
            String input = scanner.nextLine();
            String[] words = input.split(" ");

            switch (words[0].toLowerCase()) {
                case "register":
                    if (words.length != 4) {
                        System.out.println("Error: invalid arguments");
                        break;
                    }
                    System.out.print("Registering user...");
                    registerUser(words[1], words[2], words[3]);
                    break;
                case "login":
                    if (words.length != 3) {
                        System.out.println("Error: invalid arguments");
                        break;
                    }
                    System.out.print("Logging in...");
                    login(words[1], words[2]);
                    postLoginConsole();
                    break;
                case "quit":
                    quit = true;
                    break;
                case "help":
                    printPreLoginHelp();
                case "print":
                    printBoard(words[1]);
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
        scanner.close();
    }

    private void postLoginConsole() {
        System.out.print("Welcome "+ username +"!");
        printPostLoginHelp();
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {
            System.out.print("["+username+"] >> ");
            String input = scanner.nextLine();
            String[] words = input.split(" ");

            switch (words[0].toLowerCase()) {
                case "create":
                    if (words.length != 2) {
                        System.out.println("Error: invalid arguments");
                    }
                    createGame(words[1]);
                    break;
                case "list":
                    listGames();
                    break;
                case "join":
                    if (words.length != 3) {
                        System.out.println("Error: invalid arguments");
                    }
                    joinGame(Integer.parseInt(words[1]), words[2]);
                    break;
                case "observe":
                    if (words.length != 2) {
                        System.out.println("Error: invalid arguments");
                    }
                    observeGame(Integer.parseInt(words[1]));
                    break;
                case "logout":
                    logout();
                    quit = true;
                    break;
                case "help":
                    printPostLoginHelp();
                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
        scanner.close();
    }

    private void printBoard(String playerColor) {
        DisplayChessBoard board = new DisplayChessBoard(playerColor);
        board.printBoard();
    }

    private void registerUser(String username, String password, String email) {
        try {
            server.registerUser(username, password, email);
            this.username = username;
        } catch (Exception e) {
            var msg = e.getMessage();
            System.out.println(msg);
        }
    }

    private void login(String username, String password) {
        try {
            server.login(username, password);
            this.username = username;
        } catch (Exception e) {
            var msg = e.getMessage();
            System.out.println(msg);
        }
    }

    private void createGame(String gameName) {
        try {
            server.createGame(gameName);
        } catch (Exception e) {
            var msg = e.getMessage();
            System.out.println(msg);
        }
    }

    private void listGames() {
        try {
            server.listGames();
        } catch (Exception e) {
            var msg = e.getMessage();
            System.out.println(msg);
        }
    }

    private void joinGame(int gameID, String playerColor) {
        try {
            server.joinGame(gameID, playerColor);
        } catch (Exception e) {
            var msg = e.getMessage();
            System.out.println(msg);
        }
    }

    private void observeGame(int gameID) {
        try {
            server.observeGame(gameID);
        } catch (Exception e) {
            var msg = e.getMessage();
            System.out.println(msg);
        }
    }

    private void logout() {
        try {
            server.logout();
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
    }

    private void printPostLoginHelp() {
        System.out.println("\nCOMMANDS:");
        System.out.println("create <GAMENAME> \t\tCreates a new game with the specified name");
        System.out.println("list \t\t\t\t\tDisplays a list of active games");
        System.out.println("join <ID> [WHITE|BLACK] \t\t\tJoins a game with the specified ID");
        System.out.println("observe <ID> \t\t\tWatch a game with the specified ID");
        System.out.println("logout \t\t\t\t\tLogs out the current user");
        System.out.println("help \t\t\t\t\tView these commands at any time");
    }
}
