package waffles;

/**
 * The entry point and coordinator for the Waffles chatbot.
 */
public class Waffles {
    /** Handles user interaction. */
    private final Ui ui;

    /** Loads and saves tasks. */
    private final Storage storage;

    /** Interprets commands. */
    private final Parser parser;

    /** Owns the current task collection. */
    private final TaskList tasks;

    /** Creates Waffles with its user interface, parser, storage, and task list. */
    public Waffles() {
        ui = new Ui();
        storage = new Storage();
        parser = new Parser();
        tasks = new TaskList(storage.loadTasks());
    }

    /** Runs the chatbot until the user enters {@code bye} or input ends. */
    public void run() {
        ui.showWelcome();
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            try {
                if (command.equals("bye")) {
                    ui.showGoodbye();
                    break;
                } else if (command.equals("list")) {
                    ui.showTaskList(tasks);
                } else if (parser.isMarkCommand(command)) {
                    handleMarkCommand(command);
                } else if (parser.isDeleteCommand(command)) {
                    handleDeleteCommand(command);
                } else if (parser.isTaskCommand(command)) {
                    Task newTask = parser.parseTask(command);
                    tasks.addTask(newTask);
                    storage.saveTasks(tasks.asList());
                    ui.showTaskAdded(newTask, tasks.size());
                } else {
                    throw new IllegalArgumentException(
                            "I don't recognise that command. Try todo, deadline, event, list, "
                                    + "mark, unmark, delete, or bye.");
                }
            } catch (IllegalArgumentException exception) {
                ui.showError(exception.getMessage());
            }
        }
        ui.showDivider();
    }

    /** Handles a mark or unmark command after parsing its task number. */
    private void handleMarkCommand(String command) {
        int taskNumber = parser.parseTaskNumber(command);
        int taskIndex = taskNumber - 1;
        validateTaskIndex(taskIndex);

        boolean shouldMarkAsDone = command.startsWith("mark ");
        Task task = tasks.getTask(taskIndex);
        if (shouldMarkAsDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        storage.saveTasks(tasks.asList());
        ui.showMarkResult(task, shouldMarkAsDone);
    }

    /** Handles a delete command after parsing its task number. */
    private void handleDeleteCommand(String command) {
        int taskNumber = parser.parseTaskNumber(command);
        int taskIndex = taskNumber - 1;
        validateTaskIndex(taskIndex);

        Task removedTask = tasks.removeTask(taskIndex);
        storage.saveTasks(tasks.asList());
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /**
     * Checks whether a zero-based task index exists.
     *
     * @param taskIndex the index to check
     */
    private void validateTaskIndex(int taskIndex) {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new IllegalArgumentException(
                    "that task number is out of range. Pick a number from 1 to " + tasks.size() + ".");
        }
    }

    /** Starts Waffles. */
    public static void main(String[] args) {
        new Waffles().run();
    }
}
