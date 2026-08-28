import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * The entry point for the Waffles chatbot.
 */
public class Waffles {
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

        ArrayList<Task> tasks = TaskStorage.loadTasks();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();

            try {
                if (command.equals("bye")) {
                    System.out.println("Until next time, Waffleeeeeeeees out");
                    break;
                } else if (command.equals("list")) {
                    printTaskList(tasks);
                } else if (isMarkCommand(command)) {
                    handleMarkCommand(command, tasks);
                } else if (isDeleteCommand(command)) {
                    handleDeleteCommand(command, tasks);
                } else if (isTaskCommand(command)) {
                    Task newTask = createTask(command);
                    tasks.add(newTask);
                    TaskStorage.saveTasks(tasks);
                    printTaskAdded(newTask, tasks.size());
                } else {
                    throw new IllegalArgumentException(
                            "I don't recognise that command. Try todo, deadline, event, list, "
                                    + "mark, unmark, delete, or bye.");
                }
            } catch (IllegalArgumentException exception) {
                printError(exception.getMessage());
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
        return parts.length > 0 && (parts[0].equals("mark") || parts[0].equals("unmark"));
    }

    /**
     * Determines whether a command deletes a task.
     *
     * @param command the command entered by the user
     * @return whether the command starts with {@code delete}
     */
    private static boolean isDeleteCommand(String command) {
        String[] parts = command.split("\\s+");
        return parts.length > 0 && parts[0].equals("delete");
    }

    /**
     * Determines whether a command is intended to create a task.
     *
     * @param command the command entered by the user
     * @return whether the command starts with a supported task type
     */
    private static boolean isTaskCommand(String command) {
        return command.equals("todo") || command.startsWith("todo ")
                || command.equals("deadline") || command.startsWith("deadline ")
                || command.equals("event") || command.startsWith("event ");
    }

    /**
     * Prints all tasks and their current type and completion status.
     *
     * @param tasks the polymorphic task storage
     */
    private static void printTaskList(ArrayList<Task> tasks) {
        System.out.println(SEPARATOR);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(SEPARATOR);
    }

    /**
     * Marks or unmarks a task selected by its one-based list number.
     *
     * @param command the mark or unmark command
     * @param tasks the polymorphic task storage
     */
    private static void handleMarkCommand(String command, ArrayList<Task> tasks) {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "use " + parts[0] + " followed by a task number, like " + parts[0] + " 1.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "that task number looks odd. Use a number, like " + parts[0] + " 1.");
        }

        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new IllegalArgumentException(
                    "that task number is out of range. Pick a number from 1 to " + tasks.size() + ".");
        }

        boolean shouldMarkAsDone = parts[0].equals("mark");
        if (shouldMarkAsDone) {
            tasks.get(taskIndex).markAsDone();
        } else {
            tasks.get(taskIndex).markAsNotDone();
        }
        TaskStorage.saveTasks(tasks);

        System.out.println(SEPARATOR);
        if (shouldMarkAsDone) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + tasks.get(taskIndex));
        System.out.println(SEPARATOR);
    }

    /**
     * Deletes a task selected by its one-based list number.
     *
     * @param command the delete command
     * @param tasks the polymorphic task storage
     */
    private static void handleDeleteCommand(String command, ArrayList<Task> tasks) {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "use delete followed by a task number, like delete 1.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "that task number looks odd. Use a number, like delete 1.");
        }

        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new IllegalArgumentException(
                    "that task number is out of range. Pick a number from 1 to " + tasks.size() + ".");
        }

        Task removedTask = tasks.remove(taskIndex);
        TaskStorage.saveTasks(tasks);
        System.out.println(SEPARATOR);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(SEPARATOR);
    }

    /**
     * Converts a task command into the appropriate subclass.
     *
     * @param command the task command entered by the user
     * @return a todo, deadline, or event task
     */
    private static Task createTask(String command) {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new IllegalArgumentException(
                        "a todo needs a description. Try `todo something to do`.");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            return createDeadline(command);
        }

        if (command.equals("event") || command.startsWith("event ")) {
            return createEvent(command);
        }

        throw new IllegalArgumentException(
                "I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
    }

    /**
     * Parses a deadline command using its {@code /by} marker.
     *
     * @param command the deadline command
     * @return the parsed deadline
     */
    private static Deadline createDeadline(String command) {
        String content = command.substring("deadline".length()).trim();
        int byMarker = content.indexOf("/by");
        if (byMarker < 0) {
            throw new IllegalArgumentException(
                    "a deadline needs a due time after `/by`, like `deadline report /by Friday`.");
        }

        String description = content.substring(0, byMarker).trim();
        String byText = content.substring(byMarker + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new IllegalArgumentException("a deadline needs a description before `/by`.");
        }
        if (byText.isEmpty()) {
            throw new IllegalArgumentException("a deadline needs something after `/by`.");
        }
        return new Deadline(description, parseDate(byText, "deadline"));
    }

    /**
     * Parses an event command using its {@code /from} and {@code /to} markers.
     *
     * @param command the event command
     * @return the parsed event
     */
    private static Event createEvent(String command) {
        String content = command.substring("event".length()).trim();
        int fromMarker = content.indexOf("/from");
        int toMarker = content.indexOf("/to", fromMarker + 1);
        if (fromMarker < 0 || toMarker < 0) {
            throw new IllegalArgumentException(
                    "an event needs both `/from` and `/to`, like `event meeting /from 2pm /to 4pm`.");
        }

        String description = content.substring(0, fromMarker).trim();
        String fromText = content.substring(fromMarker + "/from".length(), toMarker).trim();
        String toText = content.substring(toMarker + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new IllegalArgumentException("an event needs a description before `/from`.");
        }
        if (fromText.isEmpty()) {
            throw new IllegalArgumentException("an event needs something after `/from`.");
        }
        if (toText.isEmpty()) {
            throw new IllegalArgumentException("an event needs something after `/to`.");
        }
        return new Event(description, parseDate(fromText, "event"), parseDate(toText, "event"));
    }

    /**
     * Parses an ISO-8601 date entered in a command.
     *
     * @param value the date text entered by the user
     * @param taskType the task type used in the error message
     * @return the parsed date
     */
    private static LocalDate parseDate(String value, String taskType) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            String article = taskType.equals("event") ? "an " : "a ";
            throw new IllegalArgumentException(
                    article + taskType + " date must use yyyy-MM-dd, like 2019-10-15.");
        }
    }

    /**
     * Prints an informal error message without changing the task list.
     *
     * @param message the specific problem and correction hint
     */
    private static void printError(String message) {
        System.out.println(SEPARATOR);
        System.out.println("Oops, " + message);
        System.out.println(SEPARATOR);
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
