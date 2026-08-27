import java.util.Scanner;

/**
 * The entry point for the Waffles chatbot.
 */
public class Waffles {
    private static final int MAX_TASKS = 100;
    private static final String SEPARATOR = "____________________________________________________________";

    /**
     * Starts Waffles and processes commands until the user says goodbye.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = " __        __    _      _____ _____ _      _____  ____\n"
                + " \\ \\      / /   / \\    |  ___|  ___| |    | ____|/ ___|\n"
                + "  \\ \\ /\\ / /   / _ \\   | |_  | |_  | |    |  _|  \\___ \\ \n"
                + "   \\ V  V /   / ___ \\  |  _| |  _| | |___ | |___  ___) |\n"
                + "    \\_/\\_/   /_/   \\_\\ |_|   |_|   |_____||_____||____/";

        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println("Hello! I'm Waffles.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();

            if (command.equals("bye")) {
                System.out.println("Until next time, Waffleeeeeeeees out");
                break;
            } else if (command.equals("list")) {
                printTaskList(tasks, taskCount);
            } else if (isMarkCommand(command)) {
                handleMarkCommand(command, tasks, taskCount);
            } else if (taskCount < tasks.length) {
                Task newTask = createTask(command);
                tasks[taskCount] = newTask;
                taskCount++;
                printTaskAdded(newTask, taskCount);
            } else {
                System.out.println("Message storage is full.");
            }
        }

        System.out.println(SEPARATOR);
    }

    /**
     * Determines whether a command marks or unmarks a task.
     *
     * @param command the command entered by the user
     * @return whether the command has the form {@code mark N} or {@code unmark N}
     */
    private static boolean isMarkCommand(String command) {
        String[] parts = command.split("\\s+");
        return parts.length == 2 && (parts[0].equals("mark") || parts[0].equals("unmark"));
    }

    /**
     * Prints all tasks and their current type and completion status.
     *
     * @param tasks the polymorphic task storage
     * @param taskCount the number of tasks currently stored
     */
    private static void printTaskList(Task[] tasks, int taskCount) {
        System.out.println(SEPARATOR);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
        System.out.println(SEPARATOR);
    }

    /**
     * Marks or unmarks a task selected by its one-based list number.
     *
     * @param command the mark or unmark command
     * @param tasks the polymorphic task storage
     * @param taskCount the number of tasks currently stored
     */
    private static void handleMarkCommand(String command, Task[] tasks, int taskCount) {
        String[] parts = command.split("\\s+");
        try {
            int taskNumber = Integer.parseInt(parts[1]);
            int taskIndex = taskNumber - 1;

            if (taskIndex < 0 || taskIndex >= taskCount) {
                System.out.println("Task number must refer to a task in the list.");
                return;
            }

            boolean shouldMarkAsDone = parts[0].equals("mark");
            if (shouldMarkAsDone) {
                tasks[taskIndex].markAsDone();
            } else {
                tasks[taskIndex].markAsNotDone();
            }

            System.out.println(SEPARATOR);
            if (shouldMarkAsDone) {
                System.out.println("Nice! I've marked this task as done:");
            } else {
                System.out.println("OK, I've marked this task as not done yet:");
            }
            System.out.println("  " + tasks[taskIndex]);
            System.out.println(SEPARATOR);
        } catch (NumberFormatException exception) {
            System.out.println("Task number must be a valid number.");
        }
    }

    /**
     * Converts a task command into the appropriate subclass.
     *
     * @param command the task command entered by the user
     * @return a todo, deadline, or event task
     */
    private static Task createTask(String command) {
        if (command.startsWith("todo ")) {
            return new Todo(command.substring("todo ".length()).trim());
        }

        if (command.startsWith("deadline ")) {
            return createDeadline(command);
        }

        if (command.startsWith("event ")) {
            return createEvent(command);
        }

        // Preserve the earlier behavior where ordinary input is stored as a task.
        return new Todo(command);
    }

    /**
     * Parses a deadline command using its {@code /by} marker.
     *
     * @param command the deadline command
     * @return the parsed deadline
     */
    private static Deadline createDeadline(String command) {
        String content = command.substring("deadline ".length()).trim();
        int byMarker = content.indexOf("/by");
        if (byMarker < 0) {
            return new Deadline(content, "");
        }

        String description = content.substring(0, byMarker).trim();
        String by = content.substring(byMarker + "/by".length()).trim();
        return new Deadline(description, by);
    }

    /**
     * Parses an event command using its {@code /from} and {@code /to} markers.
     *
     * @param command the event command
     * @return the parsed event
     */
    private static Event createEvent(String command) {
        String content = command.substring("event ".length()).trim();
        int fromMarker = content.indexOf("/from");
        int toMarker = content.indexOf("/to", fromMarker + 1);
        if (fromMarker < 0 || toMarker < 0) {
            return new Event(content, "", "");
        }

        String description = content.substring(0, fromMarker).trim();
        String from = content.substring(fromMarker + "/from".length(), toMarker).trim();
        String to = content.substring(toMarker + "/to".length()).trim();
        return new Event(description, from, to);
    }

    /**
     * Prints the confirmation after adding a task.
     *
     * @param task the newly added task
     * @param taskCount the updated task count
     */
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println(SEPARATOR);
        String taskType = task instanceof Event
                ? "event"
                : task instanceof Deadline ? "deadline" : "task";
        System.out.println("Got it. I've added this " + taskType + ":");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(SEPARATOR);
    }
}
