import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var serverURL = "http://localhost:8080";
        if (args.length > 0) {
            serverURL = args[0];
        }

        new ChessClient(serverURL).clientStart();
    }
}