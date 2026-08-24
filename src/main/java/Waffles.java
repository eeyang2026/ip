import java.util.Scanner;

/**
 * The entry point for the Waffles chatbot.
 */
public class Waffles {
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        String banner = " __        __    _      _____ _____ _      _____  ____\n"
                + " \\ \\      / /   / \\    |  ___|  ___| |    | ____|/ ___|\n"
                + "  \\ \\ /\\ / /   / _ \\   | |_  | |_  | |    |  _|  \\___ \\ \n"
                + "   \\ V  V /   / ___ \\  |  _| |  _| | |___ | |___  ___) |\n"
                + "    \\_/\\_/   /_/   \\_\\ |_|   |_|   |_____||_____||____/";

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm Waffles.");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        String[] storedMessages = new String[100];
        int messageCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("Until next time, Waffleeeeeeeees out");
                break;
            } else if (command.equals("list")) {
                for (int i = 0; i < messageCount; i++) {
                    System.out.println((i + 1) + ". " + storedMessages[i]);
                }
            } else if (messageCount < storedMessages.length) {
                storedMessages[messageCount] = command;
                messageCount++;
                System.out.println("Added: " + command);
            } else {
                System.out.println("Message storage is full.");
            }
        }

        System.out.println(separator);
    }
}
