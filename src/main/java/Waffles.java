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
        boolean[] completedMessages = new boolean[storedMessages.length];
        int messageCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            String trimmedCommand = command.trim();
            String[] commandParts = trimmedCommand.isEmpty()
                    ? new String[0]
                    : trimmedCommand.split("\\s+");

            if (trimmedCommand.equals("bye")) {
                System.out.println("Until next time, Waffleeeeeeeees out");
                break;
            } else if (trimmedCommand.equals("list")) {
                System.out.println(separator);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < messageCount; i++) {
                    String status = completedMessages[i] ? "[X]" : "[ ]";
                    System.out.println((i + 1) + "." + status + " " + storedMessages[i]);
                }
                System.out.println(separator);
            } else if (commandParts.length == 2
                    && (commandParts[0].equals("mark") || commandParts[0].equals("unmark"))) {
                try {
                    int taskNumber = Integer.parseInt(commandParts[1]);
                    int taskIndex = taskNumber - 1;

                    if (taskIndex < 0 || taskIndex >= messageCount) {
                        System.out.println("Task number must refer to a task in the list.");
                        continue;
                    }

                    boolean shouldMarkAsDone = commandParts[0].equals("mark");
                    completedMessages[taskIndex] = shouldMarkAsDone;

                    System.out.println(separator);
                    if (shouldMarkAsDone) {
                        System.out.println("Nice! I've marked this task as done:");
                    } else {
                        System.out.println("OK, I've marked this task as not done yet:");
                    }
                    String status = shouldMarkAsDone ? "[X]" : "[ ]";
                    System.out.println("  " + status + " " + storedMessages[taskIndex]);
                    System.out.println(separator);
                } catch (NumberFormatException exception) {
                    System.out.println("Task number must be a valid number.");
                }
            } else if (messageCount < storedMessages.length) {
                storedMessages[messageCount] = command;
                completedMessages[messageCount] = false;
                messageCount++;
                System.out.println("Added: " + command);
            } else {
                System.out.println("Message storage is full.");
            }
        }

        System.out.println(separator);
    }
}
