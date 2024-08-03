import chess.*;

import java.util.Scanner;

public class Main {
    static String username = "LOGGED_OUT";

    public static void main(String[] args) {
        System.out.println("Welcome to online Chess. Choose one of the options below to get started.");
        printPrelogin();
        preLoginConsole();
    }

    private static void preLoginConsole() {
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {
            System.out.print("["+username+"] >> ");
            String input = scanner.nextLine();
            String[] words = input.split(" ");

            switch (words[0].toLowerCase()) {
                case "register":
                    break;
                case "login":
                    break;
                case "quit":
                    quit = true;
                case "help":
                    printPrelogin();
            }
        }

        scanner.close();
    }

    private static void printPrelogin() {
        System.out.println("\nCOMMANDS:");
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL>\t\tCreates a new account");
        System.out.println("login <USERNAME> <PASSWORD> \t\t\t\tLogs in an existing user");
        System.out.println("quit \t\t\t\t\t\t\t\t\t\tExit Application");
        System.out.println("help \t\t\t\t\t\t\t\t\t\tView these commands at any time");
    }

    private static void printPostlogin() {
        System.out.println("\nCOMMANDS:");
        System.out.println("create <GAMENAME> \t\tCreates a new game with the specified name");
        System.out.println("list \t\t\t\t\tDisplays a list of active games");
        System.out.println("join <ID> \t\t\t\tJoins a game with the specified ID");
        System.out.println("observe <ID> \t\t\tWatch a game with the specified ID");
        System.out.println("logout \t\t\t\t\tLogs out the current user");
        System.out.println("help \t\t\t\t\tView these commands at any time");
    }
}