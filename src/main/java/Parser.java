import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Interprets user commands and creates the corresponding task objects.
 */
public class Parser {
    /**
     * Determines whether a command marks or unmarks a task.
     *
     * @param command the command entered by the user
     * @return whether the command is a mark or unmark command
     */
    public boolean isMarkCommand(String command) {
        String[] parts = command.split("\\s+");
        return parts.length > 0 && (parts[0].equals("mark") || parts[0].equals("unmark"));
    }

    /**
     * Determines whether a command deletes a task.
     *
     * @param command the command entered by the user
     * @return whether the command starts with {@code delete}
     */
    public boolean isDeleteCommand(String command) {
        String[] parts = command.split("\\s+");
        return parts.length > 0 && parts[0].equals("delete");
    }

    /**
     * Determines whether a command creates a supported task.
     *
     * @param command the command entered by the user
     * @return whether the command starts with a task type
     */
    public boolean isTaskCommand(String command) {
        return command.equals("todo") || command.startsWith("todo ")
                || command.equals("deadline") || command.startsWith("deadline ")
                || command.equals("event") || command.startsWith("event ");
    }

    /**
     * Parses a task command into a task object.
     *
     * @param command the task command entered by the user
     * @return the parsed task
     */
    public Task parseTask(String command) {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new IllegalArgumentException(
                        "a todo needs a description. Try `todo something to do`.");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            return parseDeadline(command);
        }

        if (command.equals("event") || command.startsWith("event ")) {
            return parseEvent(command);
        }

        throw new IllegalArgumentException(
                "I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
    }

    /**
     * Parses the one-based task number from a mark, unmark, or delete command.
     *
     * @param command the command containing a task number
     * @return the one-based task number
     */
    public int parseTaskNumber(String command) {
        String[] parts = command.split("\\s+");
        String action = parts[0];
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "use " + action + " followed by a task number, like " + action + " 1.");
        }

        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "that task number looks odd. Use a number, like " + action + " 1.");
        }
    }

    /** Parses a deadline command using its {@code /by} marker. */
    private Deadline parseDeadline(String command) {
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

    /** Parses an event command using its {@code /from} and {@code /to} markers. */
    private Event parseEvent(String command) {
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

    /** Parses one ISO-8601 date and converts parser failures into user errors. */
    private LocalDate parseDate(String value, String taskType) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            String article = taskType.equals("event") ? "an " : "a ";
            throw new IllegalArgumentException(
                    article + taskType + " date must use yyyy-MM-dd, like 2019-10-15.");
        }
    }
}
