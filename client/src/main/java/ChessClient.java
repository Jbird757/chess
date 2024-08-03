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
                        System.out.println("Error: missing arguments");
                        break;
                    }
                    System.out.print("Registering user...");
                    server.registerUser(words[1], words[2], words[3]);
                    break;
                case "login":
                    if (words.length != 3) {
                        System.out.println("Error: missing arguments");
                        break;
                    }
                    System.out.print("Logging in...");
                    postLoginConsole();
                    break;
                case "quit":
                    quit = true;
                    break;
                case "help":
                    printPreLoginHelp();
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
                    break;
                case "list":
                    break;
                case "join":
                    break;
                case "observe":
                    break;
                case "logout":
                    this.username = "LOGGED_OUT";
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
        System.out.println("join <ID> \t\t\t\tJoins a game with the specified ID");
        System.out.println("observe <ID> \t\t\tWatch a game with the specified ID");
        System.out.println("logout \t\t\t\t\tLogs out the current user");
        System.out.println("help \t\t\t\t\tView these commands at any time");
    }
}
