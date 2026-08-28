import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Reads and writes Waffles tasks in a small, human-readable text file.
 *
 * <p>Each record uses the format {@code TYPE|STATUS|VALUE...}. A backslash
 * escapes a backslash or a pipe inside a task value.</p>
 */
public final class TaskStorage {
    /** The system property used by tests or alternative launchers to choose a file. */
    private static final String DATA_FILE_PROPERTY = "waffles.data.file";

    /** The default relative location of the saved task list. */
    private static final Path DEFAULT_DATA_FILE = Path.of("data", "waffles.txt");

    private TaskStorage() {
        // Utility class; do not instantiate.
    }

    /**
     * Loads all valid tasks from the data file.
     *
     * <p>A missing file or folder is treated as an empty task list. Malformed
     * records are ignored so one corrupted line does not prevent Waffles from
     * starting or loading the other valid tasks.</p>
     *
     * @return the tasks recovered from disk
     */
    public static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Path dataFile = getDataFile();
        if (!Files.exists(dataFile)) {
            return tasks;
        }

        try (BufferedReader reader = Files.newBufferedReader(dataFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException exception) {
            System.err.println("Warning: I couldn't load the saved tasks. Starting with an empty list.");
        }
        return tasks;
    }

    /**
     * Saves the current task list, creating its parent folder when necessary.
     *
     * @param tasks the tasks to save
     */
    public static void saveTasks(List<Task> tasks) {
        Path dataFile = getDataFile();
        try {
            Path parent = dataFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(
                    dataFile, StandardCharsets.UTF_8)) {
                for (Task task : tasks) {
                    writer.write(formatTask(task));
                    writer.newLine();
                }
            }
        } catch (IOException exception) {
            System.err.println("Warning: I couldn't save the task list to disk.");
        }
    }

    /**
     * Returns the configured data path, or the default path relative to the
     * project directory when no override was supplied.
     *
     * @return the data file path
     */
    private static Path getDataFile() {
        String configuredPath = System.getProperty(DATA_FILE_PROPERTY);
        return configuredPath == null || configuredPath.isBlank()
                ? DEFAULT_DATA_FILE
                : Path.of(configuredPath);
    }

    /**
     * Converts a task into its on-disk record.
     *
     * @param task the task to format
     * @return the serialized task
     */
    private static String formatTask(Task task) {
        String type;
        StringJoiner fields = new StringJoiner("|");
        if (task instanceof Event event) {
            type = "E";
            fields.add(escape(event.getDescription()))
                    .add(escape(event.getFrom()))
                    .add(escape(event.getTo()));
        } else if (task instanceof Deadline deadline) {
            type = "D";
            fields.add(escape(deadline.getDescription()))
                    .add(escape(deadline.getBy()));
        } else {
            type = "T";
            fields.add(escape(task.getDescription()));
        }
        return type + "|" + (task.isDone() ? "1" : "0") + "|" + fields;
    }

    /**
     * Parses one saved record, returning {@code null} for corrupted input.
     *
     * @param line the record to parse
     * @return the parsed task, or {@code null} when the record is invalid
     */
    private static Task parseTask(String line) {
        List<String> fields = splitRecord(line);
        if (fields.size() < 3 || !fields.get(1).equals("0") && !fields.get(1).equals("1")) {
            return null;
        }

        String type = fields.get(0);
        String description = fields.get(2);
        if (description.isBlank()) {
            return null;
        }

        Task task;
        if (type.equals("T") && fields.size() == 3) {
            task = new Todo(description);
        } else if (type.equals("D") && fields.size() == 4 && !fields.get(3).isBlank()) {
            task = new Deadline(description, fields.get(3));
        } else if (type.equals("E") && fields.size() == 5
                && !fields.get(3).isBlank() && !fields.get(4).isBlank()) {
            task = new Event(description, fields.get(3), fields.get(4));
        } else {
            return null;
        }

        if (fields.get(1).equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Splits a record while preserving escaped pipe characters in values.
     *
     * @param line the record to split
     * @return the decoded fields
     */
    private static List<String> splitRecord(String line) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean escaped = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (escaped) {
                if (character == '|' || character == '\\') {
                    field.append(character);
                } else {
                    return List.of();
                }
                escaped = false;
            } else if (character == '\\') {
                escaped = true;
            } else if (character == '|') {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        if (escaped) {
            return List.of();
        }
        fields.add(field.toString());
        return fields;
    }

    /**
     * Escapes characters that have a special meaning in the record format.
     *
     * @param value the task value to escape
     * @return the escaped value
     */
    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|");
    }
}
